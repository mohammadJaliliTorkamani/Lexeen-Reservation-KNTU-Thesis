package ir.ac.kntu.Technical.Other.Interceptor;

import android.content.Context;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.Other.Helper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptor_TargetQRCode implements Interceptor {
    private static Interceptor_TargetQRCode instance;
    private Context context;

    private Interceptor_TargetQRCode(Context context) {
        this.context = context;
    }

    public static Interceptor_TargetQRCode getInstance(Context context) {
        if (instance == null) {
            instance = new Interceptor_TargetQRCode(context);
        }
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String code = Helper.getInstance().getRestaurantSelectionQRCode();
        Request request = chain.request();
        if (code != null)
            request = request.newBuilder().header("code", Helper.getInstance().getRestaurantSelectionQRCode()).build();
        return chain.proceed(request);
    }
}
