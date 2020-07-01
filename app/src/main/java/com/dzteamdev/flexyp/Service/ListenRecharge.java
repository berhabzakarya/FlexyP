package com.dzteamdev.flexyp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.dzteamdev.flexyp.Dashboard.MyRequestActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Orders;
import com.dzteamdev.flexyp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ListenRecharge extends Service implements ChildEventListener {
    private DatabaseReference databaseReference;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Orders").child(CONSTANTS.user.getMobileNumber());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseReference.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(Orders orders) {
        Intent intent = new Intent(getBaseContext(), MyRequestActivity.class);
        intent.putExtra(CONSTANTS.TYPE, orders.getType());
        intent.putExtra(CONSTANTS.NAME_PRODUCT, orders.getProductName());
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(getBaseContext());
        notificationCompat.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Recharge Mobile")
                .setContentInfo("Your order was updated")
                .setContentText("Order #" + orders.getProductName() + "was updated ")
                .setContentIntent(pendingIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationCompat.build();
        notificationManager.notify("", 1, notificationCompat.build());
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Orders order = snapshot.getValue(Orders.class);
        showNotification(order);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
