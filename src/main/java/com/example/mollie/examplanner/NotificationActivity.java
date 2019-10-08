package com.example.mollie.examplanner;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {

    //declare
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle toggleDrawer;
    private String notificationActivity;


    String TAG = "Notification Schedule";
    LocalData localData;

    SwitchCompat switchNotification;
    TextView textView;

    LinearLayout notificationTime;

    int hour, min;

    ClipboardManager myClipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //correct layout file
        setContentView(R.layout.activity_notification);

        localData = new LocalData(getApplicationContext());

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        notificationTime = findViewById(R.id.scheduleTime);

        textView = findViewById(R.id.timeDisplay);

        switchNotification =  findViewById(R.id.timerSwitch);

        hour = localData.get_hour();
        min = localData.get_min();

        textView.setText(getFormatedTime(hour, min));
        switchNotification.setChecked(localData.getReminderStatus());

        if (!localData.getReminderStatus())
            notificationTime.setAlpha(0.4f);

        //check if notification switch is on or off
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setReminderStatus(isChecked);
                //if the switch is on send notifications on scheduled time
                if (isChecked) {
                    Log.d(TAG, "checkedOn: true");
                    NotificationSchedule.setReminder(NotificationActivity.this, NotificationReceiver.class, localData.get_hour(), localData.get_min());
                    notificationTime.setAlpha(1f);
                }
                //if the switch is off cancel notifications
                else {
                    Log.d(TAG, "checkedOff: false");
                    NotificationSchedule.cancelReminder(NotificationActivity.this, NotificationReceiver.class);
                    notificationTime.setAlpha(0.4f);
                }

            }
        });

        //perform action when the set time option is clicked
        notificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.getReminderStatus())
                    showTimePickerDialog(localData.get_hour(), localData.get_min());
            }
        });

        //set up and display side navigation bar
        drawerListView = findViewById(R.id.navDrawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        notificationActivity = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    //allow the user to select the time they want to schedule their notifications for
    private void showTimePickerDialog(int hour, int min) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timepicker, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Log.d(TAG, "onTimeSet: hour " + hour);
                        Log.d(TAG, "onTimeSet: min " + min);
                        localData.set_hour(hour);
                        localData.set_min(min);
                        textView.setText(getFormatedTime(hour, min));
                        NotificationSchedule.setReminder(NotificationActivity.this, NotificationReceiver.class, localData.get_hour(), localData.get_min());


                    }
                }, hour, min, false);

        builder.setCustomTitle(view);
        builder.show();

    }

    //format the time
    public String getFormatedTime(int hour, int min) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = hour + ":" + min;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    //set up the items that appear in the nav drawer
    private void addDrawerItems() {
        final String[] osArray = {"Notes", "Calendar", "Notifications"};
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        drawerListView.setAdapter(arrayAdapter);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    //set up the drawer to open and close on command
    private void setupDrawer() {
        toggleDrawer = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            //when the drawer is fully opened
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }

            //when the drawer is fully closed
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(notificationActivity);
                invalidateOptionsMenu();
            }
        };

        toggleDrawer.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggleDrawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggleDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleDrawer.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent add_mem = new Intent(this, AddActivity.class);
            startActivity(add_mem);

            return true;
        }

        // navigation drawer toggle
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //set up which activity to go to depending on the option the user selects
    private void selectItem(int position) {


        // Locate Position
        switch (position) {
            //'notes' is selected, takes the user to the notes page - mainactivity
            case 0:
                startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                break;
            //'calendar' is selected, takes the user to the calendar page - examactivity
            case 1:
                startActivity(new Intent(NotificationActivity.this, ExamActivity.class));
                break;
            //'notifications' is selected, user stays where they already are
            case 2:
                break;
        }
    }
}
