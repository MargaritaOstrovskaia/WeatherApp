package com.example.weatherapp;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Cities {
    // "transient" - means "I do not want to seialize this property"
    private transient static Cities single_instance = null;

    private transient static final String fileName = "cities.dat";
    private transient Context mContext;

    protected ArrayList<City> mCities;

    private Cities(Context context)
    {
        mContext = context;

        if (mCities == null) {
            mCities = new ArrayList<>();
        }
    }

    public static Cities getInstance(Context context)
    {
        if (single_instance == null) {
            single_instance = new Cities(context);
            single_instance.Load();
        }

        return single_instance;
    }

    public ArrayList<City> getCities() {
        return mCities;
    }

    public void AddCity(City city) {
        Boolean isExist = false;

        for (City c : mCities)
        {
            Integer w1 = c.getWoeid();
            Integer w2 = city.getWoeid();

            if (w1.equals(w2)) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            mCities.add(city);
            Save();
        }
    }

    public void RemoveCity(City city) {
        int index = -1;

        for (int i = 0; i < mCities.size(); i++) {
            City c = mCities.get(i);

            Integer w1 = c.getWoeid();
            Integer w2 = city.getWoeid();

            if (w1.equals(w2)) {
                index = i;
                break;
            }
        }

        if (index >= 0 && index < mCities.size()) {
            mCities.remove(index);
            Save();
        }
    }

    public void RemoveCity(Integer woeid) {
        int index = -1;

        for (int i = 0; i < mCities.size(); i++) {
            City c = mCities.get(i);
            if (c.getWoeid().equals(woeid)) {
                index = i;
                break;
            }
        }

        if (index >= 0 && index < mCities.size()) {
            mCities.remove(index);
            Save();
        }
    }

    public void Save() {
        try {
            FileOutputStream fos = mContext.openFileOutput(Cities.fileName, mContext.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(mCities);
            os.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    public void Load() {
        try {
            FileInputStream fis = mContext.openFileInput(Cities.fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            mCities = (ArrayList<City>)is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
    }
}
