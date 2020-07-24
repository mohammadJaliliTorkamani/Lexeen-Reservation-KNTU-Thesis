package ir.ac.kntu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Food;
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

public class Adapter_Cart extends RecyclerView.Adapter {
    private View view;
    private List<Bill> list;

    public Adapter_Cart(View view, List<Bill> foodList) {
        this.view = view;
        this.list = foodList;
    }

    /**
     * creates new viewHolder UI object with XML demonstrating rows layout
     *
     * @param parent   to specify the parent of the current position
     * @param viewType to specify ViewType of the current position
     * @return ViewHolder row Object
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_cart, parent, false));
    }

    /**
     * initialize view object values + managers  from the list
     *
     * @param holder   to access item UI object
     * @param position position of the current row
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus counterSymbol = holder.itemView.findViewById(R.id.cart_item_name_counter_symbol);
        TextViewPlus counter = holder.itemView.findViewById(R.id.cart_item_name_counter);
        TextViewPlus name = holder.itemView.findViewById(R.id.cart_item_name_value);
        TextViewPlus price = holder.itemView.findViewById(R.id.cart_item_price);
        TextViewPlus priceUnit = holder.itemView.findViewById(R.id.cart_item_price_unit);
        if (list.get(position).getFoodID() != -1) {
            Connector.createService(view, Food_Server_API.class, object -> object.getFood(list.get(position).getFoodID()).enqueue(new Callback<Food>() {
                @Override
                public void onResponse(Call<Food> call, Response<Food> response) {
                    if (response.body() != null) {
                        Food food = response.body();
                        try {
                            counter.setText(String.valueOf(list.get(position).getCounter()));
                            counterSymbol.setText(Helper.getInstance().getCounterSymbol());
                            name.setText(Encryption.getInstance().decrypt(food.getName()));
                            price.setText(Helper.getInstance().getOneDigitOrNon(list.get(position).getTotalCost(), true));
                            priceUnit.setText(Helper.getInstance().getPurchaseUnit());
                        } catch (Exception e) {
                            Helper_Log.errorLog(e, Adapter_Cart.class);
                        }
                    } else
                        Helper_Log.errorLog(Adapter_Cart.class);
                }

                @Override
                public void onFailure(Call<Food> call, Throwable t) {
                    Helper_Log.errorLog(t, Adapter_Cart.class);
                }
            }));
        }
    }

    /**
     * counts items available in list
     *
     * @return size
     */
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * define ViewHolder class to store each row object
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
