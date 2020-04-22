package ir.ac.kntu.Entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Fragment.Fragment_Cart;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Fragment.Fragment_Main;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

@Layout(R.layout.item_menu_food)
public class MenuFoodInfoView {
    @ParentPosition
    int parentPosition;

    @ChildPosition
    int childPosition;

    @View(R.id.menu_item_food_top_side)
    android.view.View topSide;

    @View(R.id.menu_item_food_bottom_side)
    android.view.View bottomSide;

    @View(R.id.menu_item_food_left_side)
    android.view.View leftSide;

    @View(R.id.menu_item_food_right_side)
    android.view.View rightSide;

    @View(R.id.menu_item_food_item_frame_container)
    ConstraintLayout frameContainer;

    @View(R.id.menu_item_food_item_container)
    ConstraintLayout container;

    @View(R.id.menu_item_food_item_name)
    TextViewPlus name;

    @View(R.id.menu_item_food_item_price)
    TextViewPlus price;

    @View(R.id.menu_item_food_item_price_unit)
    TextViewPlus priceUnit;

    @View(R.id.menu_item_food_item_price_container)
    LinearLayout priceContainer;

    @View(R.id.menu_item_food_plus)
    ImageView plus;

    @View(R.id.menu_item_food_minus)
    ImageView minus;

    @View(R.id.menu_item_food_counter)
    TextViewPlus counter;

    @View(R.id.menu_item_food_card)
    ConstraintLayout card;

    private Food food;
    private String color;
    private FragmentManager fragmentManager;
    private Context context;
    private int totalChildSize;

    public MenuFoodInfoView(Context context, FragmentManager fragmentManager, String color, Food food, int totalChildSize) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.food = food;
        this.color = color;
        this.totalChildSize = totalChildSize;
    }

    @Resolve
    public void onResolved() {
        if (food == null) {
            bottomSide.setVisibility(android.view.View.VISIBLE);
//            card.setmShadowSide(ShadowLayout.LEFT | ShadowLayout.RIGHT | ShadowLayout.BOTTOM);
            priceContainer.setBackgroundResource(R.drawable.dr_rec_bottom_radius);
            ((GradientDrawable) priceContainer.getBackground()).setColor(Color.parseColor(color));
            ViewGroup.LayoutParams layoutParams = priceContainer.getLayoutParams();
            ((ConstraintLayout.LayoutParams) layoutParams).bottomMargin = Helper.dp2px(24);
            priceContainer.setLayoutParams(layoutParams);
            container.setVisibility(android.view.View.VISIBLE);
            frameContainer.setVisibility(android.view.View.GONE);
            price.setText("");
            priceUnit.setText("");
        } else {
            bottomSide.setVisibility(android.view.View.GONE);
//                card.setmShadowSide(ShadowLayout.LEFT | ShadowLayout.RIGHT);
            priceContainer.setBackgroundColor(Color.parseColor(color));
            ViewGroup.LayoutParams layoutParams = priceContainer.getLayoutParams();
            ((ConstraintLayout.LayoutParams) layoutParams).bottomMargin = Helper.dp2px(0);
            priceContainer.setLayoutParams(layoutParams);
            container.setVisibility(android.view.View.VISIBLE);
            frameContainer.setVisibility(android.view.View.VISIBLE);
            name.setText(food.getName());
            price.setText(Helper.getOneDigitOrNon(food.getPrice(), true));
            priceUnit.setText(Helper.getPurchaseUnit());
            List<Bill> list = Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithFoodID(food.getId(), Helper.getRestaurantSelectionQRCode());
            counter.setText(String.valueOf(!list.isEmpty() ? list.get(0).getCounter() : 0));
            card.setOnClickListener(v -> {
                Fragment fragment = new Fragment_FoodDescriptionDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("Food_ID", food.getId());
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().addToBackStack("food_description").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).add(R.id.main_frame, fragment).commit();
            });
            plus.setOnClickListener(v -> {
                int previousNumber = Integer.parseInt(counter.getText().toString().trim());
                int newNumber = ++previousNumber;
                if (newNumber <= Constants.MAX_ADD_TO_CART_NUMBER) {
                    counter.setText(String.valueOf(newNumber));
                    Fragment_Cart.addToCart(food.getId(), newNumber, false);
                    int counter = Bill.getTotalFoodItems(Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getAll(Helper.getRestaurantSelectionQRCode()));
                    ((Fragment_Main) fragmentManager.findFragmentById(R.id.main_frame)).updateBadge();

                }
            });
            minus.setOnClickListener(v -> {
                int previousNumber = Integer.parseInt(counter.getText().toString().trim());
                int newNumber = --previousNumber;
                if (newNumber >= 0) {
                    counter.setText(String.valueOf(newNumber));
                    Fragment_Cart.addToCart(food.getId(), newNumber, false);
                    ((Fragment_Main) fragmentManager.findFragmentById(R.id.main_frame)).updateBadge();
                }
            });
        }
    }
}