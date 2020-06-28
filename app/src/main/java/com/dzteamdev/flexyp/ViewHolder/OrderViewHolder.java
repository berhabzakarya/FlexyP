package com.dzteamdev.flexyp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.dzteamdev.flexyp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView name, price;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.cart_name_offer);
        price = itemView.findViewById(R.id.cart_price_offer);
    }
}
