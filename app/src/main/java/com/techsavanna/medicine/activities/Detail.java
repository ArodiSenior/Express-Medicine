package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.techsavanna.medicine.models.ItemModel;
import com.techsavanna.medicine.models.OrderModel;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.database.Database;

public class Detail extends AppCompatActivity {

    Intent intent;
    DatabaseReference editReference;
    ImageView expandedImage;
    TextView ViewItemName;
    TextView ViewItemPrice;
    TextView ViewItemDescription;
    TextView ViewItemCategory;
    Button AddToCartButton;
    ItemModel itemModel;

    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton elegantNumberButton;

    String edititem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        elegantNumberButton = findViewById(R.id.NumberButton);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        expandedImage = findViewById(R.id.expandedImage);
        ViewItemName = findViewById(R.id.ViewItemName);
        ViewItemPrice = findViewById(R.id.ViewItemPrice);
        ViewItemCategory = findViewById(R.id.ViewItemCategory);
        ViewItemDescription = findViewById(R.id.ViewItemDescription);
        FloatingActionButton fab =  findViewById(R.id.fab);
        AddToCartButton = findViewById(R.id.AddToCartButton);
        AddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new OrderModel(
                        edititem,
                        itemModel.getUpgitemname(),
                        elegantNumberButton.getNumber(),
                        itemModel.getUpitemprice(),
                        itemModel.getUpcategory(),
                        itemModel.getUpimage()



                ));
                Toast.makeText(Detail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        intent = getIntent();
        edititem = intent.getStringExtra("ItemDetailid");
        editReference = FirebaseDatabase.getInstance().getReference("Food").child("Food").child(edititem);
        editReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemModel = dataSnapshot.getValue(ItemModel.class);

                if (itemModel != null) {
                    ViewItemName.setText(itemModel.getUpgitemname());
                    ViewItemPrice.setText("Shs" + " " + itemModel.getUpitemprice());
                    ViewItemDescription.setText(itemModel.getUpitemdescription());
                    ViewItemCategory.setText(itemModel.getUpcategory());
                    Picasso.with(Detail.this).load(itemModel.getUpimage()).into(expandedImage);

                    collapsingToolbarLayout.setTitle(itemModel.getUpgitemname());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
