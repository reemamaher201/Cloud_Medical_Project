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
        import com.teamRTL.cloudmedicalproject.R;

        import java.util.List;


public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private Context mContext;
    private List<Doctors> mDoctors;


    public DoctorsAdapter(Context mContext, List<Doctors> mDoctors) {
        this.mDoctors = mDoctors;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.doctors_item, parent, false);
        return new DoctorsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctors doctors = mDoctors.get(position);
        holder.username.setText(doctors.getName());
        holder.doctor_major.setText(doctors.getSpecialistIn());


        if (doctors.getImage() != null && !doctors.getImage().isEmpty()) {
            Glide.with(mContext).load(doctors.getImage()).into(holder.profile_image);
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
        return mDoctors.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username,doctor_major,locate;
        public ImageView profile_image;


        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.doctor_name);
            profile_image = itemView.findViewById(R.id.image_doctor1);
            doctor_major = itemView.findViewById(R.id.doctor_major);
            locate = itemView.findViewById(R.id.locate);


        }
    }

}

