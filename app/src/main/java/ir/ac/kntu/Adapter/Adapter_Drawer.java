package ir.ac.kntu.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Entity.DrawerItem;
import ir.ac.kntu.Fragment.Fragment_FAQ;
import ir.ac.kntu.Fragment.Fragment_Setting;
import ir.ac.kntu.Fragment.Fragment_UserOrders;
import ir.ac.kntu.Fragment.Fragment_Wallet;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_Drawer extends RecyclerView.Adapter {
    private View view;
    private FragmentManager fragmentManager;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private List<DrawerItem> userList = new LinkedList<>();
    private List<DrawerItem> guestList = new LinkedList<>();

    public Adapter_Drawer(View view, FragmentManager fragmentManager, FragmentActivity activity, DrawerLayout drawerLayout) {
        this.view = view;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        userList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.my_wallet), R.drawable.ic_wallet_filled_money_tool));
        userList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.order_history), R.drawable.ic_clock));
        userList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.faq), R.drawable.ic_faq));
        userList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.setting), R.drawable.ic_settings_gears));
        guestList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.faq), R.drawable.ic_faq));
        guestList.add(new DrawerItem(ContextHelper.retrieveContext().getString(R.string.setting), R.drawable.ic_settings_gears));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_drawer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus text = holder.itemView.findViewById(R.id.drawer_list_item_text);
        ImageView image = holder.itemView.findViewById(R.id.drawer_list_item_image);
        ConstraintLayout item = holder.itemView.findViewById(R.id.drawer_list_item);
        if (Helper.isLoggedIn()) {
            image.setImageResource(userList.get(position).getPicture());
            text.setText(userList.get(position).getTitle());
            item.setOnClickListener(v -> {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new Fragment_Wallet();
                        break;
                    case 1:
                        fragment = new Fragment_UserOrders();
                        break;
                    case 2:
                        fragment = new Fragment_FAQ();
                        break;
                    case 3:
                        fragment = new Fragment_Setting();
                        break;
                }
                if (fragment != null) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(position == 0 ? "wallet" : position == 1 ? "orders" : position == 2 ? "faq" : "setting")
                            .add(R.id.main_frame, fragment)
                            .commit();
                }
            });
        } else {
            image.setImageResource(guestList.get(position).getPicture());
            text.setText(guestList.get(position).getTitle());
            if (position == 0)
                text.setTextColor(ContextHelper.retrieveContext().getResources().getColor(R.color.light_green));
            item.setOnClickListener(v -> {

                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new Fragment_FAQ();
                        break;
                    case 1:
                        fragment = new Fragment_Setting();
                        break;
                }
                if (fragment != null) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    fragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(position == 0 ? "faq" : "setting")
                            .add(R.id.main_frame, fragment)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Helper.isLoggedIn() ? userList.size() : guestList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
