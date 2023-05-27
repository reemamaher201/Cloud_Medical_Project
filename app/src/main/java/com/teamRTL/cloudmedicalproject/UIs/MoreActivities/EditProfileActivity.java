package com.teamRTL.cloudmedicalproject.UIs.MoreActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.teamRTL.cloudmedicalproject.Fragments.ProfileFragment;
import com.teamRTL.cloudmedicalproject.R;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private EditText userNameEditText, userEmailEditText, userPhoneEditText, userAddressEditText, userDateEditText, sp;
    private RoundedImageView profileImageView;
    private Button updateSettingsButton;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private StorageReference storageProfilePicsRef;
    private ProgressBar progressBar;
    private String currentUserID;
    private static final int GALLERY_PICK = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        userEmailEditText = findViewById(R.id.EditeProfileEmail);
        userNameEditText = findViewById(R.id.EditeProfileName);
        userDateEditText = findViewById(R.id.EditeProfileDate);
        userAddressEditText = findViewById(R.id.EditeProfileAddress);
        userPhoneEditText = findViewById(R.id.EditeProfilePhone);
        profileImageView = findViewById(R.id.EditeProfileImage);
        sp = findViewById(R.id.EditeProfileSp);
        updateSettingsButton = findViewById(R.id.saveEdit);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("profile_images");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });

        updateSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userNameEditText.getText().toString();
                String date = userDateEditText.getText().toString();
                String address = userAddressEditText.getText().toString();
                String phone = userPhoneEditText.getText().toString();
                String email = userEmailEditText.getText().toString();
                String sp1 = sp.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(EditProfileActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    updateAccountInfo(username, date, address, phone, email, sp1);
                }
            }
        });

        RetrieveUserInfo();
    }

    private void updateAccountInfo(final String username, final String date, final String address, final String phone, final String email, final String specialistIn) {
        progressBar.setVisibility(View.VISIBLE);
        final DatabaseReference userRef = rootRef.child("Doctors").child(currentUserID);

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(currentUserID + ".jpg");
            UploadTask uploadTask = fileRef.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            String photoUrl = downloadUri.toString();
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("id", currentUserID);
                            userMap.put("name", username);
                            userMap.put("date", date);
                            userMap.put("address", address);
                            userMap.put("phone", phone);
                            userMap.put("email", email);
                            userMap.put("specialistIn", specialistIn);
                            userMap.put("image", photoUrl);

                            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Profile info updated successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(EditProfileActivity.this, ProfileFragment.class));
                                        finish();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, "Error occurred, please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Error occurred, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("id", currentUserID);
            userMap.put("name", username);
            userMap.put("date", date);
            userMap.put("address", address);
            userMap.put("phone", phone);
            userMap.put("email", email);
            userMap.put("specialistIn", specialistIn);

            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile info updated successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfileActivity.this, ProfileFragment.class));
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Error occurred, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void RetrieveUserInfo() {
        DatabaseReference userRef = rootRef.child("Doctors").child(currentUserID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String sp3 = dataSnapshot.child("specialistIn").getValue(String.class);

                    userNameEditText.setText(name);
                    userDateEditText.setText(date);
                    userAddressEditText.setText(address);
                    userPhoneEditText.setText(phone);
                    userEmailEditText.setText(email);
                    sp.setText(sp3);
                    if (!TextUtils.isEmpty(image)) {
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

}
