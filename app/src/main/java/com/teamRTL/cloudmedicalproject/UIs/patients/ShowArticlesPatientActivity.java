package com.teamRTL.cloudmedicalproject.UIs.patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamRTL.cloudmedicalproject.Adapters.ShowArticleAdapter;
import com.teamRTL.cloudmedicalproject.Models.Articles;
import com.teamRTL.cloudmedicalproject.Models.Category;
import com.teamRTL.cloudmedicalproject.Models.MyListener;
import com.teamRTL.cloudmedicalproject.databinding.ActivityShowArtilesPatientBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

//بتعرض للمريض الموضوعات
public class ShowArticlesPatientActivity extends AppCompatActivity {
    ActivityShowArtilesPatientBinding binding;
    ShowArticleAdapter adapter;
    ArrayList<Articles> myDataList = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    private FirebaseAnalytics mFirebaseAnalytics;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowArtilesPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        category = getIntent().getParcelableExtra("category");

        screenTrack("Show to Patients Screen");
        getData();
        getAdapter();
        clickListener();

    }

    private void clickListener() {

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void getData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionRef = db.collection("Articles");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myDataList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Articles myData = document.toObject(Articles.class);
                        myDataList.add(myData);
                    }

                    adapter = new ShowArticleAdapter(getBaseContext(), myDataList, new MyListener() {
                        @Override
                        public void delete(String id, int pos) {
                        }
                    });

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

        adapter = new ShowArticleAdapter(getBaseContext(), myDataList, new MyListener() {
            @Override
            public void delete(String id, int pos) {
            }

        });


    }

    private void searchByName(String query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference usersRef = db.collection("Articles");
        Query nameQuery = usersRef.whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff");

        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Articles> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Articles user = document.toObject(Articles.class);
                        userList.add(user);
                    }

                    adapter = new ShowArticleAdapter(getBaseContext(), userList, null);
                    binding.recyclerview.setAdapter(adapter);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void cardEvent(String id,String name,String content){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }
    public void screenTrack(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ShowArticlesPatient");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    @Override
    protected void onPause() {

        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2-hour;
        int m = minute2-minute;
        int s = second2-second;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String,Object> screens = new HashMap<>();
        screens.put("name","Show to Patients Screen");
        screens.put("hours",h);
        screens.put("minute",m);
        screens.put("seconds",s);

        db.collection("Show to Patients Screen").add(screens)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        Log.e("Hours",String.valueOf(h));
        Log.e("Minutes",String.valueOf(m));
        Log.e("Seconds",String.valueOf(s));
        super.onPause();
    }
}