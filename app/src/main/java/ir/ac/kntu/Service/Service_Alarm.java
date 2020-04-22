package ir.ac.kntu.Service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.R;
import ir.ac.kntu.Receiver.Receiver_Alarm;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import saman.zamani.persiandate.PersianDate;

public class Service_Alarm extends IntentService {


    public Service_Alarm() {
        super("Service_Alarm");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<Order> orders = Database.getInstance(this, Constants._MAIN_DATABASE).orderInterface().getOrder(intent.getIntExtra("ORDER_ID", 0));
        Order order = null;
        if (orders != null && !orders.isEmpty()) {
            order = orders.get(0);
        } else {
            Helper.getInstance().toast(R.string.error, Constants.ToastMode.ERROR);
            return;
        }
        try {
            PersianDate persianDate = Helper.getInstance().getShamsiDateFromString(order.getDate_and_time_start(), "yyyy/MM/dd HH:mm");
            Intent alarmIntent = new Intent(this, Receiver_Alarm.class);
            alarmIntent.putExtra("ORDER_ID", order.getOrderID());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ContextHelper.retrieveContext(), order.getOrderID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) ContextHelper.retrieveContext().getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(persianDate.toDate());
            alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
            Helper_Log.errorLog(e, Service_Alarm.class);
        }
    }
}