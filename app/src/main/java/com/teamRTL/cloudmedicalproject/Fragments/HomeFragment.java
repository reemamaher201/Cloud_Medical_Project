package com.teamRTL.cloudmedicalproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamRTL.cloudmedicalproject.Adapters.CategoryAdapter;
import com.teamRTL.cloudmedicalproject.Models.Category;
import com.teamRTL.cloudmedicalproject.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

    // إضافة المتغيرات اللازمة لـ Firebase Firestore
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.gridRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(adapter);

        // قم بتهيئة FirebaseFirestore لاسترداد البيانات من Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // استرداد البيانات وتحديث قائمة الفئات
                            categoryList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                categoryList.add(category);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // إدراج إجراء معالجة الخطأ المناسب هنا
                        }
                    }
                });

        return view;
    }
}
