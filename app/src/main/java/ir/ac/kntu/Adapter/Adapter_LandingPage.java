package ir.ac.kntu.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Fragment.Fragment_LandingPage;
import ir.ac.kntu.Fragment.Fragment_Main;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class Adapter_LandingPage extends androidx.recyclerview.widget.RecyclerView.Adapter {
    private List<Restaurant> list;
    private FragmentManager fragmentManager;
    private Activity activity;
    private int SCANNER_ITEM_POSITION;
    private int lastPosition = -1;

    public Adapter_LandingPage(FragmentActivity activity, FragmentManager fragmentManager, List<Restaurant> list, int SCANNER_ITEM_POSITION) {
        this.list = list;
        this.activity = activity;
        this.SCANNER_ITEM_POSITION = SCANNER_ITEM_POSITION;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_landing_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardView cardView = holder.itemView.findViewById(R.id.item_lading_page_item);
        ConstraintLayout scanItem = holder.itemView.findViewById(R.id.item_lading_page_scan_item);
        ConstraintLayout nonScanItem = holder.itemView.findViewById(R.id.item_lading_page_non_scan_item);
        ConstraintLayout othersContainer = holder.itemView.findViewById(R.id.item_lading_page_others_content_continer);
        ImageView image = holder.itemView.findViewById(R.id.item_lading_page_image);
        TextViewPlus name = holder.itemView.findViewById(R.id.item_lading_page_name);
        TextViewPlus type = holder.itemView.findViewById(R.id.item_lading_page_type);
        TextViewPlus address = holder.itemView.findViewById(R.id.item_lading_page_address);

        scanItem.setVisibility(list.get(position) == null ? View.VISIBLE : View.GONE);
        nonScanItem.setVisibility(list.get(position) != null ? View.VISIBLE : View.GONE);

        if (list.get(position) != null) { //was not scan item
            try {
                ImageLoader.getInstance().displayImage(Encryption.getInstance().decrypt(list.get(position).getPictures().get(0)), image);
                if (!list.get(position).isActive()) {
                    othersContainer.setBackground(null);
                    Helper.getInstance().setLockedOnGrayScale(image);
                    othersContainer.setBackground(ContextHelper.retrieveContext().getResources().getDrawable(R.drawable.dr_landing_page_item_gradient_inactive));
                } else {
                    Helper.getInstance().setUnlockedForGrayScale(image);
                    othersContainer.setBackground(ContextHelper.retrieveContext().getResources().getDrawable(R.drawable.dr_landing_page_item_gradient_active));
                }
                name.setText(Encryption.getInstance().decrypt(list.get(position).getName()));
                type.setText(Encryption.getInstance().decrypt(list.get(position).getType()));
                address.setText(Encryption.getInstance().decrypt(list.get(position).getAddress().getCity()) + ", " + Encryption.getInstance().decrypt(list.get(position).getAddress().getStreet1()));
            } catch (Exception e) {
                Helper_Log.errorLog(e, Adapter_LandingPage.class);
            }
        }
        scanItem.setOnClickListener(v -> Fragment_LandingPage.runScanner(activity, fragmentManager, null));

        nonScanItem.setOnClickListener(v -> {
            if (list.get(position).isActive()) {
                Restaurant restaurant = list.get(position);
                if (restaurant != null) {

                    try {
                        Setting.getInstance().saveSetting(Constants._TABLE_USER,
                                Constants._KEY_RESTAURANT_SELECTION_ENCRYPTED_QR_CODE,
                                Encryption.getInstance().decrypt(restaurant.getEncryptedCode()));
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Adapter_LandingPage.class);
                    }
                    if (Database.getInstance(ContextHelper.retrieveContext(),
                            Constants._MAIN_DATABASE).restaurantInterface().getRestaurant(restaurant.getId()) == null) {
                        Database.getInstance(ContextHelper.retrieveContext(),
                                Constants._MAIN_DATABASE).restaurantInterface().insert(restaurant);
                    }
                    Fragment_Main mainFragment = new Fragment_Main();
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("main")
                            .replace(R.id.main_frame, mainFragment)
                            .commit();

                }
            } else
                Helper.getInstance().toast(list.get(position).getName() + " " + ContextHelper.retrieveContext().getString(R.string.restaurant_is_not_member), Constants.ToastMode.INFO);
        });
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if ((position > lastPosition))
        if (true) {
            Animation slide_to_left = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_rtl);
            Animation slide_to_right = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_ltr);
            Animation slide_to_down = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_down);
            Animation slide_to_up = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.slide_up);
            if (position == 0) {
                viewToAnimate.startAnimation(slide_to_down);
            } else if (position == list.size() - 1) {
                viewToAnimate.startAnimation(slide_to_up);
            } else {
                viewToAnimate.startAnimation(irRightItem(position) ? slide_to_left : slide_to_right);
            }
            lastPosition = position;
        }
    }

    private boolean irRightItem(int position) {
        float answer = (position - 2) / 3f;
        return answer == (int) answer;
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
