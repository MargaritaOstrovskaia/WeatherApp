package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        Intent caller = getIntent();

        // extras can be read using the getExtras() method
        boolean prefTemperature = caller.getBooleanExtra("prefTemperature", true);
        boolean prefSpeed = caller.getBooleanExtra("prefSpeed", true);

        SharedPreferences preference = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preference.edit();

        Switch t = findViewById(R.id.switchTemperature);
        t.setChecked(prefTemperature);

        Switch s = findViewById(R.id.switchSpeed);
        s.setChecked(prefSpeed);

        ActionBar myActionBar = getSupportActionBar();
        if ( myActionBar != null ) {
            myActionBar.setDisplayHomeAsUpEnabled(true);
        }

        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("temperature", isChecked);
                editor.commit();
            }
        });

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("speed", isChecked);
                editor.commit();
            }
        });
    }
}
