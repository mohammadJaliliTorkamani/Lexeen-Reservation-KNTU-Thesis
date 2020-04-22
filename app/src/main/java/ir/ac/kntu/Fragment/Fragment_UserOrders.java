package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_UserFoodOder;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Interface.Retrofit.Operable_Food;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_UserOrders extends Fragment {
    private ImageView back;
    private TextViewPlus emptyText;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerView_adapter;
    private RecyclerView.LayoutManager recyclerView_layout_manager;
    private List<Order> orders = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_orders, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    private void findViews(View view) {
        back = view.findViewById(R.id.fragment_user_order_back);
        emptyText = view.findViewById(R.id.fragment_user_order_text_empty);
        recyclerView = view.findViewById(R.id.fragment_user_order_recyclerview);
    }

    private void initializeViewContents(View view) {
        recyclerView.setHasFixedSize(true);
        recyclerView_layout_manager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(recyclerView_layout_manager);
        recyclerView_adapter = new Adapter_UserFoodOder(getFragmentManager(), orders);
        recyclerView.setAdapter(recyclerView_adapter);
    }

    private void initializeServerSupplied(View view) {
        Connector.createService(view, Operable_Food.class, object -> object.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.body() != null) {
                    if (response.body().isEmpty())
                        emptyText.setVisibility(View.VISIBLE);
                    orders.clear();
                    orders.addAll(response.body());
                    recyclerView_adapter.notifyDataSetChanged();
                } else
                    Helper_Log.errorLog(Fragment_UserOrders.class);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_UserOrders.class);
            }
        }));
    }

    private void manageListeners(View view) {
        back.setOnClickListener(v -> getActivity().onBackPressed());
    }
}