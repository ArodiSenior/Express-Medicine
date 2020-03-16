package com.techsavanna.medicine.activities;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.adapters.CartAdapter;
import com.techsavanna.medicine.database.Database;
import com.techsavanna.medicine.models.OrderModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    RecyclerView CartRecyclerview;
    FirebaseDatabase cartdatabase;
    DatabaseReference cartreference;
    Button PlaceOrderButton;
    CartAdapter cartAdapter;
    TextView TotalCartPrice;
    Toolbar toolbar;

    ArrayList<OrderModel> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart);
        toolbar = findViewById(R.id.CartToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TotalCartPrice = findViewById(R.id.TotalCartPrice);
        PlaceOrderButton = findViewById(R.id.PlaceOrderButton);
        PlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    startActivity(new Intent(Cart.this, Login.class));
                }else {
                    Intent intent = new Intent(Cart.this, Checkout.class);
                    startActivity(intent);
        
                }
            }
        });
        CartRecyclerview = findViewById(R.id.ListCartRecyclerview);
        CartRecyclerview.setHasFixedSize(true);
        CartRecyclerview.setAdapter(cartAdapter);
        CartRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CartRecyclerview.setItemAnimator(new DefaultItemAnimator());

        cartdatabase = FirebaseDatabase.getInstance();
        cartreference = cartdatabase.getReference("Request");

        cartList = (ArrayList<OrderModel>) new Database(this).getCarts();
        cartAdapter = new CartAdapter(this, cartList);
        CartRecyclerview.setAdapter(cartAdapter);


        int total = 0;
        for (OrderModel orderModel:cartList)
        total += (Integer.parseInt(orderModel.getPrice())) * (Integer.parseInt(orderModel.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        TotalCartPrice.setText(numberFormat.format(total));

    }

}
