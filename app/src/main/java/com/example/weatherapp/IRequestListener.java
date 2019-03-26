package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public interface IRequestListener {
    void onRequestCompleted(JSONObject result) throws JSONException;
}