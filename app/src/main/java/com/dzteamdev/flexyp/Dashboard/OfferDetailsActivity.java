package com.dzteamdev.flexyp.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Offers;
import com.dzteamdev.flexyp.Model.Orders;
import com.dzteamdev.flexyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OfferDetailsActivity extends AppCompatActivity {
    private FloatingActionButton fabFoobDetails;
    private ElegantNumberButton elegantOfferDetails;
    private TextView name, description, price;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Offers offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        initViews();
        name.setText(offer.getName());
        price.setText("Price : DZD" + offer.getPrice());
        description.setText(offer.getDescription());
        Picasso.get().load(offer.getImg());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedBar);
        fabFoobDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = elegantOfferDetails.getNumber();
                String price = offer.getPrice();
                Orders order = new Orders(offer.getName(), quantity, price, "", offer.getId());
                FirebaseDatabase.getInstance().getReference()
                        .child("Orders")
                        .child(CONSTANTS.user.getMobileNumber())
                        .child("Offers")
                        .child(offer.getName())
                        .setValue(order).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OfferDetailsActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OfferDetailsActivity.this, "Placed In Your Cart", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(OfferDetailsActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }

    private void initViews() {
        name = findViewById(R.id.name_offer_details);
        description = findViewById(R.id.description_offer_details);
        price = findViewById(R.id.price_offer_details);
        fabFoobDetails = findViewById(R.id.fab_offer_details);
        elegantOfferDetails = findViewById(R.id.elegant_offer_details);
        collapsingToolbarLayout = findViewById(R.id.collapsing_offer_details);
        offer = (Offers) getIntent().getExtras().getSerializable(CONSTANTS.OFFER);
    }
}
