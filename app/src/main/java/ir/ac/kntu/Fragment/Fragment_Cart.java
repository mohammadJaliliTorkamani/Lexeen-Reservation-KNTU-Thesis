package ir.ac.kntu.Fragment;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pushpole.sdk.PushPole;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ir.ac.kntu.Adapter.Adapter_Cart;
import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Discount;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.RefactoredBills;
import ir.ac.kntu.Entity.ServerResponse;
import ir.ac.kntu.Entity.ServiceActivation;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.Interface.Retrofit.General_Server_API;
import ir.ac.kntu.Interface.Retrofit.Order_Server_API;
import ir.ac.kntu.Interface.Retrofit.Restaurant_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.EditTextPlus;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.AlarmReminder;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.LocaleHelper;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

/**
 * cart fragment ables user to see bill and choose reservation mode and discount operation
 */
public class Fragment_Cart extends Fragment {
    private static TextViewPlus totalPrice;
    private static TextViewPlus totalPriceUnit;
    private ConstraintLayout serveModeButtonsContainer;
    private ConstraintLayout topIconView;
    private LinearLayout sumView;
    private TextViewPlus clear;
    private View freeSpace;
    private ImageView orderIcon;
    private TextViewPlus home;
    private TextViewPlus restaurant;
    private TextViewPlus sumWord;
    private ImageView bottomArrow;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager recyclerView_layout_manager;
    private EditTextPlus discountCode;
    private LinearLayout set;

    private Order order = new Order();

    /***
     * adds food ID as bill to the database
     * @param foodID id of the food
     * @param finalNumberOfFood number of food ids to be added to cart
     * @param clearRest if true, delete cart and add the id
     */
    public static void addToCart(int foodID, int finalNumberOfFood, boolean clearRest) {
        if (clearRest)
            Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().clearAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
        if (finalNumberOfFood == 0) {
            Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().deleteWithFoodID(foodID, Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
        } else {
            if (!Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithFoodID(foodID, Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).isEmpty()) {
                Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().updateCounterOfBillByFoodID(foodID, finalNumberOfFood, Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
            } else
                Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().insert(new Bill(foodID, finalNumberOfFood));
        }
    }

    /**
     * sends push notification to the user
     *
     * @param objects order(s)
     */
    public static void remindEvent(Object... objects) {
        Order order = (Order) objects[0];
        PushPole.sendSimpleNotifToUser(ContextHelper.retrieveContext(),
                Setting.getInstance().loadSetting(Constants._TABLE_USER, PushPole.getId(ContextHelper.retrieveContext()), null),
                ContextHelper.retrieveContext().getString(R.string.reminding),
                "شما یک سفارش برای تاریخ " + order.getDate_and_time_start() + " دارید.");
    }

    /**
     * changes the color of some ui elements and initialize them from server
     *
     * @param view view to work
     */
    private void initializeServerSupplied(View view) {
        Helper.getInstance().changeShapeColorToMainAppColor(sumView);
        Helper.getInstance().changeShapeColorToMainAppColor(topIconView);
        Helper.getInstance().changeShapeColorToMainAppColor(set);
        initializeList(view);
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        totalPrice = view.findViewById(R.id.fragment_cart_total_price);
        totalPriceUnit = view.findViewById(R.id.fragment_cart_total_price_unit);

        freeSpace = view.findViewById(R.id.fragment_cart_free_space_view);
        topIconView = view.findViewById(R.id.fragment_cart_top_icon_view);
        sumView = view.findViewById(R.id.fragment_cart_sum_view);
        sumWord = view.findViewById(R.id.fragment_cart_sum_word);
        bottomArrow = view.findViewById(R.id.fragment_cart_bottom_arrow);
        recyclerView = view.findViewById(R.id.fragment_cart_rv);
        clear = view.findViewById(R.id.fragment_cart_clear);
        orderIcon = view.findViewById(R.id.fragment_cart_imageView);
        serveModeButtonsContainer = view.findViewById(R.id.fragment_cart_serve_mode_container);
        set = view.findViewById(R.id.fragment_cart_set);
        discountCode = view.findViewById(R.id.fragment_cart_discount_code);
        home = view.findViewById(R.id.fragment_cart_home_btn);
        restaurant = view.findViewById(R.id.fragment_cart_restaurant_btn);
    }

    /**
     * configures some UI initializations
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        bottomArrow.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        sumWord.setVisibility(View.GONE);
        restaurant.setText(Encryption.getInstance().decrypt(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE)
                .restaurantInterface().getRestaurant(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).getName()));
        freeSpace.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        clear.setTextColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        topIconView.setBackgroundResource(R.drawable.dr_tl_tr_oval_item);
        sumView.setBackgroundResource(R.drawable.dr_bl_br_oval_item);
        if (getFragmentManager().findFragmentById(R.id.main_frame) instanceof Fragment_Main)
            ((Fragment_Main) getFragmentManager().findFragmentById(R.id.main_frame)).updateBadge();
        orderIcon.setScaleX(LocaleHelper.getLanguage(ContextHelper.retrieveContext()).equalsIgnoreCase("fa") ? -1 : 1);
        recyclerView_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(recyclerView_layout_manager);
        recyclerView.setHasFixedSize(true);
        recyclerView_adapter = new Adapter_Cart(view, order.getSpecifiedBills());
        recyclerView.setAdapter(recyclerView_adapter);
        set.setClickable(true);
    }

    /**
     * updates UI elements fom the server if exists in DB, otherwise does a bit changes in UI elements
     *
     * @param view to work
     */
    private void initializeList(View view) {
        if (!Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            Connector.createService(view, Order_Server_API.class, object -> object.completeBills(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode())).enqueue(new Callback<List<Bill>>() {
                @Override
                public void onResponse(Call<List<Bill>> call, Response<List<Bill>> completedResponse) {
                    if (completedResponse.body() != null) {
                        serveModeButtonsContainer.setVisibility(!completedResponse.body().isEmpty() ? View.VISIBLE : View.GONE);
                        clear.setVisibility(!completedResponse.body().isEmpty() ? View.VISIBLE : View.GONE);
                        sumWord.setVisibility(!completedResponse.body().isEmpty() ? View.VISIBLE : View.GONE);
                        bottomArrow.setVisibility(!completedResponse.body().isEmpty() ? View.VISIBLE : View.GONE);
                        order.clearSpecifiedBills();
                        order.getSpecifiedBills().addAll(Bill.removeDeskBillsFrom(completedResponse.body()));
                        recyclerView.setVisibility(completedResponse.body().isEmpty() ? View.GONE : View.VISIBLE);
                        order.setTotalPrice(Bill.getTotalPrice(completedResponse.body()));
                        totalPrice.setText(!completedResponse.body().isEmpty() ? Helper.getInstance().getOneDigitOrNon(order.getTotalPrice(), true) : "");
                        totalPriceUnit.setText(Helper.getInstance().getPurchaseUnit());
                        recyclerView_adapter.notifyDataSetChanged();
                    } else {
                        clear.setVisibility(View.GONE);
                        sumWord.setVisibility(View.GONE);
                        bottomArrow.setVisibility(View.GONE);
                        serveModeButtonsContainer.setVisibility(View.GONE);
                        Helper_Log.errorLog(Fragment_Cart.class);
                    }
                }

                @Override
                public void onFailure(Call<List<Bill>> call, Throwable t) {
                    clear.setVisibility(View.GONE);
                    sumWord.setVisibility(View.GONE);
                    bottomArrow.setVisibility(View.GONE);
                    Helper_Log.errorLog(t, Fragment_Cart.class);
                }
            }));

        } else {
            recyclerView.setVisibility(View.GONE);
            order.clearSpecifiedBills();
            recyclerView_adapter.notifyDataSetChanged();
            clear.setVisibility(View.GONE);
            sumWord.setVisibility(View.GONE);
            bottomArrow.setVisibility(View.GONE);
            totalPrice.setText(String.valueOf(0));
            totalPriceUnit.setText(Helper.getInstance().getPurchaseUnit());
        }
    }

    /**
     * entry point, find views, initialize elements and listeners and reads from server
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    /**
     * defines listener operations for UI elements
     *
     * @param view to work with
     */
    private void manageListeners(View view) {
        restaurant.setOnClickListener(v -> {
            if (order.getSpecifiedBills().isEmpty() || !Bill.containsFood(order.getSpecifiedBills())) {
                Helper.getInstance().toast(R.string.you_have_not_any_bill, Constants.ToastMode.WARNING);
            } else {
                Connector.createService(view, Restaurant_Server_API.class, object -> {
                    object.isActiveForServe().enqueue(new Callback<ServiceActivation>() {
                        @Override
                        public void onResponse(Call<ServiceActivation> call, Response<ServiceActivation> response) {
                            if (response.body() != null) {
                                if (!response.body().isManuallyDisabled()) {
                                    Fragment_Table fragment_table = Fragment_Table.getInstance(fragment_passed_object -> {
                                        String selectedDate = (String) fragment_passed_object[0];
                                        ProgressBar progressBar = (ProgressBar) fragment_passed_object[1];
                                        TextViewPlus textViewPlus = (TextViewPlus) fragment_passed_object[2];
                                        EditText editText_numberOfPeople = (EditText) fragment_passed_object[3];
                                        Runnable onSuccessfulOrdered = (Runnable) fragment_passed_object[4];
                                        order.clearSpecifiedBills();
                                        order.getSpecifiedBills().addAll(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode()));
                                        try {
                                            if (!Helper.getInstance().isValidTimeForIntervalFromNow(selectedDate)) {
                                                Helper.getInstance().toast(R.string.invalid_date_time, Constants.ToastMode.INFO);
                                                progressBar.setVisibility(View.GONE);
                                                textViewPlus.setVisibility(View.VISIBLE);
                                            } else {
                                                Connector.createService(view, Account_Server_API.class, object -> object.getCash().enqueue(new Callback<Double>() {
                                                    @Override
                                                    public void onResponse(Call<Double> call, Response<Double> response) {
                                                        if (response.body() != null) {
                                                            double currentCash = response.body();
                                                            try {
                                                                PersianDate persianDate = new PersianDateFormat("yyyy/MM/dd HH:mm").parse(selectedDate);
                                                                String gregorianDate = persianDate.getGrgYear() + "/" + (persianDate.getGrgMonth() < 10 ? "0" : "") + persianDate.getGrgMonth() + "/" + (persianDate.getGrgDay() < 10 ? "0" : "") + persianDate.getGrgDay() + " " + (persianDate.getHour() < 10 ? "0" : "") + persianDate.getHour() + ":" + (persianDate.getMinute() < 10 ? "0" : "") + persianDate.getMinute();
                                                                try {
                                                                    order.setDate_and_time_start(Encryption.getInstance().encrypt(gregorianDate));
                                                                } catch (Exception e) {
                                                                    Helper_Log.errorLog(e, Fragment_Cart.class);
                                                                }

                                                                isGoodRestaurantOrder(view, persianDate, Integer.parseInt(editText_numberOfPeople.getText().toString()), orderExamineResponse -> {
                                                                    switch (ServerResponse.ServerResponseCodes.getMeaningOf(orderExamineResponse.getCode())) {
                                                                        case DONE:
                                                                            if (currentCash >= order.getTotalPrice()) { //enough cash
                                                                                Connector.createService(view, Order_Server_API.class, object -> object.order(order).enqueue(new Callback<ServerResponse>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                                                        if (response.body() != null) {
                                                                                            switch (ServerResponse.ServerResponseCodes.getMeaningOf(response.body().getCode())) {
                                                                                                case DONE://so we have orderID in message
                                                                                                    try {
                                                                                                        progressBar.setVisibility(View.GONE);
                                                                                                        textViewPlus.setVisibility(View.VISIBLE);
                                                                                                        order.setOrderID(Integer.parseInt(response.body().getMessage()));
                                                                                                        Helper.getInstance().toast(R.string.ordered_successfully, Constants.ToastMode.SUCCESS);
                                                                                                        Calendar calendar = Calendar.getInstance();
                                                                                                        onSuccessfulOrdered.run();
                                                                                                        try {
                                                                                                            calendar.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US).parse(gregorianDate));
                                                                                                            AlarmReminder.getInstance().remindSingleMode(calendar, order);
                                                                                                            Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().clearAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
                                                                                                            Fragment_Table.getInstance().dismiss();
                                                                                                            getFragmentManager().popBackStack();
                                                                                                            getFragmentManager()
                                                                                                                    .beginTransaction()
                                                                                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                                                                                    .addToBackStack("user_orders")
                                                                                                                    .add(R.id.main_frame, new Fragment_UserOrders())
                                                                                                                    .commit();
                                                                                                        } catch (ParseException e) {
                                                                                                            Helper_Log.errorLog(e, Fragment_Cart.class);
                                                                                                        }
                                                                                                        break;
                                                                                                    } catch (Exception e) {
                                                                                                        Helper_Log.errorLog(e, Fragment_Cart.class);
                                                                                                    }
                                                                                                    break;
                                                                                                case FAILED:  //so we have error message in message
                                                                                                    progressBar.setVisibility(View.GONE);
                                                                                                    textViewPlus.setVisibility(View.VISIBLE);
                                                                                                    Helper.getInstance().toast(getString(R.string.ordered_fault) + "," + Encryption.getInstance().decrypt(response.body().getMessage()), Constants.ToastMode.ERROR);
                                                                                                    Fragment_Table.getInstance().dismiss();
                                                                                                    initializeList(view);
                                                                                                    break;
                                                                                                default: //so we have null in message
                                                                                                    progressBar.setVisibility(View.GONE);
                                                                                                    textViewPlus.setVisibility(View.VISIBLE);
                                                                                                    Helper.getInstance().toast(getString(R.string.unknown_error), Constants.ToastMode.ERROR);
                                                                                                    Fragment_Table.getInstance().dismiss();
                                                                                                    initializeList(view);
                                                                                            }
                                                                                        } else {
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            textViewPlus.setVisibility(View.VISIBLE);
                                                                                            Helper_Log.errorLog(Fragment_Cart.class);
                                                                                            Fragment_Table.getInstance().dismiss();
                                                                                            initializeList(view);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        textViewPlus.setVisibility(View.VISIBLE);
                                                                                        Helper_Log.errorLog(t, Fragment_Cart.class);
                                                                                        Fragment_Table.getInstance().dismiss();
                                                                                        initializeList(view);
                                                                                    }
                                                                                }));
                                                                            } else {//not enough cash
                                                                                progressBar.setVisibility(View.GONE);
                                                                                textViewPlus.setVisibility(View.VISIBLE);
                                                                                Fragment_Table.getInstance().dismiss();
                                                                                Helper.getInstance().toast(ContextHelper.retrieveContext().getString(R.string.no_enough_charge), Constants.ToastMode.INFO);
                                                                                FragmentActivity activity = getActivity();
                                                                                Fragment fragment = new Fragment_Wallet();
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putDouble("TO_CHARGE_VALUE", Helper.getInstance().getCostCeilOf(order.getTotalPrice() - currentCash));
                                                                                fragment.setArguments(bundle);
                                                                                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("wallet").add(R.id.main_frame, fragment).commit();
                                                                            }
                                                                            break;
                                                                        case FAILED:
                                                                            Helper.getInstance().toast(Encryption.getInstance().decrypt(orderExamineResponse.getMessage()), Constants.ToastMode.WARNING);
                                                                            progressBar.setVisibility(View.GONE);
                                                                            textViewPlus.setVisibility(View.VISIBLE);
                                                                            break;
                                                                    }
                                                                });


                                                            } catch (ParseException e) {
                                                                Helper_Log.errorLog(e, Fragment_Cart.class);
                                                            }
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            textViewPlus.setVisibility(View.VISIBLE);
                                                            Fragment_Table.getInstance().dismiss();
                                                            Helper_Log.errorLog(Fragment_Cart.class);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Double> call, Throwable t) {
                                                        progressBar.setVisibility(View.GONE);
                                                        textViewPlus.setVisibility(View.VISIBLE);
                                                        Fragment_Table.getInstance().dismiss();
                                                        Helper_Log.errorLog(t, Fragment_Cart.class);
                                                    }
                                                }));
                                            }
                                        } catch (Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            textViewPlus.setVisibility(View.VISIBLE);
                                            Fragment_Table.getInstance().dismiss();
                                            Helper_Log.errorLog(e, Fragment_Cart.class);
                                        }
                                    });
                                    fragment_table.show(getFragmentManager(), "TAG_1");
                                } else {
                                    Helper.getInstance().toast(getString(R.string.restaurant_is_not_able_to_accept_order), Constants.ToastMode.INFO);
                                }
                            } else {
                                Helper_Log.errorLog(Fragment_Cart.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<ServiceActivation> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_Cart.class);
                        }
                    });
                });
            }
        });
        home.setOnClickListener(v ->
        {
            if (order.getSpecifiedBills().isEmpty() || !Bill.containsFood(order.getSpecifiedBills())) {
                Helper.getInstance().toast(R.string.you_have_not_any_bill, Constants.ToastMode.WARNING);
            } else {
                Connector.createService(view, Restaurant_Server_API.class, object -> {
                    object.isActiveForDelivery().enqueue(new Callback<ServiceActivation>() {
                        @Override
                        public void onResponse(Call<ServiceActivation> call, Response<ServiceActivation> response) {
                            if (response.body() != null) {
                                if (!response.body().isManuallyDisabled()) {
                                    if (response.body().isActive()) {
                                        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
                                            @Override
                                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                if (report.areAllPermissionsGranted()) {
                                                    try {
                                                        Connector.createService(view, Account_Server_API.class, object -> object.getCash().enqueue(new Callback<Double>() {
                                                            @Override
                                                            public void onResponse(Call<Double> call, Response<Double> response) {
                                                                if (response.body() != null) {
                                                                    double currentCash = response.body();
                                                                    if (!order.getSpecifiedBills().isEmpty()) {
                                                                        if (currentCash >= order.getTotalPrice()) {//enough cash
                                                                            Fragment fragment = new Fragment_LocationPicker();
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putString("ORDER", new Gson().toJson(order));
                                                                            fragment.setArguments(bundle);
                                                                            getFragmentManager()
                                                                                    .beginTransaction()
                                                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                                                    .addToBackStack("location_picker")
                                                                                    .add(R.id.main_frame, fragment)
                                                                                    .commit();

                                                                        } else {//not enough cash
                                                                            Helper.getInstance().toast(R.string.no_enough_charge, Constants.ToastMode.INFO);
                                                                            FragmentActivity activity = getActivity();
                                                                            Fragment fragment = new Fragment_Wallet();
                                                                            Bundle bundle = new Bundle();
                                                                            bundle.putDouble("TO_CHARGE_VALUE", Helper.getInstance().getCostCeilOf((order.getTotalPrice() - currentCash)));
                                                                            fragment.setArguments(bundle);
                                                                            getFragmentManager()
                                                                                    .beginTransaction()
                                                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                                                    .addToBackStack("wallet")
                                                                                    .add(R.id.main_frame, fragment)
                                                                                    .commit();
                                                                        }
                                                                    } else {
                                                                        Helper_Log.errorLog(Fragment_Cart.class);
                                                                    }
                                                                } else {
                                                                    Helper_Log.errorLog(Fragment_Cart.class);
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Double> call, Throwable t) {
                                                                Helper_Log.errorLog(t, Fragment_Cart.class);
                                                            }
                                                        }));
                                                    } catch (Exception e) {
                                                        Helper_Log.errorLog(e, Fragment_Cart.class);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                token.continuePermissionRequest();
                                            }
                                        }).withErrorListener(error -> Helper_Log.errorLog(Fragment_Cart.class))
                                                .check();
                                    } else {
                                        Helper.getInstance().toast(getString(R.string.restaurant_is_not_able_to_do_service_at_selected_time) + "\n" + getString(R.string.restaurats_today_service_time) + " " + Encryption.getInstance().decrypt(response.body().getTodaysServiceTime()) + " " + getString(R.string.is), Constants.ToastMode.INFO);
                                    }
                                } else {
                                    Helper.getInstance().toast(getString(R.string.restaurant_is_not_able_to_accept_order), Constants.ToastMode.INFO);
                                }
                            } else
                                Helper_Log.errorLog(Fragment_Cart.class);
                        }

                        @Override
                        public void onFailure(Call<ServiceActivation> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_Cart.class);
                        }
                    });
                });
            }

        });
        clear.setOnClickListener(v ->
        {
            Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().clearAll(Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
            if (getFragmentManager().findFragmentById(R.id.main_frame) instanceof Fragment_Main)
                ((Fragment_Main) getFragmentManager().findFragmentById(R.id.main_frame)).updateBadge();
            order.clearSpecifiedBills();
            clear.setVisibility(View.GONE);
            order.setTotalPrice(0);
            order.setDiscountID(-1);
            recyclerView_adapter.notifyDataSetChanged();
            totalPrice.setText(String.valueOf(0));
            totalPriceUnit.setText(Helper.getInstance().getPurchaseUnit());
        });
        set.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                Setting.getInstance().hideKeyboard(getActivity());
        });
        set.setOnClickListener(v ->
        {
            if (order.getSpecifiedBills().isEmpty() || order.getTotalPrice() == 0) {
                Helper.getInstance().toast(R.string.you_have_not_any_bill, Constants.ToastMode.WARNING);
            } else {
                if (discountCode.getText().length() > 0) {
                    Connector.createService(view, General_Server_API.class, object -> object.getDiscountWithCode(discountCode.getText().toString()).enqueue(new Callback<Discount>() {
                        @Override
                        public void onResponse(Call<Discount> call, Response<Discount> response) {
                            if (response.body() != null) {
                                Discount discount = response.body();
                                if (discount.getMinimumAcceptablePrice() <= order.getTotalPrice()) {
                                    if (discount.getId() > 0) {
                                        order.setTotalPrice((1 - discount.getPercentage() / 100) * order.getTotalPrice());
                                        order.setDiscountID(discount.getId());
                                        totalPrice.setText(Helper.getInstance().getOneDigitOrNon(order.getTotalPrice(), true));
                                        Helper.getInstance().toast(R.string.done, Constants.ToastMode.SUCCESS);
                                        set.setBackgroundResource(R.drawable.rec_cart_gray);
                                        set.setClickable(false);
                                    } else if (discount.getId() == -1)
                                        Helper.getInstance().toast(R.string.discount_code_not_valid_for_you, Constants.ToastMode.WARNING);
                                    else if (discount.getId() == -2)
                                        Helper.getInstance().toast(R.string.no_such_discount_code, Constants.ToastMode.WARNING);
                                } else {
                                    Helper.getInstance().toast(getString(R.string.minimum_price_is) + Helper.getInstance().getOneDigitOrNon(discount.getMinimumAcceptablePrice(), false), Constants.ToastMode.WARNING);
                                }
                            } else {
                                Helper_Log.errorLog(Fragment_Cart.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<Discount> call, Throwable t) {
                            Helper_Log.errorLog(t, Fragment_Cart.class);
                        }
                    }));
                }
            }
        });
    }

    /**
     * checks whether order is good to submit into the server or not
     *
     * @param view        view to work
     * @param persianDate jalali date time to order
     * @param n           number of people
     * @param runnable    runnable to  run if was good order
     */
    private void isGoodRestaurantOrder(View view, PersianDate persianDate, final int n, Runnable_SingleArg<ServerResponse> runnable) {
        Connector.createCachedService(view, Order_Server_API.class, object -> object.isGoodOrder(new RefactoredBills(order.getSpecifiedBills(), new PersianDateFormat("H:i").format(persianDate), n)).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body() != null) {
                    runnable.run(response.body());
                } else
                    Helper_Log.errorLog(Fragment_Cart.class);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_Cart.class);
            }
        }));
    }

    /**
     * NECESSARY FOR BUG FIX in fragment display !
     *
     * @param outState state bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
}
