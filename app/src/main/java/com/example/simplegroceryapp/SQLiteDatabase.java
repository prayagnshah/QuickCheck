package com.example.simplegroceryapp;

// We will use this file to create the database and define the database schema so that this helper can be used anywhere in the different activity.
// Database query, create database and everything should be done in this file.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// This public class requires two parameters - onCreate and onUpgrade
public class SQLiteDatabase extends SQLiteOpenHelper {

    // Assigning the name of database as a private constant variable because we do not want anyone to override this name
    // static means that memory has already been allocated to the variable
    private static final String DATABASE_NAME = "grocery_database";
    private static final int DATABASE_ID = 1;
    private static final String TABLE_NAME = "grocery_list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_IS_CHECKED = "is_checked";


    // We will only keep the context and then remove other parameters as we will hardcode them
    public SQLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_ID);
    }

    // After creating two parameters it also requires a constructor which means the context of our MainActivity
    // This constructor will be used to create the database and this will stay as it is and data will be added, removed, updated unless and until users do clear cache and data from their phone.

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {

        // We will create table now
        // CREATE TABLE IF NOT EXISTS grocery_list (id INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT, notes TEXT, is_checked INTEGER)
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM_NAME + " TEXT," + COLUMN_NOTES + " TEXT," + COLUMN_IS_CHECKED + " INTEGER" + ")");

    }

    // This method is used to upgrade the database when the version number is changed
    // Any changes in schema or structure of the database will be done here
    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Calling onCreate method to create the table again with new schema in future.
        onCreate(db);

    }

    // This method will be used to insert new grocery item
    public void insertItem(String itemName) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_NOTES, ""); // Default value for notes
        values.put(COLUMN_IS_CHECKED, 0); // Default value for is_checked

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // This method will be used to delete the grocery item
    public void deleteItem(int id) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }

    // This method will be used to retrieve all the grocery items
    public ArrayList<String> getAllItems(){
        ArrayList<String> items = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "SELECT * FROM " + TABLE_NAME;

//        Cursor cursor = db.rawQuery(sql, null);
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ITEM_NAME}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
                items.add(itemName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public void deleteItemByName(String name) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ITEM_NAME + "=?", new String[]{name});
        db.close();
    }

    // Method to update the notes for a specific item name
    public void updateNotes(String itemName, String notes) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTES, notes);
        db.update(TABLE_NAME, values, COLUMN_ITEM_NAME + "=?", new String[]{itemName});
        db.close();
    }

    // Method to get notes for a specific item name
    public String getNotes(String itemName) {
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NOTES}, COLUMN_ITEM_NAME + "=?", new String[]{itemName}, null, null, null);
        String notes = "";

        // This checks that if the query at least returns one result if not then notes will be empty
        // This is same for all the functions which I am doing below
        if (cursor.moveToFirst()) {
            notes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES));
        }

        // Always close this to free up resources and avoid data leaks
        cursor.close();
        db.close();
        return notes;
    }

    // Method to update checkbox state
    public void updateCheckboxState(String itemName, boolean isChecked) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_CHECKED, isChecked ? 1 : 0);
        db.update(TABLE_NAME, values, COLUMN_ITEM_NAME + "=?", new String[]{itemName});
        db.close();
    }

    // Method to get checkbox state
    public boolean getCheckboxState(String itemName) {
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_IS_CHECKED}, COLUMN_ITEM_NAME + "=?", new String[]{itemName}, null, null, null);
        boolean isChecked = false;
        if (cursor.moveToFirst()) {
            int isCheckedInt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CHECKED));
            isChecked = isCheckedInt == 1;
        }

        cursor.close();
        db.close();
        return isChecked;
    }
}
