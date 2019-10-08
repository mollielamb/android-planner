package com.example.mollie.examplanner;

import android.content.Context;
import android.content.SharedPreferences;


public class LocalData {

    private static final String APP_SHARED_PREFS = "Exam Scheduler";

    private SharedPreferences examSharedPrefs;
    private SharedPreferences.Editor editPrefs;

    private static final String reminderStatus="notificationStatus";
    private static final String hour="hour";
    private static final String min="min";

    public LocalData(Context context)
    {
        this.examSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.editPrefs = examSharedPrefs.edit();
    }

    //check whether reminders are on or off
    public boolean getReminderStatus()
    {
        return examSharedPrefs.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status)
    {
        editPrefs.putBoolean(reminderStatus, status);
        editPrefs.commit();
    }


    public int get_hour()
    {
        return examSharedPrefs.getInt(hour, 20);
    }

    public void set_hour(int h)
    {
        int reminderHour = h;
        editPrefs.putInt(hour, reminderHour);
        editPrefs.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_min()
    {
        return examSharedPrefs.getInt(min, 0);
    }

    public void set_min(int m)
    {
        editPrefs.putInt(min, m);
        editPrefs.commit();
    }

    public void reset()
    {
        editPrefs.clear();
        editPrefs.commit();

    }

}
