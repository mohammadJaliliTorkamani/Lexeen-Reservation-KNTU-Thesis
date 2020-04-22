package ir.ac.kntu.Server;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.ac.kntu.Activity.MainActivity;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_Encryption;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_PusheID;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_TargetQRCode;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_Token;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_VersionCode;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connector {
    private static final String BASE_URL = "https://aban.dev/kntu_project/api/";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static Retrofit mRetrofit, mCachedRetrofit;
    private static Cache mCache;
    private static OkHttpClient mOkHttpClient, mCachedOkHttpClient;

    public static <S> void createService(View view, Class<S> serviceClass, Runnable_SingleArg<S> smartRunnable) {
        if (mRetrofit == null) {
            // Add all interceptors you want (headers, URL, logging)
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(7, TimeUnit.SECONDS)
                    .readTimeout(7, TimeUnit.SECONDS)
                    .writeTimeout(7, TimeUnit.SECONDS)
                    .addInterceptor(Interceptor_Token.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_PusheID.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_TargetQRCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_VersionCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_Encryption.getInstance())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .addInterceptor(chain -> {
                        Response response = chain.proceed(chain.request());
                        if (response.code() == 774) {     //Kill the Current Session
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW");
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_RESTAURANT_SELECTION_QR_CODE, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, null);
                            Intent intent = new Intent(ContextHelper.retrieveContext(), MainActivity.class);
                            ContextHelper.retrieveContext().startActivity(intent);
                            ((Activity) ContextHelper.retrieveContext()).finish();
                        }

                        return response;
                    })
                    .cache(provideCache());

            mOkHttpClient = httpClient.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()))
                    // Add your adapter factory to handler Errors
                    .client(mOkHttpClient)
                    .build();
        }
        smartRunnable.run(mRetrofit.create(serviceClass));
    }

    public static <S> void createCachedService(View view, Class<S> serviceClass, Runnable_SingleArg<S> smartRunnable) {
        if (mCachedRetrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    // Add all interceptors you want (headers, URL, logging)
                    .addInterceptor(Interceptor_Token.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_PusheID.getInstance(ContextHelper.retrieveContext()))
//                    .addInterceptor(Interceptor_Cache.getInstance(ContextHelper.retrieveContext(), 5, 7 * 24 * 60 * 60))
                    .addInterceptor(Interceptor_TargetQRCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_VersionCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(provideForcedOfflineCacheInterceptor())
                    .cache(provideCache());

            mCachedOkHttpClient = httpClient.build();

            mCachedRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()))
                    .client(mCachedOkHttpClient)
                    .build();
        }

        smartRunnable.run(mRetrofit.create(serviceClass));
    }

    private static Cache provideCache() {
        if (mCache == null) {
            try {
                mCache = new Cache(new File(ContextHelper.retrieveContext().getCacheDir(), "http-cache"),
                        128 * 1024 * 1024); // 128 MB
            } catch (Exception e) {
                Helper_Log.errorLog(e, Connector.class);
            }
        }
        return mCache;
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (Setting.getInstance().isConnected()) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };
    }

    private static Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            if (!Setting.getInstance().isConnected()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    private static Interceptor provideForcedOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);
        };
    }


    public void clean() {
        if (mOkHttpClient != null) {
            // Cancel Pending Request
            mOkHttpClient.dispatcher().cancelAll();
        }

        if (mCachedOkHttpClient != null) {
            // Cancel Pending Cached Request
            mCachedOkHttpClient.dispatcher().cancelAll();
        }

        mRetrofit = null;
        mCachedRetrofit = null;

        if (mCache != null) {
            try {
                mCache.evictAll();
            } catch (IOException e) {
                Log.e(Constants.TAG, "Error cleaning http cache");
            }
        }

        mCache = null;
    }
}
