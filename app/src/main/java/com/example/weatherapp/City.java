package com.example.weatherapp;

import java.io.Serializable;

public class City implements Serializable {
    private final String mTitle;
    private final String mLocationType;
    private final Integer mWoeid;
    private final String mLattLong;

    public City(String title, String locationType, Integer woeid, String lattLong) {
        mTitle = title;
        mLocationType = locationType;
        mWoeid = woeid;
        mLattLong = lattLong;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLocationType() {
        return mLocationType;
    }

    public Integer getWoeid() {
        return mWoeid;
    }

    public String getLattLong() {
        return mLattLong;
    }
}
