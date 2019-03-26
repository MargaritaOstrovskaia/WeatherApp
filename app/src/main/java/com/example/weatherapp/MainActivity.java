package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.net.URLEncoder;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    protected ListView listViewMain; // hold a reference to the ListView on the layout
    protected ArrayList<RowItemCity> arrayItemsMain; // store an array of row items
    protected AdapterOfDay adapterMain;
    protected Cities mCities;
    protected ProgressBar mSearchSpinner;
    Intent intentMain;
    Boolean prefTemperature;
    Boolean prefSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // whenever you call setContentView in onCreate, the activity's layout along with its subviews gets inflated (created) behind the scenes.
        setContentView(R.layout.activity_main);

        // findViewById just gives you a reference to a view than has already been created
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        // this Intent used with startActivity to launch an Second Activity
        intentMain = new Intent(this, SecondActivity.class);

        // getSharedPreferences() — Use this if you need multiple shared preference files identified by name, which you specify with the first parameter.
        SharedPreferences preference = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        prefTemperature = preference.getBoolean("temperature", true);
        prefSpeed = preference.getBoolean("speed", true);

        arrayItemsMain = new ArrayList<>();
        listViewMain = findViewById(R.id.main_listView);

        mSearchSpinner = findViewById(R.id.main_spinner);

        // fill array list after send request
        adapterMain = new AdapterOfDay(getApplicationContext(), arrayItemsMain);
        listViewMain.setAdapter(adapterMain);
        listViewMain.setOnItemClickListener(listClick);

        FloatingActionButton fab = findViewById(R.id.main_floatingActionButton);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent();
                searchIntent.setClass(MainActivity.this, SearchCityActivity.class);
                startActivity(searchIntent);
            }
        });

        mCities = Cities.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayItemsMain.clear();
        if(mCities.getCities().size() > 0)
            showSpinner(true);

        for (City city : mCities.getCities())
            GetWeatherByWoeid(city.getWoeid().toString());
    }

    // when creating Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    // when selected item on Menu from Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_page:
                // this Intent used with startActivity to launch an Settings Activity
                // key-value pairs that should be delivered to the component handling the intent
                Intent favIntent = new Intent(this, SettingsActivity.class);

                // extras can be set using the putExtras() method.
                favIntent.putExtra("prefTemperature", prefTemperature);
                favIntent.putExtra("prefSpeed", prefSpeed);
                startActivity(favIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // when send request
    private void GetWeatherByWoeid(final String s) {
        try {
            String query = URLEncoder.encode(s, "utf-8");
            URL url = new URL("https://www.metaweather.com/api/location/" + query);

            JSONSendRequest slTask = new JSONSendRequest();
            slTask.setRequestListener(new IRequestListener() {
                @Override
                public void onRequestCompleted(JSONObject result) throws JSONException {
                    if (isFinishing()) {
                        return;
                    }

                    JSONArray consolidated = result.getJSONArray("consolidated_weather");
                    JSONObject jo = (JSONObject)consolidated.get(0);

                    RowItemCity row_one = new RowItemCity( );

                    row_one.setCityId(result.getInt("woeid"));
                    row_one.setCityName(result.getString("title"));
                    //row_one.setWeatherStateName(jo.getString("weather_state_name"));

                    switch (jo.getString("weather_state_abbr")) {
                        case "sn":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_sn));
                            break;
                        case "sl":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_sl));
                            break;
                        case "t":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_t));
                            break;
                        case "hr":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_hr));
                            break;
                        case "lr":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_lr));
                            break;
                        case "s":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_s));
                            break;
                        case "hc":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_hc));
                            break;
                        case "lc":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_lc));
                            break;
                        case "c":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_c));
                            break;
                        case "h":
                            row_one.setWeatherStateName(getApplicationContext().getString(R.string.state_h));
                            break;
                    }

                    if(prefTemperature)
                        row_one.setWeatherTemperature(jo.getInt("the_temp"));
                    else
                        row_one.setWeatherTemperature((jo.getInt("the_temp") * 9/5) + 32);

                    arrayItemsMain.add(row_one);

                    adapterMain.notifyDataSetChanged();

                    if (mCities.getCities().get(mCities.getCities().size() - 1).getWoeid().toString().equals(s)) {
                        showSpinner(false);
                    }
                }
            });
            slTask.execute(url);
        } catch (Exception e) {
            Log.d("logM", "MainActivity (GetWeatherByWoeid). Exception: " + e.toString());
        }
    }

    // when click on Item from ListView
    private AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener () {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // to get which item was selected, there is a method of the ListView called getItemAtPosition
            RowItemCity itemValue = (RowItemCity) listViewMain.getItemAtPosition(position);

            // we're using putExtra to send information to the new Activity
            intentMain.putExtra("CITY_SELECTED", itemValue.getCityId());
            intentMain.putExtra("prefTemperature", prefTemperature);
            intentMain.putExtra("prefSpeed", prefSpeed);
            startActivity(intentMain);
        }
    };

    private void showSpinner(boolean isShow) {
        if (isShow) {
            mSearchSpinner.setVisibility(View.VISIBLE);
            listViewMain.setVisibility(View.INVISIBLE);
        } else {
            mSearchSpinner.setVisibility(View.GONE);
            listViewMain.setVisibility(View.VISIBLE);
        }
    }
}
