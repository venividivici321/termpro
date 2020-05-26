package com.example.myapplication.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.General_InformationClass;
import com.example.myapplication.model.fetchCorona;
import com.example.myapplication.model.fetchData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    General_InformationClass generalInstance = General_InformationClass.instance;
    public GoogleMap mMap;
    String addressText;
    MarkerOptions markerOptions;
    private final static int REQUEST_lOCATION=90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Autocomplete 300 dolarmış
       /* final String placesapi = "AIzaSyAF3yQS5_pLbp3C_J1fVFVFVDltiRN6_aA";
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),placesapi);
        }

        placesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.i("PlacesApi", "onPlaceSelected: "+latLng.latitude+"\n"+latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ZoomControls zoom=(ZoomControls)findViewById(R.id.zoom);

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        //Mainactivitye GÖTÜREN BUTON
        final Button btn_MapType=(Button) findViewById(R.id.btn_Sat);
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalInstance.setLatLng(markerOptions.getPosition());
                Intent intent=new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                fetchCorona process = new fetchCorona(generalInstance.getUlke());
                process.execute();
                fetchData process2 = new fetchData(generalInstance.getLatLng().latitude,generalInstance.getLatLng().longitude);
                process2.execute();
            }
        });

        Button btnGo=findViewById(R.id.btn_Go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLocation=(EditText)findViewById(R.id.et_location);
                String location=etLocation.getText().toString();
                if(!location.equals("")){
                    Address address;
                    List<Address> adressList=null;
                    Geocoder geocoder=new Geocoder(MapsActivity.this);
                    try {
                        adressList=geocoder.getFromLocationName(location,1);
                        if(!adressList.isEmpty()){
                        address=adressList.get(0);
                        generalInstance.setLatLng(new LatLng(address.getLatitude(),address.getLongitude()));
                        // Clears the previously touched position
                        mMap.clear();

                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(generalInstance.getLatLng()));

                        // Creating a marker
                        markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(generalInstance.getLatLng());

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                        new ReverseGeocodingTask(getBaseContext()).execute(generalInstance.getLatLng());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address> addresses = null;
            addressText="";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
                if(address.getSubLocality() == null)
                    address.setSubLocality("Can't find sublocal");
                addressText = String.format("%s, %s, %s",
                        address.getAdminArea(),
                        address.getSubLocality(),
                        address.getCountryName());

                generalInstance.setIlce(address.getSubLocality());
                generalInstance.setUlke(address.getCountryName());
                generalInstance.setSehir(address.getAdminArea());
            }
            System.out.println(addressText);
            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(addressText);
            // Placing a marker on the touched position
            mMap.addMarker(markerOptions);
        }

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        generalInstance.setLatLng(new LatLng(41.068331649738575, 28.9460164681077)); // İstanbul koordinatları
        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(generalInstance.getLatLng()));

        // Creating a marker
        markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(generalInstance.getLatLng());

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
        new ReverseGeocodingTask(getBaseContext()).execute(generalInstance.getLatLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(generalInstance.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(generalInstance.getLatLng(),10));
        //TIKLADIĞINDA İŞARETLEYİP ZOOMLAMA
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                // Getting the Latitude and Longitude of the touched location
                generalInstance.setLatLng(arg0);
                System.out.println("Lat: "+generalInstance.getLatLng().latitude +"\nLon: " +generalInstance.getLatLng().longitude );

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(generalInstance.getLatLng()));

                // Creating a marker
                markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(generalInstance.getLatLng());

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                // Adding Marker on the touched location with address
                new ReverseGeocodingTask(getBaseContext()).execute(generalInstance.getLatLng());



            }
        });
        // Add a marker in Sydney and move the camera



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            mMap.setMyLocationEnabled(true);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_lOCATION){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                mMap.setMyLocationEnabled(true);
            }else{
                Toast.makeText(getApplicationContext(),"USER Dıd not allowed location permissions",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
