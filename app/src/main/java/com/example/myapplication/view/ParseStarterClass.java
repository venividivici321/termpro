package com.example.myapplication.view;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //asağıdakiler parse sunucusu ve projeyi birbirine bağlamak için
        //parse aslında backend servisi back4app de ücretsiz bi sunucu.
        //back4appteki sunucuya verileri resimleri falan kaydedebiliriz.
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)

                .applicationId("MS1YZb0c9hV31JUmR3bUsocjIw7rk4v8j67t6WVL")
                .clientKey("1EfAvWuDF6xK6T2X2lcGmKxRANAeaWg67JGHY2P2")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}
