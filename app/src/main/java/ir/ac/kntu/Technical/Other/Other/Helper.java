package ir.ac.kntu.Technical.Other.Other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import ir.ac.kntu.R;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class Helper {

    public static void error(int code, String message) {
        Log.e(Constants.TAG, "code : " + code + " Message : " + message);
    }

    public static void info(String message) {
        Log.d(Constants.TAG, "Message : " + message);
    }


    public static void toast(String str, Constants.ToastMode toastMode) {
        switch (toastMode) {
            case NORMAL:
                Toasty.normal(ContextHelper.retrieveContext(), str).show();

                break;
            case INFO:
                Toasty.info(ContextHelper.retrieveContext(), str).show();

                break;
            case ERROR:
                Toasty.error(ContextHelper.retrieveContext(), str).show();

                break;
            case SUCCESS:
                Toasty.success(ContextHelper.retrieveContext(), str).show();

                break;
            case WARNING:
                Toasty.warning(ContextHelper.retrieveContext(), str).show();

                break;
        }
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ContextHelper.retrieveContext().getResources().getDisplayMetrics());
    }

    public static void toast(@StringRes int string_resource, Constants.ToastMode toastMode) {
        Context context = ContextHelper.retrieveContext();
        toast(context.getString(string_resource), toastMode);
    }

    public static String toCamelCase(String text) {
        if (text == null)
            return text;
        final StringBuilder ret = new StringBuilder(text.length());
        for (final String word : text.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == text.length()))
                ret.append(" ");
        }
        return ret.toString();
    }

    public static int getCategoryColor(int categoryIndex) {
        switch (categoryIndex % 9) {
            case 0:
                return Color.parseColor("#ff669900");
            case 1:
                return Color.parseColor("#ff0099cc");
            case 2:
                return Color.parseColor("#ffff8800");
            case 3:
                return Color.parseColor("#ffaa66cc");
            case 4:
                return Color.parseColor("#ffcc0000");
            case 5:
                return Color.parseColor("#ffffbb33");
            case 6:
                return Color.parseColor("#ff33b5e5");
            case 7:
                return Color.parseColor("#ff99cc00");
            case 8:
                return Color.parseColor("#ffff4444");
            default:
                return -1;
        }
    }

    public static String getOneDigitOrNon(float price, boolean deleteRialZeros) {
        if (deleteRialZeros)
            price /= 1000;
        if (price == (int) (price))
            return String.valueOf((int) price);
        else
            return new DecimalFormat("#.00").format(price);
    }

    public static String getOneDigitOrNon(double price, boolean deleteRialZeros) {
        if (deleteRialZeros)
            price /= 1000;
        if (price == (int) (price))
            return String.valueOf((int) price);
        else
            return new DecimalFormat("#.00").format(price);
    }

    public static boolean isRightName(String name) {
        return !name.contains("*") && !name.contains("|") && !name.contains("&") && !name.contains("~")
                && !name.contains("`") && !name.contains(".") && !name.contains("-") && !name.contains("=")
                && !name.contains(",") && !name.contains("#") && !name.contains("+") && !name.contains("^")
                && !name.contains("$") && !name.contains("!") && !name.contains("?") && !name.contains("%");
    }

    public static boolean isRightPhone(String phone) {
        return phone.substring(0, 1).equalsIgnoreCase("9") && isRightName(phone);
    }

    public static String toLowerCase(String text) {

        return text.toLowerCase();
    }

    public static float px2Dp(int px) {
//        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
        return px / ((float) ContextHelper.retrieveContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String getTodaysShamsiDate(String pattern) {
        PersianDate date = new PersianDate();
        PersianDateFormat dateFormatter = new PersianDateFormat(pattern);
        return dateFormatter.format(date);
    }

    public static PersianDate getShamsiDateFromString(String date, String pattern) throws Exception {
        PersianDateFormat dateFormatter = new PersianDateFormat(pattern);
        return dateFormatter.parse(date, pattern);
    }

    public static String getTodaysTime(String normalPattern, String accuratePattern, boolean accurateMode) {
        PersianDate date = new PersianDate();
        PersianDateFormat dateFormatter = new PersianDateFormat(accurateMode ? accuratePattern : normalPattern);
        return dateFormatter.format(date);
    }

    public static long getCostCeilOf(double value) {
        if (value == (int) value)
            return (long) value;
        else
            return (long) (Math.floor(value + 1));
    }

    public static Uri bitmapToUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ContextHelper.retrieveContext().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public static String get2DigitsOfDigit(int number) {
        if (number < 0)
            return "-1";
        else if (number < 10)
            return "0" + number;
        else if (number < 100)
            return String.valueOf(number);
        else
            return ContextHelper.retrieveContext().getString(R.string.error);
    }

    public static PersianDate stringToPersianDateTime(String selectedDate) {
        int year = Integer.parseInt(selectedDate.substring(0, 4));
        int month = Integer.parseInt(selectedDate.substring(5, 7));
        int day = Integer.parseInt(selectedDate.substring(8, 10));
        int hour = Integer.parseInt(selectedDate.substring(11, 13));
        int minute = Integer.parseInt(selectedDate.substring(14, 16));
        return new PersianDate().setShYear(year).setShMonth(month).setShDay(day).setHour(hour).setMinute(minute);
    }

    public static boolean isValidTimeForIntervalFromNow(PersianDate persianDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.VALID_ORDER_DATE_MINUTE_INTERVAL);
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(persianDate.toDate());
        return gregorianCalendar.compareTo(calendar) >= 0;
    }

    public static boolean isValidTimeForIntervalFromNow(String persianDate_str) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.VALID_ORDER_DATE_MINUTE_INTERVAL);
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(stringToPersianDateTime(persianDate_str).toDate());
        return gregorianCalendar.compareTo(calendar) >= 0;
    }

    public static Bitmap svgToBitmap(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(ContextHelper.retrieveContext(), drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void setLockedOnGrayScale(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(cf);
        imageView.setImageAlpha(255);   // 128 = 0.
    }

    public static void setUnlockedForGrayScale(ImageView imageView) {
        imageView.setColorFilter(null);
        imageView.setImageAlpha(255);
    }

    public static int getMinuteFromSecond(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        return seconds / 60;
    }

    public static int getSecondFromSeconds(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        return seconds % 60;
    }

    public static int generateRandomNumber(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static boolean containsNonPersianLanguage(String str) {
        int codePointAt = Character.codePointAt(str, 0);
        return !((codePointAt >= 0x0600 && codePointAt <= 0x06FF) || (codePointAt >= 0xFB50 && codePointAt <= 0xFDFF) || (codePointAt >= 0xFE70 && codePointAt <= 0xFEFF));
    }


    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static String getShamsiDateTimeFromGregortianString(String date_and_time_start) {
        String date = date_and_time_start.split(" ")[0];
        String time = date_and_time_start.split(" ")[1];
        int year = Integer.parseInt(date.split("/")[0]);
        int month = Integer.parseInt(date.split("/")[1]);
        int day = Integer.parseInt(date.split("/")[2]);
        PersianDate persianDate = new PersianDate();
        persianDate.setGrgYear(year).setGrgMonth(month).setGrgDay(day);
        return persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay() + " " + time;
    }

    public static boolean isPrime(int n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    /**
     * works for drawables which have shape <not item>
     *
     * @param view
     */
    public static void changeStrokeColorToMainAppColor(View view) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setStroke(4, Color.parseColor(getMainAppColor())); // set stroke width and stroke color
    }

    public static void changeShapeColorToMainAppColor(Drawable drawable) {
        drawable.setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.SRC_IN);
    }

    public static void changeShapeColorToMainAppColor(View view) {
        //MainActivity.info("THIS : "+getMainAppColor());

        view.getBackground().setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.SRC_IN);
        /*if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.parseColor(getMainAppColor()));
        }else if(background instanceof VectorDrawable){
            background.setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.MULTIPLY);
        }else if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(Color.parseColor(getMainAppColor()));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(Color.parseColor(getMainAppColor()));
        }else if(background instanceof StateListDrawable){
            StateListDrawable gradientDrawable = (StateListDrawable) view.getBackground();
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) gradientDrawable.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();
            children[0].setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.MULTIPLY);
//            LayerDrawable selectedItem = (LayerDrawable) children[0];
//            LayerDrawable unselectedItem = (LayerDrawable) children[1];
//            GradientDrawable selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
//            GradientDrawable unselectedDrawable = (GradientDrawable) unselectedItem.getDrawable(0);
//            selectedDrawable.setStroke(STORKE_SIZE, NOTIFICATION_COLOR);
//            unselectedDrawable.setStroke(STORKE_SIZE, NOTIFICATION_COLOR);
        }
        /**
         * StateListDrawable gradientDrawable = (StateListDrawable) inflatedView.getBackground();
         *     DrawableContainerState drawableContainerState = (DrawableContainerState) gradientDrawable.getConstantState();
         *     Drawable[] children = drawableContainerState.getChildren();
         *     LayerDrawable selectedItem = (LayerDrawable) children[0];
         *     LayerDrawable unselectedItem = (LayerDrawable) children[1];
         *     GradientDrawable selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
         *     GradientDrawable unselectedDrawable = (GradientDrawable) unselectedItem.getDrawable(0);
         *     selectedDrawable.setStroke(STORKE_SIZE, NOTIFICATION_COLOR);
         *     unselectedDrawable.setStroke(STORKE_SIZE, NOTIFICATION_COLOR);*/
        /*try {
            GradientDrawable bgShape = (GradientDrawable) view.getBackground();
            bgShape.mutate();
            bgShape.setColor(Color.parseColor(getMainAppColor()));
        } catch (Exception e) {
            MainActivity.info(e.getMessage());
            Helper_Log.errorLog(e, Setting.class);
        }*/
    }

    public static String getMainAppColor() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_APP_MAIN_COLOR, String.format("#%06X", 0xFFFFFF & ContextHelper.retrieveContext().getResources().getColor(R.color.main_theme_color)));
    }

    public static String getSharedKey() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null);
    }

    public static String getToken() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
    }

    public static boolean hasValidSharedKey() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null) != null;
    }

    public static String getPurchaseUnit() {
        return Constants.PURCHASE_UNIT;
    }

    public static String getRestaurantSelectionQRCode() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_RESTAURANT_SELECTION_QR_CODE, null);
    }

    public static void animateTheFragment(Fragment fragment, FragmentManager fragmentManager) {

    }

    public static boolean isLoggedIn() {
        return !Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW").equalsIgnoreCase("NEW");
    }


    public static String getCounterSymbol() {
        return Constants.COUNTER_SYMBOL;
    }

    public static String getDefautPrePhone() {
        return Constants.DEFAULT_PHONE_PREFIX;
    }

    public static long GET_RESEND_VERIFICATION_CODE_INTERVAL() {
        return Constants.RESEND_VERIFICATION_CODE_INTERVAL;
    }

    public static boolean isFirstUse() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, null) == null;

    }
}
