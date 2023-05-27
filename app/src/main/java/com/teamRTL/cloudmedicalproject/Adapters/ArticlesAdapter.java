package com.teamRTL.cloudmedicalproject.Adapters;


        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.teamRTL.cloudmedicalproject.Models.Articles;
        import com.teamRTL.cloudmedicalproject.Models.MyListener;
        import com.teamRTL.cloudmedicalproject.R;
        import com.teamRTL.cloudmedicalproject.UIs.doctors.EditActivity;
        import com.teamRTL.cloudmedicalproject.databinding.ArticlesItemBinding;

        import org.checkerframework.checker.units.qual.A;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.viewHolder> {

    Context context;
    private ArrayList<Articles> list;
    MyListener myListener;

    public ArticlesAdapter(Context context, ArrayList<Articles> list, MyListener myListener) {
        this.context = context;
        this.list = list;
        this.myListener = myListener;
    }


    @NonNull
    @Override
    public ArticlesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArticlesItemBinding binding = ArticlesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ArticlesAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.doctorName.setText(list.get(position).getName());
        holder.binding.doctorMajor.setText(list.get(position).getTitle());
        holder.binding.description.setText(list.get(position).getDescription());
        Glide.with(context).load(list.get(position).getLogo()).placeholder(R.drawable.ic_launcher_background).into((holder).binding.imageDoctor);


        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("title", list.get(position).getTitle());
                intent.putExtra("description", list.get(position).getDescription());
                intent.putExtra("video", list.get(position).getVideo());
                intent.putExtra("logo", list.get(position).getLogo());
                intent.putExtra("item_id", list.get(position).getItem_id());
                intent.putExtra("category",list.get(position).getCategory());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.binding.hide.setOnClickListener(View -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", list.get(position).getName());
            map.put("title", list.get(position).getTitle());
            map.put("description", list.get(position).getDescription());
            map.put("video", list.get(position).getVideo());
            map.put("logo", list.get(position).getLogo());
            map.put("item_id", list.get(position).getItem_id());
map.put("category",list.get(position).getCategory());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionRef = db.collection("Hidden");

            collectionRef.add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference itemsRef = db.collection("Hidden");

                            DocumentReference itemRef = itemsRef.document(documentReference.getId());
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("item_id", documentReference.getId());
                            itemRef.update(updatedData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("aVoid", "aVoid");
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            CollectionReference myCollection = db.collection("Articles");

                                            myCollection.document(list.get(position).getItem_id())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            notifyDataSetChanged();
                                                            Log.e("TAG", "DocumentSnapshot successfully deleted!");

                                                        }
                                                    });

                                            list.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle errors here
                                            Log.e("e", e.getMessage());

                                        }
                                    });


                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });

        });

        holder.binding.delete.setOnClickListener(View -> {
            myListener.delete(list.get(position).getItem_id(), position);
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

