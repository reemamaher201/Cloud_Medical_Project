package com.teamRTL.cloudmedicalproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamRTL.cloudmedicalproject.Adapters.CategoryAdapter;
import com.teamRTL.cloudmedicalproject.MainActivity;
import com.teamRTL.cloudmedicalproject.Models.Category;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.UIs.doctors.ShowArticlesActivity;

import com.teamRTL.cloudmedicalproject.UIs.patients.ShowArticlesPatientActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private FirebaseUser firebaseUser;

    // إضافة المتغيرات اللازمة لـ Firebase Firestore
    private FirebaseFirestore db;
    CardView card;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    private FirebaseAnalytics mFirebaseAnalytics;
    Intent intent1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        card = view.findViewById(R.id.card);
        recyclerView = view.findViewById(R.id.gridRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryList, this);
        recyclerView.setAdapter(adapter);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        screenTrack("Home Screen");

        // قم بتهيئة FirebaseFirestore لاسترداد البيانات من Firestore
        db = FirebaseFirestore.getInstance();

        // التحقق من نوع المستخدم وتنفيذ الإجراء المناسب


        db.collection("Categories").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("tag", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String id = documentSnapshot.getId();
                                    String name = documentSnapshot.getString("title");
                                    String icon = documentSnapshot.getString("icon");
                                    Category cat = new Category(icon, name, id);
                                    categoryList.add(cat);
                                    adapter.notifyDataSetChanged();
                                    Log.e("LogDATA", categoryList.toString());
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LogDATA", "get failed with ");
                    }
                });

        return view;
    }

    public void cardEvent(String id, String name, String content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Home Screen");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    @Override
    public void onPause() {
        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = minute2 - minute;
        int s = second2 - second;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> screens = new HashMap<>();
        screens.put("name", "Home Screen");
        screens.put("hours", h);
        screens.put("minute", m);
        screens.put("seconds", s);

        db.collection("Home Screen").add(screens)
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
        Log.e("Hours", String.valueOf(h));
        Log.e("Minutes", String.valueOf(m));
        Log.e("Seconds", String.valueOf(s));
        super.onPause();
    }

    @Override
    public void onItemClick(int position, String id) {
      Category category = new Category();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(firebaseUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // إذا كان المستخدم مريضًا، قم بتوجيهه إلى واجهة المريض
                        intent1 = new Intent(getActivity(), ShowArticlesPatientActivity.class);
                        intent1.putExtra("category",category.getTitle() );
                        startActivity(intent1);
                    } else {
                        // إذا كان المستخدم طبيبًا، قم بتوجيهه إلى واجهة الطبيب
                        intent1 = new Intent(getActivity(), ShowArticlesActivity.class);
                        intent1.putExtra("category", category.getTitle());
                        startActivity(intent1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
        cardEvent(id, "Category Button", categoryList.get(position).getTitle());
    }


}
