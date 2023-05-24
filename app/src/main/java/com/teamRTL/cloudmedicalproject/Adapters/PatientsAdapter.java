package com.teamRTL.cloudmedicalproject.Adapters;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamRTL.cloudmedicalproject.Models.Patients;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;


public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {
    private Context mContext;
    private List<Patients> mPatients;


    public PatientsAdapter(Context mContext, List<Patients> mPatients) {
        this.mPatients = mPatients;
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
        Patients patients = mPatients.get(position);
        holder.username.setText(patients.getName());
        holder.located.setText(patients.getAddress());

//        if (patients.getImage() != null && !doctors.getImage().isEmpty()) {
//            Glide.with(mContext).load(doctors.getImage()).into(holder.image_pat1);
//        } else {
//            holder.image_pat1.setImageResource(R.drawable.doctor_ann_marie_navar_2);
//        }

//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("doctorid", doctors.getId());
//                mContext.startActivity(intent);
//            }
//        });
//    }
    }
    @Override
    public int getItemCount() {
        return mPatients.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username,located;
        public ImageView image_pat1;


        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.patient_name);
            image_pat1 = itemView.findViewById(R.id.image_pat1);

            located = itemView.findViewById(R.id.located);


        }
    }

}

