package ir.ac.kntu.Interface.Client;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ir.ac.kntu.Technical.Other.Other.Constants;
import saman.zamani.persiandate.PersianDate;

public interface Operable_Helper {

    void error(int code, String message);

    void info(String message);

    void toast(String str, Constants.ToastMode toastMode);

    int dp2px(float dp);

    void toast(@StringRes int string_resource, Constants.ToastMode toastMode);

    String toCamelCase(String text);

    int getCategoryColor(int categoryIndex);

    String getOneDigitOrNon(float price, boolean deleteRialZeros);

    String getOneDigitOrNon(double price, boolean deleteRialZeros);

    abstract boolean isRightName(String name);

    boolean isRightPhone(String phone);

    String toLowerCase(String text);

    float px2Dp(int px);

    String getTodaysShamsiDate(String pattern);

    PersianDate getShamsiDateFromString(String date, String pattern) throws Exception;

    String getTodaysTime(String normalPattern, String accuratePattern, boolean accurateMode);

    long getCostCeilOf(double value);

    Uri bitmapToUri(Bitmap bitmap);

    String get2DigitsOfDigit(int number);

    PersianDate stringToPersianDateTime(String selectedDate);

    boolean isValidTimeForIntervalFromNow(PersianDate persianDate);

    boolean isValidTimeForIntervalFromNow(String persianDate_str);

    Bitmap svgToBitmap(int drawableId);

    void setLockedOnGrayScale(ImageView imageView);

    void setUnlockedForGrayScale(ImageView imageView);

    int getMinuteFromSecond(long millisUntilFinished);

    int getSecondFromSeconds(long millisUntilFinished);

    int generateRandomNumber(int min, int max);

    boolean containsNonPersianLanguage(String str);

    boolean isInteger(String s, int radix);

    String getShamsiDateTimeFromGregortianString(String date_and_time_start);

    boolean isPrime(int n);

    void changeStrokeColorToMainAppColor(View view);

    void changeShapeColorToMainAppColor(Drawable drawable);

    void changeShapeColorToMainAppColor(View view);

    String getMainAppColor();

    String getSharedKey();

    String getToken();

    boolean hasValidSharedKey();

    String getPurchaseUnit();

    String getRestaurantSelectionQRCode();

    void animateTheFragment(Fragment fragment, FragmentManager fragmentManager);

    boolean isLoggedIn();

    String getCounterSymbol();

    String getDefautPrePhone();

    long GET_RESEND_VERIFICATION_CODE_INTERVAL();

    boolean isFirstUse();
}
