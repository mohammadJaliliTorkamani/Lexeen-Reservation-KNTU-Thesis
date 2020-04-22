package ir.ac.kntu.Technical.Other.Interceptor;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.Other.Helper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Interceptor_Encryption implements Interceptor {
    private static Interceptor_Encryption instance;

    private Interceptor_Encryption() {
    }

    public static Interceptor_Encryption getInstance() {
        if (instance == null)
            instance = new Interceptor_Encryption();
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String sharedKey = Helper.getInstance().getSharedKey();
        Request request = chain.request();
        if (sharedKey != null)
            request = request.newBuilder().header("SharedKey", sharedKey).build();
        return chain.proceed(request);
    }
}