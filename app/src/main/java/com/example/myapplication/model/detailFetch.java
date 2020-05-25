package com.example.myapplication.model;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.DetailActivity;

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

public class detailFetch extends AsyncTask<Void,Void,Void> {
    String data = "";
    String dataParsed = "";
    String dataParsed2="";
    String ulke;
    double lat;
    double lon;

    General_InformationClass generalInstance = General_InformationClass.instance;
    DetailActivity detailInstance = DetailActivity.instance;

    public detailFetch(String ulke, Double latitude, Double longitude){
        this.ulke=ulke;
        this.lat = latitude;
        this.lon = longitude;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            URL url = new URL("https://corona.lmao.ninja/v2/countries");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i=0;i<JA.length();i++){
                JSONObject JO = JA.getJSONObject(i);
                String jsonulke = (String) JO.getString("country");
                if(ulke.equals(jsonulke)){
                    dataParsed = dataParsed + "Ülke: " + ulke+"\n";
                    dataParsed = dataParsed + "Total cases: " + JO.getString("cases")+"\n";
                    dataParsed = dataParsed + "Today cases: " + JO.getString("todayCases");
                    if(JO.getString("todayCases").equals("0")){
                        dataParsed= dataParsed+" (or not specified yet)";
                    }
                }


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data = "";
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
            dataParsed2 = "Ülke: " + generalInstance.getUlke()+"\n";
            //Şehir ismi
            dataParsed2 = dataParsed2+"Şehir: "+generalInstance.getSehir()+"\n";
            //İlçe ismi
            String ilceismi = JA.getString("name");
            dataParsed2 = dataParsed2 + "İlçe: " + generalInstance.getIlce() +"\n" ;
            //Sıcaklık
            JSONObject JA2 = JA.getJSONObject("main");
            String sicaklik = JA2.getString("temp");
            dataParsed2 = dataParsed2 + "Sıcaklık: " + sicaklik ;
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
        System.out.println(dataParsed);
        System.out.println(dataParsed2);
        detailInstance.getCoronaText().setText(this.dataParsed);
        detailInstance.getWeatherText().setText(this.dataParsed2);

    }
}
