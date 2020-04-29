package ir.ac.kntu.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roughike.bottombar.BottomBar;
import com.tooltip.Tooltip;

import ir.ac.kntu.Adapter.Adapter_Drawer;
import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.NormalUser;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
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

public class Fragment_Main extends Fragment {

    private Toolbar toolbar;
    private ImageView expand_toolbar_iv;
    private ImageView landingPage;
    private ImageView search_iv;
    private ImageView drawerHeader;
    private ImageView drawerHeaderUser;
    private BottomBar bottomBar;
    private DrawerLayout drawerLayout;
    private TextViewPlus drawer_profileName;
    private ConstraintLayout logoutContainer;
    private TextViewPlus drawer_cash;
    private TextViewPlus drawer_cash_unit;
    private RecyclerView drawer_rv;
    private RecyclerView.Adapter drawer_rv_adapter;
    private RecyclerView.LayoutManager drawer_rv_layout_manager;
    private TextViewPlus topName;
    private View view;
    private int tabID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_main, container, false);
        tabID = getArguments() != null ? getArguments().getInt("TAB_ID") : R.id.tab_home;
        findViews(view);
        Setting.getInstance().inverseBarColor(getActivity(), false);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        bottomBar.selectTabWithId(tabID);
        return view;
    }

    private void initializeOnlineContents(View view) {
        if (!Setting.getInstance().isConnected()) {
            Helper.getInstance().toast(R.string.not_connected_to_internet, Constants.ToastMode.WARNING);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Setting.getInstance().loadSetting(Constants._TABLE_USER, Constants._KEY_TOOLTIP_SHOWN, null) == null) {
            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_TOOLTIP_SHOWN, "TRUE");
            showTooltip(landingPage, getResources().getString(R.string.Lexin_tooltip), search_iv);
        }
    }

    private void manageListeners(View view) {


        logoutContainer.setOnClickListener(v -> Connector.createService(view, Account_Server_API.class, object -> object.logOut().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                        case DONE:
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW");
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_RESTAURANT_SELECTION_ENCRYPTED_QR_CODE, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, null);
                            Helper.getInstance().toast(R.string.log_out_successfully, Constants.ToastMode.SUCCESS);
                            Intent intent = new Intent();
                            Activity activity = getActivity();
                            intent.setClass(activity, activity.getClass());
                            activity.startActivity(intent);
                            activity.finish();
                            break;
                        case FAILED:
                        case UNKNOWN:
                            Helper.getInstance().toast(ContextHelper.retrieveContext().getString(R.string.error) + " : " + response.body().getMessage(), Constants.ToastMode.ERROR);
                            break;
                    }
                } else {
                    Helper_Log.errorLog(Adapter_Drawer.class);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Helper_Log.errorLog(t, Adapter_Drawer.class);
            }
        })));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                initDrawer_InfoMode(view);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        expand_toolbar_iv.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        search_iv.setOnClickListener(v -> {
            Setting.getInstance().hideKeyboard(getActivity());
            if (!(getFragmentManager().findFragmentById(R.id.main_fragment_top_bb_frame) instanceof Fragment_Search))
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("search")
                        .add(R.id.main_fragment_top_bb_frame, new Fragment_Search())
                        .commit();
        });

        bottomBar.setOnTabSelectListener(tabId -> {
            Fragment fragment = null;
            switch (tabId) {
                case R.id.tab_home:
                    fragment = new Fragment_Home();
                    break;
                case R.id.tab_menu:
                    fragment = new Fragment_Menu();
                    break;
                case R.id.tab_shop:
                    fragment = new Fragment_Cart();
                    break;
            }
            if (fragment != null)
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(tabId == R.id.tab_home ? " home" : tabId == R.id.tab_menu ? "menu" : "cart")
                        .add(R.id.main_fragment_top_bb_frame, fragment)
                        .commit();
        });

        landingPage.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_frame, new Fragment_LandingPage())
                .commit());
    }

    private void findViews(View view) {
        logoutContainer = view.findViewById(R.id.drawer_list_log_out_container);
        toolbar = view.findViewById(R.id.main_fragment_toolbar);
        topName = view.findViewById(R.id.main_fragment_top_text);
        expand_toolbar_iv = view.findViewById(R.id.main_fragment_toolbar_expand_iv);
        search_iv = view.findViewById(R.id.main_fragment_toolbar_iv_search);
        landingPage = view.findViewById(R.id.main_fragment_toolbar_lading_page);
        bottomBar = view.findViewById(R.id.main_fragment_bottombar);
        drawerHeaderUser = view.findViewById(R.id.drawer_top_header_user_image);
        drawerHeader = view.findViewById(R.id.drawer_top_header);
        drawerLayout = view.findViewById(R.id.main_fragment_drawer_layout);
        drawer_cash = view.findViewById(R.id.drawer_cash);
        drawer_cash_unit = view.findViewById(R.id.drawer_cash_unit);
        drawer_profileName = view.findViewById(R.id.drawer_profile_name);
        drawer_rv = view.findViewById(R.id.drawer_list_rv);
    }

    private void initializeViewContents(View view) {
        logoutContainer.setVisibility(Helper.getInstance().isLoggedIn() ? View.VISIBLE : View.GONE);
        drawerHeader.setBackgroundResource(R.drawable.ic_nav_rec);
        Helper.getInstance().changeShapeColorToMainAppColor(drawerHeader.getDrawable());
        toolbar.setVisibility(View.VISIBLE);
        Restaurant restaurant = Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).restaurantInterface().getRestaurant(Helper.getInstance().getRestaurantSelectionQRCode());
        if (restaurant != null)
            try {
                topName.setText(Encryption.getInstance().decrypt(restaurant.getName()));
            } catch (Exception e) {
                Helper_Log.errorLog(e, Fragment_Main.class);
            }
        bottomBar.setSoundEffectsEnabled(false);
        bottomBar.setTabTitleTypeface("fonts/farsi/syekan.otf");

        drawer_rv_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        drawer_rv.setHasFixedSize(true);
        drawer_rv.setLayoutManager(drawer_rv_layout_manager);
        drawer_rv_adapter = new Adapter_Drawer(view, getFragmentManager(), getActivity(), drawerLayout);
        drawer_rv.setAdapter(drawer_rv_adapter);
    }

    private void initDrawer_InfoMode(View view) {
        drawerHeaderUser.setVisibility(Helper.getInstance().isLoggedIn() ? View.VISIBLE : View.GONE);
        Connector.createService(view, Account_Server_API.class, object -> object.getDrawerContent().enqueue(new Callback<NormalUser>() {
            @Override
            public void onResponse(Call<NormalUser> call, Response<NormalUser> response) {
                if (response.body() != null) {
                    NormalUser normalUser = response.body();
                    try {
                        normalUser.setName(Encryption.getInstance().decrypt(normalUser.getName()));
                        normalUser.setLastName(Encryption.getInstance().decrypt(normalUser.getLastName()));
                        drawer_profileName.setText(normalUser.getName() + " " + normalUser.getLastName());
                        drawer_cash.setText(Helper.getInstance().getOneDigitOrNon(normalUser.getCash(), false));
                        drawer_cash_unit.setText(Helper.getInstance().getPurchaseUnit());
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Fragment_Main.class);
                    }
                } else {
                    Helper_Log.errorLog(Fragment_Main.class);
                    initDrawer_ErrorMode(view);
                }
            }

            @Override
            public void onFailure(Call<NormalUser> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_Main.class);
                initDrawer_ErrorMode(view);
            }
        }));
    }

    private void initDrawer_ErrorMode(View view) {
        drawer_profileName.setText("خطا");
        drawer_cash.setText("-1");
        drawer_cash_unit.setText("");
    }

    private void showTooltip(ImageView img, String msg, ImageView img1) {
        new Tooltip.Builder(img)
                .setText(msg)
                .setArrowEnabled(true)
                .setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()))
                .setCornerRadius(15f)
                .setTypeface(Typeface.createFromAsset(ContextHelper.retrieveContext().getAssets(), "fonts/farsi/syekan.otf"))
                .setArrowWidth(70f)
                .setTextColor(getResources().getColor(R.color.white))
                .setOnClickListener(tooltip1 -> {
                    tooltip1.dismiss();

                    new Tooltip.Builder(img1)
                            .setText(getResources().getString(R.string.search_your_favorite_food_easily))
                            .setArrowEnabled(true)
                            .setTypeface(Typeface.createFromAsset(ContextHelper.retrieveContext().getAssets(), "fonts/farsi/syekan.otf"))
                            .setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()))
                            .setCornerRadius(15f)
                            .setArrowWidth(70f)
                            .setTextColor(getResources().getColor(R.color.white))
                            .setOnClickListener(tooltip11 -> {
                                Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_FIRST_USE_STATE, "FALSE");
                                tooltip11.dismiss();
                            })
                            .show();
                })
                .show();
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }

    public void updateBadge() {
        int counter = Bill.getTotalFoodItems(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getRestaurantSelectionQRCode()));
        bottomBar.getTabAtPosition(2).setBadgeCount(counter);
    }
}
