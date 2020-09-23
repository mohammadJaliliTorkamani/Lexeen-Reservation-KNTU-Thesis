package ir.ac.kntu.Technical.Other.Other;

/**
 * Constants used in app
 */
public class Constants {
    public static final String TAG = "_LEXEEN_";
    public static final int MAX_ADD_TO_CART_NUMBER = 100;
    public static final int VALID_ORDER_DATE_MINUTE_INTERVAL = 30;
    public static final String _TABLE_USER = "User_Table";
    public static final String _TABLE_PROFILE = "Profile_Table";
    public static final String _MAIN_DATABASE = "database_1";
    public static final String _DATABASE_ORDER_TABLE = "Order_Table";
    public static final String _DATABASE_BILL_TABLE = "Bill_Table";
    public static final String _DATABASE_RESTAURANT_TABLE = "Restaurant_Table";
    public static final String _KEY_FIRST_USE_STATE = "_key_first_use_state";
    public static final String _KEY_SHARED_KEY = "_key_shared_key";
    public static final String _KEY_LOGIN_STATE = "_key_login_state";
    public static final String _KEY_TOKEN = "_key_token";
    public static final String _KEY_SELECTED_RESTAURANT_QR_CODE = "_key_selected_restaurant_qr_code";
    public static final String _KEY_APP_MAIN_COLOR = "_key_app_main_color";
    public static final String ABAN_WEBSITE = "https://lexeen.ir";
    public static final String BITMAPS_DIRECTORY_NAME = "Lexin";
    public static final int PASSWORD_MINIMUM_LENGTH = 8;
    public static final String _KEY_TOOLTIP_SHOWN = "_key_tooltip_shown";
    public static final String BAZAAR_PACKAGE_NAME = "com.farsitel.bazaar";
    public static final String COUNTER_SYMBOL = "x";
    public static final String DEFAULT_PHONE_PREFIX = "0098";
    public static final long RESEND_VERIFICATION_CODE_INTERVAL = 90 * 1000;
    public static final String PURCHASE_UNIT = "T";
    public static final long CONNECTION_READ_TIMEOUT = 10;
    public static final long CONNECTION_WRITE_TIMEOUT = 10;
    public static final long CONNECTION_TIMEOUT = 10;
    public static final String BASE_URL = "APIs URL GOES HERE";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String DEFAULT_FARSI_FONT_ASSET_ADDRESS = "fonts/farsi/syekan.otf";

    public enum ToastMode {
        SUCCESS, INFO, WARNING, ERROR, NORMAL
    }
}
