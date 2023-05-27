package com.teamRTL.cloudmedicalproject.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;


public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {
    private Context mContext;
    private List<Patients> mPatient;


    public PatientsAdapter(Context mContext, List<Patients> mPatient) {
        this.mPatient = mPatient;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.patients_item, parent, false);
        return new PatientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patients patients = mPatient.get(position);
        holder.username.setText(patients.getName());



        if (patients.getImage() != null && !patients.getImage().isEmpty()) {
            Glide.with(mContext).load(patients.getImage()).into(holder.profile_image);
        } else {
            holder.profile_image.setImageResource(R.drawable.doctor_ann_marie_navar_2);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(mContext, ge.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("doctorid", doctors.getId());
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPatient.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username,locate;
        public ImageView profile_image;


        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.patient_name);
            profile_image = itemView.findViewById(R.id.image_p1);

            locate = itemView.findViewById(R.id.locatet);


        }
    }

}

