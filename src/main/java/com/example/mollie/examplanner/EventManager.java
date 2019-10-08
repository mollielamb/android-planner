package com.example.mollie.examplanner;
//created by Mollie on 04/04/2018

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import static com.example.mollie.examplanner.DatabaseHelper.EVENT_AM_PM;
import static com.example.mollie.examplanner.DatabaseHelper.EVENT_TIME;
import static com.example.mollie.examplanner.DatabaseHelper.TABLE2_NAME;


public class EventManager {
    //declare

    private DatabaseHelper databaseHelper;

    private Context context;

    private SQLiteDatabase myDatabase;

    public EventManager(Context c) {
        context = c;
    }

    public EventManager open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        myDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public void insert(String type, String title, String content, String addDate, String am_pm, String addTime) {
        //adding all necessary values

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EVENT_TYPE, type);
        contentValue.put(DatabaseHelper.EVENT_TITLE, title);
        contentValue.put(DatabaseHelper.EVENT_DESC, content);
        contentValue.put(DatabaseHelper.EVENT_DATE, addDate);
        contentValue.put(DatabaseHelper.EVENT_AM_PM, am_pm);
        contentValue.put(DatabaseHelper.EVENT_TIME, addTime);
        myDatabase.insert(TABLE2_NAME, null, contentValue);
    }



    public void delete(long _id) {
        myDatabase.delete(TABLE2_NAME, DatabaseHelper.EVENT_DATE + "=" + _id, null);
    }

    public Cursor fetchDate(String date) {
        //fetching all data with the passed through date, ordering firstly by am/pm and then by the actual time
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.EVENT_TYPE, DatabaseHelper.EVENT_TITLE, DatabaseHelper.EVENT_DESC, DatabaseHelper.EVENT_DATE, DatabaseHelper.EVENT_AM_PM, DatabaseHelper.EVENT_TIME};
        Cursor cursor = myDatabase.query(TABLE2_NAME, columns, "event_date=?",new String[]{date}, null, null, EVENT_AM_PM + " ASC, " + EVENT_TIME  + " ASC", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int getEvents(String strDate) {
        int count = 0;

        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.EVENT_TYPE, DatabaseHelper.EVENT_TITLE, DatabaseHelper.EVENT_DESC, DatabaseHelper.EVENT_DATE,
                DatabaseHelper.EVENT_AM_PM, DatabaseHelper.EVENT_TIME};
        Cursor cursor = myDatabase.query(TABLE2_NAME, columns, "event_date=?",new String[]{strDate}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getCount();
        }
        return count;
    }

}

