package ir.ac.kntu.Adapter;

import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import ir.ac.kntu.Entity.Offer;
import ir.ac.kntu.Fragment.Fragment_FoodDescriptionDetail;
import ir.ac.kntu.Interface.Retrofit.Operable_Food;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_our_offer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardView constraintLayout = holder.itemView.findViewById(R.id.our_offer_item_item);
        Setting.getInstance().setItemWidth(constraintLayout, 2.1);

        ImageView image = holder.itemView.findViewById(R.id.our_offer_item_image);
        TextViewPlus foodName = holder.itemView.findViewById(R.id.our_offer_item_food_name);
        TextViewPlus offPercentage = holder.itemView.findViewById(R.id.our_offer_item_discount_percentage);
        image.setBackgroundResource(R.drawable.dr_gradient_effect);

        offPercentage.setText(Helper.getInstance().getOneDigitOrNon(list.get(position).getDiscountPercentage(), false) + " %");
        Connector.createService(view, Operable_Food.class, object -> {
            Call<ir.ac.kntu.Entity.Food> call = object.getFood(list.get(position).getFoodID());
            call.enqueue(new Callback<ir.ac.kntu.Entity.Food>() {
                @Override
                public void onResponse(Call<ir.ac.kntu.Entity.Food> call, Response<ir.ac.kntu.Entity.Food> response) {
                    if (response.body() != null) {
                        foodName.setText(response.body().getName());
                        ImageLoader.getInstance().displayImage(response.body().getPictures().get(0), image, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setBackgroundColor(ContextHelper.retrieveContext().getResources().getColor(R.color.gray_default_background));
                                image.setImageResource(R.drawable.ic_lexin_gray);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                Helper_Log.errorLog(failReason.getCause(), Adapter_OurOfferFood.class);
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setImageResource(R.drawable.ic_lexin_gray);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setImageBitmap(loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                Helper_Log.errorLog(Adapter_OurOfferFood.class);
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setBackgroundColor(ContextHelper.retrieveContext().getResources().getColor(R.color.gray_default_background));
                                image.setImageResource(R.drawable.ic_lexin_gray);
                            }
                        });
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
