package com.spamdetector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spamdetector.chatbox.ChatBoxViewHolder;
import com.spamdetector.chatbox.LeftChatViewHolder;
import com.spamdetector.chatbox.RightChatViewHolder;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxViewHolder> {
    List<Sms> smsList;
    private static final int ITEM_LEFT = 1;
    private static final int ITEM_RIGHT  = 2;

    String timeStampStr;

    public ChatBoxAdapter(List<Sms> smsList) {
        this.smsList = smsList;
    }

    @Override
    public int getItemViewType(int position) {
        if(smsList.get(position).getFolderName().equals("inbox"))
            return 2;
        else
            return 1;
    }

    @NonNull
    @Override
    public ChatBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_LEFT:
                return new LeftChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message,parent,false));
            case ITEM_RIGHT:
                return new RightChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBoxViewHolder holder, int position) {
        Sms sms = smsList.get(position);
        if(holder.getItemViewType() == ITEM_LEFT){
            LeftChatViewHolder viewHolder = (LeftChatViewHolder) holder;
            viewHolder.thread_message_body_sent.setText(sms.getMsg());


        }else{
            RightChatViewHolder viewHolder = (RightChatViewHolder) holder;
            viewHolder.thread_message_body.setText(sms.getMsg());
      ;
        }
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }
}
