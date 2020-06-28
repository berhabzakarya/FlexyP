package com.dzteamdev.flexyp.Dashboard.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzteamdev.flexyp.Dashboard.OfferDetailsActivity;
import com.dzteamdev.flexyp.Model.BS;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.BSViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BeinSport extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private BS offer;

    public BeinSport() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_bein_sport, null);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Offers").child("BS");
        recyclerView = view.findViewById(R.id.recycler_view_beinsport);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return view;
    }

    private void getItems() {
        FirebaseRecyclerOptions<BS> options =
                new FirebaseRecyclerOptions.Builder<BS>().setQuery(databaseReference, BS.class).build();
        FirebaseRecyclerAdapter<BS, BSViewHolder> adapter =
                new FirebaseRecyclerAdapter<BS, BSViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BSViewHolder holder, int position, @NonNull BS model) {
                        offer = model;
                        Picasso.get().load(model.getImg()).into(holder.imgBs);
                        holder.nameBs.setText(model.getName());
                        holder.priceBs.setText("PRICE : " + model.getPrice() + " DZ");
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), OfferDetailsActivity.class)
                                        .putExtra(CONSTANTS.OFFER, offer));
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_beinsport, null);
                        return new BSViewHolder(view);
                    }
                };
    }

}
