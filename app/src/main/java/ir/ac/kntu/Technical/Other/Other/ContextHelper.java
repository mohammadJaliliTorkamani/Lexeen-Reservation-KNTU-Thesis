package ir.ac.kntu.Technical.Other.Other;

import android.app.Application;
import android.content.Context;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.pushpole.sdk.PushPole;

import ir.ac.kntu.BuildConfig;
import ir.map.sdk_map.Mapir;
import ir.map.servicesdk.MapirService;

/**
 * initialize global shared context and initializes services (e.g Map etc)
 */
public class ContextHelper extends Application {
    private static Context context;

    public static Context retrieveContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PushPole.initialize(context, true);
        MapirService.init(this, BuildConfig.MAP_API_KEY);
        Mapir.getInstance(this, BuildConfig.MAP_API_KEY);
        AppCenter.start(this, BuildConfig.APP_CENTER_API_KEY, Analytics.class, Crashes.class);
    }
}
