package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techsavanna.medicine.R;
import com.techsavanna.medicine.adapters.CartAdapter;
import com.techsavanna.medicine.database.Database;
import com.techsavanna.medicine.models.OrderModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Checkout extends AppCompatActivity {
    RecyclerView CheckRecyclerview;
    Button CheckButton;
    CartAdapter checkAdapter;
    TextView CheckPrice;
    Toolbar toolbar;
    Location location;

    ArrayList<OrderModel> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_checkout);
        toolbar = findViewById(R.id.CheckToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckPrice = findViewById(R.id.CheckPrice);
        CheckButton = findViewById(R.id.CheckButton);
        CheckRecyclerview = findViewById(R.id.CheckRecyclerview);
        CheckRecyclerview.setHasFixedSize(true);
        CheckRecyclerview.setAdapter(checkAdapter);
        CheckRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CheckRecyclerview.setItemAnimator(new DefaultItemAnimator());
        

        cartList = (ArrayList<OrderModel>) new Database(this).getCarts();
        checkAdapter = new CartAdapter(this, cartList);
        CheckRecyclerview.setAdapter(checkAdapter);


        int total = 0;
        for (OrderModel orderModel:cartList)
        total += (Integer.parseInt(orderModel.getPrice())) * (Integer.parseInt(orderModel.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        CheckPrice.setText(numberFormat.format(total));
    
        CheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, SubmitActivity.class);
                intent.putExtra("price",CheckPrice.getText().toString().trim());
                startActivity(intent);
            }
        });

    }

    
}
