package ir.ac.kntu.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class Activity_AlarmDialog extends AppCompatActivity {
    private TextViewPlus orderType;
    private TextViewPlus restaurantName;
    private TextViewPlus dateTime;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_dialog);
        findViews();
        initializeViewContents();
    }


    private void findViews() {
        orderType = findViewById(R.id.dialog_alarm_restaurabt_type_value);
        restaurantName = findViewById(R.id.dialog_alarm_restaurant_restaurant_name_value);
        dateTime = findViewById(R.id.dialog_alarm_restaurabt_date_value);
    }

    private void initializeViewContents() {
        Setting.getInstance().vibrate(1000);
        MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).start();
        setFinishOnTouchOutside(true);
        List<Order> orders = Database.getInstance(this, Constants._MAIN_DATABASE).orderInterface().getOrder(getIntent().getIntExtra("ORDER_ID", 0));
        if (orders != null && !orders.isEmpty()) {
            order = orders.get(0);
            Database.getInstance(this, Constants._MAIN_DATABASE).orderInterface().remove(order.getOrderID());
        } else {
            Helper.toast(R.string.error, Constants.ToastMode.ERROR);
            return;
        }
        orderType.setText(order.isDelivery() ? R.string.delivery_in_place : R.string.serve_at_restaurant);
        restaurantName.setText(order.getRestaurant());
        dateTime.setText(Helper.getShamsiDateTimeFromGregortianString(order.getDate_and_time_start()));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
