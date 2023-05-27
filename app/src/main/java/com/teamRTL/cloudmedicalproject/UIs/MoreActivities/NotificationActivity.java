package com.teamRTL.cloudmedicalproject.UIs.MoreActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.teamRTL.cloudmedicalproject.Adapters.NotificationAdapter;
import com.teamRTL.cloudmedicalproject.Models.NotificationModel;
import com.teamRTL.cloudmedicalproject.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private List<NotificationModel> notificationList;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // تهيئة قائمة الإشعارات ومحولها
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);

        // العثور على عنصر ListView وتعيين المحول عليه
        notificationListView = findViewById(R.id.notificationListView);
        notificationListView.setAdapter(notificationAdapter);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("TAG", token);
                        Toast.makeText(NotificationActivity.this, "Your device token is: " + token, Toast.LENGTH_SHORT).show();
                        // et_token.setText(token);
                    }
                });
    }

}