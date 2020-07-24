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
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_PusheID;
import ir.ac.kntu.Technical.Other.Interceptor.Interceptor_SharedKey;
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

import static ir.ac.kntu.Technical.Other.Other.Constants.BASE_URL;
import static ir.ac.kntu.Technical.Other.Other.Constants.HEADER_CACHE_CONTROL;
import static ir.ac.kntu.Technical.Other.Other.Constants.HEADER_PRAGMA;

/**
 * used to have communication through the server (REST Mode)
 */
public class Connector {

    private static Retrofit mRetrofit, mCachedRetrofit;
    private static Cache mCache;
    private static OkHttpClient mOkHttpClient, mCachedOkHttpClient;

    /**
     * creates a service in which interface execute and what to do after communication
     *
     * @param view          to connect
     * @param serviceClass  interface to load methods from.
     * @param smartRunnable to run after communication
     * @param <S>           generic class
     */
    public static <S> void createService(View view, Class<S> serviceClass, Runnable_SingleArg<S> smartRunnable) {
        if (mRetrofit == null) {
            // Add all interceptors you want (headers, URL, logging)
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.CONNECTION_READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constants.CONNECTION_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(Interceptor_Token.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_PusheID.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_TargetQRCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_VersionCode.getInstance(ContextHelper.retrieveContext()))
                    .addInterceptor(Interceptor_SharedKey.getInstance())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .addInterceptor(chain -> {
                        Response response = chain.proceed(chain.request());
                        if (response.code() == 774) {     //Kill the Current Session
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW");
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_SELECTED_RESTAURANT_QR_CODE, null);
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

    /**
     * creates a service in which interface execute and what to do after communication (with cache mode )
     *
     * @param view          to connect
     * @param serviceClass  interface to load methods from.
     * @param smartRunnable to run after communication
     * @param <S>           generic class
     */
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

    /**
     * creates 128 MB Cache
     *
     * @return Cache object
     */
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

    /**
     * creates cache interceptor used in retrofit
     *
     * @return Interceptor
     */
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

    /**
     * creates cache interceptor used in retrofit offline mode (with connection status in mind)
     *
     * @return Interceptor
     */
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

    /**
     * creates cache interceptor used in retrofit force offline mode (no matter connection status)
     *
     * @return Interceptor
     */
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


    /**
     * stops and cleans client info for server connection
     */
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
