package ir.ac.kntu.Interface.Client;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import saman.zamani.persiandate.PersianDate;

public interface Operable_General {

    void saveSetting(String table, String key, String value);

    String loadSetting(String table, String key, String defaultValue);

    void vibrate(long millis);

    String toCamelCase(String text);

    String toLowerCase(String text);

    void animateTheFragment(Fragment fragment, FragmentManager fragmentManager);

    float px2Dp(int px);

    String getTodaysShamsiDate(String pattern);

    String getTodaysTime(String normalPattern, String accuratePattern, boolean accurateMode);

    PersianDate getShamsiDateFromString(String date, String pattern) throws Exception;

    void setDeviceWidth(WindowManager windowManager);

    void setItemWidth(View view, double devision);

    boolean isFirstUse();

    Uri bitmapToUri(Bitmap bitmap);

    void saveBitmap(String fileName, Bitmap bitmap, String bitmapsDirectoryName) throws Exception;

    void hideKeyboard(Activity activity);
}
