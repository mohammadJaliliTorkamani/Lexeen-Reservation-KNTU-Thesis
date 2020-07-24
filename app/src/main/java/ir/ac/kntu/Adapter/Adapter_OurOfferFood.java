package ir.ac.kntu.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.ac.kntu.Entity.Offer;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Interface.Retrofit.Food_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_OurOfferFood extends RecyclerView.Adapter {
    private View view;
    private List<Offer> list;
    private FragmentManager fragmentManager;
    private int lastPosition = -1;

    public Adapter_OurOfferFood(View view, FragmentManager fragmentManager, List<Offer> list) {
        this.view = view;
        this.list = list;
        this.fragmentManager = fragmentManager;
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
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_our_offer, parent, false));
    }

    /**
     * initialize view object values + managers  from the list
     *
     * @param holder   to access item UI object
     * @param position position of the current row
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardView constraintLayout = holder.itemView.findViewById(R.id.our_offer_item_item);
        Setting.getInstance().setItemWidth(constraintLayout, 2.1);

        ImageView image = holder.itemView.findViewById(R.id.our_offer_item_image);
        TextViewPlus foodName = holder.itemView.findViewById(R.id.our_offer_item_food_name);
        TextViewPlus offPercentage = holder.itemView.findViewById(R.id.our_offer_item_discount_percentage);
        image.setBackgroundResource(R.drawable.dr_gradient_effect);

        offPercentage.setText(Helper.getInstance().getOneDigitOrNon(list.get(position).getDiscountPercentage(), false) + " %");
        Connector.createService(view, Food_Server_API.class, object -> {
            Call<ir.ac.kntu.Entity.Food> call = object.getFood(list.get(position).getFoodID());
            call.enqueue(new Callback<ir.ac.kntu.Entity.Food>() {
                @Override
                public void onResponse(Call<ir.ac.kntu.Entity.Food> call, Response<ir.ac.kntu.Entity.Food> response) {
                    if (response.body() != null) {
                        foodName.setText(Encryption.getInstance().decrypt(response.body().getName()));
                        Picasso.get().load(Encryption.getInstance().decrypt(response.body().getPictures().get(0))).into(image);
                    } else
                        Helper_Log.errorLog(Adapter_OurOfferFood.class);
                }

                @Override
                public void onFailure(Call<ir.ac.kntu.Entity.Food> call, Throwable t) {
                    Helper_Log.errorLog(t, Adapter_OurOfferFood.class);
                }
            });
        });
        constraintLayout.setOnClickListener(v -> {
            Fragment fragment = new Fragment_FoodDescriptionDetail();
            Bundle bundle = new Bundle();
            bundle.putInt("Food_ID", list.get(position).getFoodID());
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("food_description").add(R.id.main_frame, fragment).commit();
        });
        setAnimation(holder.itemView, position);
    }

    /**
     * starts animation for the passed view object
     *
     * @param viewToAnimate view to show in animation mode
     * @param position      position of the passed view object
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation scale_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both);
            viewToAnimate.startAnimation(scale_animation);
            lastPosition = position;
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
