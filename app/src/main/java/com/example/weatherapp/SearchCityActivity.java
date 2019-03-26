package com.example.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchCityActivity extends AppCompatActivity {

    protected SearchView mSearchView;
    protected ListView mListView;
    protected ArrayList<City> mCities;
    protected CityAdapter mCitiesAdapter;
    protected Cities mAppCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        Toolbar myToolbar = findViewById(R.id.search_Toolbar);
        setSupportActionBar(myToolbar);

        mAppCities = Cities.getInstance(this);

        mSearchView = findViewById(R.id.searchView); //mSearchView.setQueryHint(getApplicationContext().getString(R.string.enterCity));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                SearchTextChanged(s);
                return false;
            }
        });

        mCities = new ArrayList<>();
        mCitiesAdapter = new CityAdapter(this, mCities);

        mListView = findViewById(R.id.listView);
        mListView.setAdapter(mCitiesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = mCities.get(i);
                mAppCities.AddCity(city);
                onBackPressed();
            }
        });

        ActionBar myActionBar = getSupportActionBar();
        if ( myActionBar != null ) {
            myActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void SearchTextChanged(String s) {
        try {
            String query = URLEncoder.encode(s, "utf-8");
            URL url = new URL("https://www.metaweather.com/api/location/search/?query=" + query);
            SearchLocationTask slTask = new SearchLocationTask();
            slTask.execute(url);
        } catch (Exception e) {
            Log.d("logS", "SearchActivity. Exception:" + e.toString());
        }
    }

    private class SearchLocationTask extends AsyncTask<URL, Integer, JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... url) {
            JSONArray result = null;

            try {
                URL locationURL = url[0];

                HttpURLConnection urlConnection = (HttpURLConnection)locationURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000); /* milliseconds */
                urlConnection.setConnectTimeout(15000); /* milliseconds */
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(locationURL.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                String jsonString = sb.toString(); //System.out.println("JSON: " + jsonString);
                result = new JSONArray(jsonString);
            } catch (Exception e) {
                Log.d("logS", "SearchActivity (SearchLocationTask). Exception: " + e.toString());
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            if (isFinishing()) {
                return;
            }

            mCities.clear();

            try {
                if (result != null) {
                    for (int i = 0; i < result.length(); i++) {
                        if (result.get(i) instanceof JSONObject) {
                            JSONObject cityJSON = (JSONObject)result.get(i);
                            String title = cityJSON.getString("title");
                            String location_type = cityJSON.getString("location_type");
                            Integer woeid = cityJSON.getInt("woeid");
                            String latt_long = cityJSON.getString("latt_long");
                            City city = new City(title, location_type, woeid, latt_long);
                            mCities.add(city);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("logS", "SearchActivity (onPostExecute). Exception: " + e.toString());
            }

            mCitiesAdapter.notifyDataSetChanged();
        }
    }
}
