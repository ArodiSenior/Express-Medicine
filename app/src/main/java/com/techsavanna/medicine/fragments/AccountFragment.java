package com.techsavanna.medicine.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.MainActivity;
import com.techsavanna.medicine.activities.MapsActivity;

public class AccountFragment extends Fragment{
    RelativeLayout orders;
   
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        orders = view.findViewById(R.id.LayoutOrders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                intent.putExtra("lat",-1.27);
                intent.putExtra("lng",38.5);
                startActivity(intent);
            }
        });
        
       return view;
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        // Set title bar
        if (getActivity() != null) {
            ((MainActivity) getActivity())
                    .setActionBarTitle("Account");
        }
    }
    
}
