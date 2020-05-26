package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.General_InformationClass;
import com.example.myapplication.model.detailFetch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    public static final DetailActivity instance = new DetailActivity();

    private GoogleMap mMap;
    TextView sehirText,
            coronaText,
            weatherText;
    ImageView detail_activityImageView;
    String sehirName,
            ilceName;
    String placeName,
            latitude,
            longitude,
            imgURL;
    Double latitudeDouble;
    Double longitudeDouble;
    Button gotomap;
    General_InformationClass generalInstance = General_InformationClass.instance;


    public TextView getCoronaText() {
        return coronaText;
    }

    public TextView getWeatherText() {
        return weatherText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        gotomap = findViewById(R.id.goto_Map);
        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //ulke_text_detail_activity=findViewById(R.id.ulke_text_detail_activity);
        sehirText=findViewById(R.id.sehir_text_detail_activity);
        instance.coronaText = findViewById(R.id.corona_text_detail_activity);
        instance.weatherText=findViewById(R.id.weatherof_text_detail_activity);
        detail_activityImageView=findViewById(R.id.detail_activityImageView);
        //artık ülke ve sehir olarak alıyoruz. sadece sehir ismi ile aratıyoruz.
        Intent intent=getIntent();
        placeName=intent.getStringExtra("ulkevesehir");
        String[] a = placeName.split(" ");
        placeName = a[0];
        sehirName = a[1];
        ilceName = a[2];
        generalInstance.setSehir(sehirName);
        generalInstance.setUlke(placeName);
        generalInstance.setIlce(ilceName);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetail);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        getMapDataParse();
        mMap.setOnMapLongClickListener(this);

    }

    public void getMapDataParse(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("PLACES");
        //Object ıd den çekmek lazım.ben ülkeden cektim şimdilik
        // ama iki turkiye olsa falan karısıklık cıkıcak duzeltmek lazım
        //deneyince bi sorun gözükmedi.
        query.whereEqualTo("ulke",placeName);
        query.whereEqualTo("sehir",sehirName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else {

                    if (objects.size()>0) {

                        for (final ParseObject object : objects) {

                            //ulke_text_detail_activity.setText(object.getString("ulke"));

                            sehirText.setText(object.getString("sehir"));
                           // coronaText.setText(object.getString("coronaInfo"));
                           // weatherText.setText(object.getString("weatherInfo"));

                            latitude = object.getString("latitude");
                            longitude = object.getString("longitude");
                            //uploadlanan foto urlsini çekip yüklüyoruz.
                            imgURL = object.getString("imageURL");
                            Picasso.get().load(imgURL).placeholder(R.drawable.imageloading_foreground).into(detail_activityImageView);

                            latitudeDouble = Double.parseDouble(latitude);
                            longitudeDouble = Double.parseDouble(longitude);
                            detailFetch process = new detailFetch(object.getString("ulke"),latitudeDouble,longitudeDouble);
                            process.execute();

                            mMap.clear();

                            LatLng placeLocation= new LatLng(latitudeDouble,longitudeDouble);
                            mMap.addMarker(new MarkerOptions().position(placeLocation).title("Start Position: "+placeName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,9));


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
    //Kullanıcı sehir içinde gezmelik yer işaretlesin diye
    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String adress="";
        try{
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList !=null && addressList.size()>0){
                if(addressList.get(0).getThoroughfare() !=null){
                    adress+=addressList.get(0).getThoroughfare();
                }
                if(addressList.get(0).getSubThoroughfare() !=null){
                    adress+=addressList.get(0).getSubThoroughfare();
                }
            }
        }catch (Exception ignored){

        }
        if (adress.matches("")){
            adress="Address is Not Defined";
        }
        //bu adresleri kaydetmek lazım
        mMap.addMarker(new MarkerOptions().position(latLng).title(adress));
    }
}
