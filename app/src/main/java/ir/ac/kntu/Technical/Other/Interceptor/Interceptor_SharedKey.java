package ir.ac.kntu.Technical.Other.Interceptor;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.Other.Helper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptor_SharedKey implements Interceptor {
    private static Interceptor_SharedKey instance;

    private Interceptor_SharedKey() {
    }

    public static Interceptor_SharedKey getInstance() {
        if (instance == null)
            instance = new Interceptor_SharedKey();
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String sharedKey = Helper.getInstance().getSharedKey();
        Request request = chain.request();
        if (sharedKey != null)
            request = request.newBuilder().header("EncSharedKey", sharedKey).build();
        return chain.proceed(request);
    }
}