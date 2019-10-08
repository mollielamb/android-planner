package com.example.mollie.examplanner;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener {
    //declare

    private Button addBtn;
    private EditText titleEditText;
    private EditText contentEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Note");
        //get layout
        setContentView(R.layout.activity_add_note);
        //items by layout id
        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);

        addBtn = findViewById(R.id.addNote);
        //database managed needed to add items to database
        dbManager = new DBManager(this);
        dbManager.open();
        addBtn.setOnClickListener(this);

    }

    @Override
    //call when addNote button is clicked
    public void onClick(View v) {
        //call validate to make sure both fields are filled in
        if (validate()) {
            switch (v.getId()) {
                case R.id.addNote:
                    //setting value of variables to the text written in boxes
                    final String title = titleEditText.getText().toString();
                    final String content = contentEditText.getText().toString();
                    //insert function from database manager, passing through title and content data
                    dbManager.insert(title, content);
                    //going back to notes (main) activity
                    Intent main = new Intent(AddActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);
                    break;
            }
        }
    }


    public boolean validate() {
        boolean valid = true;

        //Get values from EditText fields
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        //do if there is no text in title edittext
        if (title.isEmpty()) {
            valid = false;
            titleEditText.setError("Please enter the event title");
        }
        //do if there is no text in content edittext
        if (content.isEmpty()) {
            valid = false;
            contentEditText.setError("Please enter the event description");
        }
        return valid;
    }


}