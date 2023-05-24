package com.teamRTL.cloudmedicalproject.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.teamRTL.cloudmedicalproject.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private String receiverId, senderUserId, currentState;
    private CircleImageView visitProfile;
    private TextView visitName, visitStatus;
    private Button requestButton, declineButton;
    private FirebaseAuth mAuth;
    private DatabaseReference ref, chatRequestRef, contactsRef, notificationRef;

  //  @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        mAuth = FirebaseAuth.getInstance();
//        senderUserId = mAuth.getCurrentUser().getUid();
//
//        receiverId = getArguments().getString("visit_user_id");
//
//        visitProfile = view.findViewById(R.id.visit_profile_image);
//        visitName = view.findViewById(R.id.visit_user_name);
//        visitStatus = view.findViewById(R.id.visit_status);
//        requestButton = view.findViewById(R.id.send_message_request_button);
//        declineButton = view.findViewById(R.id.decline_message_request_button);
//        currentState = "new";
//
//        if (senderUserId.equals(receiverId))
//            requestButton.setVisibility(View.INVISIBLE);
//        else
//            requestButton.setVisibility(View.VISIBLE);
//
//        ref = FirebaseDatabase.getInstance().getReference();
//        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
//        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
//        notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
//
//        ref.child("Users").child(receiverId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")) {
//                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
//                    String retrieveUserStatus = dataSnapshot.child("status").getValue().toString();
//                    String retrieveUserImage = dataSnapshot.child("image").getValue().toString();
//
//                    visitName.setText(retrieveUsername);
//                    visitStatus.setText(retrieveUserStatus);
//                    Picasso.get().load(retrieveUserImage).into(visitProfile);
//
//                    manageChatRequest();
//                } else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")) {
//                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
//                    String retrieveUserStatus = dataSnapshot.child("status").getValue().toString();
//
//                    visitName.setText(retrieveUsername);
//                    visitStatus.setText(retrieveUserStatus);
//
//                    manageChatRequest();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        return view;
//    }

    private void manageChatRequest() {
        chatRequestRef.child(senderUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(receiverId)) {
                    String requestType = dataSnapshot.child(receiverId).child("request_type").getValue().toString();
                    if (requestType.equals("sent")) {
                        currentState = "request_sent";
                        requestButton.setText("  Cancel Chat Request  ");
                    } else if (requestType.equals("received")) {
                        currentState = "request_received";
                        requestButton.setText("  Accept Chat Request  ");

                        declineButton.setVisibility(View.VISIBLE);
                        declineButton.setEnabled(true);
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelChatRequest();
                            }
                        });
                    }
                } else {
                    contactsRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiverId)) {
                                currentState = "friends";
                                requestButton.setText(" Remove this contact ");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestButton.setEnabled(false);

                if (currentState.equals("new")) {
                    sendChatRequest();
                }
                if (currentState.equals("request_sent")) {
                    cancelChatRequest();
                }
                if (currentState.equals("request_received")) {
                    acceptChatRequest();
                }
                if (currentState.equals("friends")) {
                    removeSpecificChatRequest();
                }
            }
        });
    }

    private void removeSpecificChatRequest() {
        contactsRef.child(senderUserId).child(receiverId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            contactsRef.child(receiverId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                requestButton.setEnabled(true);
                                                requestButton.setText("  Send Request  ");
                                                currentState = "new";

                                                declineButton.setVisibility(View.INVISIBLE);
                                                declineButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void acceptChatRequest() {
        contactsRef.child(senderUserId).child(receiverId)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            contactsRef.child(receiverId).child(senderUserId)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                chatRequestRef.child(senderUserId).child(receiverId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    chatRequestRef.child(receiverId).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    requestButton.setEnabled(true);
                                                                                    currentState = "friends";
                                                                                    requestButton.setText(" Remove this contact ");

                                                                                    declineButton.setVisibility(View.INVISIBLE);
                                                                                    declineButton.setEnabled(false);
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelChatRequest() {
        chatRequestRef.child(senderUserId).child(receiverId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(receiverId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                requestButton.setEnabled(true);
                                                requestButton.setText("  Send Request  ");
                                                currentState = "new";

                                                declineButton.setVisibility(View.INVISIBLE);
                                                declineButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendChatRequest() {
        chatRequestRef.child(senderUserId).child(receiverId).child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(receiverId).child(senderUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderUserId);
                                                chatNotificationMap.put("type", "request");
                                                notificationRef.child(receiverId).push().setValue(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    requestButton.setEnabled(true);
                                                                    currentState = "request_sent";
                                                                    requestButton.setText("  Cancel Chat Request  ");
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
