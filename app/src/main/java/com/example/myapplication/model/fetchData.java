package com.example.myapplication.model;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.example.myapplication.view.MainActivity;
import com.example.myapplication.view.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data="";
    String dataParsed = "";
    General_InformationClass generalInstance = General_InformationClass.instance;
    double lat;
    double lon;

    public fetchData(Double latitude,Double longitude) {
        lat = latitude;
        lon = longitude;
    }




    @Override
    protected Void doInBackground(Void... voids) {
        try {
            System.out.println("Lat: " + lat + " Lon: " + lon);
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid=83126a20180f46255f3b2fdd4e981ec3");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject JA = new JSONObject(data);
            //Ülke ismi
            dataParsed = "Ülke: " + generalInstance.getUlke()+"\n";
            //Şehir ismi
            dataParsed = dataParsed+"Şehir: "+generalInstance.getSehir()+"\n";
            //İlçe ismi
            String ilceismi = JA.getString("name");
            dataParsed = dataParsed + "İlçe: " + generalInstance.getIlce() +"\n" ;
            //Sıcaklık
            JSONObject JA2 = JA.getJSONObject("main");
            String sicaklik = JA2.getString("temp");
            dataParsed = dataParsed + "Sıcaklık: " + sicaklik ;
            //Durum
            JSONArray JA3 = JA.getJSONArray("weather");
            JSONObject JA4 = JA3.getJSONObject(0);
            String havadurumu = JA4.getString("description");
            havadurumu = havadurumu.toUpperCase();
            dataParsed+="\nHava: " + havadurumu;
            /*for(int i=0; i<JA.length();i++){
                JSONObject JO = (JSONObject) JA.get(i);
                System.out.println("Çalıştı");

                System.out.println(JO.toString());
            }
            */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
        generalInstance.getWeatherData().setText(this.dataParsed);
    }
}
