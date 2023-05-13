package com.spamdetector.chatbox;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.spamdetector.ChatBoxAdapter;
import com.spamdetector.R;

public class RightChatViewHolder extends ChatBoxViewHolder {
    public TextView thread_message_body;

    public RightChatViewHolder(@NonNull View itemView) {
        super(itemView);
    thread_message_body = itemView.findViewById(R.id.thread_message_body);
    }

}
