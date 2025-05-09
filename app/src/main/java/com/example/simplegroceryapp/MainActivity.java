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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    // Load XML screen layout
//Always you will call ID which is mentioned in the activity_main.xml file
        listView = findViewById(R.id.list);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);
        context = this;

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

//        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
//        adapter = new ListViewAdapter(getApplicationContext(), items);
//        listView.setAdapter(adapter);

//        loadContent();

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
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");

//        byte[] content = new byte[(int) readFrom.length()];

//        FileInputStream stream = null;

        if (!readFrom.exists()){
            return;    // If file doesn't exist then return nothing.
        }
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            // reading JSON
            byte[] content = new byte[(int) readFrom.length()];
            stream.read(content);
            stream.close();

            String s = new String(content);
            // [Apple, Banana, Kiwi, Strawberry]

            JSONArray jsonArray = new JSONArray(s);

            items = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(jsonArray.getString(i));
            }
//            s = s.substring(1, s.length() - 1);
//            String[] split = s.split(", ");

            adapter = new ListViewAdapter(this, items);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    We want to store the item when the app is closed and then the data is there when we re run the app. OnDestroy is used for this

//    @Override
//    protected void onDestroy() {
//        File path = getApplicationContext().getFilesDir();
//        try {
//            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
//
//            //Saving the items as the JSON array
//            JSONArray jsonArray = new JSONArray(items);
//            // Items will be saved like this: [Apple, Banana, Kiwi, Oranges]
//            writer.write(jsonArray.toString().getBytes());
//            writer.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        super.onDestroy();
//    }

    // onDestroy is not the best way to store the data and instead I am using saveItems function and storing in JSON for maintainability
    // In this way we can easily pass context and this will save the items right away instead of waiting the app to close properly or not.
    public static void saveItems(Context context) {
//        File path = getApplicationContext().getFilesDir();
        File path = context.getFilesDir();
        File file = new File(path, "list.txt");
        try (FileOutputStream writer = new FileOutputStream(file)) {
            // Convert the list to a JSON string
            JSONArray jsonArray = new JSONArray(items);
            writer.write(jsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    We need to add the items to the listView
//    Just having public void then we will not be able to call them in another java class instead we need add public static void and then we can use them in ListViewAdapter java class
//    We also need to make the variable statc like items, listView
// These are also known as helper functions add, remove and makeToast
    public static void addItem(String item){
        items.add(item);
        // This should be added to our list view
//        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        saveItems(listView.getContext());
//        ((MainActivity)listView.getContext()).saveItems();

        Log.d("TAG", "addItem: " + item);
        Log.d("TAG", "addItem: " + items.toString());


//        Another way to add the data to our list view is by this command
//        adapter.notifyDataSetChanged();
    }

//    we will create the function to remove the item
    public static void removeItem(int remove){
        items.remove(remove);
//        adapter = new ListViewAdapter(context, items);
        adapter.notifyDataSetChanged();
        saveItems(listView.getContext());

//        ((MainActivity)listView.getContext()).saveItems();
//        saveItems(listView.getContext());
    }

    static Toast t;
    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context,s,Toast.LENGTH_SHORT);
        t.show();

    }
}
