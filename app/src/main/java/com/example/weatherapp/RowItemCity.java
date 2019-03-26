package com.example.weatherapp;

public class RowItemCity {
    private Integer cityId;
    private String cityName;
    private String weatherStateName;
    private Integer weatherTemperature;

    public void setCityId(Integer temp ) {
        this.cityId = temp;
    }
    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityName( String name ) {
        this.cityName = name;
    }
    public String getCityName() {
        return this.cityName;
    }

    public void setWeatherStateName( String name ) {
        this.weatherStateName = name;
    }
    public String getWeatherStateName() {
        return this.weatherStateName;
    }

    public void setWeatherTemperature(Integer temp ) {
        this.weatherTemperature = temp;
    }
    public Integer getWeatherTemperature() {
        return this.weatherTemperature;
    }

}
