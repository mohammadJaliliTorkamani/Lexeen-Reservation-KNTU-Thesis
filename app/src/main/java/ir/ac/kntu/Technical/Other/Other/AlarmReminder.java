package ir.ac.kntu.Technical.Other.Other;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Receiver.Receiver_Alarm;

/**
 * used to set alarm at the given date/time woth order information in mind (to load when time arrived)
 */
public class AlarmReminder {
    private static AlarmReminder instance;
    AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;

    private AlarmReminder() {
        alarmManager = (AlarmManager) ContextHelper.retrieveContext().getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(ContextHelper.retrieveContext(), Receiver_Alarm.class);
    }

    public static AlarmReminder getInstance() {
        if (instance == null) {
            instance = new AlarmReminder();
        }
        return instance;
    }

    /**
     * set alarm and save order in DB
     *
     * @param orderDateTime date/time to set alarm
     * @param order         order to save in DB, and lad when date/time arrived
     */
    public void remindSingleMode(Calendar orderDateTime, Order order) {
        Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).orderInterface().insert(order);
        orderDateTime.add(Calendar.HOUR_OF_DAY, -1);
        alarmIntent.putExtra("ID", order.getOrderID());
        pendingIntent = PendingIntent.getBroadcast(ContextHelper.retrieveContext(), order.getOrderID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC, orderDateTime.getTimeInMillis(), pendingIntent);
    }
}
