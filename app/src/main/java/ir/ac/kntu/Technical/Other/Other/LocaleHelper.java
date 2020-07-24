package ir.ac.kntu.Technical.Other.Other;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * locale getter and setter
 */
public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.getInstance().Selected.Language";

    /**
     * sets locale to default language on attached
     *
     * @param context to work with
     * @return new context
     */
    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    /**
     * sets locale to passed language on attached
     *
     * @param context         to work with
     * @param defaultLanguage language to set as locale
     * @return new context
     */
    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    /**
     * retrieves current language
     *
     * @param context to wotk
     * @return current language
     */
    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    /**
     * sets locale as current
     *
     * @param context  to work
     * @param language new locale
     * @return new context
     */
    public static Context setLocale(Context context, String language) {
        persist(context, language.toLowerCase());
        Context new_context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            new_context = updateResources(context, language.toLowerCase());
        } else
            new_context = updateResourcesLegacy(context, language.toLowerCase());

        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language.toLowerCase().toLowerCase()));
        res.updateConfiguration(conf, dm);
        return new_context;
    }

    /**
     * loads current language from shared preferences
     *
     * @param context         to work
     * @param defaultLanguage default language if not set before and not found
     * @return current language
     */
    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    /**
     * saves language to sharedpreferences
     *
     * @param context  to work
     * @param language language to save
     */
    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    /**
     * updates resources to the new language
     *
     * @param context  to work
     * @param language to update into.
     * @return new context
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    /**
     * updates  resources to the new language (updates whole legacy)
     *
     * @param context  to work
     * @param language to update into
     * @return new context
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}