package com.example.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class AdapterOfWeek extends ArrayAdapter<RowItemDay> {
    public AdapterOfWeek(Context context, ArrayList<RowItemDay> days) {
        super(context, 0, days);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowItemDay day = getItem(position);

        if (convertView == null) {
            // LayoutInflater is used to create a new View object from xml layouts (row_tab2)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_tab2, parent, false);
        }

        // find text views
        TextView textViewDate = convertView.findViewById(R.id.textDateRowTab2);
        ImageView imageViewImage = convertView.findViewById(R.id.imageRowTab2);
        TextView textViewTemp = convertView.findViewById(R.id.textTempRowTab2);

        RowItemDay currentRow = getItem(position);

        // fill text views
        textViewDate.setText(currentRow.getDay());
        new DownloadImageTask(imageViewImage).execute("https:/www.metaweather.com/static/img/weather/png/64/" + currentRow.getImage() + ".png");
        textViewTemp.setText(currentRow.getTemperature());

        // return the completed view to render on screen
        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            // Android provides Bitmap class to handle images
            Bitmap mIcon11 = null;
            try {
                // read data
                InputStream in = new java.net.URL(urldisplay).openStream();
                // decode image using BitmapFactory
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
