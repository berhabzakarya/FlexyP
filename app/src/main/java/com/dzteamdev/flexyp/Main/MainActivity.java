package com.dzteamdev.flexyp.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dzteamdev.flexyp.Dashboard.HomeActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Users;
import com.dzteamdev.flexyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private String mobile, password;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/font.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);
        loadSplash();
        Paper.init(getBaseContext());

    }

    private void loadSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mobile = Paper.book().read(CONSTANTS.MOBILE);
                password = Paper.book().read(CONSTANTS.PASSWORD);
                if (mobile != null && password != null) {
                    login(mobile, password);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, CONSTANTS.DISPLAY_LENGTH);
    }

    private void login(final String mobileNumber, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waiting ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        databaseReference.child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    assert user != null;
                    if (password.equals(user.getPassword())) {
                        progressDialog.dismiss();
                        CONSTANTS.user = user;
                        user.setMobileNumber(mobileNumber);
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                        databaseReference.removeEventListener(this);
                    } else {
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
