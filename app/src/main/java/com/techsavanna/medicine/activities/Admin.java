package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsavanna.medicine.adapters.AdminAdapter;
import com.techsavanna.medicine.models.ItemModel;
import com.techsavanna.medicine.R;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {
    
    FloatingActionButton floatingActionButton;
    RecyclerView foodecyclerview;
    FirebaseDatabase fooddatabase;
    DatabaseReference foodreference;
    AdminAdapter adminAdapter;
    Toolbar toolbar;
    
    ArrayList<ItemModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin);
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    
        floatingActionButton = findViewById(R.id.Foodfab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminAdd.class);
                startActivity(intent);
            }
        });
    
        foodecyclerview = findViewById(R.id.FoodRecyclerview);
        foodecyclerview.setHasFixedSize(true);
        foodecyclerview.setAdapter(adminAdapter);
        foodecyclerview.setLayoutManager(new LinearLayoutManager(Admin.this));
        foodecyclerview.setItemAnimator(new DefaultItemAnimator());
    
        fooddatabase = FirebaseDatabase.getInstance();
        foodreference = fooddatabase.getReference("Food").child("Food");
        foodreference.keepSynced(true);
        foodreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ItemModel itemModel = dataSnapshot1.getValue(ItemModel.class);
                    assert itemModel != null;
                    itemList.add(itemModel);
                
                }
                adminAdapter = new AdminAdapter(Admin.this, itemList);
                foodecyclerview.setAdapter(adminAdapter);
            
            
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin.this, "Opssss....something wrong", Toast.LENGTH_SHORT).show();
            
            }
        });

    }
}