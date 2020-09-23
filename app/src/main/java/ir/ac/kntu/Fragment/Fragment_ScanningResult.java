package ir.ac.kntu.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Interface.Retrofit.Restaurant_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.ButtonPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * scanning result fragment ables user to show scanned restaurant info and enter it
 */
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

    /**
     * fragment entry point which finds views, initialize UI elements and declares listeners
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

    /**
     * starts animation for some ui elements
     *
     * @param view view to work
     */
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

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
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

    /**
     * initializes some UI elements and store CODE which is passed from "Fragment_Scanner"
     *
     * @param view view to work
     */
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

    /**
     * loads data from server and sets value for some UI element
     *
     * @param view view to work
     */
    private void initializeOnlineContents(View view) {
        Connector.createService(view, Restaurant_Server_API.class, object -> object.getRestaurantInfo(code).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

                if (response.body() != null) {
                    restaurant = response.body();
                    add.setVisibility(View.VISIBLE);
                    name.setText(Encryption.getInstance().decrypt(restaurant.getName()));
                    topName.setText(Encryption.getInstance().decrypt(restaurant.getName()));
                    type.setText(Encryption.getInstance().decrypt(restaurant.getType()));
                    phone.setText(Encryption.getInstance().decrypt(restaurant.getPhone()));
                    address.setText(restaurant.getAddress().toString());

                    Picasso.get().load(Encryption.getInstance().decrypt(restaurant.getPictures().get(0))).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            image.setImageBitmap(bitmap);
                            image.setVisibility(View.VISIBLE);
                            imageProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            Helper_Log.errorLog(e, Fragment_ScanningResult.class);
                            imageProgressBar.setVisibility(View.GONE);
                            image.setBackgroundColor(Color.BLACK);
                            image.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

                    startAnimations(view);
                } else {
                    Helper_Log.errorLog(Fragment_ScanningResult.class);
                    Helper.getInstance().toast(R.string.wrong_qr_code, Constants.ToastMode.INFO);
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_ScanningResult.class);
                getFragmentManager().popBackStack();
            }
        }));
    }

    /**
     * declares listeners for some UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
        back.setOnClickListener(v -> getActivity().onBackPressed());
        add.setOnClickListener(v -> {
            if (restaurant != null) {
                Setting.getInstance().saveSetting(Constants._TABLE_USER,
                        Constants._KEY_SELECTED_RESTAURANT_QR_CODE,
                        restaurant.getQrCode());
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
