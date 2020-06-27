package com.dzteamdev.flexyp.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.Users;
import com.dzteamdev.flexyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    private TextView linkToSignIn;
    private MaterialEditText edtPhone, edtName, edtPassword, edtCodePin;
    private Button signUp;
    private ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/font.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_register);
        initViews();
        linkToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtName.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())
                        || TextUtils.isEmpty(edtCodePin.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Please Complete Form", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.show();
                    signUP();
                }
            }
        });

    }

    private void signUP() {
        Users user = new Users(edtPhone.getText().toString(),
                edtName.getText().toString(), edtPassword.getText().toString()
                , edtCodePin.getText().toString(), "false");
        FirebaseDatabase.getInstance().getReference().child("Users").child(edtPhone.getText().toString()).setValue(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Register Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void initViews() {
        edtPhone = findViewById(R.id.edtPhoneUP);
        edtName = findViewById(R.id.edtNameUP);
        edtPassword = findViewById(R.id.edtPasswordUP);
        edtCodePin = findViewById(R.id.edtCodePin);
        linkToSignIn = findViewById(R.id.linkToSignIn);
        signUp = findViewById(R.id.btnSignUP);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registration ... \n Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

}
