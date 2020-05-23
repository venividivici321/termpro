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
    String singleParsed = "";
    public static String ilceismi;

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
            dataParsed = "Ülke: " + MapsActivity.ulke +"\n";
            //Şehir ismi
            dataParsed = dataParsed+"Şehir: "+MapsActivity.sehir+"\n";
            //İlçe ismi
            ilceismi = JA.getString("name");
            dataParsed = dataParsed + "İlçe: " + ilceismi +"\n" ;
            //Sıcaklık
            JSONObject JA2 = JA.getJSONObject("main");
            String sicaklik = JA2.getString("temp");
            dataParsed = dataParsed + "Sıcaklık: " + sicaklik ;
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
        MainActivity.weatherData.setText(this.dataParsed);
    }
}
