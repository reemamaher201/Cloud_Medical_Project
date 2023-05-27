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
import com.teamRTL.cloudmedicalproject.UIs.doctors.ShowArticlesActivity;
import com.teamRTL.cloudmedicalproject.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private boolean isActivityRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowArticlesActivity.class);
                startActivity(intent);
            }
        });

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
        isActivityRunning = true;
        if (firebaseUser == null) {
            sendUserToLoginActivity();
        } else {
            checkUserExistence();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            status("online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            status("offline");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            status("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            status("offline");
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

    private void checkUserExistence() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("name").exists()) {
                        Toast.makeText(MainActivity.this, "Welcome " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                        binding.fab.setVisibility(View.GONE); // إخفاء العنصر إذا كان المستخدم مريضًا
                    } else {
                        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors").child(userId);
                        doctorRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    binding.fab.setVisibility(View.VISIBLE); // إظهار العنصر إذا كان المستخدم طبيبًا
                                    Intent intent = new Intent(MainActivity.this, HomeFragment.class);
                                    intent.putExtra("isDoctor", true);
                                    replaceFragment(new HomeFragment());
                                } else {
                                    binding.fab.setVisibility(View.GONE);
                                    Intent intent = new Intent(MainActivity.this, HomeFragment.class);
                                    intent.putExtra("isDoctor", false);// إخفاء العنصر في الحالة الأخرى
                                    replaceFragment(new HomeFragment());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // التعامل مع أخطاء قاعدة البيانات
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // التعامل مع أخطاء قاعدة البيانات
                }
            });
        }
    }

    private void sendUserToSettingsActivity() {
        // قم بتنفيذ منطقك للانتقال إلى شاشة الإعدادات
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

}