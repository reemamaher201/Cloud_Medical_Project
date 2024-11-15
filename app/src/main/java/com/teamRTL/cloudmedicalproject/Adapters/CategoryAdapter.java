package com.teamRTL.cloudmedicalproject.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamRTL.cloudmedicalproject.Models.Category;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categoryList;
    private Context context;
    private ItemClickListener cClickListener;

    public CategoryAdapter(List<Category> categoryList, ItemClickListener onClick) {
        this.categoryList = categoryList;
        this.cClickListener = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Category category = categoryList.get(position);
        String icon = category.getIcon();
        Picasso.get().load(icon).into(holder.iconImageView);
        holder.titleTextView.setText(category.getTitle());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cClickListener.onItemClick(holder.getAdapterPosition(), categoryList.get(position).getId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImageView;
        TextView titleTextView;
        public CardView card;
        ViewHolder(View itemView) {
            super(itemView);
            this.iconImageView = itemView.findViewById(R.id.catIcon);
            this.titleTextView = itemView.findViewById(R.id.textCat);
            this.card = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.cClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }
}
