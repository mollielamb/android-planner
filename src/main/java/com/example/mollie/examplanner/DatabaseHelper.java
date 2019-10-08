package com.example.mollie.examplanner;

//created by Mollie on 02/04/2018

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase myDatabase;

    //set up two tables, one for the notes and one for scheduled events
    public static final String TABLE_NAME = "Notes";
    public static final String TABLE2_NAME = "Events";

    //columns for notes table
    public static final String ID = "_id";
    public static final String TITLE = "Title";
    public static final String CONTENT= "Content";

    //columns for events table
    public static final String _ID = "_id";
    public static final String EVENT_TYPE = "event_type";
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_DESC = "event_desc";
    public static final String EVENT_DATE = "event_date";
    public static final String EVENT_AM_PM = "am_pm";
    public static final String EVENT_TIME = "event_time";

    //database name
    static final String DB_NAME = "Exam.DB";

    // database version
    static final int DB_VERSION = 1;

    //table queries, adding columns to tables
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + CONTENT + " TEXT);";

    private static final String CREATE_TABLE2 = "create table " + TABLE2_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  EVENT_TYPE + " TEXT NOT NULL, " + EVENT_TITLE + " TEXT NOT NULL, " + EVENT_DESC + " TEXT NOT NULL, "
            + EVENT_DATE + " DATE, " + EVENT_AM_PM + " TEXT NOT NULL, " + EVENT_TIME + " TIME);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //execute queries
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
    }


}
