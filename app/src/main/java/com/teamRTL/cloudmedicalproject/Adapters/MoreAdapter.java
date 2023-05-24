package com.teamRTL.cloudmedicalproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamRTL.cloudmedicalproject.Models.Item;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {

    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;

    public MoreAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textMore);
            imageView = itemView.findViewById(R.id.iconMore);

            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            titleTextView.setText(item.getText());
            imageView.setImageResource(item.getImageResource());
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
