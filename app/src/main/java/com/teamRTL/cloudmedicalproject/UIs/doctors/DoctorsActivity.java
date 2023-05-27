package com.teamRTL.cloudmedicalproject.UIs.doctors;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamRTL.cloudmedicalproject.Adapters.DoctorsAdapter;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityDoctorsBinding;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {
    ActivityDoctorsBinding binding;
    private DoctorsAdapter adapter;
    private RecyclerView doctorsRecyclerview;
    private List<Doctors> mDoctors;

    private DatabaseReference doctorsRef; // إضافة المرجع لقاعدة البيانات

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        doctorsRecyclerview = binding.doctorsRecyclerview;

        doctorsRecyclerview.setHasFixedSize(true);
        doctorsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDoctors = new ArrayList<>();

        // الحصول على المرجع لقاعدة البيانات
        doctorsRef = FirebaseDatabase.getInstance().getReference("Doctors");

        getAllDoctors();

        binding.searchViewDoc.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchByName(newText);
                return false;
            }
        });
    }

    private void getAllDoctors() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        doctorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDoctors.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Doctors doctors = snapshot.getValue(Doctors.class);
                    assert doctors != null;
                    assert firebaseUser != null;
                    if (!doctors.getId().equals(firebaseUser.getUid())) {
                        mDoctors.add(doctors);
                    }
                }
                adapter = new DoctorsAdapter(getApplicationContext(), mDoctors);
                binding.doctorsRecyclerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Error retrieving doctors from database: " + error.getMessage());
            }
        });
    }

    private void searchByName(String query) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        doctorsRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Doctors> doctorList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctors doctors = snapshot.getValue(Doctors.class);
                            if (doctors != null && firebaseUser != null && !doctors.getId().equals(firebaseUser.getUid())) {
                                doctorList.add(doctors);
                            }
                        }
                        adapter = new DoctorsAdapter(getBaseContext(), doctorList);
                        binding.doctorsRecyclerview.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "Error querying doctors from database: " + error.getMessage());
                    }
                });
    }


}
