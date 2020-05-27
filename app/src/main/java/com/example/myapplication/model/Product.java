package com.example.myapplication.model;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

//recyclerview'dakı her bir card için itemlerin sınıfı
public class Product {

    private String productName;
    private String productDescription;
    private int imageID;
    private String objID;



    public Product() {
    }



    public Product(int imageID, String productName, String productDescription, String objID) {
        this.imageID = imageID;
        this.productName = productName;
        this.productDescription = productDescription;
        this.objID= objID;
    }


    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public static ArrayList<Product> getData() {
        final ArrayList<Product> productList = new ArrayList<Product>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PLACES");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        productList.clear();

                        for (ParseObject object : objects) {
                            Product temp = new Product();
                            temp.setImageID(R.drawable.select);
                            temp.setProductName(object.getString("ulke")+ " "+ object.getString("sehir") + " "+object.getString("ilce"));
                            temp.setProductDescription(object.getCreatedAt().toString());
                            temp.setObjID(object.getObjectId());
                            productList.add(temp);
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        return productList;
    }
}