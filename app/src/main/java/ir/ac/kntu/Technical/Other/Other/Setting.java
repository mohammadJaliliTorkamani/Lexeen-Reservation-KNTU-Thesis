package ir.ac.kntu.Technical.Other.Other;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;
import ir.ac.kntu.Interface.Client.Setting_API;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static ir.ac.kntu.Technical.Other.Other.ContextHelper.retrieveContext;

public class Setting implements Setting_API {
    private static SharedPreferences preferences;
    private static Setting instance;
    private Context context;

    private int mDevicewidth, mDeviceHeight;

    private Setting() {
    }

    public static Setting getInstance() {
        if (instance == null)
            instance = new Setting();
        return instance;
    }

    @Override
    public void saveSetting(String table, String key, String value) {
        preferences = retrieveContext().getSharedPreferences(table, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }

    @Override
    public String loadSetting(String table, String key, String defaultValue) {
        preferences = retrieveContext().getSharedPreferences(table, MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    @Override
    public void vibrate(long millis) {
        Vibrator v = (Vibrator) retrieveContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(millis);
        }
    }

    @Override
    public boolean hasNetwork() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) retrieveContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

    @Override
    public void makeScreenNoLimits(Activity activity, boolean noLimits) {
        if (noLimits)
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        else
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void inverseBarColor(Activity activity, boolean black) {
        if (!black) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        }
        activity.getWindow().setNavigationBarColor(Color.parseColor(black ? "#000000" : "#FFFFFF"));
        activity.getWindow().setStatusBarColor(Color.parseColor(black ? "#000000" : "#FFFFFF"));
    }

    @Override
    public void copyToClipBoard(@NonNull Context context, @NonNull String text) {
        ClipboardManager clipboard = (ClipboardManager) retrieveContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constants.TAG, text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public String readAssetFile(String fileName) {
        StringBuilder toReturn = new StringBuilder();
        try {
            InputStream is = retrieveContext().getAssets().open("licenses/" + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            toReturn.append(text);
        } catch (IOException e) {
            Helper_Log.errorLog(e, Setting.class);
        }
        return toReturn.toString();
    }

    @Override
    public int getDeviceWidth() {
        return mDevicewidth;
    }

    @Override
    public void setDeviceWidth(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDevicewidth = (int) displayMetrics.widthPixels;
    }

    @Override
    public int getDeviceHeight() {
        return mDeviceHeight;
    }

    @Override
    public void setDeviceHeight(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDeviceHeight = displayMetrics.heightPixels;
    }

    @Override
    public void setItemWidth(View view, double devision) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = (int) (getDeviceWidth() / devision);
        view.requestLayout();
    }

    @Override
    public void configureToasty() {
        Toasty.Config.getInstance()
                .tintIcon(true)
                .setToastTypeface(Typeface.createFromAsset(ContextHelper.retrieveContext().getAssets(), "fonts/farsi/syekan.otf"))
                .setTextSize(16)
                .allowQueue(true)
                .apply();
    }

    @Override
    public void saveBitmap(String fileRawName, Bitmap bitmap, String directoryName) throws Exception {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/lexinPaymentBill";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileRawName + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
    }

    @Override
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean isApplicationInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Helper_Log.errorLog(e, Setting.class);
        }

        return false;
    }

    @Override
    public boolean isConnected() {
        try {
            android.net.ConnectivityManager e = (android.net.ConnectivityManager) ContextHelper.retrieveContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Helper_Log.errorLog(e, Setting.class);
        }

        return false;
    }
}
