package com.dzteamdev.flexyp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.dzteamdev.flexyp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsViewHolder extends RecyclerView.ViewHolder {
    public TextView totalAmount, date, status;

    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);
        totalAmount = itemView.findViewById(R.id.total_amount);
        date = itemView.findViewById(R.id.date);
        status = itemView.findViewById(R.id.status);
    }
}
