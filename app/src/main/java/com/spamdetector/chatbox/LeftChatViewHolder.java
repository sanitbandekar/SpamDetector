package com.spamdetector.chatbox;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.spamdetector.R;

public class LeftChatViewHolder extends ChatBoxViewHolder{
    public TextView thread_message_body_sent;
    public LeftChatViewHolder(@NonNull View itemView) {
        super(itemView);

        thread_message_body_sent = itemView.findViewById(R.id.thread_message_body_sent);
    }
}
