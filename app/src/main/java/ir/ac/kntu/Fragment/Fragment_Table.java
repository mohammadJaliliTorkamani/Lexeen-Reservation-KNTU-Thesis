package ir.ac.kntu.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alirezaafkar.sundatepicker.DatePicker;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_Roof;
import ir.ac.kntu.Adapter.Adapter_Table;
import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Desk;
import ir.ac.kntu.Entity.TableInfo;
import ir.ac.kntu.Interface.Retrofit.Restaurant_Server_API;
import ir.ac.kntu.Interface.Retrofit.Table_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_MultiArg;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class Fragment_Table extends DialogFragment {
    private static Fragment_Table instance;
    private static Runnable_MultiArg toRun;
    private RecyclerView tableView;
    private RecyclerView.Adapter tableView_adapter;
    private RecyclerView.LayoutManager tableView_layout_manager;
    private RecyclerView roof_recyclerview;
    private RecyclerView.Adapter roof_recyclerview_adapter;
    private RecyclerView.LayoutManager roof_recyclerview_layout_manager;
    private List<Integer> roofs;
    private TableInfo tabeInfo;
    private TextViewPlus selectedDateTime;
    private String dateTime;
    private CoordinatorLayout payContainer;
    private EditText counter;
    private TextViewPlus payText;
    private ProgressBar payProgressbar;

    public static Fragment_Table getInstance(Runnable_MultiArg toRunAfterSelection) {
        if (instance == null) {
            toRun = toRunAfterSelection;
            instance = new Fragment_Table();
        }
        return instance;
    }

    public static Fragment_Table getInstance() {
        if (instance == null) {
            instance = new Fragment_Table();
        }
        return instance;
    }

    private static Desk achieveDeskInPosition(int i, int j, List<Desk> desks) {
        for (Desk desk : desks)
            if (desk.getRow_index() == i && desk.getColumn_index() == j)
                return desk;
        return null;
    }

    private int getDifferenceBetweenSelectedShamsiDateAndToday() {
        if (dateTime == null)
            return -1;
        PersianDate selectedDate = Helper.getInstance().stringToPersianDateTime(dateTime);
        PersianDate todayShamsiDate = new PersianDate();
        if (selectedDate.getShYear() == todayShamsiDate.getShYear() &&
                selectedDate.getShMonth() == todayShamsiDate.getShMonth()) {
            return selectedDate.getShDay() - todayShamsiDate.getShDay();
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.fragment_table, null, false);
        builder.setView(view);
        findViews(view);
        initializeViewContents(view);
        initializeOnlineContents(view);
        manageListeners(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return dialog;
    }

    private void initializeOnlineContents(View view) {
        Connector.createService(view, Restaurant_Server_API.class, object -> object.getRoofs().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.body() != null) {
                    roofs.clear();
                    roofs.addAll(response.body());
                    roof_recyclerview_adapter.notifyDataSetChanged();
                    if (dateTime != null) {
                        int selectedRoofPos = ((Adapter_Roof) (roof_recyclerview_adapter)).getClickedPosition();
                        Connector.createService(view, Table_Server_API.class, object -> object.getDesks(roofs.get(selectedRoofPos), dateTime).enqueue(new Callback<List<Desk>>() {
                            @Override
                            public void onResponse(Call<List<Desk>> call, Response<List<Desk>> response) {
                                if (response.body() != null) {
                                    if (response.body().size() == 0)
                                        Helper.getInstance().toast(R.string.no_empty_desks, Constants.ToastMode.WARNING);
                                    else {
                                        int maxRow = getMaxRowOf(response.body());
                                        Connector.createService(view, Table_Server_API.class, object1 -> object1.getMaxColumnsOf().enqueue(new Callback<Integer>() {
                                            @Override
                                            public void onResponse(Call<Integer> call, Response<Integer> response1) {
                                                if (response1.body() != null) {
                                                    tabeInfo.setMaxCol(response1.body());
                                                    int maxCol = tabeInfo.getMaxCol();
                                                    tabeInfo.getDesks().clear();
                                                    tabeInfo.setMaxRow(0);
                                                    for (int i = 0; i < maxRow; i++) {
                                                        LinkedList<Desk> list = new LinkedList<>();
                                                        list.clear();
                                                        for (int j = 0; j < maxCol; j++)
                                                            list.add(null);
                                                        tabeInfo.getDesks().add(list);
                                                    }
                                                    for (int i = 0; i < maxRow; i++)
                                                        for (int j = 0; j < maxCol; j++)
                                                            tabeInfo.getDesks().get(i).set(maxCol - j - 1, achieveDeskInPosition(i, j, response.body()));
                                                    tabeInfo.setMaxRow(maxRow);
                                                    ((Adapter_Table) tableView_adapter).clearFullDesks();
                                                    tableView_adapter.notifyDataSetChanged();
                                                } else {
                                                    Helper_Log.errorLog(Fragment_Table.class);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Integer> call, Throwable t) {
                                                Helper_Log.errorLog(t, Fragment_Table.class);
                                            }
                                        }));
                                    }
                                } else
                                    Helper_Log.errorLog(Fragment_Table.class);
                            }

                            @Override
                            public void onFailure(Call<List<Desk>> call, Throwable t) {
                                Helper_Log.errorLog(t, Fragment_Table.class);
                            }
                        }));
                    }
                } else
                    Helper_Log.errorLog(Fragment_Table.class);
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_Table.class);
            }
        }));
    }

    private int getMaxRowOf(List<Desk> body) {
        int rowCounter = 0;
        for (Desk desk : body)
            if (desk.getRow_index() > rowCounter)
                rowCounter = desk.getRow_index();
        rowCounter++;
        return rowCounter;
    }

    private void findViews(View view) {
        selectedDateTime = view.findViewById(R.id.fragment_table_selected_date_time_value);
        tableView = view.findViewById(R.id.fragment_table_table_view);
        roof_recyclerview = view.findViewById(R.id.fragment_table_roof_rv);
        payContainer = view.findViewById(R.id.fragment_table_pay_text_container);
        payText = view.findViewById(R.id.fragment_table_pay_text);
        payProgressbar = view.findViewById(R.id.fragment_table_pay_pb);
        counter = view.findViewById(R.id.fragment_table_counter_value);
    }

    private void initializeViewContents(View view) {
        Helper.getInstance().changeStrokeColorToMainAppColor(counter);
        Helper.getInstance().changeShapeColorToMainAppColor(payContainer);
        counter.setTextColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        selectedDateTime.setTextColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        payText.setVisibility(View.VISIBLE);
        payProgressbar.setVisibility(View.GONE);
        payContainer.setClickable(true);
        roofs = new LinkedList<>();
        tabeInfo = new TableInfo();
        updateOrderTimeText();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        tableView_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext());
        tableView.setLayoutManager(tableView_layout_manager);
        tableView.setHasFixedSize(true);
        tableView_adapter = new Adapter_Table(tabeInfo);
        tableView.setAdapter(tableView_adapter);
        roof_recyclerview.setHasFixedSize(true);
        roof_recyclerview_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.HORIZONTAL, false);
        roof_recyclerview.setLayoutManager(roof_recyclerview_layout_manager);
        roof_recyclerview_adapter = new Adapter_Roof(roofs, tableView_adapter, 0, selectedRoof -> initializeOnlineContents(view));
        roof_recyclerview.setAdapter(roof_recyclerview_adapter);
    }

    private void manageListeners(View view) {
        selectedDateTime.setOnClickListener(v -> {
            final Calendar minimumTodayCalendar = Calendar.getInstance();
            final Calendar endCalendar = Calendar.getInstance();
            minimumTodayCalendar.add(Calendar.MINUTE, Constants.VALID_ORDER_DATE_MINUTE_INTERVAL + 1);
            endCalendar.set(Calendar.YEAR, endCalendar.get(Calendar.YEAR) + 1);
            new DatePicker.Builder()
                    .id(View.generateViewId())
                    .minDate(minimumTodayCalendar)
                    .maxDate(endCalendar)
                    .build((id, calendar, day, month, year) -> {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (view1, hourOfDay, minute) -> {
                            if (view1.isShown()) {
                                PersianDate persianDate = new PersianDate();
                                persianDate.setShYear(year);
                                persianDate.setShMonth(month);
                                persianDate.setShDay(day);
                                persianDate.setHour(hourOfDay);
                                persianDate.setMinute(minute);
                                PersianDate selectedDate = new PersianDate();
                                selectedDate.initJalaliDate(year, month, day);
                                selectedDate.setHour(hourOfDay).setMinute(minute);
                                if (Helper.getInstance().isValidTimeForIntervalFromNow(persianDate)) {
                                    dateTime = selectedDate.getShYear()
                                            + "/"
                                            + Helper.getInstance().get2DigitsOfDigit(selectedDate.getShMonth())
                                            + "/"
                                            + Helper.getInstance().get2DigitsOfDigit(selectedDate.getShDay())
                                            + " "
                                            + Helper.getInstance().get2DigitsOfDigit(selectedDate.getHour())
                                            + ":"
                                            + Helper.getInstance().get2DigitsOfDigit(selectedDate.getMinute());
                                    updateOrderTimeText();
                                    initializeOnlineContents(view);
                                } else {
                                    Helper.getInstance().toast(R.string.invalid_date_time, Constants.ToastMode.ERROR);
                                }
                            }
                        }, minimumTodayCalendar.get(Calendar.HOUR_OF_DAY), minimumTodayCalendar.get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(R.string.time_of_order);
                        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        timePickerDialog.show();
                    })
                    .show(getFragmentManager(), "");

        });
        payText.setOnClickListener(v -> {
            List<Bill> selectedDeskBills = Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getToReserveLexinTables(Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
            if (!selectedDeskBills.isEmpty()) {
                if (counter.getText() == null || counter.getText().toString().isEmpty() || Integer.parseInt(counter.getText().toString()) < 1)
                    Helper.getInstance().toast(R.string.invalid_number_of_customers, Constants.ToastMode.ERROR);
                else {
                    if (dateTime != null) {
                        Helper
                                .getInstance()
                                .showBiOptionsDiagram(Fragment_Table.this,
                                        getString(R.string.accept_order),
                                        getString(R.string.are_you_sure_to_accept_order),
                                        getString(R.string.yes),
                                        getString(R.string.cancel2),
                                        dialog -> {
                                            dialog.dismiss();
                                            payProgressbar.setVisibility(View.VISIBLE);
                                            payText.setVisibility(View.GONE);
                                            toRun.run(dateTime, payProgressbar, payText, counter, (Runnable) () -> dateTime = null); // no need to pass selected bills. it's stored in room database
                                        }, dialog -> dialog.cancel(), false);
                    } else
                        Helper.getInstance().toast(R.string.select_date_and_time, Constants.ToastMode.WARNING);
                }
            } else {
                Helper.getInstance().toast(getString(R.string.select_chairs_to_continue), Constants.ToastMode.WARNING);
            }
        });

    }

    private void updateOrderTimeText() {
        if (dateTime == null) {
            selectedDateTime.setText(getString(R.string.please_tap));
        } else {
            try {
                if (Helper.getInstance().isValidTimeForIntervalFromNow(dateTime)) {
                    PersianDate selectedDate = Helper.getInstance().stringToPersianDateTime(dateTime);
                    int difference = getDifferenceBetweenSelectedShamsiDateAndToday();
                    if (difference == 0) {
                        selectedDateTime.setText((selectedDate.getHour() > 19 ? (getString(R.string.tonight) + " ") : getString(R.string.today)) + " - " + Helper.getInstance().get2DigitsOfDigit(selectedDate.getHour()) + ":" + Helper.getInstance().get2DigitsOfDigit(selectedDate.getMinute()));
                    } else {
                        selectedDateTime.setText(selectedDate.getShYear() + "/" + Helper.getInstance().get2DigitsOfDigit(selectedDate.getShMonth()) + "/" + Helper.getInstance().get2DigitsOfDigit(selectedDate.getShDay()) + " " + Helper.getInstance().get2DigitsOfDigit(selectedDate.getHour()) + ":" + Helper.getInstance().get2DigitsOfDigit(selectedDate.getMinute()));
                    }
                } else {
                    dateTime = null;
                    updateOrderTimeText();
                }
            } catch (Exception e) {
                Helper_Log.errorLog(e, Fragment_Table.class);
            }
        }
    }

}
