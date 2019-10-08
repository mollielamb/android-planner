package com.example.mollie.examplanner;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Locale.getDefault;


public class ExamActivity extends AppCompatActivity {

    //declare

    TextView dateDisplay;

    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle toggleDrawer;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalendarView calendarView;

        super.onCreate(savedInstanceState);
        //getting correct layout file
        setContentView(R.layout.simple_calendar);

        calendarView = findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        //show the current date using the simple date format
        dateDisplay = findViewById(R.id.date_display);
        dateDisplay.setText("Date: " + strDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                //when one of the dates is selected, the user is taken to the events page for that day with the current date being passed through in the process
                int month = i1 + 1;
                String monthCon = "0"+month;
                Intent intent = new Intent(ExamActivity.this, EventActivity.class);
                intent.putExtra("Date", i2 + "/" + monthCon + "/" + i);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + i2 + "\n" + "Month = " + monthCon + "\n" + "Year = " + i, Toast.LENGTH_LONG).show();
            }
        });
        //setting up the side navigation drawer
        drawerListView = findViewById(R.id.navDrawer);
        drawerLayout =  findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void addDrawerItems() {
        //adding the titles of the three pages to the drawer
        final String[] osArray = {"Notes", "Calendar", "Notifications"};
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        drawerListView.setAdapter(arrayAdapter);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    private void setupDrawer() {
        toggleDrawer = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        toggleDrawer.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggleDrawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent add_mem = new Intent(this, AddActivity.class);
            startActivity(add_mem);

            return true;
        }

        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {

        //based on the item selected, the user is taken to the relevant page
        switch (position) {
            case 0:
                startActivity(new Intent(ExamActivity.this, MainActivity.class));
                break;
            case 1:
                break;
            case 2:
                startActivity(new Intent(ExamActivity.this, NotificationActivity.class));
                break;
        }
    }

}


