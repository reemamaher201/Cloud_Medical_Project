package com.teamRTL.cloudmedicalproject.UIs.doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        doctorsRecyclerview = binding.doctorsRecyclerview;

        doctorsRecyclerview.setHasFixedSize(true);
        doctorsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDoctors = new ArrayList<>();

        getAllDoctors();



//        binding.searchViewDoc.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchByName(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchByName(newText);
//                return false;
//            }
//        });
    }
    private void getAllDoctors() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");

        reference.addValueEventListener(new ValueEventListener() {
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

            }
        });
    }




//    private void searchByName(String query) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        CollectionReference usersRef = db.collection("Doctors");
//        Query nameQuery = usersRef.whereGreaterThanOrEqualTo("name", query)
//                .whereLessThanOrEqualTo("name", query + "\uf8ff");
//
//        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<Doctors> doctorList = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Doctors doctors = document.toObject(Doctors.class);
//                        doctorList.add(doctors);
//                    }
//
//                    adapter = new DoctorsAdapter(getBaseContext(),doctorList);
//                    binding.doctorsRecyclerview.setAdapter(adapter);
//                } else {
//                    Log.d("TAG", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//    }
}