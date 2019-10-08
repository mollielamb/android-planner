package com.example.mollie.examplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;


public class NotificationSchedule
{
    //declare

    public static final int DAILY_REMINDER_REQUEST_CODE=100;
    public static final String TAG="Notification Schedule";
    private static EventManager eventManager;

    public static void setReminder(Context context,Class<?> cls,int hour, int min)
    {

        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.set(Calendar.HOUR_OF_DAY, hour);
        thisCalendar.set(Calendar.MINUTE, min);
        thisCalendar.set(Calendar.SECOND, 0);

        // cancel already scheduled reminders
        cancelReminder(context,cls);

        if(thisCalendar.before(thisCalendar))
            thisCalendar.add(Calendar.DATE,1);

        //receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, thisCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static void cancelReminder(Context context,Class<?> cls)
    {
        // disable receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context,Class<?> cls)
    {
        //get today's date in a string format
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date());

        //instance of eventManager
        eventManager = new EventManager(context);
        eventManager.open();
        //pass the current date through to the getEvents function of EventManager
        //this returns the number of events scheduled for the day
        int dailyCount = eventManager.getEvents(date);

        android.support.v7.app.NotificationCompat.Builder notificationBuilder= new android.support.v7.app.NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.calendar);
        //set the title as the day's date
        if(dailyCount > 0) {
            notificationBuilder.setContentTitle(date);
        }else {
            notificationBuilder.setContentTitle(date);
        }
        notificationBuilder.setTicker("Take a look");

        notificationBuilder.setAutoCancel(true);



        //show ic_icon in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar);
        android.support.v7.app.NotificationCompat.BigPictureStyle s = new android.support.v7.app.NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        //change the message displayed based on whether there are any events
        //if there are events, show number, otherwise show 'no events' message
        if(dailyCount > 0) {
            s.setSummaryText("You have " + dailyCount + " events today");
        }else {
            s.setSummaryText("You don't have any events today");
        }
        notificationBuilder.setStyle(s);



        Intent resultIntent = new Intent(context, NotificationActivity.class);
        android.app.TaskStackBuilder stackBuilder = null;
        stackBuilder = android.app.TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11221, notificationBuilder.build());

    }

}
