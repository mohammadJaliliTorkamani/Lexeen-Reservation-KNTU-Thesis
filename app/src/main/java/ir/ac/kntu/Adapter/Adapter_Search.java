package ir.ac.kntu.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.List;

import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_Search extends RecyclerView.Adapter {
    protected FragmentManager fragmentManager;
    private ImageView selectIcon;
    private Activity activity;
    private List<Food> list;
    private int lastPosition = -1;

    public Adapter_Search(FragmentManager fragmentManager, FragmentActivity activity, List<Food> list) {
        this.fragmentManager = fragmentManager;
        this.list = list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus text = holder.itemView.findViewById(R.id.search_item_text);
        ImageView image = holder.itemView.findViewById(R.id.search_item_image);
        ImageView selectIcon = holder.itemView.findViewById(R.id.search_item_select_icon);
        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.search_item_item);
        image.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));

        text.setText(list.get(position).getName());
        SvgLoader.pluck().with(activity).load(list.get(position).getPictures().get(4), image);
        constraintLayout.setOnClickListener(v -> {
            Fragment fragment = new Fragment_FoodDescriptionDetail();
            Bundle bundle = new Bundle();
            bundle.putInt("Food_ID", list.get(position).getId());

            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("food_description").add(R.id.main_frame, fragment).commit();
        });
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
        if (true) {
            Animation scale_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both);
            viewToAnimate.startAnimation(scale_animation);
            lastPosition = position;
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
