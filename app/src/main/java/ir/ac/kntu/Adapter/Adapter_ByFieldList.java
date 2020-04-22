package ir.ac.kntu.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.List;

import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;

public class Adapter_ByFieldList extends RecyclerView.Adapter {
    private View view;
    private List<FoodMenu> foodMenu;
    private int clickedIndex;
    private Adapter_ByFieldFood by_field_content_rv_adapter;

    public Adapter_ByFieldList(View view, List<FoodMenu> _list_by_field, int clickedIndex, Adapter_ByFieldFood food_adapter) {
        this.view = view;
        this.foodMenu = _list_by_field;
        this.clickedIndex = clickedIndex;
        this.by_field_content_rv_adapter = food_adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_by_field, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.by_field_item);
        TextViewPlus catName = holder.itemView.findViewById(R.id.by_field_cat_name);
        ImageView image = holder.itemView.findViewById(R.id.by_field_item_image);
        catName.setText(foodMenu.get(position).getCategory().getName());

        if (clickedIndex == position) {
            constraintLayout.setBackgroundResource(getRandomFoodListItemBackground(-1));
            catName.setTextColor(Color.WHITE);
            SvgLoader.pluck().with((Activity) view.getContext())
                    .load(foodMenu.get(position).getCategory().getLogos().get(0), image);

        } else {
            constraintLayout.setBackgroundResource(R.drawable.dr_by_field_black_border_white);
            catName.setTextColor(Color.BLACK);
            SvgLoader.pluck().with((Activity) view.getContext())
                    .load(foodMenu.get(position).getCategory().getLogos().get(1), image);
        }
        constraintLayout.setOnClickListener(v -> {
            if (clickedIndex != position) {
                clickedIndex = position;
                notifyDataSetChanged();
                by_field_content_rv_adapter.setToShowCategoryIndex(clickedIndex);
            }
        });
    }

    private int getRandomFoodListItemBackground(int position) {
        if (position == -1)
            return R.drawable.dr_by_field_black_border_brown;
        switch (position % 10) {
            case 0:
                return R.drawable.dr_by_field_black_border_green;
            case 1:
                return R.drawable.dr_by_field_black_border_blue;
            case 2:
                return R.drawable.dr_by_field_black_border_orange;
            case 3:
                return R.drawable.dr_by_field_black_border_purple;
            case 4:
                return R.drawable.dr_by_field_black_border_red;
            case 5:
                return R.drawable.dr_by_field_black_border_yellow;
            case 6:
                return R.drawable.dr_by_field_black_border_blue2;
            case 7:
                return R.drawable.dr_by_field_black_border_green2;
            case 8:
                return R.drawable.dr_by_field_black_border_red2;
            case 9:
                return R.drawable.dr_by_field_black_border_black;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (foodMenu == null)
            return 0;
        return foodMenu.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
