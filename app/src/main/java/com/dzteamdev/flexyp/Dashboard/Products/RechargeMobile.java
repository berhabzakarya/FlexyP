package com.dzteamdev.flexyp.Dashboard.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzteamdev.flexyp.Dashboard.Products.Offers.OffersActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class RechargeMobile extends Fragment {

    private View view;
    private CardView ooredoo, djezzy, mobilis;

    public RechargeMobile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_recharge_mobile, null);
        initViews();
        ooredoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), OffersActivity.class)
                        .putExtra(CONSTANTS.CATEGORIES, "RM")
                        .putExtra(CONSTANTS.TYPE, "ooredoo"));
            }
        });
        djezzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), OffersActivity.class)
                        .putExtra(CONSTANTS.CATEGORIES, "RM")
                        .putExtra(CONSTANTS.TYPE, "djezzy"));
            }
        });
        mobilis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), OffersActivity.class)
                        .putExtra(CONSTANTS.CATEGORIES, "RM")
                        .putExtra(CONSTANTS.TYPE, "mobilis"));
            }
        });
        return view;
    }


    private void initViews() {
        ooredoo = view.findViewById(R.id.ooredoo);
        djezzy = view.findViewById(R.id.djezzy);
        mobilis = view.findViewById(R.id.mobilis);
    }


}
