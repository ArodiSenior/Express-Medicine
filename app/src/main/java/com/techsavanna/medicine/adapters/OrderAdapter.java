package com.techsavanna.medicine.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techsavanna.medicine.models.OrderModel;
import com.techsavanna.medicine.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.CartViewHolder> {

    private Context mContext;
    private List<OrderModel> orderModel;


    public OrderAdapter(Context mContext, List<OrderModel > orderModel){
        this.mContext = mContext;
        this.orderModel = orderModel;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ordersraw, viewGroup, false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return orderModel.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView OrderItemId;
        TextView OrderItemAddress;
        TextView OrderItemStatus;
        TextView OrderItemPhone;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            OrderItemId = itemView.findViewById(R.id.OrderItemId);
            OrderItemAddress = itemView.findViewById(R.id.OrderItemAddress);
            OrderItemStatus = itemView.findViewById(R.id.OrderItemStatus);
            OrderItemPhone = itemView.findViewById(R.id.OrderItemPhone);

        }

    }


}
