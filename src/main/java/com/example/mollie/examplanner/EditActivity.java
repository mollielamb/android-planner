package com.example.mollie.examplanner;

//created by Mollie on 02/04/2018

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity implements OnClickListener {

    //declare
    private EditText titleText;
    private Button update, cancel;
    private EditText contentText;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Edit Note");
        //display the correct layout
        setContentView(R.layout.activity_edit);

        //set up connectiion to the database
        dbManager = new DBManager(this);
        dbManager.open();
        //values from layout
        titleText = findViewById(R.id.title);
        contentText =  findViewById(R.id.content);

        update = findViewById(R.id.updateNote);
        cancel = findViewById(R.id.cancelEdit);

        //set up getting and setting values to update
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("desc");

        _id = Long.parseLong(id);

        titleText.setText(title);
        contentText.setText(content);
        //setting onclicklisteners for the two buttons
        update.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //when update button is pressed get values of title and content and update their current values in the database using its unchangeable id
            case R.id.updateNote:
                String title = titleText.getText().toString();
                String content = contentText.getText().toString();

                dbManager.update(_id, title, content);
                this.returnHome();
                break;
            //when cancel is pressed break and go back to main page
            case R.id.cancelEdit:
                this.returnHome();
                break;
        }
    }
    //called when buttons are pressed, returns to main notes page
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
