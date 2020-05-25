package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LocationsActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> placeNames;
    ArrayAdapter arrayAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menü ekleyip bağlıyoruz
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_place) {
            //intent ile yer ekleme aktivitesine geçiyoruz
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
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
        /*
        else (item.getItemId() == R.id.goto){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
         */

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        listView = findViewById(R.id.listview_locations_activity);

        placeNames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,placeNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("ulkevesehir",placeNames.get(i));
                startActivity(intent);
            }
        });

        download();
    }
    public void download() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PLACES");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        placeNames.clear();

                        for (ParseObject object : objects) {

                            placeNames.add(object.getString("ulke")+ " "+ object.getString("sehir"));


                            arrayAdapter.notifyDataSetChanged();

                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
