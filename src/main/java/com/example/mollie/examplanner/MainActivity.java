package com.example.mollie.examplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<String> arrayAdapter;
    private ActionBarDrawerToggle toggleDrawer;
    private String noteActivity;

    private DBManager databaseManager;

    private ListView listView;

    private SimpleCursorAdapter cursorAdapter;

    private Cursor c;


    final String[] from = new String[]{DatabaseHelper._ID,
            DatabaseHelper.TITLE, DatabaseHelper.CONTENT};

    final int[] to = new int[]{R.id.id, R.id.title, R.id.content};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //open up database, use cursor to fetch all notes
        databaseManager = new DBManager(this);
        databaseManager.open();
        Cursor cursor = databaseManager.fetch();

        listView =  findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.view_notes, cursor, from, to, 0);
        cursorAdapter.notifyDataSetChanged();

        listView.setAdapter(cursorAdapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = view.findViewById(R.id.id);
                TextView titleTextView = view.findViewById(R.id.title);
                TextView descTextView =  view.findViewById(R.id.content);
                //set up values and pass them through to edit page
                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), EditActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
            }
        });
        //set up to delete item on long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView arg0, View v, final int position, final long id) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Delete");
                ad.setMessage("Sure you want to delete this note?");
                //final long id = id;
                ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseManager = new DBManager(MainActivity.this);
                        databaseManager.open();
                        //locating note in database based on the id
                        databaseManager.delete(id);
                        Toast.makeText(MainActivity.this, "Item Successfully Deleted", Toast.LENGTH_LONG).show();
                        //display updated listview
                        c=databaseManager.fetch();
                        cursorAdapter.swapCursor(c);
                        listView.setAdapter(cursorAdapter);
                    }
                });
                ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                return false;
            }
        });
        //set up side navigation drawer
        drawerListView = findViewById(R.id.navDrawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        noteActivity = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        //add titles of three pages to side nav drawer
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

            //Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(noteActivity);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, AddActivity.class);
            startActivity(add_mem);

            return true;
        }

        // Activate the navigation drawer toggle
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {


        // Locate Position and take user to relevant page
        switch (position) {
            case 0:
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, ExamActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
        }
    }
}