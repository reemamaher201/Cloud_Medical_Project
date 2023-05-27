package com.teamRTL.cloudmedicalproject.UIs.patients;
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
import com.teamRTL.cloudmedicalproject.Adapters.PatientsAdapter;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityDoctorsBinding;
import com.teamRTL.cloudmedicalproject.databinding.ActivityPatientsBinding;

import java.util.ArrayList;
import java.util.List;

public class PatientsActivity extends AppCompatActivity {
    ActivityPatientsBinding binding;
    private PatientsAdapter adapter;
    private RecyclerView doctorsRecyclerview;
    private List<Patients> mPatients;

    private DatabaseReference doctorsRef; // إضافة المرجع لقاعدة البيانات

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        doctorsRecyclerview = binding.doctorsRecyclerview;

        doctorsRecyclerview.setHasFixedSize(true);
        doctorsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mPatients = new ArrayList<>();

        // الحصول على المرجع لقاعدة البيانات
        doctorsRef = FirebaseDatabase.getInstance().getReference("Patients");

        getAllPatients();

        binding.searchViewpa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void getAllPatients() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        doctorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPatients.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Patients patients = snapshot.getValue(Patients.class);
                    assert patients != null;
                    assert firebaseUser != null;
                    if (!patients.getId().equals(firebaseUser.getUid())) {
                        mPatients.add(patients);
                    }
                }
                adapter = new PatientsAdapter(getApplicationContext(), mPatients);
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
                        List<Patients> patientsList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Patients patients = snapshot.getValue(Patients.class);
                            if (patients != null && firebaseUser != null && !patients.getId().equals(firebaseUser.getUid())) {
                                patientsList.add(patients);
                            }
                        }
                        adapter = new PatientsAdapter(getBaseContext(), patientsList);
                        binding.doctorsRecyclerview.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("TAG", "Error querying doctors from database: " + error.getMessage());
                    }
                });
    }
}
