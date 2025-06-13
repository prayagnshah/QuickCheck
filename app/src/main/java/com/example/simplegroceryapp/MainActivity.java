package com.example.simplegroceryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase dbHelper;

    @SuppressLint("StaticFieldLeak")
    static ListView listView;   // This is used to display the list of items
//    Adding array list of items
    static ArrayList<String> items; //    This is used to store the items in the list
    @SuppressLint("StaticFieldLeak")
    static Context context;

//    We need to create the adapter so the list view can display it
//    Now we have created custom view layout so we will use listviewadapter name
//    static ArrayAdapter<String> adapter;
    @SuppressLint("StaticFieldLeak")
    static ListViewAdapter adapter;   // This is used to properly display the items inside ListView


//    We will call the EditView from the activity_main.xml file to set the logic
    EditText input;  // a text field to enter the item
    ImageView enter;  // image of check mark to add the item


    // Adding this override method to show the settings menu of the app on the top right corner of the app
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // In future, if I want to include the functionality that if anything is clicked and it will take it to the next page then below option will be required.
    // I am just commenting it out for now and not deleting it.
//    @Override
//    public boolean onOptionsItemSelected(android.view.MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.version_number) {
//            makeToast("Settings clicked");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    // Load XML screen layout
//Always you will call ID which is mentioned in the activity_main.xml file
        listView = findViewById(R.id.list);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);
        context = this;
        dbHelper = new SQLiteDatabase(this);

        items = new ArrayList<>();
        items.add("Apple");
        items.add("Banana");
        items.add("Kiwi");
        items.add("Oranges");

        listView.setLongClickable(true);
        adapter = new ListViewAdapter(this, items);   // custom way to display the items
        listView.setAdapter(adapter);  // Attach the data to this list so it shows


        // popup comes up when someone clicks on the item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                This is done to store our items in position variable so that our items can be clicked
                String clickedItem = (String) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, clickedItem, Toast.LENGTH_SHORT).show();
//                String name = items.get(i);


//                Toast displaying the items once they are clicked and this is used a lot so I am mentioning outside the function so that we can use anywhere
//                makeToast(name);
            }
        });

//        We will create a onclick listener so on the long press item is deleted

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast("Removed " + items.get(i));
                removeItem(i);
                return true; // Return true to indicate that the long click was handled

            }
        });

//      This onclick listener means that when user clicks on text field then it just add the values
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString().trim();
//                Need to check that text is valid or not
                if(text.isEmpty()){
                    makeToast("Enter valid item");
                }else {
                    addItem(text);
                    input.setText("");  // Clear the text after adding
                    makeToast("Added " + text);
                }


            }
        });

        loadContent();

    }

//    Now we want to read the content as soon as the app is loaded
// So this will load the previous items when the app is opened
    public void loadContent() {

            items = dbHelper.getAllItems();

            adapter = new ListViewAdapter(this, items);
            listView.setAdapter(adapter);
    }


//    Just having public void then we will not be able to call them in another java class instead we need add public static void and then we can use them in ListViewAdapter java class
//    We also need to make the variable statc like items, listView
// These are also known as helper functions add, remove and makeToast
    public static void addItem(String item){
        items.add(item);
        adapter.notifyDataSetChanged();
        SQLiteDatabase db = new SQLiteDatabase(context);
        db.insertItem(item);


        Log.d("TAG", "addItem: " + item);
        Log.d("TAG", "addItem: " + items.toString());


//        Another way to add the data to our list view is by this command
//        adapter.notifyDataSetChanged();
    }

//    we will create the function to remove the item
    public static void removeItem(int index){
//        items.remove(remove);
//        adapter = new ListViewAdapter(context, items);
        String itemName = items.get(index);
        SQLiteDatabase db = new SQLiteDatabase(context);
        db.deleteItemByName(itemName);
        items.remove(index);
        adapter.notifyDataSetChanged();
    }

    static Toast t;
    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context,s,Toast.LENGTH_SHORT);
        t.show();

    }

}
