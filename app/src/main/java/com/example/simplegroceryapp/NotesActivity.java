package com.example.simplegroceryapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Extending AppCompatActivity to create a new activity and it is required in order to get the Android component.
public class NotesActivity extends AppCompatActivity {

    // We will create a companion object because item_name is static and we do not want to mess up anywhere
    public static final String ITEM_NAME = "item_name";
    private SQLiteDatabase dbHelper;
    private EditText editText;
    private String currentItemName;


    public static void openNotesActivity(Context context, String s) {

        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra(ITEM_NAME, s); // Pass the item name to the NotesActivity
        context.startActivity(intent); // Start the NotesActivity

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity); // Load the XML layout for the NotesActivity

        // Creating a database helper object
        dbHelper = new SQLiteDatabase(this);

        // Get the item name passed from the ListViewAdapter
        // As we have pass string so we will get the string from the intent
//        String s = getIntent().getStringExtra(ITEM_NAME);
        currentItemName = getIntent().getStringExtra(ITEM_NAME);
        // Adding this toast message to show the notes activity is opened
        Toast.makeText(this, "Notes for " + currentItemName, Toast.LENGTH_SHORT).show();

        // Initializing ids when NotesActivity page is opened
        EditText editText = findViewById(R.id.textView);
        Button save_button = findViewById(R.id.save_button);
        Button back_button = findViewById(R.id.back_button);

        // Load the existing notes for the item if any
        String existingNotes = dbHelper.getNotes(currentItemName);
        if (existingNotes != null && !existingNotes.isEmpty()) {
            editText.setText(existingNotes);
        }

        // Once clicked on save button should save the notes entered by the user
        save_button.setOnClickListener(v -> {
        String notes = editText.getText().toString();
        dbHelper.updateNotes(currentItemName, notes);
        Toast.makeText(this, "Notes saved for " + currentItemName, Toast.LENGTH_SHORT).show();
        });



   // Setting the editText to the item name passed from the ListViewAdapter
//        editText.setText("Enter notes for the item" , TextView.BufferType.valueOf(s));

        // Once clicked on back button should bring it back to the main activity
        back_button.setOnClickListener(v -> {
            finish(); // Close the NotesActivity and return to the previous activity
        });

    }

    // this is done to proper cleaning up the database when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
