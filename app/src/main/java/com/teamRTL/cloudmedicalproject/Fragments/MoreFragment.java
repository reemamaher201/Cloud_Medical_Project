package com.teamRTL.cloudmedicalproject.Fragments;



import androidx.appcompat.app.AlertDialog;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.AboutActivity;
import com.teamRTL.cloudmedicalproject.Adapters.MoreAdapter;
import com.teamRTL.cloudmedicalproject.Models.Item;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.NotificationActivity;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.UIs.Auth.LoginActivity;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.ContactsActivity;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.FindFriendsActivity;
import com.teamRTL.cloudmedicalproject.UIs.MoreActivities.RequestsActivity;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends Fragment {
    private RecyclerView recyclerView;
    private MoreAdapter adapter;
    private List<Item> itemList;
    AppCompatButton cancelButton;
    AppCompatButton logoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemList = createItemList(); // Call method to create your list of items
        adapter = new MoreAdapter(itemList);
        recyclerView.setAdapter(adapter);

// تعيين حدث النقر على الكارد
        adapter.setOnItemClickListener(new MoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item clickedItem = itemList.get(position);

                switch (position){
                    case 0:
                        Intent intent0 = new Intent(getActivity(), ContactsActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), FindFriendsActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), RequestsActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), NotificationActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), AboutActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        openDialog();
                        break;
                }

                Toast.makeText(getActivity(), "تم النقر على الكارد " + clickedItem.getText(), Toast.LENGTH_SHORT).show();
                Log.e("possssssss", String.valueOf(position));
            }
        });


        return view;
    }

    private List<Item> createItemList() {
        List<Item> itemList = new ArrayList<>();

        // Add your items to the list

        itemList.add(new Item("جهات الاتصال",R.drawable.users));
        itemList.add(new Item("أضف جهة اتصال",R.drawable.baseline_person_search_24));
        itemList.add(new Item("الطلبات المرسلة",R.drawable.add_friends));
        itemList.add(new Item("الاشعارات",R.drawable.baseline_notifications_24));
        itemList.add(new Item("من نحن",R.drawable.about));
        itemList.add(new Item("تسجيل خروج",R.drawable.baseline_logout_24));
        return itemList;
    }


    private void openDialog(){

            // إنشاء مربع الحوار
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(R.layout.logout_dialog);
            AlertDialog dialog = builder.create();

            // عرض مربع الحوار
            dialog.show();

            // استرجاع مراجع الأزرار بعد عرض مربع الحوار
            cancelButton = dialog.findViewById(R.id.cancel);
            logoutButton = dialog.findViewById(R.id.out);

            // إضافة استمع النقر لزر الإلغاء
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // قم بإغلاق مربع الحوار
                    dialog.dismiss();
                }
            });

            // إضافة استمع النقر لزر تسجيل الخروج
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // أدخل هنا الإجراء المطلوب عند النقر على زر تسجيل الخروج
                    // مثلاً قد تقوم بتسجيل الخروج من التطبيق
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            });
        }




}