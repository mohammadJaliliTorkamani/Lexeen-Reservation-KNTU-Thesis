package ir.ac.kntu.Technical.Other.Interceptor;

import android.content.Context;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.Other.Setting;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptor_Cache implements Interceptor {
    private static Interceptor_Cache instance;
    private Context context;
    private long hasInternetCacheAgeSeconds;
    private long notInternetCacheAgeSeconds;

    private Interceptor_Cache(Context context, long hasInternetCacheAgeSeconds, long notInternetCacheAgeSeconds) {
        this.context = context;
        this.hasInternetCacheAgeSeconds = hasInternetCacheAgeSeconds;
        this.notInternetCacheAgeSeconds = notInternetCacheAgeSeconds;
    }

    public static Interceptor_Cache getInstance(Context context, long hasInternetCacheAge, long notInternetCacheAge) {
        if (instance == null)
            instance = new Interceptor_Cache(context, hasInternetCacheAge, notInternetCacheAge);
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (Setting.getInstance().hasNetwork()) {
            request = request.newBuilder().header("cache-control", "public,max-age=" + hasInternetCacheAgeSeconds).build();
        } else {
            request = request.newBuilder().header("cache-control", "public,only-if-cached,max-staled=" + notInternetCacheAgeSeconds).build();
        }
        Response response = chain.proceed(request);
        return response;
    }
}
