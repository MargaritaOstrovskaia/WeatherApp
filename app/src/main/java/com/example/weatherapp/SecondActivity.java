package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    Boolean prefTemperature;
    Boolean prefSpeed;
    ViewPager viewPager;
    protected ListView listViewTab2;
    protected ArrayList<RowItemDay> arrayOfDaysSecond;
    protected AdapterOfWeek adapterSecond;
    protected ProgressBar sSearchSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        arrayOfDaysSecond = new ArrayList<>();
        adapterSecond = new AdapterOfWeek(getApplicationContext(), arrayOfDaysSecond);
        sSearchSpinner = findViewById(R.id.second_spinner);

        Intent secondIntent = getIntent();
        int message = secondIntent.getIntExtra("CITY_SELECTED", 0);
        prefTemperature = secondIntent.getBooleanExtra("prefTemperature", true);
        prefSpeed = secondIntent.getBooleanExtra("prefSpeed", true);

        Toolbar toolbar = findViewById(R.id.myToolbarSecond);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.week));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        final AdapterOfPager adapter = new AdapterOfPager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        ActionBar myActionBar = getSupportActionBar();
        if ( myActionBar != null ) {
            myActionBar.setDisplayHomeAsUpEnabled(true);
        }


        showSpinner(true);
        SearchTextChanged(String.valueOf(message));

        Log.d("logF", "SecondActivity (onCreate)");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_city, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    // when selected item on Menu from Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_city:
                Cities.getInstance(this).RemoveCity(new Integer(getIntent().getIntExtra("CITY_SELECTED", 0)));
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SearchTextChanged(String s) {
        try {
            String query = URLEncoder.encode(s, "utf-8");
            URL url = new URL("https://www.metaweather.com/api/location/" + query);
            JSONSendRequest slTask = new JSONSendRequest();
            slTask.setRequestListener(new IRequestListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onRequestCompleted(JSONObject result) throws JSONException {
                    if (isFinishing()) {
                        return;
                    }

                    JSONArray consolidated = result.getJSONArray("consolidated_weather");
                    JSONObject jo = (JSONObject)consolidated.get(0);

                    int currentTemp = jo.getInt("the_temp");
                    int minTemp = jo.getInt("min_temp");
                    int maxTemp = jo.getInt("max_temp");
                    if(!prefTemperature) {
                        currentTemp = (currentTemp * 9 / 5) + 32;
                        minTemp = (minTemp * 9 / 5) + 32;
                        maxTemp = (maxTemp * 9 / 5) + 32;
                    }

                    // tab 1
                    TextView textCityName = findViewById(R.id.textView_cityName);
                    textCityName.setText(result.getString("title"));

                    TextView textState = findViewById(R.id.textView_state);
                    //textState.setText(jo.getString("weather_state_name"));

                    switch (jo.getString("weather_state_abbr")) {
                        case "sn":
                            textState.setText(getApplicationContext().getString(R.string.state_sn));
                            break;
                        case "sl":
                            textState.setText(getApplicationContext().getString(R.string.state_sl));
                            break;
                        case "t":
                            textState.setText(getApplicationContext().getString(R.string.state_t));
                            break;
                        case "hr":
                            textState.setText(getApplicationContext().getString(R.string.state_hr));
                            break;
                        case "lr":
                            textState.setText(getApplicationContext().getString(R.string.state_lr));
                            break;
                        case "s":
                            textState.setText(getApplicationContext().getString(R.string.state_s));
                            break;
                        case "hc":
                            textState.setText(getApplicationContext().getString(R.string.state_hc));
                            break;
                        case "lc":
                            textState.setText(getApplicationContext().getString(R.string.state_lc));
                            break;
                        case "c":
                            textState.setText(getApplicationContext().getString(R.string.state_c));
                            break;
                        case "h":
                            textState.setText(getApplicationContext().getString(R.string.state_h));
                            break;
                    }

                    TextView textTemperature = findViewById(R.id.textView_temperature);
                    String t = String.format("%d°", currentTemp);
                    textTemperature.setText(t);

                    TextView textMinTemperature = findViewById(R.id.textView_min);
                    String minT = String.format(getApplicationContext().getString(R.string.min) + "%d°", minTemp);
                    textMinTemperature.setText(minT);

                    TextView textMaxTemperature = findViewById(R.id.textView_max);
                    String maxT = String.format(getApplicationContext().getString(R.string.max) + "%d°", maxTemp);
                    textMaxTemperature.setText(maxT);

                    TextView textWindSpeed = findViewById(R.id.textView_windSpeed);
                    TextView textVisibility = findViewById(R.id.textView_visibility);
                    if(prefSpeed) {
                        int windSpeed = jo.getInt("wind_speed");
                        String ws = String.format("%d " + getApplicationContext().getString(R.string.speed_mph), windSpeed);
                        textWindSpeed.setText(ws);

                        int v = jo.getInt("visibility");
                        String vis = String.format("%d " + getApplicationContext().getString(R.string.miles), v);
                        textVisibility.setText(vis);
                    }
                    else {
                        int windSpeed = (int) (jo.getInt("wind_speed") * 1.609);
                        String ws = String.format("%d " + getApplicationContext().getString(R.string.speed_kph), windSpeed);
                        textWindSpeed.setText(ws);

                        int v = (int) (jo.getInt("visibility") * 1.609);
                        String vis = String.format("%d " + getApplicationContext().getString(R.string.kilometers), v);
                        textVisibility.setText(vis);
                    }

                    TextView textPressure = findViewById(R.id.textView_airPressure);
                    String p = String.format("%d " + getApplicationContext().getString(R.string.pressure),
                            jo.getInt("air_pressure")).toLowerCase(Locale.getDefault());
                    textPressure.setText(p);

                    TextView textHumidity= findViewById(R.id.textView_humidity);
                    textHumidity.setText(String.valueOf(jo.getInt("humidity")) + " %");

                    // tab 2
                    TextView textCityName2 = findViewById(R.id.tab2CityName);
                    textCityName2.setText(result.getString("title"));

                    for (int i = 0; i < consolidated.length(); i++) {
                        JSONObject obj = (JSONObject)consolidated.get(i);
                        RowItemDay row_one = new RowItemDay();

                        row_one.setDay(obj.getString("applicable_date"));
                        row_one.setImage(obj.getString("weather_state_abbr"));
                        String temp = String.valueOf(minTemp) + "°    " + String.valueOf(maxTemp) + "°";

                        row_one.setTemperature(temp);
                        arrayOfDaysSecond.add(row_one);
                    }

                    listViewTab2 = findViewById(R.id.tab2ListView);
                    listViewTab2.setAdapter(adapterSecond);

                    adapterSecond.notifyDataSetChanged();
                    showSpinner(false);
                }
            });
            slTask.execute(url);
        } catch (Exception e) {
            Log.d("logF", "SecondActivity (SearchTextChanged). Exception: " + e.toString());
        }
    }

    private void showSpinner(boolean isShow) {
        if (isShow) {
            sSearchSpinner.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
        } else {
            sSearchSpinner.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
    }
}
