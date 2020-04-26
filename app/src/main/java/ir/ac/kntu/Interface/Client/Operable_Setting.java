package ir.ac.kntu.Interface.Client;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public interface Operable_Setting {

    void saveSetting(String table, String key, String value);

    String loadSetting(String table, String key, String defaultValue);

    void vibrate(long millis);

    boolean hasNetwork();

    void makeScreenNoLimits(Activity activity, boolean noLimits);

    void inverseBarColor(Activity activity, boolean black);

    void copyToClipBoard(@NonNull Context context, @NonNull String text);

    String readAssetFile(String fileName);

    int getDeviceWidth();

    void setDeviceWidth(WindowManager windowManager);

    int getDeviceHeight();

    void setDeviceHeight(WindowManager windowManager);

    void setItemWidth(View view, double devision);

    void configureImageLoader();

    void configureToasty();

    void saveBitmap(String fileName, Bitmap bitmap, String bitmapsDirectoryName) throws Exception;

    void hideKeyboard(Activity activity);

    boolean isApplicationInstalled(String packageName);

    boolean isConnected();
}
