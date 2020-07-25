package ir.ac.kntu.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Activity.Activity_Payment;
import ir.ac.kntu.Adapter.Adapter_AcceptableCashAmounts;
import ir.ac.kntu.Interface.Retrofit.General_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * fragment wallet, ables user to charge wallet
 */
public class Fragment_Wallet extends Fragment {
    private ImageView back;
    private TextViewPlus toChargeValue;
    private TextViewPlus purchase;
    private RecyclerView recyclerView;
    private double chargeValue;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager recyclerView_layout_manager;
    private List<Double> acceptableCashAmountList = new LinkedList<>();

    /**
     * fragment entry point which finds views, loads data, initializes UI elements and declares
     * listeners
     *
     * @param inflater           inflater
     * @param container          container view
     * @param savedInstanceState saved instance bundle
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    /**
     * gets data from server and populates list and refreshes the adapter
     *
     * @param view
     */
    private void initializeServerSupplied(View view) {

        Connector.createService(view, General_Server_API.class, object -> object.getAcceptableCashAmounts().enqueue(new Callback<List<Double>>() {
            @Override
            public void onResponse(Call<List<Double>> call, Response<List<Double>> response) {
                if (response.body() != null) {
                    acceptableCashAmountList.clear();
                    acceptableCashAmountList.addAll(response.body());
                    recyclerView_adapter.notifyDataSetChanged();
                } else {
                    Helper_Log.errorLog(Fragment_Wallet.class);
                }
            }

            @Override
            public void onFailure(Call<List<Double>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_Wallet.class);
            }
        }));

    }

    /**
     * initialises some UI elements
     *
     * @param view view to work
     */
    private void initializeViewContents(View view) {
        purchase.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        recyclerView.setHasFixedSize(true);
        recyclerView_layout_manager = new GridLayoutManager(ContextHelper.retrieveContext(), 3);
        recyclerView.setLayoutManager(recyclerView_layout_manager);
        recyclerView_adapter = new Adapter_AcceptableCashAmounts(acceptableCashAmountList,
                object -> {
                    chargeValue = object;
                    toChargeValue.setText(Helper.getInstance().getOneDigitOrNon(object, false));
                });
        recyclerView.setAdapter(recyclerView_adapter);
        if (getArguments() != null) {
            chargeValue = getArguments().getDouble("TO_CHARGE_VALUE");
            if (chargeValue % 1000 == 0)
                toChargeValue.setText(String.valueOf(Helper.getInstance().getCostCeilOf(chargeValue)));
            else {
                double difference = (chargeValue + 1000) % 1000;
                toChargeValue.setText(String.valueOf(Helper.getInstance().getCostCeilOf(((int) (chargeValue + 1000 - difference)))));
            }
        }
    }

    /**
     * assign view objects to view elements
     *
     * @param view to find views with it
     */
    private void findViews(View view) {
        back = view.findViewById(R.id.fragment_wallet_back_iv);
        recyclerView = view.findViewById(R.id.fragment_wallet_rv);
        purchase = view.findViewById(R.id.fragment_wallet_purchase_container);
        toChargeValue = view.findViewById(R.id.fragment_wallet_to_charge_value);
    }

    /**
     * declares some listeners for some UI elements
     *
     * @param view view to work
     */
    private void manageListeners(View view) {
        back.setOnClickListener(v -> getActivity().onBackPressed());
        purchase.setOnClickListener(v -> {
            if (chargeValue == 0)
                Helper.getInstance().toast(R.string.empty_value_to_charge, Constants.ToastMode.WARNING);
            else if (chargeValue % 1000 != 0) {
                Helper.getInstance().toast(R.string.to_charge_value_must_be_divided_to_thousand, Constants.ToastMode.ERROR);
            } else if (chargeValue > 0) {
                Intent intent = new Intent(getActivity(), Activity_Payment.class);
                intent.putExtra("PRICE", Double.parseDouble(toChargeValue.getText().toString()));
                startActivity(intent);
                getActivity().finish();
            } else
                Helper.getInstance().toast(R.string.error, Constants.ToastMode.ERROR);
        });
    }
}
