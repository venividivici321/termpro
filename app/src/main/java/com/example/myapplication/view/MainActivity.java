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
import android.widget.PopupMenu;
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
import com.squareup.picasso.Picasso;

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

import static com.parse.Parse.getApplicationContext;

public class MainActivity<informationClass> extends AppCompatActivity {
    General_InformationClass generalInstance = General_InformationClass.instance;

    Button weatherClick , coronaClick;
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

        generalInstance.setImageView((ImageView) findViewById(R.id.imageView));
        generalInstance.setWeatherData((TextView) findViewById(R.id.weatherText));
        generalInstance.instance.setCoronaData((TextView) findViewById(R.id.coronaText));

      /*
        Gson gson= new GsonBuilder().setLenient().create();
         retrofit=new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
       */
        String lowersehir = generalInstance.getSehir().toLowerCase(new Locale("tr","TR"));
        fetchPhoto photo = new fetchPhoto(lowersehir);
        photo.execute();

        generalInstance.setPhotoButton((Button) findViewById(R.id.selectPhoto));
        final Button photoBtnClick = generalInstance.getPhotoButton();
        photoBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(generalInstance.getImgURLarray().get(0));
                generalInstance.setPhotoPopup(new PopupMenu(getApplicationContext(),generalInstance.getPhotoButton()));
                for(int i=0;i<generalInstance.getImgURLarray().size();i++) {
                    generalInstance.getPhotoPopup().getMenu().add(i,i,i,(i+1)+". Photo");

                }
                generalInstance.getPhotoPopup().show();
                generalInstance.getPhotoPopup().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       System.out.println(item.getItemId());
                       System.out.println(generalInstance.getImgURLarray().get(item.getItemId()));
                       photoBtnClick.setText((item.getItemId()+1)+". Photo");
                       Picasso.get().load((String)generalInstance.getImgURLarray().get(item.getItemId())).placeholder(R.drawable.imageloading_foreground).into(generalInstance.getImageView());
                       return false;
                   }
               });
            }


        });
      //Main açıldığında şehir fotosu yükle


        Button buttonBack=(Button)findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });

        weatherClick = (Button) findViewById(R.id.weatherButton);
        weatherClick.setText(generalInstance.getSehir()+" Weather");
        weatherClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchData process = new fetchData(generalInstance.getLatLng().latitude,generalInstance.getLatLng().longitude);
                process.execute();

            }
        });

        coronaClick = (Button)findViewById(R.id.coronaButton);
        coronaClick.setText(generalInstance.getUlke()+" Corona Data");
        coronaClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                fetchCorona process = new fetchCorona(generalInstance.getUlke());
                process.execute();

            }
        });
    }

    public void toSignActivity(View view){
        Intent intent=new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
    }

    //BUTUN STATİC ŞEYLERİ BU ŞEKLE ÇEVİRMEMİZ LAZIM MAPS AKTİVİTEDKİLER DE DAHİL


    public void upload(){
        //resmi byte haline çevirip öyle kaydediyoruz.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //chosenImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("image.png",bytes);

        //parse upload
        ParseObject object=new ParseObject("PLACES");
        object.put("username",ParseUser.getCurrentUser().getUsername());
        object.put("weatherInfo",generalInstance.getWeatherData().getText().toString());
        object.put("coronaInfo",generalInstance.getCoronaData().getText().toString());
        object.put("latitude", String.valueOf(generalInstance.getLatLng().latitude));
        object.put("longitude",String.valueOf(generalInstance.getLatLng().longitude));
        object.put("ulke",generalInstance.getUlke());
        object.put("sehir",generalInstance.getSehir());
        object.put("ilce",generalInstance.getIlce());
        object.put("Name",generalInstance.getUlke() +" "+generalInstance.getSehir());

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
