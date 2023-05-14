package com.spamdetector;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ViewHolder> {
    private static final String TAG = "ChatBoxAdapter";
    List<Sms> smsList;
    private static final int ITEM_LEFT = 1;
    private static final int ITEM_RIGHT  = 2;
    private ChatBoxInterface chatBoxInterface;

    String timeStampStr;

    public ChatBoxAdapter(List<Sms> smsList, ChatBoxInterface mChatBoxInterface) {
        this.smsList = smsList;
        this.chatBoxInterface = mChatBoxInterface;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_LEFT:
                return new LeftChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message,parent,false));
            case ITEM_RIGHT:
                return new RightChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message,parent,false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatBoxAdapter.ViewHolder holder, int position) {

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


    public  class LeftChatViewHolder extends ViewHolder{
        public TextView thread_message_body_sent;
        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);

            thread_message_body_sent = itemView.findViewById(R.id.thread_message_body_sent);
            
            thread_message_body_sent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sms sms = smsList.get(getAdapterPosition());

                    chatBoxInterface.onLongPress(sms);
                }
            });
        }
    }

    public class RightChatViewHolder extends ViewHolder {

        public TextView thread_message_body;

        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            thread_message_body = itemView.findViewById(R.id.thread_message_body);


            thread_message_body.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Sms sms = smsList.get(getAdapterPosition());

                    chatBoxInterface.onLongPress(sms);
                    return false;
                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    interface ChatBoxInterface{
        public void onLongPress(Sms sms);
    }

}
