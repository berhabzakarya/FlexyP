package com.dzteamdev.flexyp.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Dashboard.HomeActivity;
import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Users;
import com.dzteamdev.flexyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private MaterialEditText mobilePhone, password;
    private TextView linkToSignUP;
    private CheckBox rememberMe;
    private Button login;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/font.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_login);
        initViews();
        linkToSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobilePhone.getText().toString().isEmpty()) {
                    mobilePhone.setErrorColor(Color.RED);
                    mobilePhone.setError("Please complete form");
                } else if (password.getText().toString().isEmpty()) {
                    password.setErrorColor(Color.RED);
                    password.setError("Please complete form");
                } else {
                    login(mobilePhone.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void initViews() {
        Paper.init(getBaseContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");
        mobilePhone = findViewById(R.id.edtPhoneIN);
        password = findViewById(R.id.edtPasswordIN);
        rememberMe = findViewById(R.id.check_box_remember_me);
        login = findViewById(R.id.btnSignIn);
        linkToSignUP = findViewById(R.id.linkToSignUP);
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
                        if (rememberMe.isChecked()) {
                            Paper.book().write(CONSTANTS.MOBILE, mobileNumber);
                            Paper.book().write(CONSTANTS.PASSWORD, password);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        CONSTANTS.user = user;
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        databaseReference.removeEventListener(this);

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "This phone number is not registered here.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
