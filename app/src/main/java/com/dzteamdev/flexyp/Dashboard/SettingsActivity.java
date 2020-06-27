package com.dzteamdev.flexyp.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dzteamdev.flexyp.Model.CONSTANTS;
import com.dzteamdev.flexyp.Model.Users;
import com.dzteamdev.flexyp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {
    private MaterialEditText fullname, email, phone;
    private TextView upadate_img;
    private Button verify, update;
    private AlertDialog.Builder builder;
    private DatabaseReference db;
    private ProgressDialog progressDialog;
    private StorageReference ref;
    private Uri uriImage;
    private CircleImageView circleImageView;
    private String downloadFileUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        fullname.setText(CONSTANTS.user.getFullName());
        phone.setText(CONSTANTS.user.getMobileNumber());
        if (CONSTANTS.user.getImg() != null) {
            Picasso.get().load(Uri.parse(CONSTANTS.user.getImg())).into(circleImageView);
        }
        upadate_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uriImage)
                        .setAspectRatio(1, 1).start(SettingsActivity.this);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuilder();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CONSTANTS.user.getVerifyNumber().equals("false")) {
                    Toast.makeText(SettingsActivity.this, "You must to verify your number before update data", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            uriImage = result.getUri();
            circleImageView.setImageURI(uriImage);
            uploadImage();
        } else {
            Toast.makeText(this, "Error , Try again", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void uploadImage() {
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait , while we are updating your account information...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (uriImage != null) {
            final StorageReference storageReference = ref.child(uriImage.getLastPathSegment());
            UploadTask uploadTask = storageReference.putFile(uriImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    String exception = e.getMessage();
                    Toast.makeText(SettingsActivity.this, "Error : " + exception, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    } else {
                        downloadFileUrl = storageReference.getDownloadUrl().toString();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    downloadFileUrl = task.getResult().toString();
                    Toast.makeText(SettingsActivity.this, "Profile Updated  ...", Toast.LENGTH_SHORT).show();


                    saveImageToDatabase();
                    finish();
                }
            });
        }
    }

    private void saveImageToDatabase() {
        final HashMap<String, Object> user = new HashMap<>();
        user.put("img", downloadFileUrl);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(CONSTANTS.user.getMobileNumber()).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Users")
                            .child(CONSTANTS.user.getMobileNumber())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users user = snapshot.getValue(Users.class);
                                    CONSTANTS.user = user;
                                    Toast.makeText(SettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                } else {
                    Toast.makeText(SettingsActivity.this, "Error occured when save img to databse , " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.view_send_code, null);
        MaterialEditText editText = view.findViewById(R.id.codeSent);
        Button verify = view.findViewById(R.id.verifyCode);
        Button resend = view.findViewById(R.id.resendCode);
        builder.setView(view);
        builder.show();
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Update INFO");
        builder.setMessage("You would to change your info ?");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.check_pin_code, null);
        builder.setView(view);
        final MaterialEditText editText = view.findViewById(R.id.codePinSettings);
        Button button = view.findViewById(R.id.checkCode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Updating ...");
                progressDialog.setMessage("Please wait while updating info ...");

                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Please type your pin code");
                    editText.requestFocus();
                } else
                    checkPinCode(editText.getText().toString());
            }
        });
        builder.show();
    }

    private void checkPinCode(final String pin) {
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Users").child(CONSTANTS.user.getMobileNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pinCode = snapshot.child("codePin").getValue(String.class);
                if (pin.equals(pinCode)) {
                    updateINFO();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this, "Code Pin Wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateINFO() {
        Map<String, Object> map = new HashMap<>();
        map.put(CONSTANTS.EMAIL, email.getText().toString());
        map.put(CONSTANTS.MOBILE, phone.getText().toString());
        map.put(CONSTANTS.FULL_NAME, fullname.getText().toString());
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(CONSTANTS.user.getMobileNumber());
        db.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            Users user = snapshot.getValue(Users.class);
                            CONSTANTS.user = user;
                            Toast.makeText(SettingsActivity.this, "Update Info Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this, "" + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SettingsActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initViews() {
        circleImageView = findViewById(R.id.profileImg);
        upadate_img = findViewById(R.id.update_img);
        progressDialog = new ProgressDialog(SettingsActivity.this);
        ref = FirebaseStorage.getInstance().getReference().child("Pictures");
        verify = findViewById(R.id.verifyNumber);
        update = findViewById(R.id.update_settings);
        fullname = findViewById(R.id.fullname_settings);
        email = findViewById(R.id.email_settings);
        phone = findViewById(R.id.phonenumber_settings);
    }
}
