package com.dzteamdev.flexyp.Dashboard.Products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzteamdev.flexyp.R;

import androidx.fragment.app.Fragment;

public class BeinSport extends Fragment {

    public BeinSport() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bein_sport, container, false);
    }

}
