package com.teamRTL.cloudmedicalproject.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamRTL.cloudmedicalproject.Models.Chat;
import com.teamRTL.cloudmedicalproject.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_SENDER = 0;
    public static final int MSG_TYPE_RECEIVER = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext , List<Chat> mChat , String imageUrl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RECEIVER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_receiver,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_sender,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        Log.e("position", String.valueOf(position));
        Log.e("size", String.valueOf(mChat.size()));
        if (position == (mChat.size()-1)){
            if(chat.getIsSeen()== 1){
                holder.seenText.setText("Seen");

            }
            else {

                holder.seenText.setText("Delivered");
            }

        }
        else {
            holder.seenText.setVisibility(View.GONE);
        }

        Log.e("chat.isSeen()", String.valueOf(chat.getIsSeen()));
        if (imageUrl != null && !imageUrl.isEmpty()){
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        } else {
            holder.profile_image.setImageResource(R.drawable.user);
        }


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_SENDER;
        }else {

            return MSG_TYPE_RECEIVER;
        }
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView seenText;


        public ViewHolder(View itemView){
            super(itemView);
            show_message= itemView.findViewById(R.id.showMessage);
            profile_image = itemView.findViewById(R.id.profile_image4);
            seenText = itemView.findViewById(R.id.seenText);

        }


    }

}
