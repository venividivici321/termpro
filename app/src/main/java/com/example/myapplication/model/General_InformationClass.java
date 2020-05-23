package com.example.myapplication.model;

import android.graphics.Bitmap;

public class General_InformationClass {
    //singleton design pattern için
    private static General_InformationClass instance;

    String coronaData;
    String weatherData;
    Bitmap photoOfPlaces;
    String latitudeOfPlace;
    String longitudeOfPlace;
    String ulke;
    String sehir;
    String ilce;

    public String getUlke() {
        return ulke;
    }

    public void setUlke(String ulke) {
        this.ulke = ulke;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getIlce() {
        return ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public String getLatitudeOfPlace() {
        return latitudeOfPlace;
    }

    public void setLatitudeOfPlace(String latitudeOfPlace) {
        this.latitudeOfPlace = latitudeOfPlace;
    }

    public String getLongitudeOfPlace() {
        return longitudeOfPlace;
    }

    public void setLongitudeOfPlace(String longitudeOfPlace) {
        this.longitudeOfPlace = longitudeOfPlace;
    }


    //constructor
    public  General_InformationClass(){

    }

    public String getCoronaData() {
        return coronaData;
    }

    public void setCoronaData(String coronaData) {
        this.coronaData = coronaData;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public Bitmap getPhotoOfPlaces() {
        return photoOfPlaces;
    }

    public void setPhotoOfPlaces(Bitmap photoOfPlaces) {
        this.photoOfPlaces = photoOfPlaces;
    }


    public static General_InformationClass getInstance(){
        if(instance==null){
            instance=new General_InformationClass();
        }
        return instance;
    }


}
