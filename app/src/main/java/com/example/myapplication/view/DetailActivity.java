package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView ulke_text_detail_activity,
            sehir_text_detail_activity,
            corona_text_detail_activity,
            weatherof_text_detail_activity;
    ImageView detail_activityImageView;
    String placeName,
            latitude,
            longitude;
    Double latitudeDouble;
    Double longitudeDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //ulke_text_detail_activity=findViewById(R.id.ulke_text_detail_activity);
        sehir_text_detail_activity=findViewById(R.id.sehir_text_detail_activity);
        corona_text_detail_activity=findViewById(R.id.corona_text_detail_activity);
        weatherof_text_detail_activity=findViewById(R.id.weatherof_text_detail_activity);
        detail_activityImageView=findViewById(R.id.detail_activityImageView);
        //bu locations aktivitesinin intentle yolladığı ülke ismi
        Intent intent=getIntent();
        placeName=intent.getStringExtra("ulke");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetail);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        getMapDataParse();
    }

    public void getMapDataParse(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("PLACES");
        //Object ıd den çekmek lazım.ben ülkeden cektim şimdilik
        // ama iki turkiye olsa falan karısıklık cıkıcak duzeltmek lazım
        //deneyince bi sorun gözükmedi.
        query.whereEqualTo("ulke",placeName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else {

                    if (objects.size()>0) {

                        for (final ParseObject object : objects) {

                            //ulke_text_detail_activity.setText(object.getString("ulke"));
                            sehir_text_detail_activity.setText(object.getString("sehir"));
                            corona_text_detail_activity.setText(object.getString("coronaInfo"));
                            weatherof_text_detail_activity.setText(object.getString("weatherInfo"));

                            latitude = object.getString("latitude");
                            longitude = object.getString("longitude");

                            latitudeDouble = Double.parseDouble(latitude);
                            longitudeDouble = Double.parseDouble(longitude);

                            mMap.clear();

                            LatLng placeLocation= new LatLng(latitudeDouble,longitudeDouble);
                            mMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));


                            /*
                             ParseFile parseFile = (ParseFile) object.get("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        detail_activityImageView.setImageBitmap(bitmap);

                                    }


                                }
                            });
                             */
                        }

                    }

                }
            }
        });
    }
}
