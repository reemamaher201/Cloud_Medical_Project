package com.teamRTL.cloudmedicalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamRTL.cloudmedicalproject.Fragments.ChatsFragment;
import com.teamRTL.cloudmedicalproject.Fragments.HomeFragment;
import com.teamRTL.cloudmedicalproject.Fragments.MoreFragment;
import com.teamRTL.cloudmedicalproject.Fragments.ProfileFragment;
import com.teamRTL.cloudmedicalproject.UIs.Auth.LoginActivity;
import com.teamRTL.cloudmedicalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
Intent intent = getIntent();
        String p = intent.getStringExtra("p");
        String d = intent.getStringExtra("d");
        if (value == p){
            binding.fab.setVisibility(View.GONE);
        }else if (value == d){
            binding.fab.setVisibility(View.VISIBLE);
        }

        binding.bottomNav.setBackground(null);
        replaceFragment(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeFrag:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.profileFrag:
                    replaceFragment(new ProfileFragment());
                    break;

                case R.id.moreFrag:
                    replaceFragment(new MoreFragment());
                    break;

                case R.id.chatFrag:
                    replaceFragment(new ChatsFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            sendUserToLoginActivity();
        } else {
            VerifyUserExistence();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            //updateUserStatus("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //updateUserStatus("offline");
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void VerifyUserExistence() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("name").exists()) {
                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    } else {
                        sendUserToSettingsActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void sendUserToSettingsActivity() {
        // Implement your logic to navigate to the settings activity
    }
}
