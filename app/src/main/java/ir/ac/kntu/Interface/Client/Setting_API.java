package ir.ac.kntu.Interface.Client;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public interface Setting_API {

    /**
     * saves setting in sharedpreference table
     *
     * @param table table to store
     * @param key   key to store
     * @param value value to store
     */
    void saveSetting(String table, String key, String value);

    /**
     * loads setting from sharedpreference table
     *
     * @param table        table to load value from
     * @param key          key to return value
     * @param defaultValue default value if key not exists
     * @return value from the passed key
     */
    String loadSetting(String table, String key, String defaultValue);

    /**
     * vibrates the device
     *
     * @param millis duration in milli seconds
     */
    void vibrate(long millis);

    /**
     * checks whether the user has accessibility to the network or no.
     *
     * @return whether user has network or no
     */
    boolean hasNetwork();

    /**
     * configures screen size of the device
     *
     * @param activity activity to change screen size
     * @param noLimits specifies whether show in fullscreen or no (enable/disable switch)
     */
    void makeScreenNoLimits(Activity activity, boolean noLimits);

    /**
     * change status bar color
     *
     * @param activity to change status bar from.
     * @param black    if true, status bar will be black, otherwise will be white.
     */
    void inverseBarColor(Activity activity, boolean black);

    /**
     * copies text to clipboard
     *
     * @param context context to do system operation
     * @param text    to copy into the clipboard
     */
    void copyToClipBoard(@NonNull Context context, @NonNull String text);

    /**
     * reads file and retrieve contents as string
     *
     * @param fileName file to read
     * @return contents of the passed file
     */
    String readAssetFile(String fileName);

    /**
     * retrieve device widths
     *
     * @return device width
     */
    int getDeviceWidth();

    /**
     * sets device width
     *
     * @param windowManager windowmanager to calculate
     */
    void setDeviceWidth(WindowManager windowManager);

    /**
     * retrieve device height
     *
     * @return device height
     */
    int getDeviceHeight();

    /**
     * sets device height
     *
     * @param windowManager windowmanager to calculate
     */
    void setDeviceHeight(WindowManager windowManager);

    /**
     * @param view
     * @param devision
     */
    void setItemWidth(View view, double devision);

    /**
     * configures some Toasty library setting
     */
    void configureToasty();

    /**
     * saves bitmap into a file in the path
     *
     * @param fileName             to store bitmap in it
     * @param bitmap               to be saved
     * @param bitmapsDirectoryName to store file in it
     * @throws Exception when saving operation failed
     */
    void saveBitmap(String fileName, Bitmap bitmap, String bitmapsDirectoryName) throws Exception;

    /**
     * closes soft input mode(keyboard) from the screen
     *
     * @param activity activity to hide keyboard from.
     */
    void hideKeyboard(Activity activity);

    /**
     * checks whether the passed package is installed in device or no
     *
     * @param packageName package name to check
     * @return whether package is installed or no
     */
    boolean isApplicationInstalled(String packageName);

    /**
     * checks whether the user is now connected to network and can communicate or no
     *
     * @return whether user is fully connected to network or no
     */
    boolean isConnected();
}
