package ir.ac.kntu.Activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.ac.kntu.Fragment.Fragment_FAQ;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Fragment.Fragment_LandingPage;
import ir.ac.kntu.Fragment.Fragment_Login;
import ir.ac.kntu.Fragment.Fragment_Main;
import ir.ac.kntu.Fragment.Fragment_Setting;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Setting;

/**
 * @author Mohammad Jalili Torkamani
 * @version 1.0
 * @since 2019
 * @since 2019
 * Student No: 9523783
 * Field: Computer Engineering
 * Organization: Kh.N.Toosi University Of Technology
 * <p>
 * Lexeen is the first reservation service, helps people to use restaurants services like
 * reservation or delivery.People can use it to reserve their desired foods in desired date/time
 * or get it at the desired position they chose while ordering from the map.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * main application entry.
     * it configures the first needed settings and checks whether the user has logged in or not
     * and then navigate the user to another fragment
     * @param savedInstanceState to pass to it's super method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.getInstance().setDeviceWidth(getWindowManager());
        Setting.getInstance().setDeviceHeight(getWindowManager());
        Setting.getInstance().configureToasty();
        Setting.getInstance().makeScreenNoLimits(this, true);
        Setting.getInstance().inverseBarColor(this, false);
        setContentView(R.layout.activity_main);

        //manageListeners();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_frame, Helper.getInstance().isLoggedIn() ? new Fragment_LandingPage() : new Fragment_Login())
                .commit();
    }

    /**
     * toasts a message and call the super method
     */
    @Override
    protected void onDestroy() {
        Helper.getInstance().toast(R.string.on_exit_message, Constants.ToastMode.SUCCESS);
        super.onDestroy();
    }

    /**
     * hides Keyboard when clicked outside of an edittext
     * @param ev to check which direction has been operated by the user
     * @return super.dispatchTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText && !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Setting.getInstance().hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * makes status bar white or black by means of the passed fragment
     *
     * @param fragment to check and change status bar color
     */
    private void configureInverseBarColor(Fragment fragment) {
        if (fragment instanceof Fragment_Main || fragment instanceof Fragment_Setting || fragment instanceof Fragment_FAQ)
            Setting.getInstance().inverseBarColor(this, false);
        else if (!(fragment instanceof Fragment_FoodDescriptionDetail))
            Setting.getInstance().inverseBarColor(this, true);
    }

    /**
     * hides status bar by means of the passed fragment
     *
     * @param fragment to check and change scree size
     */
    private void configureScreenLimit(Fragment fragment) {
        if (fragment instanceof Fragment_Setting || fragment instanceof Fragment_FAQ)
            Setting.getInstance().makeScreenNoLimits(this, false);
        else if (!(fragment instanceof Fragment_FoodDescriptionDetail))
            Setting.getInstance().makeScreenNoLimits(this, true);
    }

    /**
     * listens on the fragment stack and operates some UI changes when stack changes
     */
    private void manageListeners() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
            configureScreenLimit(fragment);
            configureInverseBarColor(fragment);
            if (fragment instanceof Fragment_FoodDescriptionDetail || fragment instanceof Fragment_LandingPage) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                getWindow().setStatusBarColor(Color.WHITE);
            }
        });
    }
}