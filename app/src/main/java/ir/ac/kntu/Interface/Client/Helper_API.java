package ir.ac.kntu.Interface.Client;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.Constants;
import saman.zamani.persiandate.PersianDate;

public interface Helper_API {

    /**
     * shows error log
     *
     * @param code    error code
     * @param message error message
     */
    void error(int code, String message);

    /**
     * shows info log
     *
     * @param message info message
     */
    void info(String message);

    /**
     * toasts a message on the screen
     *
     * @param str       message
     * @param toastMode toast mode (e.g success,info,...)
     */
    void toast(String str, Constants.ToastMode toastMode);

    /**
     * converts dp unit into dp
     *
     * @param dp dp value
     * @return px value
     */
    int dp2px(float dp);

    /**
     * toasts a message on the screen
     *
     * @param string_resource sting resource to extract message from.
     * @param toastMode       toast mode(e.g success,info,...)
     */
    void toast(@StringRes int string_resource, Constants.ToastMode toastMode);

    /**
     * reformat text in camel case mode
     *
     * @param text to be reformatted
     * @return camel case mode of text
     */
    String toCamelCase(String text);

    /**
     * get corresponding color of the passed index
     *
     * @param categoryIndex to get mapped color
     * @return corresponding color of the index
     */
    int getCategoryColor(int categoryIndex);

    /**
     * rounds price in one digit (if was round, return it as it was)
     *
     * @param price           to round
     * @param deleteRialZeros delete 3 zeros at the end and then round the price
     * @return rounded (and modified for rial zeros if needed) price
     */
    String getOneDigitOrNon(float price, boolean deleteRialZeros);

    /**
     * rounds price in one digit (if was round, return it as it was)
     *
     * @param price           to round
     * @param deleteRialZeros delete 3 zeros at the end and then round the price
     * @return rounded (and modified for rial zeros if needed) price
     */
    String getOneDigitOrNon(double price, boolean deleteRialZeros);

    /**
     * checks whether the name is appropriate for profile or no
     *
     * @param name name to check
     * @return check result
     */
    boolean isRightName(String name);

    /**
     * checks whether the phone is appropriate for profile or no
     *
     * @param phone phone to check
     * @return check result
     */
    boolean isRightPhone(String phone);

    /**
     * lowercase the string
     *
     * @param text to be lowercased
     * @return lowercased string
     */
    String toLowerCase(String text);

    /**
     * converts px unit to dp
     *
     * @param px to be converted to dp
     * @return px value
     */
    float px2Dp(int px);

    /**
     * returns today jalali date in pattern format
     *
     * @param pattern pattern for date be formatted like it
     * @return jalali date in passed pattern mode
     */
    String getTodaysShamsiDate(String pattern);

    /**
     * converts string with passed pattern to corresponding jalali date
     *
     * @param date    date to convert to date object
     * @param pattern date pattern
     * @return jalali date object
     * @throws Exception when conversion failed
     */
    PersianDate getShamsiDateFromString(String date, String pattern) throws Exception;

    /**
     * retrieves date AND time from the passed pattern
     *
     * @param normalPattern   normal date time pattern
     * @param accuratePattern accurate date time pattern
     * @param accurateMode    specifies if seconds will be added or no
     * @return today date time string
     */
    String getTodaysTime(String normalPattern, String accuratePattern, boolean accurateMode);

    /**
     * calculates ceil of value
     *
     * @param value to calculate it's ceil
     * @return veil value
     */
    long getCostCeilOf(double value);

    /**
     * converts bitmap to URI
     *
     * @param bitmap to be converted
     * @return URI value of the passed bitmap
     */
    Uri bitmapToUri(Bitmap bitmap);

    /**
     * converts one digit number to 2 digit if needed
     *
     * @param number to be converted if needed
     * @return 2 digit mode of the passed number
     */
    String get2DigitsOfDigit(int number);

    /**
     * converts string to jalali date object (with format year/month/date hour:minute)
     *
     * @param selectedDate string to be converted
     * @return jalali date object
     */
    PersianDate stringToPersianDateTime(String selectedDate);

    /**
     * checks whether the passed persian date is valid (has at least Constants.VALID_ORDER_DATE_MINUTE_INTERVAL minutes (distance) from now or no)
     *
     * @param persianDate to be checked
     * @return if has valid distance from now or no
     */
    boolean isValidTimeForIntervalFromNow(PersianDate persianDate);

    /**
     * checks whether the passed persian date is valid (has at least Constants.VALID_ORDER_DATE_MINUTE_INTERVAL minutes (distance) from now or no)
     *
     * @param persianDate_str to be checked
     * @return if has valid distance from now or no
     */
    boolean isValidTimeForIntervalFromNow(String persianDate_str);

    /**
     * converts svg to bitmap
     *
     * @param drawableId svg object to be converted
     * @return bitmap object
     */
    Bitmap svgToBitmap(int drawableId);

    /**
     * converts image to grayscale image
     *
     * @param imageView to be converted
     */
    void setLockedOnGrayScale(ImageView imageView);

    /**
     * converts gray scale image to normal mode
     *
     * @param imageView to be converted
     */
    void setUnlockedForGrayScale(ImageView imageView);

    /**
     * converts mill seconds to minutes
     *
     * @param millisUntilFinished
     * @return minute
     */
    int getMinuteFromSecond(long millisUntilFinished);

    /**
     * converts milliseconds to seconds
     *
     * @param millisUntilFinished
     * @return second
     */
    int getSecondFromSeconds(long millisUntilFinished);

    /**
     * generates a random number x ( min<=x<=max)
     *
     * @param min minimum of interval
     * @param max maximum of interval
     * @return random number
     */
    int generateRandomNumber(int min, int max);

    /**
     * checks whether the passed string contains non-persian characters or no)
     *
     * @param str string to be checked
     * @return whether has non-persian or no
     */
    boolean containsNonPersianLanguage(String str);

    /**
     * checks whether passed string contains integer (in radix 'radix') or no
     *
     * @param s     string to be checked
     * @param radix radix of integer
     * @return integer check result
     */
    boolean isInteger(String s, int radix);

    /**
     * converts gregorian date time string to corresponding jalali date time string
     *
     * @param date_and_time_start string to be converted
     * @return jalali date time string
     */
    String getShamsiDateTimeFromGregortianString(String date_and_time_start);

    /**
     * cheks whether passed number is prime or no
     *
     * @param n number to be checked
     * @return whether is prime or no
     */
    boolean isPrime(int n);

    /**
     * changes the stroke color of the passed view into App Main color
     *
     * @param view to change stroke color
     */
    void changeStrokeColorToMainAppColor(View view);

    /**
     * changes the color of the passed view into the App Main color
     *
     * @param drawable drawable to change color
     */
    void changeShapeColorToMainAppColor(Drawable drawable);

    /**
     * changes the color of the passed view into the App Main color
     *
     * @param view view to change color
     */
    void changeShapeColorToMainAppColor(View view);

    /**
     * retrieves the app main color
     *
     * @return app main color
     */
    String getMainAppColor();

    /**
     * retrieves app shared key with the server
     *
     * @return shared key
     */
    String getSharedKey();

    /**
     * retrieves client token to communicate with server
     *
     * @return token
     */
    String getToken();

    /**
     * checks whether we still have valid shared key or no
     *
     * @return is valid shared key or no
     */
    boolean hasValidSharedKey();

    /**
     * gets purchase unit for buy foods or payment operations or UI initializations
     *
     * @return purchase unit
     */
    String getPurchaseUnit();

    /**
     * retrieves selected restaurant DECRYPTED QR code from the shared preferences
     *
     * @return decrypted QR Code for the selected restaurant
     */
    String getSelectedRestaurantDecryptedQRCode();

    /**
     * checks whether the user has logged into the app or no
     *
     * @return whether the user has logged into the app or no
     */
    boolean isLoggedIn();

    /**
     * retrieves counter symbol (default is 'x'. e.g  2x italian pizza)
     *
     * @return counter symbol
     */
    String getCounterSymbol();

    /**
     * retrieves country pre phone (default is +98 for Iran)
     *
     * @return prePhone symbol
     */
    String getDefaultPrePhone();

    /**
     * retrieves resend activation sms time (in millis)
     *
     * @return millis time to resend sms activation code
     */
    long GET_RESEND_VERIFICATION_CODE_INTERVAL();

    /**
     * checks whether the user has opened the app for the first time or no
     *
     * @return whether user has opened for the first time or no
     */
    boolean isFirstUse();

    /**
     * converts string to it's hash SHA-256 value
     *
     * @param str to get hash
     * @return hash value (SHA-256)
     * @throws Exception when hashing operation failed
     */
    String hash(String str) throws Exception;

    /**
     * shows a two-option dialog with desired species
     *
     * @param fragment       fragment to show dialog in it
     * @param title          title of dialog
     * @param message        message of dialog
     * @param option1        title of the first button
     * @param option2        title of the second button
     * @param option1Handler runnable of the first button
     * @param option2Handler runnable of the second button
     * @param cancelable     dialog is cancelable if clicked outside of dialog or no
     */
    void showBiOptionsDialog(Fragment fragment, String title, String message, String option1,
                             String option2, Runnable_SingleArg<Dialog> option1Handler,
                             Runnable_SingleArg<Dialog> option2Handler, boolean cancelable);
}
