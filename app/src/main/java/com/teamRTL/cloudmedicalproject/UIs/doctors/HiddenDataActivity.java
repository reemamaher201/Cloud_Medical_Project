package com.teamRTL.cloudmedicalproject.UIs.doctors;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamRTL.cloudmedicalproject.Adapters.HiddenDataAdapter;
import com.teamRTL.cloudmedicalproject.Models.Articles;
import com.teamRTL.cloudmedicalproject.databinding.ActivityHiddenDataBinding;

import java.util.ArrayList;

//للعناصر المخفية
public class HiddenDataActivity extends AppCompatActivity {
    ActivityHiddenDataBinding binding;
    HiddenDataAdapter adapter;
    ArrayList<Articles> myDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHiddenDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        getAdapter();
        clickListener();
    }

    private void clickListener() {
        binding.back.setOnClickListener(View -> {
            finish();
        });
    }

    private void getData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionRef = db.collection("Hidden");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myDataList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Articles myData = document.toObject(Articles.class);
                        myDataList.add(myData);
                    }

                    adapter = new HiddenDataAdapter(getBaseContext(),myDataList,null);

                    binding.recyclerview.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("TAG", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    private void getAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);

        adapter = new HiddenDataAdapter(getBaseContext(), myDataList,null);


    }

}