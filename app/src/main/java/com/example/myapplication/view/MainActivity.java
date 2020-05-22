package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.fetchCorona;
import com.example.myapplication.model.fetchData;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

    //menu için
    public boolean onCreateOptionsMenu(Menu menu) {
        //menü ekleyip bağlıyoruz
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_place,menu);

        return super.onCreateOptionsMenu(menu);
    }
    //menu secimiyle ne yapılacak
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_place) {
            //intent ile yer ekleme aktivitesine geçiyoruz
            Intent intent = new Intent(getApplicationContext(), CreatePlaceActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.log_out) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }


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
        coronaClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchCorona process = new fetchCorona(MapsActivity.ulke);
                process.execute();

            }
        });
    }
    public void selectPicture(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2 ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void toSıngActivity(View view){
        Intent intent=new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
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
