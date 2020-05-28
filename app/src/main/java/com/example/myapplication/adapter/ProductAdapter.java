package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.model.General_InformationClass;
import com.example.myapplication.model.Product;
import com.example.myapplication.view.DetailActivity;
import com.example.myapplication.view.LocationsActivity;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;

import static com.parse.Parse.getApplicationContext;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    ArrayList<Product> mProductList;
    LayoutInflater inflater;
    Context context;
    General_InformationClass generalInstance = General_InformationClass.instance;
    public ProductAdapter(Context context, ArrayList<Product> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Product selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);


    }

    @Override
    public int getItemCount() {

        return mProductList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, productDescription;
        ImageView productImage, deleteproduct;
        String ObjId;
        //item_product_card.xml'inde bulunan itemleri tanımlıyoruz
        public MyViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productName.setOnClickListener(this);
            productDescription = itemView.findViewById(R.id.productDescription);
            productImage = itemView.findViewById(R.id.productImage);
            productImage.setOnClickListener(this);
            deleteproduct = itemView.findViewById(R.id.deleteproduct);
            deleteproduct.setOnClickListener(this);

        }
        //Sınıf objesini alıp verileri set ediyor
        public void setData(Product selectedProduct, int position) {
            this.ObjId=selectedProduct.getObjID();
            this.productName.setText(selectedProduct.getProductName());
            this.productDescription.setText(selectedProduct.getProductDescription());
            this.productImage.setImageResource(selectedProduct.getImageID());
        }
        @Override
        //çöp kutusuna tıklatınca silme işlemi aşağıdaki methodu çağırıyor
        public void onClick(View v) {
            if (v == deleteproduct) {
                deleteproduct(getLayoutPosition());
                deleteObject(ObjId);
            }
            if(v==productImage || v==productName || v==productDescription){
              Product p = mProductList.get(getLayoutPosition());
              String ulkesehirilce = p.getProductName();
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("ulkesehirilce", ulkesehirilce);
                context.startActivity(intent);
            }
        }
        //databaseden de silmek lazım.
        private void deleteproduct(int position) {
            mProductList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mProductList.size());

        }





        public void deleteObject(String s) {
            // TODO: modify me!
            final boolean deleteAttributesOnly = true;

            ParseQuery<ParseObject> query = ParseQuery.getQuery("PLACES");

            // Retrieve the object by id
            query.getInBackground(s, new GetCallback<ParseObject>() {
                public void done(ParseObject entity, ParseException e) {
                    if (e == null) {
                        if (deleteAttributesOnly) {
                            // If you want to undefine a specific field, do this:
                            try {
                                entity.delete();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }

                            // Then save the changes
                            entity.saveInBackground();
                        } else {

                        }
                    }
                }
            });
        }

    }
}