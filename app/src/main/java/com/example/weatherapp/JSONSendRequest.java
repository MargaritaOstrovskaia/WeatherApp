package com.example.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONSendRequest extends AsyncTask <URL, Integer, JSONObject> {

    public String jsonString = null;
    private IRequestListener mRequestListener = null;

    public void setRequestListener(IRequestListener requestListener) {
        mRequestListener = requestListener;
    }

    public IRequestListener getRequestListener() {
        return mRequestListener;
    }

    @Override
    protected JSONObject doInBackground(URL... url) {
        try {
            URL locationURL = url[0];

            HttpURLConnection urlConnection = (HttpURLConnection)locationURL.openConnection();
            urlConnection.setRequestMethod("GET"); // Used to set the request method. Default is GET.
            urlConnection.setReadTimeout(10000); /* milliseconds */
            urlConnection.setConnectTimeout(15000); /* milliseconds */
            // You need to set it to true if you want to send (output) a request body, for example with POST or PUT requests.
            // With GET, you do not usually send a body, so you do not need it.
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            // Reading the response from the server:
            // after obtaining an InputStream, we use the BufferedReader to output the results from the server.
            BufferedReader br = new BufferedReader(new InputStreamReader(locationURL.openStream()));
            // StringBuilder represents a mutable sequence of characters
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                // appends the string representation of the X type argument to the sequence
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();
            System.out.println("JSON: " + jsonString);
            // JSONObject is an unordered collection of key and value pairs, resembling Java's native Map implementations
            JSONObject myResponse = new JSONObject(jsonString);
            return myResponse;

        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (mRequestListener != null) {
            try {
                mRequestListener.onRequestCompleted(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}