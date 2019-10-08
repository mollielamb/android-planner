package com.example.mollie.examplanner;

//created by Mollie on 02/04/2018

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

public class SimpleCalendar extends AppCompatActivity {

        //declare
        CalendarView calendarView;
        TextView dateDisplay;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up calendar view and display the current date
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        dateDisplay = (TextView) findViewById(R.id.date_display);
        dateDisplay.setText("Date: ");


}
}

