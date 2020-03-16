package com.techsavanna.medicine.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techsavanna.medicine.models.OrderModel;
import com.techsavanna.medicine.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;
    private List<OrderModel> orderModel;


    public CartAdapter(Context mContext, List<OrderModel > orderModel){
        this.mContext = mContext;
        this.orderModel = orderModel;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cartraw, viewGroup, false);
        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i) {
        final OrderModel model = orderModel.get(i);
//        TextDrawable textDrawable = TextDrawable.builder()
//                .buildRound("" + orderModel.get(i).getQuantity(), Color.RED);
//        cartViewHolder.CartCount.setImageDrawable(textDrawable);

        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(orderModel.get(i).getPrice())) * (Integer.parseInt(orderModel.get(i).getQuantity()));
        cartViewHolder.CartItemPrice.setText(numberFormat.format(price));
        cartViewHolder.CartItemName.setText(orderModel.get(i).getProductName());
        cartViewHolder.CartItemCategory.setText("Qty" + " " + orderModel.get(i).getQuantity());
        Picasso.with(mContext).load(orderModel.get(i).getImage()).into(cartViewHolder.CartItemImage);


    }

    @Override
    public int getItemCount() {
        return orderModel.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView CartItemName;
        TextView CartItemPrice;
        TextView CartItemCategory;
        ImageView CartItemImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            CartItemName = itemView.findViewById(R.id.CartItemName);
            CartItemPrice = itemView.findViewById(R.id.CartItemPrice);
            CartItemCategory = itemView.findViewById(R.id.CartItemCategory);
            CartItemImage = itemView.findViewById(R.id.CartItemImage);

        }

    }


}
