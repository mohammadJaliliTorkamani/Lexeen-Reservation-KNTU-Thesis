package ir.ac.kntu.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import ir.ac.kntu.Technical.Other.Other.Setting;
import ir.map.servicesdk.MapService;
import ir.map.servicesdk.ResponseListener;
import ir.map.servicesdk.model.base.MapirError;
import ir.map.servicesdk.response.ReverseGeoCodeResponse;

public class Adapter_UserFoodOder extends RecyclerView.Adapter {
    private FragmentManager fragmentManager;
    private List<Order> orders;
    private int lastPosition = -1;
    private Activity activity;
    private MapService mapService = new MapService();


    public Adapter_UserFoodOder(FragmentManager fragmentManager, Activity activity, List<Order> orders) {
        this.fragmentManager = fragmentManager;
        this.orders = orders;
        this.activity = activity;

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
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_user_order, parent, false));
    }

    /**
     * initialize view object values + managers  from the list
     *
     * @param holder   to access item UI object
     * @param position position of the current row
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardView foodIconCard = holder.itemView.findViewById(R.id.user_order_item_category_image_card);
        ImageView foodIcon = holder.itemView.findViewById(R.id.user_order_item_category_image);
        TextViewPlus restaurant = holder.itemView.findViewById(R.id.user_order_item_restaurant);
        ImageView qrCode = holder.itemView.findViewById(R.id.user_order_item_qr_code);
        RecyclerView recyclerView = holder.itemView.findViewById(R.id.user_order_item_items);
        TextViewPlus date = holder.itemView.findViewById(R.id.user_order_item_date);
        TextViewPlus price = holder.itemView.findViewById(R.id.user_order_item_price);
        TextViewPlus priceUnit = holder.itemView.findViewById(R.id.user_order_item_price_unit);

        try {
            Bitmap bitmap = new QRGEncoder(Encryption.getInstance().decrypt(orders.get(position).getQrCodeValue()), null, QRGContents.Type.TEXT, 640).encodeAsBitmap();
            qrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Helper_Log.errorLog(e, Adapter_UserFoodOder.class);
        }

        price.setText(Helper.getInstance().getOneDigitOrNon(orders.get(position).getTotalPrice(), true));
        priceUnit.setText(Helper.getInstance().getPurchaseUnit());

        try {
            date.setText(Encryption.getInstance().decrypt(orders.get(position).getDate_and_time_start()));
            restaurant.setText(Encryption.getInstance().decrypt(orders.get(position).getRestaurant()));
        } catch (Exception e) {
            Helper_Log.errorLog(e, Adapter_UserFoodOder.class);
        }

        foodIconCard.setBackgroundResource(orders.get(position).isDelivery() ? R.drawable.dr_gray_border : 0);
        foodIcon.setImageResource(orders.get(position).isDelivery() ? R.drawable.ic_delivery_motorbike : R.drawable.ic_sample_chairs);
        foodIcon.setOnClickListener(v -> {
            if (orders.get(position).isDelivery()) {
                mapService.reverseGeoCode(orders.get(position).getLatitude(), orders.get(position).getLongitude(), new ResponseListener<ReverseGeoCodeResponse>() {
                    @Override
                    public void onSuccess(ReverseGeoCodeResponse response) {
                        Helper.getInstance().toast(ContextHelper.retrieveContext().getString(R.string.deliver_to_address) + " : " + response.getAddress(), Constants.ToastMode.INFO);
                    }

                    @Override
                    public void onError(MapirError error) {
                        Helper_Log.errorLog(Adapter_UserFoodOder.class);
                    }
                });
            }
        });

        RecyclerView.Adapter adapter = new Adapter_UserOrdersSubItems(orders.get(position).getSpecifiedBills());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        qrCode.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View inflateView = LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.dialog_order_code, null, false);
            builder.setView(inflateView);
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            ImageView qr = inflateView.findViewById(R.id.dialog_order_qr_image);
            ConstraintLayout titleContainer = inflateView.findViewById(R.id.dialog_order_title_container);
            TextViewPlus title = inflateView.findViewById(R.id.dialog_order_title);
            TextViewPlus dateTime = inflateView.findViewById(R.id.dialog_order_date_time);
            TextViewPlus code = inflateView.findViewById(R.id.dialog_order_code);
            TextViewPlus close = inflateView.findViewById(R.id.dialog_order_close);
            titleContainer.setBackgroundResource(R.drawable.rec_curve_top);
            close.setBackgroundResource(R.drawable.rec_curve_bottom);
            Helper.getInstance().changeShapeColorToMainAppColor(titleContainer);
            Helper.getInstance().changeShapeColorToMainAppColor(close);
            close.setOnClickListener(v1 -> dialog.dismiss());
            qr.setOnClickListener(v1 -> {
                Setting.getInstance().copyToClipBoard(ContextHelper.retrieveContext(), Encryption.getInstance().decrypt(orders.get(position).getQrCodeValue()));
                Helper.getInstance().toast(R.string.copied_to_clipboard, Constants.ToastMode.SUCCESS);
            });
            title.setText(orders.get(position).isDelivery() ? "رسید تحویل" : "رسید رزرو");
            try {
                Bitmap bitmap = new QRGEncoder(Encryption.getInstance().decrypt(orders.get(position).getQrCodeValue()), null, QRGContents.Type.TEXT, 480).encodeAsBitmap();
                qr.setImageBitmap(bitmap);
            } catch (Exception e) {
                Helper_Log.errorLog(e, Adapter_UserFoodOder.class);
            }

            try {
                code.setText(Encryption.getInstance().decrypt(orders.get(position).getQrCodeValue()));
                code.setOnClickListener(v1 -> qr.callOnClick());
                dateTime.setText(Encryption.getInstance().decrypt(orders.get(position).getDate_and_time_start()));
            } catch (Exception e) {
                Helper_Log.errorLog(e, Adapter_UserFoodOder.class);
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });
        setAnimation(holder.itemView, position);

    }

    /**
     * starts animation fro the passed view object
     *
     * @param viewToAnimate view object to show in animation mode
     * @param position      of the passed view object
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
        if (orders == null)
            return 0;
        return orders.size();
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
