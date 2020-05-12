package ir.ac.kntu.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.Fragment.Fragment_Cart;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Fragment.Fragment_Main;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class Adapter_ByFieldFood extends RecyclerView.Adapter {
    private int toShowCategoryIndex = 0;
    private View view;
    private FragmentManager fragmentManager;
    private List<FoodMenu> list;
    private int lastPosition = -1;

    public Adapter_ByFieldFood(View view, FragmentManager fragmentManager, List<FoodMenu> list) {
        this.view = view;
        this.fragmentManager = fragmentManager;
        this.list = list;
    }

    public void setToShowCategoryIndex(int index) {
        toShowCategoryIndex = index;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_by_field_food, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardView cardView = holder.itemView.findViewById(R.id.field_food_item_item);
        ImageView image = holder.itemView.findViewById(R.id.field_food_image);
        Setting.getInstance().setItemWidth(cardView, 1.2);

        TextViewPlus add = holder.itemView.findViewById(R.id.field_food_add);
        TextViewPlus foodName = holder.itemView.findViewById(R.id.field_food_food_name_tv);
        TextViewPlus timeAndCalorie = holder.itemView.findViewById(R.id.field_food_time_and_calorie_tv);
        foodName.setText(Encryption.getInstance().decrypt(list.get(toShowCategoryIndex).getFoodList().get(position).getName()));
        timeAndCalorie.setText(list.get(toShowCategoryIndex).getFoodList().get(position).getCookTimeMinutes() + " " + ContextHelper.retrieveContext().getString(R.string.min) + " . " + list.get(toShowCategoryIndex).getFoodList().get(position).getCalories() + " " + ContextHelper.retrieveContext().getString(R.string.calories));


        add.setOnClickListener(v -> {
            Food food = list.get(toShowCategoryIndex).getFoodList().get(position);
            List<Bill> billList = Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithFoodID(food.getId(), Helper.getInstance().getSelectedRestaurantDecryptedQRCode());

            int counter = billList.isEmpty() ? 0 : billList.get(0).getCounter();
            counter++;
            Fragment_Cart.addToCart(food.getId(), counter, false);
            ((Fragment_Main) fragmentManager.findFragmentById(R.id.main_frame)).updateBadge();
            Helper.getInstance().toast(Encryption.getInstance().decrypt(list.get(toShowCategoryIndex).getFoodList().get(position).getName()) + " به سید خرید اضافه شد", Constants.ToastMode.SUCCESS);
        });
        Helper.getInstance().changeStrokeColorToMainAppColor(add);
        add.setTextColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        Picasso.get().load(Encryption.getInstance().decrypt(list.get(toShowCategoryIndex).getFoodList().get(position).getPictures().get(0))).into(image);
        cardView.setOnClickListener(v -> {
            Fragment_FoodDescriptionDetail fragment = new Fragment_FoodDescriptionDetail();
            Bundle bundle = new Bundle();
            bundle.putInt("Food_ID", list.get(toShowCategoryIndex).getFoodList().get(position).getId());
            fragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("food_description")
                    .add(R.id.main_frame, fragment)
                    .commit();
        });
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation scale_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both);
            viewToAnimate.startAnimation(scale_animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (!list.isEmpty() && list.get(toShowCategoryIndex).getFoodList() != null)
            return list.get(toShowCategoryIndex).getFoodList().size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
