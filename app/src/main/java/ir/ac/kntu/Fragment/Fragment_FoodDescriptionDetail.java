package ir.ac.kntu.Fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;

/**
 * food description fragment ables user to see details about a food
 */
public class Fragment_FoodDescriptionDetail extends Fragment {
    private static int id;
    private static Food food;
    private ImageView backIcon;
    private ImageBadgeView buyIcon;
    private Animation food_description_calorie_icon_slide_rtl_animation;
    private Animation food_description_time_iconslide_ltr_animation;
    private Animation food_description_linearLayout_bottom_slide_up_animation;
    private Animation number_slide_down;
    private Animation food_description_price_scale_animation;

    private ImageView expandedImage;
    private CollapsingToolbarLayout expandFoodName;
    private TextViewPlus mealType;
    private TextViewPlus mealTypeInfo;
    private TextViewPlus cookTimeText;
    private TextViewPlus calorieText;
    private TextViewPlus priceText;
    private TextViewPlus priceUnit;
    private TextViewPlus description;
    private TextViewPlus addToCartText;
    private ImageView plus;
    private ImageView minus;
    private TextViewPlus counter;
    private TextViewPlus foodName;

    //animated views
    private ImageView food_description_calorie_icon;
    private ImageView food_description_time_icon;
    private ImageView food_description_price_icon;
    private LinearLayout food_description_linearLayout_bottom;

    /**
     * postpones enter transition
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }

    /**
     * fragment entry point which finds view and initialize UI elements and declares listeners and
     * starts animations
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_description_detail, container, false);
        findViews(view);
        initializeViewContents(view);
        setAnimations();
        manageListeners(view);
        initializeServerSupplied(view, food -> {

            Picasso.get().load(Encryption.getInstance().decrypt(food.getPictures().get(0))).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    expandedImage.setImageBitmap(bitmap);
                    startPostponedEnterTransition();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    startPostponedEnterTransition();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    startPostponedEnterTransition();
                }
            });
            foodName.setText(Encryption.getInstance().decrypt(food.getName()));
            expandFoodName.setTitle(Encryption.getInstance().decrypt(food.getName()));
            expandFoodName.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);

            priceText.setText(Helper.getInstance().getOneDigitOrNon(food.getPrice(), true));
            priceUnit.setText(Helper.getInstance().getPurchaseUnit());

            calorieText.setText(Helper.getInstance().getOneDigitOrNon(food.getCalories(), false) + " " + getActivity().getString(R.string.cal));
            cookTimeText.setText(food.getCookTimeMinutes() + " " + getString(R.string.min));
            description.setText(Encryption.getInstance().decrypt(food.getDescription()));
            select_title_zero_and_one(view);
        }, () -> {
            Helper_Log.errorLog(Fragment_FoodDescriptionDetail.class);
            backIcon.callOnClick();
        });
        return view;
    }

    /**
     * loads food tags (titles) from server into UI elements
     *
     * @param view view to work
     */
    private void select_title_zero_and_one(View view) {
        Connector.createService(view, Food_Server_API.class, object -> object.getFoodField(id).enqueue(new retrofit2.Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    String retVal = Encryption.getInstance().decrypt(response.body().get(0));
                    if (retVal.equalsIgnoreCase("offer")) {
                        mealType.setText("غذای پیشنهادی ما");
                        mealTypeInfo.setText("%" + response.body().get(1) + "  تخفیف!");
                    } else if (retVal.equalsIgnoreCase("popular")) {
                        mealType.setText("غذای پر طرفدار");
                        mealTypeInfo.setText("امتحان کنید !");
                    } else {
                        mealType.setVisibility(View.GONE);
                        mealTypeInfo.setVisibility(View.GONE);
                    }
                } else
                    Helper_Log.errorLog(Fragment_FoodDescriptionDetail.class);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_FoodDescriptionDetail.class);
            }
        }));
    }

    /**
     * loads info from server and populates elements and runs runnable
     *
     * @param view         view to work
     * @param foodLoadWork runnable to run after success loading
     * @param loadFailWork runnable to run after failed loading
     */
    private void initializeServerSupplied(View view, Runnable_SingleArg<Food> foodLoadWork, Runnable loadFailWork) {
        Connector.createService(view, Food_Server_API.class, object -> {
            object.getFood(id).enqueue(new retrofit2.Callback<ir.ac.kntu.Entity.Food>() {
                @Override
                public void onResponse(Call<ir.ac.kntu.Entity.Food> call, Response<ir.ac.kntu.Entity.Food> response) {
                    if (response.body() != null) {
                        food = response.body();
                        foodLoadWork.run(response.body());
                    } else {
                        Helper_Log.errorLog(Fragment_FoodDescriptionDetail.class);
                        loadFailWork.run();
                    }
                }

                @Override
                public void onFailure(Call<ir.ac.kntu.Entity.Food> call, Throwable t) {
                    Helper_Log.errorLog(t, Fragment_FoodDescriptionDetail.class);
                    loadFailWork.run();
                }
            });
        });
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        food_description_calorie_icon = view.findViewById(R.id.food_description_calorie_icon);
        food_description_price_icon = view.findViewById(R.id.food_description_price_icon);
        food_description_time_icon = view.findViewById(R.id.food_description_time_icon);
        food_description_linearLayout_bottom = view.findViewById(R.id.food_description_linearLayout_bottom);
        backIcon = view.findViewById(R.id.food_description_back_icon);
        buyIcon = view.findViewById(R.id.food_description_buy_icon);
        expandedImage = view.findViewById(R.id.food_description_expandedImage);
        expandFoodName = view.findViewById(R.id.food_description_collapsing_toolbar_layout);
        mealType = view.findViewById(R.id.food_description_title_1);
        mealTypeInfo = view.findViewById(R.id.food_description_title_2);
        cookTimeText = view.findViewById(R.id.food_description_time);
        calorieText = view.findViewById(R.id.food_description_calorie);
        priceText = view.findViewById(R.id.food_description_price);
        priceUnit = view.findViewById(R.id.food_description_price_unit);
        description = view.findViewById(R.id.food_description_description);
        addToCartText = view.findViewById(R.id.food_description_add_to_cart);
        plus = view.findViewById(R.id.food_description_plus);
        minus = view.findViewById(R.id.food_description_minus);
        counter = view.findViewById(R.id.food_description_counter);
        foodName = view.findViewById(R.id.food_description_food_name);
    }

    /**
     * initializes some UI elements
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        food_description_linearLayout_bottom.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        id = getArguments().getInt("Food_ID", -1);
        expandedImage.setColorFilter(ContextHelper.retrieveContext().getResources().getColor(R.color.food_description_black_mask_color));
        buyIcon.setMaxBadgeValue(Constants.MAX_ADD_TO_CART_NUMBER);
        buyIcon.setBadgeTextFont(Typeface.createFromAsset(ContextHelper.retrieveContext().getAssets(), "fonts/farsi/syekan.otf"));
        int badgeCounterValue = Bill.getTotalFoodItems(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()));
        if (badgeCounterValue == 0)
            buyIcon.clearBadge();
        else
            buyIcon.setBadgeValue(badgeCounterValue);
        if (!Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithFoodID(id, Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).isEmpty()) {
            counter.setText(String.valueOf(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithFoodID(id, Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).get(0).getCounter()));
        } else {
            counter.setText("0");
        }
        food_description_calorie_icon_slide_rtl_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_rtl);
        food_description_time_iconslide_ltr_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_ltr);
        food_description_linearLayout_bottom_slide_up_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_up);
        food_description_price_scale_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both);
        number_slide_down = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_down);
    }

    /**
     * declares listeners for some UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
        backIcon.setOnClickListener(v -> getActivity().onBackPressed());
        buyIcon.setOnClickListener(v -> {
            Fragment toOpen = new Fragment_Main();
            Bundle bundle = new Bundle();
            bundle.putInt("TAB_ID", R.id.tab_shop);
            toOpen.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.main_frame, toOpen)
                    .commit();
        });
        plus.setOnClickListener(v -> {
            if (Integer.parseInt(counter.getText().toString().trim()) < Constants.MAX_ADD_TO_CART_NUMBER) {
                addToCartText.setText(R.string.add_to_cart);
                int number = Integer.parseInt(counter.getText().toString().trim()) + 1;
                counter.startAnimation(food_description_linearLayout_bottom_slide_up_animation);
                counter.setText(String.valueOf(number));
            }
        });
        minus.setOnClickListener(v -> {
            if (Integer.parseInt(counter.getText().toString().trim()) > 0) {
                int number = Integer.parseInt(counter.getText().toString().trim()) - 1;
                if (number == 0)
                    addToCartText.setText(R.string.delete_from_cart);
                counter.startAnimation(number_slide_down);
                counter.setText(String.valueOf(number));
            }
        });
        addToCartText.setOnClickListener(v -> {
            Fragment_Cart.addToCart(id, Integer.parseInt(counter.getText().toString().trim()), false);
            if (food != null) {
                if (Integer.parseInt(counter.getText().toString().trim()) > 0)
                    Helper.getInstance().toast(counter.getText().toString().trim() + " " + Encryption.getInstance().decrypt(food.getName()) + " به سبد خرید اضافه شد", Constants.ToastMode.SUCCESS);
                else
                    Helper.getInstance().toast(Encryption.getInstance().decrypt(food.getName()) + " از سبد خرید حذف شد", Constants.ToastMode.SUCCESS);
            }
            int counter = Bill.getTotalFoodItems(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()));
            if (counter == 0)
                buyIcon.clearBadge();
            else
                buyIcon.setBadgeValue(counter);
        });
    }

    /**
     * stars animation for some ui elements
     */
    private void setAnimations() {
        food_description_calorie_icon.startAnimation(food_description_calorie_icon_slide_rtl_animation);
        food_description_time_icon.startAnimation(food_description_time_iconslide_ltr_animation);
        food_description_price_icon.startAnimation(food_description_price_scale_animation);
        food_description_linearLayout_bottom.startAnimation(food_description_linearLayout_bottom_slide_up_animation);
    }
}
