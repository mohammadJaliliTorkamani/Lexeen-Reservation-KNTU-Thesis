package ir.ac.kntu.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Interface.Retrofit.Operable_Food;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Cart extends RecyclerView.Adapter {
    private View view;
    private List<Bill> list;
    private FragmentManager fragmentManager;

    public Adapter_Cart(View view, List<Bill> foodList, FragmentManager fragmentManager) {
        this.view = view;
        this.list = foodList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus counterSymbol = holder.itemView.findViewById(R.id.cart_item_name_counter_symbol);
        TextViewPlus counter = holder.itemView.findViewById(R.id.cart_item_name_counter);
        TextViewPlus name = holder.itemView.findViewById(R.id.cart_item_name_value);
        TextViewPlus price = holder.itemView.findViewById(R.id.cart_item_price);
        TextViewPlus priceUnit = holder.itemView.findViewById(R.id.cart_item_price_unit);
        if (list.get(position).getFoodID() != -1) {
            Connector.createService(view, Operable_Food.class, object -> {
                object.getFood(list.get(position).getFoodID()).enqueue(new Callback<Food>() {
                    @Override
                    public void onResponse(Call<Food> call, Response<Food> response) {
                        if (response.body() != null) {
                            Food food = response.body();
                            try {
                                counter.setText(String.valueOf(list.get(position).getCounter()));
                                counterSymbol.setText(Helper.getCounterSymbol());
                                name.setText(food.getName());
                                price.setText(Helper.getOneDigitOrNon(list.get(position).getTotalCost(), true));
                                priceUnit.setText(Helper.getPurchaseUnit());
                                name.setOnClickListener(v -> {
                                    Fragment fragment = new Fragment_FoodDescriptionDetail();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("Food_ID", food.getId());
                                    fragment.setArguments(bundle);
                                    fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("food_description").add(R.id.main_frame, fragment).commit();
                                });
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
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
