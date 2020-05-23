package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.General_InformationClass;
import com.example.myapplication.model.fetchCorona;
import com.example.myapplication.model.fetchData;
import com.example.myapplication.model.fetchPhoto;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

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
    Bitmap chosenImage;
    public static ImageView imageView;

    //menu için
    public boolean onCreateOptionsMenu(Menu menu) {
        //menü ekleyip bağlıyoruz
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_place,menu);

        return super.onCreateOptionsMenu(menu);
    }
    //menu secimiyle ne yapılacak
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_place) {
            update();
            Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
            upload();
            //intent ile yer ekleme aktivitesine geçiyoruz
            //Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
            //startActivity(intent);
        }
        /*
        else if (item.getItemId() == R.id.log_out) {
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
         */

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView) findViewById(R.id.imageView);
        Button buttonBack=(Button)findViewById(R.id.backButton);
        weatherClick = (Button) findViewById(R.id.weatherButton);
        weatherData = (TextView) findViewById(R.id.weatherText);
        coronaData = (TextView) findViewById(R.id.coronaText);
        coronaClick = (Button)findViewById(R.id.coronaButton);
      /*
        Gson gson= new GsonBuilder().setLenient().create();
         retrofit=new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
       */
      //Main açıldığında şehir fotosu yükle
        String sehir = MapsActivity.sehir.toLowerCase(new Locale("tr","TR"));
        fetchPhoto photo = new fetchPhoto(MapsActivity.ulke + " "+sehir);
        photo.execute();


        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });

        weatherClick.setText(MapsActivity.sehir+" Weather");
        weatherClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchData process = new fetchData(lat,lon);
                process.execute();

            }
        });
        coronaClick.setText(MapsActivity.ulke+" Corona Data");
        coronaClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchCorona process = new fetchCorona(MapsActivity.ulke);
                process.execute();

            }
        });
    }
    //version farklılıkları için activitycompat ve contextcompat kullanıyoruz
    // (SDK 23 VE ALTI 23 ÜSTÜNDEN FARKLI İZİN KONUSUNDA İKİSİNDE DE CALISSIN DİYE)
    public void selectPicture(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }
    @Override
    //izin isteği için işlemler
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2 ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode ==1 && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                chosenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageView.setImageBitmap(chosenImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void toSıngActivity(View view){
        Intent intent=new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
    }

    //BUTUN STATİC ŞEYLERİ BU ŞEKLE ÇEVİRMEMİZ LAZIM MAPS AKTİVİTEDKİLER DE DAHİL
    public void update(){
        General_InformationClass general_informationClass=General_InformationClass.getInstance();
        String weatherInfo=weatherData.getText().toString();
        String coronaInfo=coronaData.getText().toString();
        String latitude= String.valueOf(lat);
        String longitude= String.valueOf(lon);
        String ulke=MapsActivity.ulke;
        String sehir=MapsActivity.sehir;
        String ilce=MapsActivity.ilce;

        general_informationClass.setWeatherData(weatherInfo);
        general_informationClass.setCoronaData(coronaInfo);
        general_informationClass.setLatitudeOfPlace(latitude);
        general_informationClass.setLongitudeOfPlace(longitude);
        general_informationClass.setUlke(ulke);
        general_informationClass.setSehir(sehir);
        general_informationClass.setIlce(ilce);
        general_informationClass.setPhotoOfPlaces(chosenImage);
    }

    public void upload(){

        //resmi byte haline çevirip öyle kaydediyoruz.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //chosenImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("image.png",bytes);

        //parse upload
        ParseObject object=new ParseObject("PLACES");
        object.put("username",ParseUser.getCurrentUser().getUsername());
        object.put("weatherInfo",weatherData.getText().toString());
        object.put("coronaInfo",coronaData.getText().toString());
        object.put("latitude", String.valueOf(lat));
        object.put("longitude",String.valueOf(lon));
        object.put("ulke",MapsActivity.ulke);
        object.put("sehir",MapsActivity.sehir);
        object.put("ilce",MapsActivity.ilce);
        object.put("Name",MapsActivity.ulke +" "+MapsActivity.sehir);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e != null ) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}
