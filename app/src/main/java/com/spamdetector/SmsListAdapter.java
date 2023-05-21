package com.spamdetector;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.spamdetector.databinding.ItemConversationBinding;
import com.spamdetector.databinding.RowBinding;

import java.util.List;

public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.ViewHolder> {
    private List<Sms> smsList;
    private ItemConversationBinding binding;
    private OnItemClickInterface itemClickInterface;

    public SmsListAdapter(List<Sms> smsList, OnItemClickInterface clickInterface) {
        this.smsList = smsList;
        this.itemClickInterface = clickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sms sms = smsList.get(position);
        binding.conversationBodyShort.setText(sms.getMsg());
        binding.conversationAddress.setText(sms.getAddress());
        if (sms.getSpam()) {
            binding.spamIndicator.setVisibility(View.VISIBLE);
        }else {
            binding.spamIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemConversationBinding rowBinding;
        public ViewHolder(@NonNull ItemConversationBinding binding) {
            super(binding.getRoot());
            rowBinding = binding;



            binding.conversationFrame.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Sms sms = smsList.get(getAdapterPosition());


                    itemClickInterface.longPress(sms);
                    return false;
                }
            });

            binding.conversationFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sms sms = smsList.get(getAdapterPosition());


                    itemClickInterface.OnItemClick(sms);
                }
            });
        }
    }
    interface OnItemClickInterface{
        public void OnItemClick(Sms sms);
        public void longPress(Sms sms);
    }
}
