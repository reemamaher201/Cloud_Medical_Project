package com.teamRTL.cloudmedicalproject.Adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;
import com.teamRTL.cloudmedicalproject.Models.NotificationModel;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        super(context, 0, notificationList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
        }

        // استرداد إشعار من القائمة بواسطة الموقع
        NotificationModel notification = getItem(position);

        // عرض عناصر الإشعار في واجهة المستخدم
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        titleTextView.setText(notification.getTitle());
        messageTextView.setText(notification.getMessage());

        return convertView;
    }
}
