package ir.ac.kntu.Activity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ir.ac.kntu.Fragment.Fragment_LandingPage;
import ir.ac.kntu.Fragment.Fragment_Login;
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
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_frame, Helper.isLoggedIn() ? new Fragment_LandingPage() : new Fragment_Login())
                .commit();
    }

    @Override
    protected void onDestroy() {
        Helper.toast(R.string.on_exit_message, Constants.ToastMode.ERROR);
        super.onDestroy();
    }
}