package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.CoronaModel;
import com.example.myapplication.services.CoronaAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ArrayList<CoronaModel> coronaModels;
    public String BASE_URL="https://corona-virus-stats.herokuapp.com/api/v1/";
    Retrofit retrofit;
    public static TextView weatherData, coronaData;
    Button weatherClick, coronaClick;

    double lat = MapsActivity.latLng.latitude;
    double lon = MapsActivity.latLng.longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson= new GsonBuilder().setLenient().create();
        retrofit=new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();


        Button buttonBack=(Button)findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });
        weatherClick = (Button) findViewById(R.id.weatherButton);
        weatherClick.setText(MapsActivity.sehir+" Weather");
        weatherData = (TextView) findViewById(R.id.weatherText);
        coronaData = (TextView) findViewById(R.id.coronaText);

        weatherClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchData process = new fetchData(lat,lon);
                process.execute();

            }
        });

        coronaClick = (Button)findViewById(R.id.coronaButton);
        coronaClick.setText(MapsActivity.ulke+" Corona Data");
        coronaClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchCorona process = new fetchCorona(MapsActivity.ulke);
                process.execute();

            }
        });

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
