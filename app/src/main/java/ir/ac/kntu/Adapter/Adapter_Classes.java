package ir.ac.kntu.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.RestaurantClass;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;

public class Adapter_Classes extends RecyclerView.Adapter {
    private List<RestaurantClass> list;
    private int selectedIndex;
    private Runnable_SingleArg<RestaurantClass> onClicked;

    public Adapter_Classes(List<RestaurantClass> list, Runnable_SingleArg<RestaurantClass> onClicked) {
        this.list = list;
        this.selectedIndex = 0;
        this.onClicked = onClicked;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_restaurant_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus item = holder.itemView.findViewById(R.id.item_restaurant_class_view);
        try {
            item.setText(ContextHelper.retrieveContext().getString(R.string.class_of) + " " + Encryption.getInstance().decrypt(list.get(position).getName()));
        } catch (Exception e) {
            Helper_Log.errorLog(e, Adapter_Classes.class);
        }
        item.setTextColor(selectedIndex == position ? Color.WHITE : Color.BLACK);
        item.setBackgroundResource(selectedIndex == position ? R.drawable.dr_restaurant_class_selected_item : R.drawable.dr_restaurant_class_unselected_item);
        item.setOnClickListener(v -> {
            if (position != selectedIndex) {
                selectedIndex = position;
                onClicked.run(list.get(position));
                notifyDataSetChanged();
            }
        });
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
