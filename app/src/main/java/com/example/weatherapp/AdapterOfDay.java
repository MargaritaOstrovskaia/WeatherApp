package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterOfDay extends BaseAdapter {

    private ArrayList<RowItemCity> singleRow;

    private LayoutInflater thisInflater;

    public AdapterOfDay(Context context, ArrayList<RowItemCity> aRow) {
        this.singleRow = aRow;
        thisInflater = ( LayoutInflater.from(context) );
    }

    @Override
    public int getCount() {
        return singleRow.size();
    }

    @Override
    public Object getItem(int position) {
        return singleRow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // LayoutInflater is used to create a new View object from xml layouts (row_main)
            convertView = thisInflater.inflate(R.layout.row_main, parent, false);
        }

        // find text views
        TextView textViewCity = convertView.findViewById (R.id.textViewCityRowMain);
        TextView textViewTemperature = convertView.findViewById (R.id.textViewTemperatureRowMain);
        TextView textViewState = convertView.findViewById (R.id.textViewStateRowMain);

        RowItemCity currentRow = (RowItemCity)getItem(position);

        // fill text views
        textViewCity.setText(currentRow.getCityName());
        textViewTemperature.setText(currentRow.getWeatherTemperature().toString() + "°");
        textViewState.setText(currentRow.getWeatherStateName());

        return convertView;
    }
}
