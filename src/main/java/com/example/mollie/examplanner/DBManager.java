package com.example.mollie.examplanner;

//created by Mollie on 02/04/2018

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    //declare
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    //open link with the database
    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    //close
    public void close() {
        dbHelper.close();
    }

    //insert into the notes database table
    public void insert(String title, String content) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.CONTENT, content);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        close();
    }

    //get all the notes currently stored
    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.ID, DatabaseHelper.TITLE, DatabaseHelper.CONTENT };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //update a certain note based around its id
    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TITLE, name);
        contentValues.put(DatabaseHelper.CONTENT, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.ID + " = " + _id, null);
        close();
        return i;
    }

    //remove a specific note from the database using the id
    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + _id, null);
        close();
    }

}
