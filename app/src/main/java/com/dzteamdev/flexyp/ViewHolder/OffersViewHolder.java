package com.dzteamdev.flexyp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.dzteamdev.flexyp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OffersViewHolder extends RecyclerView.ViewHolder {
    public TextView name,price;
    public OffersViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_offer);
        price = itemView.findViewById(R.id.price_offer);
    }
}
