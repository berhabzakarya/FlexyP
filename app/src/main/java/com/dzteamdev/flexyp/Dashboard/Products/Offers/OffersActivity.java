package com.dzteamdev.flexyp.Dashboard.Products.Offers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Offers;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.OffersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OffersActivity extends AppCompatActivity {
    private String type;
    private String category;
    private DatabaseReference db;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/font.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_offers);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        category = getIntent().getStringExtra(CONSTANTS.CATEGORIES);
        type = getIntent().getStringExtra(CONSTANTS.TYPE);
        db = FirebaseDatabase.getInstance().getReference().child("Offers").child(category).child(type).child("offers");
        recyclerView = findViewById(R.id.recycler_view_offer);
        toolbar = findViewById(R.id.toolbar_offers);
    }
    private void loadOffers() {
        FirebaseRecyclerOptions<Offers> options =
                new FirebaseRecyclerOptions.Builder<Offers>().setQuery(db, Offers.class).build();
        FirebaseRecyclerAdapter<Offers, OffersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Offers, OffersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OffersViewHolder holder, int position, @NonNull Offers model) {
                        holder.name.setText(model.getName());
                        holder.price.setText(model.getPrice());
                    }

                    @NonNull
                    @Override
                    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, null);
                        return new OffersViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOffers();
    }
}
