package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goMethod(View v) {
        DownloadWeatherInfo downloadWeatherInfo = new DownloadWeatherInfo();
        try {
            String url = "http://api.openweathermap.org/data/2.5/weather?q=Izmir&appid=83126a20180f46255f3b2fdd4e981ec3";
            downloadWeatherInfo.execute(url);
        } catch (Exception e) {

        }

    }

    public void getCorona(View view) {
        DownloadCoronaInfo downloadCoronaInfo=new DownloadCoronaInfo();
        try{
            //String coronaUrl="https://api.covid19api.com/summary";
           // String coronaUrl="https://api.apify.com/v2/key-value-stores/tVaYRsPHLjNdNBu7S/records/LATEST?disableRedirect=true";
            String coronaUrl="https://corona-virus-stats.herokuapp.com/api/v1/cases/countries-search";
            downloadCoronaInfo.execute(coronaUrl);
        }catch (Exception e){

        }

    }

    public class DownloadWeatherInfo extends AsyncTask<String, Void, String> {

        @Override
        //arka planda yapılacak işler
        //burdan api url'sini veriyoruz string olarak ,onpostexecute'da apideki hava durumunu çekiyoruz
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data > 0) {
                    char c = (char) data;
                    result += c;
                    data = inputStreamReader.read();

                }
            } catch (Exception e) {
                return null;
            }
            return result;
        }

        @Override
        //işlem bittiğinde olacaklar
        //lintteki bilgileri stringe atıp gösterdik şimdi JSON'u prse yapıp ihtiyacımız olanları çekebiliriz.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println("alınan data: "+ s);
            //JSON object{} ve JSON array[] kullanacagız
            try {
                //Json objesiyle apideki veriyi çekmece,çalışıyor.
                /*JSONObject jsonObject=new JSONObject(s);
                String deneme=jsonObject.getString("id");
                System.out.println("deneme: "+ deneme);*/
                JSONObject jsonObject = new JSONObject(s);
                String deneme = jsonObject.getString("coord");
                System.out.println("coordinates:" + deneme);
                JSONObject jsonObject1 = new JSONObject(deneme);
                String lon = jsonObject1.getString("lon");
                System.out.println("coordinates:" + lon);
                String lat = jsonObject1.getString("lat");
                System.out.println("coordinates:" + lat);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public class DownloadCoronaInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String coronaResult = "";
            URL urlCorona;
            HttpsURLConnection httpsURLConnection;
            try {
                urlCorona = new URL(strings[0]);
                httpsURLConnection = (HttpsURLConnection) urlCorona.openConnection();
                InputStream inputStream = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int dataCorona = inputStreamReader.read();
                while (dataCorona > 0) {
                    char c = (char) dataCorona;
                    coronaResult += c;
                    dataCorona = inputStreamReader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return  coronaResult;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject=new JSONObject(s);
                String deneme=jsonObject.getString("status");
                System.out.println("coronadeneme: "+deneme);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
}
