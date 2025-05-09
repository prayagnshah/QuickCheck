package com.example.simplegroceryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// This is the custom layout for the list view
// ArrayAdapter shows the list of strings in a list view with a custom layout

public class ListViewAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;

    // context is the abstract class that provides access to system-level resources like it will handle global level information about the app's environment.
    Context context;

    public ListViewAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.list_row, items);
        this.context = context;
        this.list = items;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // This is called everytime when a row appears on the screen

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, parent, false);

            // Creating a helper object to keep references to the row's views like text and image buttons
            holder = new ViewHolder();
            holder.number = convertView.findViewById(R.id.number);
            holder.name = convertView.findViewById(R.id.name);
            holder.copy = convertView.findViewById(R.id.copy);
            holder.remove = convertView.findViewById(R.id.remove);
            holder.checkBox = convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Always update these views, regardless of whether convertView was null
        holder.number.setText((position + 1) + ".");
        holder.name.setText(list.get(position));

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addItem(list.get(position));
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.removeItem(position);
            }
        });

        return convertView;
    }

    // This is the helper class which will avoid views repeatedly for better performance.
    static class ViewHolder {
        TextView number;
        TextView name;
        CheckBox checkBox;
        ImageView copy;
        ImageView remove;
    }
}
