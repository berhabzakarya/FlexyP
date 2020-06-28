package com.dzteamdev.flexyp.Dashboard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Orders;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity {
    private Button next;
    private TextView total;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private double totalAmount;
    private int wallet = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkOrders();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Your Wallet");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        getMyWallet();
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        if (wallet == 0) {
            Toast.makeText(this, "You can't buy this item , check your wallet", Toast.LENGTH_SHORT).show();
        } else {
            if (wallet >= totalAmount) {
                builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CartActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else {
                Toast.makeText(this, "You can't buy this item , check your wallet", Toast.LENGTH_SHORT).show();
            }
        }

        builder.show();
    }

    private void getMyWallet() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(CONSTANTS.user.getMobileNumber()).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallet = Integer.parseInt(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        total = findViewById(R.id.total);
        next = findViewById(R.id.next);
        recyclerView = findViewById(R.id.recycler_view_carts);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(CONSTANTS.user.getMobileNumber())
                .child("Offers");
    }

    private void checkOrders() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    getOrders();
                } else {
                    setContentView(R.layout.empty);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrders() {
        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>().setQuery(databaseReference, Orders.class).build();
        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                        double price = Double.parseDouble(model.getPrice()) * Double.parseDouble(model.getQuantity());
                        holder.name.setText(model.getProductName());
                        Locale locale = new Locale("en", "US");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                        holder.price.setText(numberFormat.format(price));
                        totalAmount = totalAmount + price;
                        total.setText(String.valueOf(totalAmount) + "$");
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, null);
                        return new OrderViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
