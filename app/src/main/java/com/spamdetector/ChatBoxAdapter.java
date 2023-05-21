package com.spamdetector;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Log.d(TAG, "onCreateViewHolder: "+viewType);
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
            Log.d(TAG, "onBindViewHolder: "+ sms.getTime());
            if (!sms.getTime().isEmpty() && sms.getTime() !=null) {
                viewHolder.thread_date_time.setText(sms.getTime());
            }

        }else if (holder.getItemViewType() == ITEM_RIGHT) {
            RightChatViewHolder viewHolder = (RightChatViewHolder) holder;
            viewHolder.thread_message_body.setText(sms.getMsg());
            viewHolder.thread_date_time_sent.setText(sms.getTime());

            if (sms.getSpam()){
                viewHolder.spam.setVisibility(View.VISIBLE);
            }else {
                viewHolder.spam.setVisibility(View.GONE);
            }
        }
    }





    @Override
    public int getItemCount() {
        return smsList.size();
    }


    public  class LeftChatViewHolder extends ViewHolder{
        public TextView thread_message_body_sent, thread_date_time;
        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);

            thread_message_body_sent = itemView.findViewById(R.id.thread_message_body_sent);
            thread_date_time = itemView.findViewById(R.id.thread_date_time_sent);
            
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

        public TextView thread_message_body,spam ,thread_date_time_sent;

        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);
            thread_message_body = itemView.findViewById(R.id.thread_message_body);
            thread_date_time_sent= itemView.findViewById(R.id.thread_date_time);

            spam = itemView.findViewById(R.id.thread_message_spam);



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
