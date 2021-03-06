package com.techsavanna.medicine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.MainActivity;


public class FavFragment extends Fragment {
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    
        // Set title bar
        if (getActivity() != null) {
            ((MainActivity) getActivity())
                    .setActionBarTitle("Favourites");
        }
    }
    
    
}
