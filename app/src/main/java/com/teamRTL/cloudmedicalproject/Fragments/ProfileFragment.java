package com.teamRTL.cloudmedicalproject.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.EditProfileActivity;

public class ProfileFragment extends Fragment {
    private TextView profileName, profileEmail, profilePhone, profileDate, profileAddress, profileSp;
    private ImageView profileEdite;
    private ImageView profileImage,spIm;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileEdite = view.findViewById(R.id.profileEdite);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilePhone = view.findViewById(R.id.profilePhone);
        profileDate = view.findViewById(R.id.profileDate);
        profileAddress = view.findViewById(R.id.profileAddress);
        profileSp = view.findViewById(R.id.profileSp);
        spIm  = view.findViewById(R.id.spIm);
        profileEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images").child(currentUser.getUid() + ".jpg");

        // Load user data and update the views
        databaseReference.child("Doctors").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Doctors doctor = dataSnapshot.getValue(Doctors.class);
                    if (doctor != null) {
                        profileName.setText(doctor.getName());
                        profileDate.setText(doctor.getDate());
                        profileAddress.setText(doctor.getAddress());
                        profilePhone.setText(doctor.getPhone());
                        profileEmail.setText(doctor.getEmail());
                        profileSp.setText(doctor.getSpecialistIn());
                    }
                } else {
                    databaseReference.child("Patients").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Patients patient = dataSnapshot.getValue(Patients.class);
                                if (patient != null) {
                                    profileName.setText(patient.getName());
                                    profileDate.setText(patient.getDate());
                                    profileAddress.setText(patient.getAddress());
                                    profilePhone.setText(patient.getPhone());
                                    profileEmail.setText(patient.getEmail());
                                    profileSp.setVisibility(View.GONE);
                                    spIm.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Load the user's profile image from Firebase Storage using Picasso
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        return view;
    }
}
