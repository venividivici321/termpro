package com.example.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.myapplication.R;
import com.example.myapplication.view.MainActivity;
import com.example.myapplication.view.MapsActivity;
import com.squareup.picasso.Picasso;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static com.parse.Parse.getApplicationContext;

public class fetchPhoto extends AsyncTask<Void,Void,Void> {
    String data="";
    String query;
    String imageURL;
    General_InformationClass generalInstance = General_InformationClass.instance;


    public fetchPhoto(String query) {
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.unsplash.com")
                    .appendPath("search")
                    .appendPath("photos")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("query", query)
                    .appendQueryParameter("client_id","lotMnSg7KRG_TE-Z8qVbrvMImDwHEDvA_ubSAKn-Fcw");
            String myUrl = builder.build().toString();
            URL url;
            System.out.println("Searching for: "+query);
            url = new URL(myUrl);
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
            generalInstance.setImgURLarray(new ArrayList<String>());
            for(int i = 0;i< JA.length();i++) {
                System.out.println(i);
                JSONObject JO2 = new JSONObject(JA.getString(i));
                JSONObject JO3 = JO2.getJSONObject("urls");
                imageURL = JO3.getString("full");
                System.out.println(imageURL);
                generalInstance.getImgURLarray().add(imageURL);

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
        Picasso.get().load((String)generalInstance.getImgURLarray().get(0)).placeholder(R.drawable.imageloading_foreground).into(generalInstance.getImageView());
    }
}
