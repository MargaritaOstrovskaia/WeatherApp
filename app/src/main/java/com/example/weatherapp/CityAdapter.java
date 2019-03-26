package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<City> {

    public CityAdapter(Context context, ArrayList<City> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);

        if (convertView == null) {
            // LayoutInflater is used to create a new View object from xml layouts (row_search)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_search, parent, false);
        }

        // find and fill text views
        TextView titleTextView = convertView.findViewById(R.id.textViewRowSearch);
        titleTextView.setText(city.getTitle());

        return convertView;
    }
}
