package com.spamdetector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.spamdetector.databinding.RowBinding;

import java.util.List;

public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.ViewHolder> {
    private List<Sms> smsList;
    private RowBinding binding;

    public SmsListAdapter(List<Sms> smsList) {
        this.smsList = smsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sms sms = smsList.get(position);
        binding.lblMsg.setText(sms.getMsg());
        binding.lblNumber.setText(sms.getAddress());
//        if (sms.getSpam()) {
//            binding.isSpam.setVisibility(View.VISIBLE);
//        }else {
//            binding.isSpam.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowBinding rowBinding;
        public ViewHolder(@NonNull RowBinding binding) {
            super(binding.getRoot());
            rowBinding = binding;
        }
    }
}
