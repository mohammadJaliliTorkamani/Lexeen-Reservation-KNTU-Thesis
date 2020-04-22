package ir.ac.kntu.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_Classes;
import ir.ac.kntu.Adapter.Adapter_Drawer;
import ir.ac.kntu.Adapter.Adapter_LandingPage;
import ir.ac.kntu.Adapter.Adapter_VersionDescriptor;
import ir.ac.kntu.Entity.NormalUser;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Entity.RestaurantClass;
import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Entity.VersionFeature;
import ir.ac.kntu.Interface.Retrofit.Operable_General;
import ir.ac.kntu.Interface.Retrofit.Operable_User;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.EditTextPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import ir.ac.kntu.Technical.Other.Other.SpannableGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_LandingPage extends Fragment {
    private static final int SCANNER_ITEM_POSITION = 3;
    private RecyclerView mainRecyclerView;
    private ConstraintLayout logoutContainer;
    private RecyclerView.Adapter mainAdapter;
    private ImageView expand;
    private ImageView header;
    private RecyclerView.LayoutManager main_layout_manager;
    private List<Restaurant> main_list = new LinkedList<>();
    private List<RestaurantClass> classes_list = new LinkedList<>();
    private DrawerLayout drawerLayout;
    private TextViewPlus drawer_profileName;
    private ImageView filter;
    private EditTextPlus searchBar;
    private TextViewPlus restaurants_of;
    private TextViewPlus drawer_cash;
    private TextViewPlus drawer_cash_unit;
    private RecyclerView drawer_rv;
    private RecyclerView.Adapter drawer_rv_adapter;
    private RecyclerView.LayoutManager drawer_rv_layout_manager;
    private ImageView drawerHeaderUser;
    private RecyclerView classes_rv;
    private RecyclerView.Adapter classes_adapter;
    private RecyclerView.LayoutManager classes_layout_manager;

    public static void runScanner(Activity activity, FragmentManager fragmentManager, String code) {
        Fragment scannerFragment = new Fragment_Scanner();
        Bundle bundle = new Bundle();
        bundle.putString("CODE", code);
        scannerFragment.setArguments(bundle);
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.VIBRATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack("scanner")
                                .add(R.id.main_frame, scannerFragment)
                                .commitAllowingStateLoss();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing_page, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        filter = view.findViewById(R.id.landing_page_filter);
        searchBar = view.findViewById(R.id.landing_page_search_et);
        logoutContainer = view.findViewById(R.id.drawer_list_log_out_container);
        header = view.findViewById(R.id.drawer_top_header);
        expand = view.findViewById(R.id.landing_page_toolbar_expand);
        mainRecyclerView = view.findViewById(R.id.landing_page_recyclerview);
        drawerLayout = view.findViewById(R.id.landing_page_view);
        drawerHeaderUser = view.findViewById(R.id.drawer_top_header_user_image);
        drawer_cash = view.findViewById(R.id.drawer_cash);
        drawer_cash_unit = view.findViewById(R.id.drawer_cash_unit);
        drawer_profileName = view.findViewById(R.id.drawer_profile_name);
        drawer_rv = view.findViewById(R.id.drawer_list_rv);
        classes_rv = view.findViewById(R.id.landing_page_restaurant_classes_filter_rv);
        restaurants_of = view.findViewById(R.id.landing_page_restaurants_of);
    }

    private void initializeViewContents(View view) {

        restaurants_of.setVisibility(View.GONE);
        classes_rv.setVisibility(View.GONE);
        logoutContainer.setVisibility(Helper.getInstance().isLoggedIn() ? View.VISIBLE : View.GONE);
        drawerHeaderUser.setVisibility(Helper.getInstance().isLoggedIn() ? View.VISIBLE : View.GONE);
        header.setBackgroundResource(R.drawable.ic_nav_rec);
        Helper.getInstance().changeShapeColorToMainAppColor(header.getDrawable());
        mainRecyclerView.setHasFixedSize(true);
        main_layout_manager = new SpannableGridLayoutManager(
                position -> {
                    if (position % 3 == 0) {
                        return new SpannableGridLayoutManager.SpanInfo(2, 1);
                    } else {
                        return new SpannableGridLayoutManager.SpanInfo(1, 1);
                    }
                },
                2, 1.05f);

        mainRecyclerView.setLayoutManager(main_layout_manager);
        mainAdapter = new Adapter_LandingPage(getActivity(), getFragmentManager(), main_list, SCANNER_ITEM_POSITION);
        mainRecyclerView.setAdapter(mainAdapter);


        drawer_rv_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        drawer_rv.setHasFixedSize(true);
        drawer_rv.setLayoutManager(drawer_rv_layout_manager);
        drawer_rv_adapter = new Adapter_Drawer(view, getFragmentManager(), getActivity(), drawerLayout);
        drawer_rv.setAdapter(drawer_rv_adapter);

        classes_rv.setHasFixedSize(true);
        classes_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        classes_rv.setLayoutManager(classes_layout_manager);
        classes_adapter = new Adapter_Classes(classes_list, restaurantClass -> Connector.createService(view, Operable_General.class, object -> object.getRestaurantList(restaurantClass.getName()).enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.body() != null) {
                    main_list.clear();
                    main_list.addAll(response.body());
                    if (main_list.size() >= SCANNER_ITEM_POSITION - 1)
                        main_list.add(SCANNER_ITEM_POSITION - 1, null);
                    else
                        main_list.add(null);
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Helper_Log.errorLog(Fragment_LandingPage.class);
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_LandingPage.class);
            }
        })));
        classes_rv.setAdapter(classes_adapter);

    }

    private void manageListeners(View view) {
        filter.setOnClickListener(v -> {
            classes_rv.setVisibility(classes_rv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            restaurants_of.setVisibility(restaurants_of.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        logoutContainer.setOnClickListener(v -> Connector.createService(view, Operable_User.class, object -> object.logOut().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                        case DONE:
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_LOGIN_STATE, "NEW");
                            Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_TOKEN, null);
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_RESTAURANT_SELECTION_QR_CODE, null);
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
        expand.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.RIGHT));
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
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter.setVisibility(s.length() > 0 ? View.INVISIBLE : View.VISIBLE);
                classes_rv.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);

                Connector.createService(view, Operable_General.class, object -> {
                    String restaurantName = searchBar.getText().toString();
                    object.searchRestaurants(restaurantName).enqueue(new Callback<List<Restaurant>>() {
                        @Override
                        public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                            if (response.body() != null) {
                                List<Restaurant> restaurants = response.body();
                                main_list.clear();
                                main_list.addAll(restaurants);
                                if (restaurants.size() >= SCANNER_ITEM_POSITION - 1)
                                    main_list.add(SCANNER_ITEM_POSITION - 1, null);
                                else
                                    main_list.add(null);
                                mainAdapter.notifyDataSetChanged();
                            } else {

                                Log.d("AAA", "AAAAAAAAAAAAAAAA");
                                Helper_Log.errorLog(Fragment_LandingPage.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_LandingPage.class);
                        }
                    });
                });
            }
        });
    }

    private void initDrawer_InfoMode(View view) {
        drawerHeaderUser.setVisibility(Helper.getInstance().isLoggedIn() ? View.VISIBLE : View.GONE);
        Connector.createService(view, Operable_User.class, object -> object.getDrawerContent().enqueue(new Callback<NormalUser>() {
            @Override
            public void onResponse(Call<NormalUser> call, Response<NormalUser> response) {
                if (response.body() != null) {
                    try {
                        NormalUser normalUser = response.body();
                        drawer_profileName.setText(Encryption.getInstance().decrypt(normalUser.getName()) + " " + Encryption.getInstance().decrypt(normalUser.getLastName()));
                        drawer_cash.setText(!normalUser.getPhone().equalsIgnoreCase("-1") ? Helper.getInstance().getOneDigitOrNon(normalUser.getCash(), false) : " ");
                        drawer_cash_unit.setText(!normalUser.getPhone().equalsIgnoreCase("-1") ? Helper.getInstance().getPurchaseUnit() : "");
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Fragment_LocationPicker.class);
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

    private void initializeOnlineContents(View view) {
        Connector.createService(view, Operable_General.class, object -> object.isLatestAppVersion().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                        case DONE:
                            Setting.getInstance().saveSetting(Constants._TABLE_USER, Constants._KEY_APP_MAIN_COLOR, response.body().getMessage());

                            Connector.createService(view, Operable_General.class, object -> object.getRestaurantClasses().enqueue(new Callback<List<RestaurantClass>>() {
                                @Override
                                public void onResponse(Call<List<RestaurantClass>> call, Response<List<RestaurantClass>> response) {
                                    if (response.body() != null) {
                                        classes_list.clear();
                                        classes_list.addAll(response.body());
                                        classes_adapter.notifyDataSetChanged();

                                        Connector.createService(view, Operable_General.class, object -> {
                                            object.getRestaurantList(classes_list.get(0).getName()).enqueue(new Callback<List<Restaurant>>() {
                                                @Override
                                                public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                                                    if (response.body() != null) {
                                                        List<Restaurant> restaurants = response.body();
                                                        main_list.clear();
                                                        main_list.addAll(restaurants);
                                                        if (restaurants.size() >= SCANNER_ITEM_POSITION - 1)
                                                            main_list.add(SCANNER_ITEM_POSITION - 1, null);
                                                        else
                                                            main_list.add(null);
                                                        mainAdapter.notifyDataSetChanged();
                                                    } else {

                                                        Log.d("AAA", "AAAAAAAAAAAAAAAA");
                                                        Helper_Log.errorLog(Fragment_LandingPage.class);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                                                    Helper_Log.errorLog(t, Fragment_LandingPage.class);
                                                }
                                            });
                                        });

                                    } else {
                                        Helper_Log.errorLog(Fragment_LandingPage.class);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<RestaurantClass>> call, Throwable t) {
                                    Helper_Log.errorLog(t, Fragment_LandingPage.class);
                                }
                            }));
                            break;
                        case FAILED:
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContextHelper.retrieveContext());
                            View inflateView = LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.dialog_update, null, false);
                            builder.setView(inflateView);
                            builder.setCancelable(false);
                            Dialog dialog = builder.create();
                            RecyclerView recyclerView = inflateView.findViewById(R.id.dialog_update_recycler_view);
                            List<VersionFeature> features = new Gson().fromJson(response.body().getMessage(), new TypeToken<List<VersionFeature>>() {
                            }.getType());
                            RecyclerView.Adapter adapter = new Adapter_VersionDescriptor(features);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false));
                            recyclerView.setAdapter(adapter);
                            TextViewPlus versionCode = inflateView.findViewById(R.id.dialog_update_version_code);
                            TextViewPlus update = inflateView.findViewById(R.id.dialog_update_update);
                            TextViewPlus exit = inflateView.findViewById(R.id.dialog_update_exit);
                            versionCode.setText("( " + features.get(0).getVersionCode() + " )");
                            update.setOnClickListener(v -> {
                                final String packageName = ContextHelper.retrieveContext().getPackageName();
                                if (Setting.getInstance().isApplicationInstalled(Constants.BAZAAR_PACKAGE_NAME)) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("bazaar://details?id=" + "PACKAGE_NAME"));
                                    intent.setPackage("com.farsitel.bazaar");
                                    startActivity(intent);
                                } else {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                                    }
                                }
                            });
                            exit.setOnClickListener(v -> {
                                getActivity().finishAffinity();
                                getActivity().finish();
                            });
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            break;
                    }
                } else {
                    Helper_Log.errorLog(Fragment_LandingPage.class);
                    Helper.getInstance().toast(R.string.unknown_error, Constants.ToastMode.ERROR);
                    getActivity().finishAffinity();
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_LandingPage.class);
                Helper.getInstance().toast(R.string.unknown_error, Constants.ToastMode.ERROR);
                getActivity().finishAffinity();
                getActivity().finish();
            }
        }));
    }
}
