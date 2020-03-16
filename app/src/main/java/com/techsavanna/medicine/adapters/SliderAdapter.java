package com.techsavanna.medicine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.techsavanna.medicine.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    
    
    public SliderAdapter(Context context) {
        this.context = context;
    }
    
    public int [] slide_images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9,
            R.drawable.image10
    };
    
    @Override
    public int getCount() {
        return slide_images.length;
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (CardView) o;
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView imageView = view.findViewById(R.id.ImageV);
        
        imageView.setImageResource(slide_images[position]);
        
        container.addView(view);
   return view;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView)object);
    }
}
