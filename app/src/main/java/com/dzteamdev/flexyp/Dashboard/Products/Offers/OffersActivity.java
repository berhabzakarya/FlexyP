package com.dzteamdev.flexyp.Dashboard.Products.Offers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dzteamdev.flexyp.Dashboard.OfferDetailsActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Offers;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.OffersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private Offers offer;
    private FloatingActionButton addOffer;
    private FirebaseRecyclerAdapter<Offers, OffersViewHolder> adapter;

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
        if (CONSTANTS.user.isInStuff()) {
            addOffer.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddOffer();
            }
        });
    }

    private void showDialogAddOffer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_offer, null);
        final MaterialEditText edtName = view.findViewById(R.id.edit_offer_name);
        final MaterialEditText edtPrice = view.findViewById(R.id.edit_offer_price);
        final MaterialEditText edtDescription = view.findViewById(R.id.edit_offer_description);
        Button addOffer = view.findViewById(R.id.add_offer_btn);
        final String name = edtName.getText().toString();
        final String price = edtPrice.getText().toString();
        final String description = edtDescription.getText().toString();
        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.isEmpty()) {
                    edtName.setError("Please type offer name");
                    edtName.requestFocus();
                } else if (price.isEmpty()) {
                    edtPrice.setError("Please type offer price");
                    edtPrice.requestFocus();
                } else if (description.isEmpty()) {
                    edtDescription.setError("Please type offer description");
                    edtDescription.requestFocus();
                } else {
                    addOffer(name, price, description);
                }
            }
        });
        builder.setView(view);
        builder.show();
    }

    private void addOffer(String name, String price, String description) {
        if (type != null) {
            Offers offer = new Offers(name, price, "", description, type);
        } else {
            Offers offer = new Offers(name, price, "", description);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Offers");
        DatabaseReference db;
        if (type != null) {
            db = databaseReference.child(category);
        } else {
            db = databaseReference.child(category).child(type);
        }
        db.setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OffersActivity.this, "Add Offer Successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(OffersActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OffersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        addOffer = findViewById(R.id.add_offers);
        category = getIntent().getStringExtra(CONSTANTS.CATEGORIES);
        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra(CONSTANTS.TYPE);
        }
        db = FirebaseDatabase.getInstance().getReference().child("Offers").child(category).child(type).child("offers");
        recyclerView = findViewById(R.id.recycler_view_offer);
        toolbar = findViewById(R.id.toolbar_offers);
    }

    private void loadOffers() {
        FirebaseRecyclerOptions<Offers> options =
                new FirebaseRecyclerOptions.Builder<Offers>().setQuery(db, Offers.class).build();
        adapter =
                new FirebaseRecyclerAdapter<Offers, OffersViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull OffersViewHolder holder, final int position, @NonNull final Offers model) {
                        holder.name.setText(model.getName());
                        holder.price.setText(model.getPrice());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                offer = model;
                                offer.setId(type);
                                if (CONSTANTS.user.isInStuff()) {
                                    showDialogDeleteItems(getRef(position).getKey());
                                } else {
                                    startActivity(new Intent(OffersActivity.this, OfferDetailsActivity.class)
                                            .putExtra(CONSTANTS.OFFER, offer));
                                    finish();
                                }
                            }
                        });
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

    private void showDialogDeleteItems(final String ref) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Offer");
        builder.setMessage("You Would To Delete Offer ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                db.child(ref).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OffersActivity.this, "Success Deleted Item", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(OffersActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOffers();
    }
}
