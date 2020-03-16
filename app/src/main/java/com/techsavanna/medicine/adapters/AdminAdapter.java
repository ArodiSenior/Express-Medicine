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
import com.techsavanna.medicine.activities.EditItem;
import com.techsavanna.medicine.models.ItemModel;
import com.techsavanna.medicine.R;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private Context mContext;
    private List<ItemModel> itemModel;


    public AdminAdapter(Context mContext, List<ItemModel > itemModel){
        this.mContext = mContext;
        this.itemModel = itemModel;

    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adminraw, viewGroup, false);
        return new AdminViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final AdminViewHolder adminViewHolder, int i) {
        final ItemModel model = itemModel.get(i);

        adminViewHolder.ItemName.setText(model.getUpgitemname());
        adminViewHolder.ItemPrice.setText("Shs" + " " + model.getUpitemprice());
        adminViewHolder.ItemCategory.setText(model.getUpcategory());

        Picasso.with(mContext)
                .load(model.getUpimage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(adminViewHolder.ItemImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(model.getUpimage())
                                .into(adminViewHolder.ItemImage);

                    }
                });

        adminViewHolder.AdminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditItem.class);
                intent.putExtra("ItemEditid", model.getUpitemid());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemModel.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder{
        ImageView ItemImage;
        TextView ItemName;
        TextView ItemPrice;
        TextView ItemCategory;
        ImageView AdminEdit;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName = itemView.findViewById(R.id.ItemName);
            ItemImage = itemView.findViewById(R.id.ItemImage);
            ItemPrice = itemView.findViewById(R.id.ItemPrice);
            ItemCategory = itemView.findViewById(R.id.ItemCategory);
            AdminEdit = itemView.findViewById(R.id.AdminEdit);

        }

    }


}
