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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.getInstance().setDeviceWidth(getWindowManager());
        Setting.getInstance().setDeviceHeight(getWindowManager());
        Setting.getInstance().configureToasty();
        Setting.getInstance().configureImageLoader();
        Setting.getInstance().makeScreenNoLimits(this, true);
        Setting.getInstance().inverseBarColor(this, false);
        setContentView(R.layout.activity_main);
//        manageListeners();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_frame, Helper.getInstance().isLoggedIn() ? new Fragment_LandingPage() : new Fragment_Login())
                .commit();
    }

    @Override
    protected void onDestroy() {
        Helper.getInstance().toast(R.string.on_exit_message, Constants.ToastMode.ERROR);
        super.onDestroy();
    }


    /**
     * hides Keyboard when clicked outside of an edittext
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Setting.getInstance().hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

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

    private void configureInverseBarColor(Fragment fragment) {
        if (fragment instanceof Fragment_Main || fragment instanceof Fragment_Setting || fragment instanceof Fragment_FAQ)
            Setting.getInstance().inverseBarColor(this, false);
        else if (!(fragment instanceof Fragment_FoodDescriptionDetail))
            Setting.getInstance().inverseBarColor(this, true);
    }

    private void configureScreenLimit(Fragment fragment) {
        if (fragment instanceof Fragment_Setting || fragment instanceof Fragment_FAQ)
            Setting.getInstance().makeScreenNoLimits(this, false);
        else if (!(fragment instanceof Fragment_FoodDescriptionDetail))
            Setting.getInstance().makeScreenNoLimits(this, true);
    }

}