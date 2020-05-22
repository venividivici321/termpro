package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.fetchCorona;
import com.example.myapplication.model.fetchData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    //ArrayList<CoronaModel> coronaModels;
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

      /*
        Gson gson= new GsonBuilder().setLenient().create();
         retrofit=new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
       */
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
        //static bunlar memory leak verir değiştirmemiz lazım.
        weatherData = (TextView) findViewById(R.id.weatherText);
        coronaData = (TextView) findViewById(R.id.coronaText);

        //buton tıklanınca hava durumu
        weatherClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchData process = new fetchData(lat,lon);
                process.execute();

            }
        });

        coronaClick = (Button)findViewById(R.id.coronaButton);
        coronaClick.setText(MapsActivity.ulke+" Corona Data");
        //buton tıklanınca corona durumu
        coronaClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCorona process = new fetchCorona(MapsActivity.ulke);
                process.execute();
            }
        });

        Button signbutton = (Button) findViewById(R.id.nextButton);
        signbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class );
                startActivity(intent);
                finish();
            }
        });


}
}