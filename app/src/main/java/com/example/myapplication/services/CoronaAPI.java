package com.example.myapplication.services;

import com.example.myapplication.model.CoronaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoronaAPI {
    //GET POST DELETE UPDATE GIBI ISLEMLER ICIN
    //"https://corona-virus-stats.herokuapp.com/api/v1/cases/countries-search"
    //yukardaki urlye bir get istegi yapıyoruz @get içinde v1den sonraki kısmını yazıyoruz ondan öncesi base url olarak mainactivityde.

    // sonra call ile linkteki içeriği Liste biçiminde
    // CoronaModel sınıfındaki haliyle alıyoruz.
    @GET("\"content-type\", \"application/json\",\"authorization\", \"apikey 7KskMWk4LthKPk4t8bFJfF:1LXt8Jo0BLId60XaZ3mBjE\"")
    Call<List<CoronaModel>> getCoronaData();

}
