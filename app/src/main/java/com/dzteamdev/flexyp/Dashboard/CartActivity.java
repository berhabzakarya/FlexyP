package com.dzteamdev.flexyp.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.dzteamdev.flexyp.R;

public class CartActivity extends AppCompatActivity {
    private Button next;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
    }

    private void initViews() {
        next = findViewById(R.id.next);
        recyclerView = findViewById(R.id.recycler_view_carts);
    }

}
