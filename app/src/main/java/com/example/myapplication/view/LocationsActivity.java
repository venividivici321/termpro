package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.model.General_InformationClass;
import com.example.myapplication.model.Product;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LocationsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

       recyclerView=findViewById(R.id.recyclerView);

       ProductAdapter productAdapter=new ProductAdapter(this,Product.getData());
       recyclerView.setAdapter(productAdapter);
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       recyclerView.setLayoutManager(linearLayoutManager);
    }
    //MENU KISMI
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menü ekleyip bağlıyoruz
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }
    //MENU İTEMI SECILDIGINDE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        switch (id) {
            case R.id.add_place:
                //intent ile yer ekleme aktivitesine geçiyoruz
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.log_out:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                        } else {
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.linearViewVertical:
                linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case R.id.gridView:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
