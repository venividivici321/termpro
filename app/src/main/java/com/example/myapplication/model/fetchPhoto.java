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

public class fetchPhoto extends AsyncTask<Void,Void,Void> {
    String data="";
    String query;



    public fetchPhoto(String query) {
       query = MapsActivity.sehir;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL("https://api.unsplash.com/search/photos?page=1&query="+query+"&client_id=lotMnSg7KRG_TE-Z8qVbrvMImDwHEDvA_ubSAKn-Fcw");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject JO = new JSONObject(data);
            JSONArray JA = new JSONArray(JO.getString("results"));
            System.out.println(JA);


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
        //MainActivity.weatherData.setText();
    }
}
