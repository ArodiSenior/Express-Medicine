package com.techsavanna.medicine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.MainActivity;
import com.techsavanna.medicine.adapters.ItemAdapter;
import com.techsavanna.medicine.adapters.SliderAdapter;
import com.techsavanna.medicine.models.ItemModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    
    ViewPager viewPager;
    LinearLayout linearLayout;
    int dotscount;
    NestedScrollView scrollView;
    ImageView[] dots;
    SliderAdapter sliderAdapter;
    
    RecyclerView MainRecyclerview;
    FirebaseDatabase maindatabase;
    DatabaseReference mainreference;
    ItemAdapter itemAdapter;

    ArrayList<ItemModel> itemList = new ArrayList<>();
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.homefragment, container, false);
        
    
        viewPager = view.findViewById(R.id.ViewPager1);
        sliderAdapter = new SliderAdapter(getActivity());
        viewPager.setAdapter(sliderAdapter);
        scrollView = view.findViewById(R.id.FirstScroll);
        linearLayout = view.findViewById(R.id.LinearLayout);
        dotscount = sliderAdapter.getCount();
        dots = new ImageView[dotscount];
        
        for (int i = 0; i<dotscount; i++) {
            if (getActivity() != null) {
                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(8, 0, 8, 0);
                linearLayout.addView(dots[i], layoutParams);
            }
        }
        if (getActivity() != null) {
            dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new Tymer(), 2000, 4000);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            
                }
        
                @Override
                public void onPageSelected(int position) {
                    if (getActivity() != null) {
                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                        }
                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
                
                    }
                    for (int i = 0; i < dotscount; i++) {
                        if (getActivity() != null) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                        }
                    }
                    if (getActivity() != null) {
                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
                    }
                }
        
                @Override
                public void onPageScrollStateChanged(int state) {
            
                }
            });
        }
        MainRecyclerview = view.findViewById(R.id.MainRecyclerview);
        MainRecyclerview.setHasFixedSize(true);
        MainRecyclerview.setAdapter(itemAdapter);
        MainRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
                    String string = "American Recipes";
                    if (itemModel.upcategory.equals(string)) {
                    itemList.add(itemModel);
                    }
                }
                itemAdapter = new ItemAdapter(getActivity(), itemList);
                MainRecyclerview.setAdapter(itemAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opssss....something wrong", Toast.LENGTH_SHORT).show();

            }
        });


            return view;
        }
        
        public class Tymer extends TimerTask {
    
            @Override
            public void run() {
                if (getActivity()!=null){
                    (getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (viewPager.getCurrentItem() ==0){
                                viewPager.setCurrentItem(1);
                            }else if (viewPager.getCurrentItem() == 1){
                                viewPager.setCurrentItem(2);
                            }else if (viewPager.getCurrentItem() == 2){
                                viewPager.setCurrentItem(3);
                            }else if (viewPager.getCurrentItem() == 3){
                                viewPager.setCurrentItem(4);
                            }else if (viewPager.getCurrentItem() == 4){
                                viewPager.setCurrentItem(5);
                            }else if (viewPager.getCurrentItem() == 5){
                                viewPager.setCurrentItem(6);
                            }else if (viewPager.getCurrentItem() == 6){
                                viewPager.setCurrentItem(7);
                            }else if (viewPager.getCurrentItem() == 7){
                                viewPager.setCurrentItem(8);
                            }else if (viewPager.getCurrentItem() == 8){
                                viewPager.setCurrentItem(9);
                            }else viewPager.setCurrentItem(0);
                        }
                    });
                }
        
            }
        }
    
    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null) {
            // Set title bar
            ((MainActivity) getActivity())
                    .setActionBarTitle("Express Medicine");
        }
    }
        

    }



