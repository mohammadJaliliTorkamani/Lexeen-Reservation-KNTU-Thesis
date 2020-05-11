package ir.ac.kntu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_UserOrdersSubItems extends RecyclerView.Adapter {
    private List<Bill> bills;
    private boolean printedTableRecord = false;

    public Adapter_UserOrdersSubItems(List<Bill> specifiedBills) {
        this.bills = specifiedBills;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_user_orders_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus text1 = holder.itemView.findViewById(R.id.item_history_sub_text_value);
        TextViewPlus text2 = holder.itemView.findViewById(R.id.item_history_sub_text_descriptor);
        if (bills.get(position).getFoodID() != -1) {
            Connector.createService(holder.itemView, Food_Server_API.class, object -> object.getFood(bills.get(position).getFoodID()).enqueue(new Callback<ir.ac.kntu.Entity.Food>() {
                @Override
                public void onResponse(Call<ir.ac.kntu.Entity.Food> call, Response<ir.ac.kntu.Entity.Food> response) {
                    if (response.body() != null) {
                        text1.setText(Helper.getInstance().getCounterSymbol() + "  " + bills.get(position).getCounter());
                        text2.setText(Encryption.getInstance().decrypt(response.body().getName()));
                    } else
                        Helper_Log.errorLog(Adapter_UserOrdersSubItems.class);
                }

                @Override
                public void onFailure(Call<ir.ac.kntu.Entity.Food> call, Throwable t) {
                    Helper_Log.errorLog(t, Adapter_UserOrdersSubItems.class);
                }
            }));
        } else if (!printedTableRecord) {
            printedTableRecord = true;
            text1.setText(Helper.getInstance().getCounterSymbol() + "  " + countReservedTables());
            text2.setText(ContextHelper.retrieveContext().getString(R.string.chair));
        }
    }

    private int countReservedTables() {
        int counter = 0;
        for (Bill bill : bills)
            if (bill.getLexinTableID() != -1)
                counter++;
        return counter;
    }

    @Override
    public int getItemCount() {
        if (bills == null || bills.isEmpty())
            return 0;
        return bills.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
