package com.dzteamdev.flexyp.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Orders;
import com.dzteamdev.flexyp.Model.Requests;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRequestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    // Request Recharge Mobile
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        initViews();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMyRequest();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_my_request);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(CONSTANTS.user.getMobileNumber());
    }

    private void getMyRequest() {
        FirebaseRecyclerOptions<Requests> options = new FirebaseRecyclerOptions.Builder<Requests>().setQuery(databaseReference, Requests.class).build();
        FirebaseRecyclerAdapter<Requests, RequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestViewHolder holder, final int position, @NonNull final Requests model) {
                        holder.date.setText(model.getDate());
                        holder.price.setText(model.getTotalAmount());
                        holder.name.setText(model.getFullName());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<Orders> list = model.getList();
                                showDialogRequest(list.get(position).getType(), list.get(position).getPrice());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_request, parent);
                        return new RequestViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showDialogRequest(String type, String price) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Recharge").child(type).child(price);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.view_request, null);
        final TextView order = view.findViewById(R.id.order_request);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        builder.show();
    }
}
