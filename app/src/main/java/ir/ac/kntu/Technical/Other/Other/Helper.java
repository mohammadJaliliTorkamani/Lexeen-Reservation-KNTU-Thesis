package ir.ac.kntu.Technical.Other.Other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import ir.ac.kntu.Interface.Client.Helper_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class Helper implements Helper_API {
    private static Helper instance;

    private Helper() {
    }

    public static Helper getInstance() {
        if (instance == null)
            instance = new Helper();
        return instance;
    }

    @Override
    public void error(int code, String message) {
        Log.e(Constants.TAG, "code : " + code + " Message : " + message);
    }

    @Override
    public void info(String message) {
        Log.d(Constants.TAG, "Message : " + message);
    }

    @Override
    public void toast(String str, Constants.ToastMode toastMode) {
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

    @Override
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ContextHelper.retrieveContext().getResources().getDisplayMetrics());
    }

    @Override
    public void toast(@StringRes int string_resource, Constants.ToastMode toastMode) {
        Context context = ContextHelper.retrieveContext();
        toast(context.getString(string_resource), toastMode);
    }

    @Override
    public String toCamelCase(String text) {
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

    @Override
    public int getCategoryColor(int categoryIndex) {
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

    @Override
    public String getOneDigitOrNon(float price, boolean deleteRialZeros) {
        if (deleteRialZeros)
            price /= 1000;
        if (price == (int) (price))
            return String.valueOf((int) price);
        else
            return new DecimalFormat("#.00").format(price);
    }

    @Override
    public String getOneDigitOrNon(double price, boolean deleteRialZeros) {
        if (deleteRialZeros)
            price /= 1000;
        if (price == (int) (price))
            return String.valueOf((int) price);
        else
            return new DecimalFormat("#.00").format(price);
    }

    @Override
    public boolean isRightName(String name) {
        return !name.contains("*") && !name.contains("|") && !name.contains("&") && !name.contains("~")
                && !name.contains("`") && !name.contains(".") && !name.contains("-") && !name.contains("=")
                && !name.contains(",") && !name.contains("#") && !name.contains("+") && !name.contains("^")
                && !name.contains("$") && !name.contains("!") && !name.contains("?") && !name.contains("%");
    }

    @Override
    public boolean isRightPhone(String phone) {
        return phone.substring(0, 1).equalsIgnoreCase("9") && isRightName(phone);
    }

    @Override
    public String toLowerCase(String text) {

        return text.toLowerCase();
    }

    @Override
    public float px2Dp(int px) {
//        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
        return px / ((float) ContextHelper.retrieveContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public String getTodaysShamsiDate(String pattern) {
        PersianDate date = new PersianDate();
        PersianDateFormat dateFormatter = new PersianDateFormat(pattern);
        return dateFormatter.format(date);
    }

    @Override
    public PersianDate getShamsiDateFromString(String date, String pattern) throws Exception {
        PersianDateFormat dateFormatter = new PersianDateFormat(pattern);
        return dateFormatter.parse(date, pattern);
    }

    @Override
    public String getTodaysTime(String normalPattern, String accuratePattern, boolean accurateMode) {
        PersianDate date = new PersianDate();
        PersianDateFormat dateFormatter = new PersianDateFormat(accurateMode ? accuratePattern : normalPattern);
        return dateFormatter.format(date);
    }

    @Override
    public long getCostCeilOf(double value) {
        if (value == (int) value)
            return (long) value;
        else
            return (long) (Math.floor(value + 1));
    }

    @Override
    public Uri bitmapToUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ContextHelper.retrieveContext().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public String get2DigitsOfDigit(int number) {
        if (number < 0)
            return "-1";
        else if (number < 10)
            return "0" + number;
        else if (number < 100)
            return String.valueOf(number);
        else
            return ContextHelper.retrieveContext().getString(R.string.error);
    }

    @Override
    public PersianDate stringToPersianDateTime(String selectedDate) {
        int year = Integer.parseInt(selectedDate.substring(0, 4));
        int month = Integer.parseInt(selectedDate.substring(5, 7));
        int day = Integer.parseInt(selectedDate.substring(8, 10));
        int hour = Integer.parseInt(selectedDate.substring(11, 13));
        int minute = Integer.parseInt(selectedDate.substring(14, 16));
        return new PersianDate().setShYear(year).setShMonth(month).setShDay(day).setHour(hour).setMinute(minute);
    }

    @Override
    public boolean isValidTimeForIntervalFromNow(PersianDate persianDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.VALID_ORDER_DATE_MINUTE_INTERVAL);
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(persianDate.toDate());
        return gregorianCalendar.compareTo(calendar) >= 0;
    }

    @Override
    public boolean isValidTimeForIntervalFromNow(String persianDate_str) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, Constants.VALID_ORDER_DATE_MINUTE_INTERVAL);
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(stringToPersianDateTime(persianDate_str).toDate());
        return gregorianCalendar.compareTo(calendar) >= 0;
    }

    @Override
    public Bitmap svgToBitmap(int drawableId) {
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

    @Override
    public void setLockedOnGrayScale(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(cf);
        imageView.setImageAlpha(255);   // 128 = 0.
    }

    @Override
    public void setUnlockedForGrayScale(ImageView imageView) {
        imageView.setColorFilter(null);
        imageView.setImageAlpha(255);
    }

    @Override
    public int getMinuteFromSecond(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        return seconds / 60;
    }

    @Override
    public int getSecondFromSeconds(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        return seconds % 60;
    }

    @Override
    public int generateRandomNumber(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    @Override
    public boolean containsNonPersianLanguage(String str) {
        int codePointAt = Character.codePointAt(str, 0);
        return !((codePointAt >= 0x0600 && codePointAt <= 0x06FF) || (codePointAt >= 0xFB50 && codePointAt <= 0xFDFF) || (codePointAt >= 0xFE70 && codePointAt <= 0xFEFF));
    }

    @Override
    public boolean isInteger(String s, int radix) {
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

    @Override
    public String getShamsiDateTimeFromGregortianString(String date_and_time_start) {
        String date = date_and_time_start.split(" ")[0];
        String time = date_and_time_start.split(" ")[1];
        int year = Integer.parseInt(date.split("/")[0]);
        int month = Integer.parseInt(date.split("/")[1]);
        int day = Integer.parseInt(date.split("/")[2]);
        PersianDate persianDate = new PersianDate();
        persianDate.setGrgYear(year).setGrgMonth(month).setGrgDay(day);
        return persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay() + " " + time;
    }

    @Override
    public boolean isPrime(int n) {
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
    @Override
    public void changeStrokeColorToMainAppColor(View view) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setStroke(4, Color.parseColor(getMainAppColor())); // set stroke width and stroke color
    }

    @Override
    public void changeShapeColorToMainAppColor(Drawable drawable) {
        drawable.setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void changeShapeColorToMainAppColor(View view) {
        view.getBackground().setColorFilter(Color.parseColor(getMainAppColor()), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public String getMainAppColor() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_APP_MAIN_COLOR, String.format("#%06X", 0xFFFFFF & ContextHelper.retrieveContext().getResources().getColor(R.color.main_theme_color)));
    }

    @Override
    public String getSharedKey() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null);
    }

    @Override
    public String getToken() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
    }

    @Override
    public boolean hasValidSharedKey() {
        return Setting.getInstance().loadSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null) != null;
    }

    @Override
    public String getPurchaseUnit() {
        return Constants.PURCHASE_UNIT;
    }

    @Override
    public String getSelectedRestaurantDecryptedQRCode() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_SELECTED_RESTAURANT_QR_CODE, null);
    }


    @Override
    public boolean isLoggedIn() {
        return !Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW").equalsIgnoreCase("NEW");
    }

    @Override
    public String getCounterSymbol() {
        return Constants.COUNTER_SYMBOL;
    }

    @Override
    public String getDefaultPrePhone() {
        return Constants.DEFAULT_PHONE_PREFIX;
    }

    @Override
    public long GET_RESEND_VERIFICATION_CODE_INTERVAL() {
        return Constants.RESEND_VERIFICATION_CODE_INTERVAL;
    }

    @Override
    public boolean isFirstUse() {
        return Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, null) == null;

    }

    @Override
    public String hash(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(str.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @Override
    public void showBiOptionsDialog(@NonNull Fragment fragment, @NonNull String title, @NonNull String message, @NonNull String option1,
                                    @NonNull String option2, @Nullable Runnable_SingleArg<Dialog> option1Handler,
                                    @Nullable Runnable_SingleArg<Dialog> option2Handler, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setCancelable(cancelable);
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.dialog_bi_option_layout, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        TextViewPlus title_TV = view.findViewById(R.id.dialog_bi_option_title);
        TextViewPlus message_TV = view.findViewById(R.id.dialog_bi_option_message);
        TextViewPlus option1_TV = view.findViewById(R.id.dialog_bi_option_option1_tv);
        TextViewPlus option2_TV = view.findViewById(R.id.dialog_bi_option_option2_tv);
        title_TV.setText(title);
        message_TV.setText(message);
        option1_TV.setText(option1);
        option2_TV.setText(option2);
        option1_TV.setOnClickListener(v -> {
            if (option1Handler != null)
                option1Handler.run(dialog);
        });
        option2_TV.setOnClickListener(v -> {
            if (option2Handler != null)
                option2Handler.run(dialog);
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
