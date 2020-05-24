package com.example.myapplication.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.myapplication.view.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.net.URL;
import java.util.ArrayList;

public class General_InformationClass {
    //singleton design pattern için
    public static final General_InformationClass instance = new General_InformationClass();
    private General_InformationClass(){};
    private LatLng latLng;
    private TextView coronaData;
    private TextView weatherData;
    private Bitmap photoOfPlaces;
    private ImageView imageView;
    private String ulke = "Türkiye";
    private String sehir = "İstanbul";
    private String ilce = "Emniyettepe";
    private Button photoButton;
    private ArrayList<String> imgURLarray = new ArrayList<String>();
    private PopupMenu photoPopup;

    public PopupMenu getPhotoPopup() {
        return photoPopup;
    }

    public void setPhotoPopup(PopupMenu photoPopup) {
        this.photoPopup = photoPopup;
    }

    public ArrayList getImgURLarray() { return imgURLarray; }

    public void setImgURLarray(ArrayList imgURLarray) { this.imgURLarray = imgURLarray; }

    public void setPhotoButton(Button photoButton) { this.photoButton = photoButton; }

    public Button getPhotoButton() { return photoButton; }

    public LatLng getLatLng() { return latLng; }

    public void setLatLng(LatLng latLng) { this.latLng = latLng; }

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

    public ImageView getImageView() { return imageView; }

    public void setImageView(ImageView imageView) { this.imageView = imageView; }

    public TextView getCoronaData() {
        return coronaData;
    }

    public void setCoronaData(TextView coronaData) {
        this.coronaData = coronaData;
    }

    public TextView getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(TextView weatherData) {
        this.weatherData = weatherData;
    }

    public Bitmap getPhotoOfPlaces() {
        return photoOfPlaces;
    }

    public void setPhotoOfPlaces(Bitmap photoOfPlaces) {
        this.photoOfPlaces = photoOfPlaces;
    }


}
