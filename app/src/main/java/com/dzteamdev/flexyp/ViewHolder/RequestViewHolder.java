package com.dzteamdev.flexyp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.dzteamdev.flexyp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public TextView name, price, date;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_offer_myrequest);
        price = itemView.findViewById(R.id.price_myrequest);
        date = itemView.findViewById(R.id.date_my_request);
    }
}
