package com.example.weatherapp;

public class RowItemDay {
    public String day;
    public String image;
    public String temperature;

    public void setDay(String data ) {
        this.day = data;
    }
    public String getDay() {
        return this.day;
    }

    public void setImage( String image ) {
        this.image = image;
    }
    public String getImage() {
        return this.image;
    }

    public void setTemperature( String temp ) {
        this.temperature = temp;
    }
    public String getTemperature() {
        return this.temperature;
    }
}
