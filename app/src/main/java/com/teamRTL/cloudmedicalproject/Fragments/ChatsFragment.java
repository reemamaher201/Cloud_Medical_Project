package com.teamRTL.cloudmedicalproject.Fragments;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamRTL.cloudmedicalproject.Adapters.UsersAdapter;
import com.teamRTL.cloudmedicalproject.Models.Chat;
import com.teamRTL.cloudmedicalproject.Models.Users;
import com.teamRTL.cloudmedicalproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    private UsersAdapter userAdapter;
    private List<Users> mUsers;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.chat_rys);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    assert chat != null;
                    if (chat.getSender().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getSender());
                    }
                }
                getChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        return view;
    }
    private void getChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    for (String id : usersList) {
                        assert user != null;
                        if (user.getId().equals(id)) {
                            if (mUsers.size() != 0) {
                                boolean userExists = false;
                                for (Users existingUser : mUsers) {
                                    if (existingUser.getId().equals(user.getId())) {
                                        userExists = true;
                                        break;
                                    }
                                }
                                if (!userExists) {
                                    mUsers.add(user);
                                }
                            }
                            else
                            {
                                mUsers.add(user);
                            }
                        }

                    }
                }
                userAdapter = new UsersAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });


    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap =new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);

    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();

        status("offline");
    }

}
