package ir.ac.kntu.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.ac.kntu.Activity.Activity_AlarmDialog;
import ir.ac.kntu.Service.Service_Alarm;

public class Receiver_Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, Service_Alarm.class);
            serviceIntent.putExtra("ORDER_ID", intent.getIntExtra("ID", 0));
            context.startService(serviceIntent);
        } else {
            Intent alarmIntent = new Intent(context, Activity_AlarmDialog.class);
            alarmIntent.putExtra("ORDER_ID", intent.getIntExtra("ID", 0));
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);
        }
    }
}
