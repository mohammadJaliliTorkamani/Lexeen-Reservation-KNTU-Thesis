package ir.ac.kntu.Technical.Other.Other;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;
import ir.ac.kntu.R;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static ir.ac.kntu.Technical.Other.Other.ContextHelper.retrieveContext;

public class Setting {
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

    public void saveSetting(String table, String key, String value) {
        preferences = retrieveContext().getSharedPreferences(table, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }

    public String loadSetting(String table, String key, String defaultValue) {
        preferences = retrieveContext().getSharedPreferences(table, MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public void vibrate(long millis) {
        Vibrator v = (Vibrator) retrieveContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(millis);
        }
    }

    public boolean hasNetwork() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) retrieveContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

    public void changeStatusBarColor(Window window, boolean whiteMode) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = window.getDecorView();
            if (whiteMode) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextHelper.retrieveContext().getResources().getColor(android.R.color.white));
            } else {
                decor.setSystemUiVisibility(0);
            }
        }*/
    }


    public void copyToClipBoard(@NonNull Context context, @NonNull String text) {
        ClipboardManager clipboard = (ClipboardManager) retrieveContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constants.TAG, text);
        clipboard.setPrimaryClip(clip);
    }

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


    public int getDeviceWidth() {
        return mDevicewidth;
    }

    public void setDeviceWidth(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDevicewidth = (int) displayMetrics.widthPixels;
    }

    public int getDeviceHeight() {
        return mDeviceHeight;
    }

    public void setDeviceHeight(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDeviceHeight = displayMetrics.heightPixels;
    }

    public void setItemWidth(View view, double devision) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = (int) (getDeviceWidth() / devision);
        view.requestLayout();
    }

    public void configureImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_lexin_gray)
                .showImageForEmptyUri(R.drawable.ic_lexin_gray)
                .showImageOnFail(R.drawable.ic_lexin_gray)
                .resetViewBeforeLoading(false)
                .delayBeforeLoading(50)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(600))
                .handler(new Handler())
                .build();
//                .preProcessor()
//		        .postProcessor()
//		        .extraForDownloader()
//                .decodingOptions()

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ContextHelper.retrieveContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

    }

    public void configureToasty() {
        Toasty.Config.getInstance()
                .tintIcon(true)
                .setToastTypeface(Typeface.createFromAsset(ContextHelper.retrieveContext().getAssets(), "fonts/farsi/syekan.otf"))
                .setTextSize(16)
                .allowQueue(true)
                .apply();
    }

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

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


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
