package com.example.mollie.examplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    String TAG = "Notification Receive";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the notification here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationSchedule.setReminder(context, NotificationReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");


        NotificationSchedule.showNotification(context, NotificationActivity.class);

    }
}


