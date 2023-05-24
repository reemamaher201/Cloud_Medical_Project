package com.teamRTL.cloudmedicalproject.UIs.MoreActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.teamRTL.cloudmedicalproject.Adapters.ContactAdapter;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;

import com.teamRTL.cloudmedicalproject.R;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsActivity extends AppCompatActivity {

DatabaseReference databaseRef;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Object> contactList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("قائمة المستخدمين");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
// Initialize RecyclerView
        recyclerView = findViewById(R.id.friendsUsersRyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize contact list
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(contactAdapter);


        getAllUsers();


    }


    private void getAllUsers() {

        databaseRef.child("Patients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                    String name = patientSnapshot.child("name").getValue(String.class);
                    String image = patientSnapshot.child("image").getValue(String.class);

                    Patients patient = new Patients(name, image);
                    contactList.add(patient);
                }
                contactAdapter.notifyDataSetChanged();
                // استخدم patientList لعرض البيانات في قائمة RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // تعامل مع خطأ الاسترجاع إذا لزم الأمر
            }
        });

        databaseRef.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    String name = doctorSnapshot.child("name").getValue(String.class);
                    String image = doctorSnapshot.child("image").getValue(String.class);

                    Doctors doctor = new Doctors(name, image);
                    contactList.add(doctor);
                }
                contactAdapter.notifyDataSetChanged();
                // استخدم doctorList لعرض البيانات في قائمة RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // تعامل مع خطأ الاسترجاع إذا لزم الأمر
            }
        });


    }


}


