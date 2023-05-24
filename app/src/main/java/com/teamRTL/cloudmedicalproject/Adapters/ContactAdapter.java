package com.teamRTL.cloudmedicalproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Object> contactList;

    public ContactAdapter(List<Object> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Object contact = contactList.get(position);

        if (contact instanceof Patients) {
            Patients patient = (Patients) contact;
            holder.nameTextView.setText(patient.getName());
            // تعيين الصورة لـ ImageView الخاص بالمرضى
            // يمكن استخدام مكتبة تحميل الصور مثل Picasso أو Glide لتحميل الصورة من الرابط الموجود في الكائن Patient
        } else if (contact instanceof Doctors) {
            Doctors doctor = (Doctors) contact;
            holder.nameTextView.setText(doctor.getName());
            // تعيين الصورة لـ ImageView الخاص بالأطباء
            // يمكن استخدام مكتبة تحميل الصور مثل Picasso أو Glide لتحميل الصورة من الرابط الموجود في الكائن Doctor
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.username3);
            imageView = itemView.findViewById(R.id.profile_image1);
        }
    }
}
