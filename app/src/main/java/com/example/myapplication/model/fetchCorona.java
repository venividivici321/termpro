package com.example.myapplication.model;

import android.os.AsyncTask;

import com.example.myapplication.view.MainActivity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

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

public class fetchCorona extends AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String ulke;
    General_InformationClass generalInstance = General_InformationClass.instance;
    public fetchCorona(String ulke){
        this.ulke=ulke;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {

            URL url = new URL("https://disease.sh/v2/countries?yesterday=1");
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



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        generalInstance.getCoronaData().setText(dataParsed);
    }
}
