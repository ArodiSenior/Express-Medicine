package com.techsavanna.medicine.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techsavanna.medicine.models.ItemModel;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.activities.Detail;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<ItemModel> itemModel;


    public ItemAdapter(Context mContext, List<ItemModel > itemModel){
        this.mContext = mContext;
        this.itemModel = itemModel;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemraw, viewGroup, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
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

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ItemImage;
        TextView ItemName;
        TextView ItemPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName = itemView.findViewById(R.id.ItemName);
            ItemImage = itemView.findViewById(R.id.ItemImage);
            ItemPrice = itemView.findViewById(R.id.ItemPrice);

        }

    }


}
