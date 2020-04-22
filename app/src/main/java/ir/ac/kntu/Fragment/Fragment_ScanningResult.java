package ir.ac.kntu.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Interface.Retrofit.Operable_General;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.ButtonPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ScanningResult extends Fragment {
    private static String code;
    private static Restaurant restaurant;
    private Animation scanning_qr_result_add_animation;
    private Animation left_to_right_animation;
    private Animation right_lo_left_animation;

    private TextViewPlus nameWord;
    private TextViewPlus name;
    private TextViewPlus topName;
    private TextViewPlus typeWord;
    private TextViewPlus type;
    private TextViewPlus phoneWord;
    private TextViewPlus phone;
    private TextViewPlus addressWord;
    private TextViewPlus address;
    private ImageView image;
    private ImageView back;
    private ButtonPlus add;
    private ProgressBar imageProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanning_result, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        return view;
    }

    private void startAnimations(View view) {
        add.startAnimation(scanning_qr_result_add_animation);
        address.startAnimation(right_lo_left_animation);
        addressWord.startAnimation(right_lo_left_animation);
        name.startAnimation(left_to_right_animation);
        nameWord.startAnimation(right_lo_left_animation);
        type.startAnimation(left_to_right_animation);
        typeWord.startAnimation(right_lo_left_animation);
        phone.startAnimation(left_to_right_animation);
        phoneWord.startAnimation(right_lo_left_animation);
    }

    private void findViews(View view) {
        name = view.findViewById(R.id.scanning_qr_result_name_value);
        nameWord = view.findViewById(R.id.scanning_qr_result_name_word);
        topName = view.findViewById(R.id.scanning_qr_result_restaurant_name);
        type = view.findViewById(R.id.scanning_qr_result_type_value);
        typeWord = view.findViewById(R.id.scanning_qr_result_type_word);
        phone = view.findViewById(R.id.scanning_qr_result_address_phone_value);
        phoneWord = view.findViewById(R.id.scanning_qr_result_address_phone_word);
        image = view.findViewById(R.id.scanning_qr_result_restaurant_image_container_image);
        addressWord = view.findViewById(R.id.scanning_qr_result_address_word);
        address = view.findViewById(R.id.scanning_qr_result_address_value);
        imageProgressBar = view.findViewById(R.id.scanning_qr_result_restaurant_image_container_progressbar);
        add = view.findViewById(R.id.scanning_qr_result_add);
        back = view.findViewById(R.id.scanning_qr_result_back);
    }

    private void initializeViewContents(View view) {
        add.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        restaurant = null;
        if (getArguments() != null)
            code = getArguments().getString("CODE");
        else
            Helper.getInstance().toast(getString(R.string.error_in_code_reading), Constants.ToastMode.ERROR);


        imageProgressBar.setVisibility(View.GONE);
        imageProgressBar.setVisibility(View.VISIBLE);
        add.setVisibility(View.GONE);
        scanning_qr_result_add_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_up);
        left_to_right_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_ltr);
        right_lo_left_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_rtl);
    }

    private void initializeOnlineContents(View view) {
        Connector.createService(view, Operable_General.class, object -> object.getRestaurantInfo(code).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

                if (response.body() != null) {
                    restaurant = response.body();
                    add.setVisibility(View.VISIBLE);
                    name.setText(restaurant.getName());
                    topName.setText(restaurant.getName());
                    type.setText(restaurant.getType());
                    phone.setText(restaurant.getPhone());
                    address.setText(restaurant.getAddress().toString());
                    ImageLoader.getInstance().displayImage(restaurant.getPictures().get(0), image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            Helper_Log.errorLog(failReason.getCause(), Fragment_ScanningResult.class);
                            imageProgressBar.setVisibility(View.GONE);
                            image.setBackgroundColor(Color.BLACK);
                            image.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            image.setVisibility(View.VISIBLE);
                            imageProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            imageProgressBar.setVisibility(View.GONE);
                            image.setBackgroundColor(Color.BLACK);
                            image.setVisibility(View.VISIBLE);
                        }
                    });
                    startAnimations(view);
                } else {
                    Helper_Log.errorLog(Fragment_ScanningResult.class);
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_ScanningResult.class);
            }
        }));
    }

    private void manageListeners(View view) {
        back.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.main_frame, new Fragment_LandingPage())
                .commit());
        add.setOnClickListener(v -> {
            if (restaurant != null) {
                Setting.getInstance().saveSetting(Constants._TABLE_USER,
                        Constants._KEY_RESTAURANT_SELECTION_QR_CODE,
                        restaurant.getEncryptedCode());
                if (Database.getInstance(ContextHelper.retrieveContext(),
                        Constants._MAIN_DATABASE).restaurantInterface().getRestaurant(restaurant.getId()) == null) {
                    Database.getInstance(ContextHelper.retrieveContext(),
                            Constants._MAIN_DATABASE).restaurantInterface().insert(restaurant);
                }
                Fragment_Main mainFragment = new Fragment_Main();
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.main_frame, mainFragment)
                        .commit();

            }
        });
    }
}
