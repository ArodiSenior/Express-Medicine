package com.techsavanna.medicine.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.Detail;
import com.techsavanna.medicine.models.ItemModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CartegoryViewHolder> {

    private Context mContext;
    private List<ItemModel> itemModel;


    public CategoryAdapter(Context mContext, List<ItemModel > itemModel){
        this.mContext = mContext;
        this.itemModel = itemModel;

    }

    @NonNull
    @Override
    public CartegoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categoryraw, viewGroup, false);
        return new CartegoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CartegoryViewHolder itemViewHolder, int i) {
        final ItemModel model = itemModel.get(i);

        itemViewHolder.ItemName.setText(model.getUpgitemname());
        itemViewHolder.ItemPrice.setText("Shs" + " " + model.getUpitemprice());

        Picasso.with(mContext)
                .load(model.getUpimage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(itemViewHolder.ItemImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(model.getUpimage())
                                .into(itemViewHolder.ItemImage);

                    }
                });



        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Detail.class);
                intent.putExtra("ItemDetailid", model.getUpitemid());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemModel.size();
    }

    public class CartegoryViewHolder extends RecyclerView.ViewHolder{
        ImageView ItemImage;
        TextView ItemName;
        TextView ItemPrice;

        public CartegoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName = itemView.findViewById(R.id.ItemName);
            ItemImage = itemView.findViewById(R.id.ItemImage);
            ItemPrice = itemView.findViewById(R.id.ItemPrice);

        }

    }


}
