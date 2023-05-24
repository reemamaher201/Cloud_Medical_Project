package com.teamRTL.cloudmedicalproject.UIs.MoreActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, profileEmail, profilePhone, profileDate, profileAddress, profileSpecialization;
    private Button saveButton, cancelButton;
    private DatabaseReference databaseReference;
    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AnimationDrawable animationDrawable = (AnimationDrawable) binding.navHeader.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // Initialize views
        profileImage = findViewById(R.id.EditeProfileImage);
        profileName = findViewById(R.id.EditeProfileName);
        profileEmail = findViewById(R.id.EditeProfileEmail);
        profilePhone = findViewById(R.id.EditeProfilePhone);
        profileDate = findViewById(R.id.EditeProfileDate);
        profileAddress = findViewById(R.id.EditeProfileAddress);
        profileSpecialization = findViewById(R.id.EditeProfileSp);
        saveButton = findViewById(R.id.saveEdit);
        cancelButton = findViewById(R.id.cancelEdit);

        // Get reference to Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("user1");

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update user data in the database
                updateProfileData();
                Toast.makeText(EditProfileActivity.this, "تم حفظ التعديلات", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform cancel operation here
                Toast.makeText(EditProfileActivity.this, "تم إلغاء التعديلات", Toast.LENGTH_SHORT).show();
            }
        });

        // Retrieve user data from the database and populate the views
        // Replace the following code with your own data retrieval logic
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve user data from the dataSnapshot and populate the views
                Doctors doctors = dataSnapshot.getValue(Doctors.class);
                if (doctors != null) {
                    //profileImage.setImageResource(doctors.getImage());
                    profileName.setText(doctors.getName());
                    profileEmail.setText(doctors.getEmail());
                    profilePhone.setText(doctors.getPhone());
                    profileDate.setText(doctors.getDate());
                    profileAddress.setText(doctors.getAddress());
                    profileSpecialization.setText(doctors.getSpecialistIn());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(EditProfileActivity.this, "فشل في استرجاع البيانات", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileData() {
        // Retrieve the updated data from the views
        String name = profileName.getText().toString();
        String email = profileEmail.getText().toString();
        String phone = profilePhone.getText().toString();
        String dateOfBirth = profileDate.getText().toString();
        String address = profileAddress.getText().toString();
        String specialistIn = profileSpecialization.getText().toString();

        // Update the user data in the database
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        databaseReference.child("phone").setValue(phone);
        databaseReference.child("date").setValue(dateOfBirth);
        databaseReference.child("address").setValue(address);
        databaseReference.child("specialistIn").setValue(specialistIn);
    }
}
