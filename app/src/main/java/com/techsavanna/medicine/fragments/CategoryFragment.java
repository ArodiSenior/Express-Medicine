package com.techsavanna.medicine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.MainActivity;
import com.techsavanna.medicine.adapters.CategoryAdapter;
import com.techsavanna.medicine.models.ItemModel;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements View.OnClickListener {
    RecyclerView MainRecyclerview;
    FirebaseDatabase maindatabase;
    DatabaseReference mainreference;
    CategoryAdapter categoryAdapter;
    CardView drugs,british,chinese,french,greek,indian,japanese,mexican;
    
    ArrayList<ItemModel> itemList = new ArrayList<>();
    boolean clicked = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        MainRecyclerview = view.findViewById(R.id.MainRecyclerview);
        drugs = view.findViewById(R.id.Drugs);
        //drugs.setCardBackgroundColor(getResources().getColor(R.color.gray));
        british = view.findViewById(R.id.British);
        chinese = view.findViewById(R.id.Chinese);
        french = view.findViewById(R.id.French);
        greek = view.findViewById(R.id.Greek);
        indian = view.findViewById(R.id.Indian);
        japanese = view.findViewById(R.id.Japanese);
        mexican = view.findViewById(R.id.Mexican);
        japanese.setOnClickListener(this);
        indian.setOnClickListener(this);
        greek.setOnClickListener(this);
        french.setOnClickListener(this);
        chinese.setOnClickListener(this);
        drugs.setOnClickListener(this);
        british.setOnClickListener(this);
        mexican.setOnClickListener(this);
        displayItems("American Recipes");
        return view;
    }
    
    
    @Override
    public void onClick(View v) {
        
        int id = v.getId();
        switch (id){
            case R.id.Drugs:
                
//                displayItems("Total");
                //drugs.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.British:
//                displayItems("Harshi");
                //british.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.Chinese:
//                displayItems("Pro-Gas");
                //chinese.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.French:
//                displayItems("K-Gas");
                //french.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.Greek:
//                displayItems("Oilibya");
                //greek.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.Indian:
//                displayItems("Mpishi");
                //indian.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.Japanese:
//                displayItems("G-Gas");
                //japanese.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
    
            case R.id.Mexican:
//                displayItems("Jiji-Gas");
                //mexican.setCardBackgroundColor(getResources().getColor(R.color.gray));
                break;
        }
    }
    public void displayItems(final String string){
        MainRecyclerview.setHasFixedSize(true);
        MainRecyclerview.setAdapter(categoryAdapter);
        MainRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        MainRecyclerview.setItemAnimator(new DefaultItemAnimator());
    
        maindatabase = FirebaseDatabase.getInstance();
        mainreference = maindatabase.getReference("Food").child("Food");
        mainreference.keepSynced(true);
        mainreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ItemModel itemModel = dataSnapshot1.getValue(ItemModel.class);
                    assert itemModel != null;
                    if (itemModel.upcategory.equals(string)) {
                        itemList.add(itemModel);
                    }
                }
                categoryAdapter = new CategoryAdapter(getActivity(), itemList);
                MainRecyclerview.setAdapter(categoryAdapter);
            
            
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opssss....something wrong", Toast.LENGTH_SHORT).show();
            
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
    
        // Set title bar
        if (getActivity() != null) {
            ((MainActivity) getActivity())
                    .setActionBarTitle("Category");
        }
    }
}
