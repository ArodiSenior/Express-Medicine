package com.techsavanna.medicine.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsavanna.medicine.models.OrderModel;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.adapters.OrderAdapter;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    RecyclerView OrdersRecycleview;
    FirebaseDatabase orderdatabase;
    DatabaseReference orderreference;
    OrderAdapter orderAdapter;


    ArrayList<OrderModel> orderList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.orderfragment, container, false);

        OrdersRecycleview = view.findViewById(R.id.OrdersRecycleview);
        OrdersRecycleview.setHasFixedSize(true);
        OrdersRecycleview.setAdapter(orderAdapter);
        OrdersRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrdersRecycleview.setItemAnimator(new DefaultItemAnimator());

        orderdatabase = FirebaseDatabase.getInstance();
        orderreference = orderdatabase.getReference("Request");
        orderreference.keepSynced(true);
        orderreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                    assert orderModel != null;
                    orderList.add(orderModel);

                }
                orderAdapter = new OrderAdapter(getActivity(), orderList);
                OrdersRecycleview.setAdapter(orderAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opssss....something wrong", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }


}
