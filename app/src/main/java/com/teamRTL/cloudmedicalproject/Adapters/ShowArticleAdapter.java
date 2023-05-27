package com.teamRTL.cloudmedicalproject.Adapters;


        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;

        import com.teamRTL.cloudmedicalproject.Models.Articles;
        import com.teamRTL.cloudmedicalproject.Models.MyListener;
        import com.teamRTL.cloudmedicalproject.R;
        import com.teamRTL.cloudmedicalproject.UIs.patients.ShowDetailsArticleActivity;
        import com.teamRTL.cloudmedicalproject.databinding.ArticlesItemBinding;

        import java.util.ArrayList;

public class ShowArticleAdapter extends RecyclerView.Adapter<ShowArticleAdapter.viewHolder> {

    Context context;
    private ArrayList<Articles> list;
    MyListener myListener;

    public ShowArticleAdapter(Context context, ArrayList<Articles> list, MyListener myListener) {
        this.context = context;
        this.list = list;
        this.myListener = myListener;
    }


    @NonNull
    @Override
    public ShowArticleAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArticlesItemBinding binding = ArticlesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ShowArticleAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.doctorName.setText(list.get(position).getName());
        holder.binding.doctorMajor.setText(list.get(position).getTitle());
        holder.binding.description.setText(list.get(position).getDescription());
        Glide.with(context).load(list.get(position).getLogo()).placeholder(R.drawable.ic_launcher_background).into((holder).binding.imageDoctor);

        holder.binding.hide.setVisibility(View.GONE);
        holder.binding.edit.setVisibility(View.GONE);
        holder.binding.delete.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(View->{
            Intent intent = new Intent(context, ShowDetailsArticleActivity.class);
            intent.putExtra("name", list.get(position).getName());
            intent.putExtra("title", list.get(position).getTitle());
            intent.putExtra("description", list.get(position).getDescription());
            intent.putExtra("video", list.get(position).getVideo());
            intent.putExtra("logo", list.get(position).getLogo());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        ArticlesItemBinding binding;

        public viewHolder(ArticlesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }


}

