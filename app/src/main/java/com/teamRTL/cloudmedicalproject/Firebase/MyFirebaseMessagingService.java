package com.teamRTL.cloudmedicalproject.Firebase;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.teamRTL.cloudmedicalproject.Models.NotificationModel;
import com.teamRTL.cloudmedicalproject.R;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        getFirebaseMessage(message.getNotification().getTitle(), message.getNotification().getBody());
    }

    private void getFirebaseMessage(String title, String msg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);

        List<NotificationModel> notificationList = new ArrayList<>();

        // داخل الدالة getFirebaseMessage()
// إنشاء كائن NotificationModel وإضافته إلى القائمة
        NotificationModel notification = new NotificationModel(title, msg);
        notificationList.add(notification);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        manager.notify(101, builder.build());



    }


}
