package com.dzteamdev.flexyp.Dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Orders;
import com.dzteamdev.flexyp.Model.Requests;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String codePin;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        getMyWallet();
        getCodePine();
        checkOrders();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showSnack(String message) {
        Snackbar.make(findViewById(R.id.layout_cart_view), message, Snackbar.LENGTH_LONG).show();

    }

    private void getMyWallet() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Your Wallet");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Users").child(CONSTANTS.user.getMobileNumber()).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallet = Integer.parseInt(snapshot.getValue(String.class));
                if (wallet == 0) {
                    showSnack("Your wallet is empty : DZD 0.0");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCodePine() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(CONSTANTS.user.getMobileNumber())
                .child("codePin")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        codePin = snapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkOrders() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    getOrders();
                    next.setEnabled(true);
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
                        Locale locale = new Locale("en", "DZ");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                        holder.price.setText(numberFormat.format(price));
                        totalAmount = totalAmount + price;
                        total.setText(String.valueOf(totalAmount) + "DZD");
                        if (wallet < totalAmount) {
                            next.setEnabled(false);
                            showSnack("You can't purchase this item , check your wallet");
                        } else {
                            next.setEnabled(true);
                        }
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_cart, null);
        final MaterialEditText pincode = view.findViewById(R.id.checkPinCode);
        TextView mywallet = view.findViewById(R.id.my_wallet);
        Button button = view.findViewById(R.id.buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pincode.getText().toString().isEmpty()) {
                    if (pincode.getText().toString().equals(codePin)) {
                        setRequest();
                    } else {
                        pincode.setError("Wrong...");
                        pincode.requestFocus();
                    }
                } else {
                    pincode.setError("Please type code pin");
                    pincode.requestFocus();
                }
            }
        });
        mywallet.setText(getString(R.string.my_wallet) + wallet);
        builder.setView(view);
        builder.show();
    }

    private void setRequest() {
        databaseReference.removeValue();
        Date date = Calendar.getInstance().getTime();
        Date time = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
                Locale.ENGLISH);
        String var = date.toString() + " \n " + dateFormat.format(time);
        Requests request = new Requests
                (CONSTANTS.user.getFullName(),
                        String.valueOf(totalAmount),
                        var,
                        "pending");
        FirebaseDatabase.getInstance().getReference()
                .child("Requests")
                .child(CONSTANTS.user.getMobileNumber()).setValue(request)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CartActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CartActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initViews() {
        total = findViewById(R.id.total);
        next = findViewById(R.id.next);
        next.setEnabled(false);
        recyclerView = findViewById(R.id.recycler_view_carts);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(CONSTANTS.user.getMobileNumber())
                .child("Offers");
    }

}
