package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class CoronaModel {
    @SerializedName("country")
    public String country;
    @SerializedName("total_cases")
    public  String total_cases;
    @SerializedName("total_deaths")
    public String total_deaths;
    @SerializedName("total_recovered")
    public String total_recovered;
    @SerializedName("active_cases")
    public String active_cases;



}
