package com.dzteamdev.flexyp.Dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Requests;
import com.dzteamdev.flexyp.R;
import com.dzteamdev.flexyp.ViewHolder.RequestsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RequestActivity extends AppCompatActivity {
    private FirebaseRecyclerOptions<Requests> options;
    private FirebaseRecyclerAdapter<Requests, RequestsViewHolder> adapter;
    private DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("Requests").child(CONSTANTS.user.getMobileNumber());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        initViews();
    }

    private void initViews() {
        options = new FirebaseRecyclerOptions.Builder<Requests>().setQuery(databaseReference.orderByChild("status").equalTo("pending"), Requests.class).build();
        adapter = new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestsViewHolder holder, final int position, @NonNull Requests model) {
                holder.status.setText(model.getStatus());
                holder.date.setText(model.getDate());
                holder.totalAmount.setText(model.getTotalAmount());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogDeleteItem();
                    }
                });
            }

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_request, null);
                return new RequestsViewHolder(view);
            }
        };
    }

    private void showDialogDeleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Items");
        builder.setMessage("You would to cancel your buying");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RequestActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        finish();
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
}
