package com.example.mollie.examplanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends Activity implements OnClickListener{

    //declare

    private Button addTodoBtn;
    private EditText event_title;
    private String event_type;
    private EditText event_desc;
    private EditText event_hour;
    private EditText event_mins;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private TextView today;
    private Spinner spinner1;
    private Spinner am_pm;
    private Cursor c;
    private EventManager eventManager;


    final String[] from = new String[]{ DatabaseHelper._ID, DatabaseHelper.EVENT_TYPE,
            DatabaseHelper.EVENT_TITLE, DatabaseHelper.EVENT_DESC,  DatabaseHelper.EVENT_DATE, DatabaseHelper.EVENT_TIME, DatabaseHelper.EVENT_AM_PM};

    final int[] to = new int[]{R.id.id, R.id.event_type, R.id.event_name, R.id.event_content, R.id.event_date, R.id.event_time};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Event");
        //get layout
        setContentView(R.layout.activity_event);
        //get the date from the previous page
        Bundle bundle = getIntent().getExtras();
        final String date = bundle.getString("Date");
        //set up variables
        spinner1 = findViewById(R.id.spinner1);
        event_title = findViewById(R.id.event_title);
        event_desc = findViewById(R.id.event_content);
        event_hour =  findViewById(R.id.timeHH);
        event_mins =  findViewById(R.id.timeMM);
        am_pm = findViewById(R.id.am_pm_spinner);

        addTodoBtn = findViewById(R.id.addNote);

        eventManager = new EventManager(this);
        eventManager.open();
        //onclicklistener on add event page
        addTodoBtn.setOnClickListener(this);
        //spinner listeners
        addListenerOnSpinnerItemSelection();
        addListenerOnTimeSpinnerItemSelection();
        //fetch all the events occurring on that date from the database using a cursor
        Cursor cursor = eventManager.fetchDate(date);
        //set up listview from different layout file
        listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));
        //cursor adapter to get the required values from the database
        adapter = new SimpleCursorAdapter(EventActivity.this, R.layout.view_events, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);


    }



    @Override
    public void onClick(View v){
        if (validate()) {
            Bundle bundle = getIntent().getExtras();
            String date = bundle.getString("Date");

            switch (v.getId()) {
                //add note button listener
                case R.id.addNote:
                    //toast message showing which options from the 2 spinners the user chose
                    Toast.makeText(EventActivity.this,
                            "You have selected : " +
                                    "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()),
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(EventActivity.this,
                            "You have selected : " +
                                    "\nAM/PM Spinner : " + String.valueOf(am_pm.getSelectedItem()),
                            Toast.LENGTH_SHORT).show();
                    //set up values to be inserted into database
                    final String type = spinner1.getSelectedItem().toString();
                    final String name = event_title.getText().toString();
                    final String desc = event_desc.getText().toString();
                    final String hours = event_hour.getText().toString();
                    final String mins = event_mins.getText().toString();
                    final String time_of_day = am_pm.getSelectedItem().toString();
                    final String time = hours + ":" + mins;

                    //add type, name, description, date, am/pm and time
                    eventManager.insert(type, name, desc, date, time_of_day, time);
                    //go back to calendar view page
                    Intent main = new Intent(EventActivity.this, ExamActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);
                    break;
            }
        }
    }

    public boolean validate() {
        boolean valid = true;

        //Get values from EditText fields
        String title = event_title.getText().toString();
        String content = event_desc.getText().toString();
        String hours = event_hour.getText().toString();
        String mins = event_mins.getText().toString();
        int startHour = 0;
        int startMin = 0;
        //basic input validation
        //check time fields have numbers entered
        try {
            startHour = Integer.parseInt(hours);
        } catch (NumberFormatException nfe) {
            event_hour.setError("Enter Hour");
            valid = false;
        }

        try {
            startMin = Integer.parseInt(mins);
        } catch (NumberFormatException nfe) {
            event_mins.setError("Enter Minutes");
            valid = false;
        }


        //Handling validation for title field
        if (title.isEmpty()) {
            valid = false;
            event_title.setError("Please enter the event title");
        }
        //checking content isn't empty
        if (content.isEmpty()) {
            valid = false;
            event_desc.setError("Please enter the event description");
        }
        //checking hours and minutes aren't empty and that they are within the proper range of times
        if (hours.isEmpty()) {
            valid = false;
            event_hour.setError("Please enter the starting hour");
        } else if (startHour > 23 || startHour < 0) {
            valid = false;
            event_hour.setError("Please enter a valid time");
        } else if (mins.isEmpty()) {
            valid = false;
            event_mins.setError("Please enter the starting hour");
        } else if (startMin > 59 || startMin < 0) {
            valid = false;
            event_mins.setError("Please enter a valid time");
        } else {
            return valid;
        }

        return valid;
    }
    //listeners for spinners
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }
    public void addListenerOnTimeSpinnerItemSelection() {
        am_pm = (Spinner) findViewById(R.id.am_pm_spinner);
        am_pm.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }
}


