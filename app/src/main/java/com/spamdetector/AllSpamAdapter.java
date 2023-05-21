package com.spamdetector;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spamdetector.databinding.AllSpamItemBinding;

import java.util.List;


public class AllSpamAdapter extends RecyclerView.Adapter<AllSpamAdapter.ViewHolder> {
    private AllSpamItemBinding binding;
    private List<Sms> smsList;

    public AllSpamAdapter(List<Sms> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public AllSpamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AllSpamItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AllSpamAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllSpamAdapter.ViewHolder holder, int position) {
        Sms sms = smsList.get(position);

        holder.mBinding.allMsg.setText(sms.getMsg());
        holder.mBinding.mNumber.setText(sms.getAddress());
//        holder.setIsRecyclable(false);

    }



    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AllSpamItemBinding mBinding;
        public ViewHolder(@NonNull AllSpamItemBinding itemView) {
            super(itemView.getRoot());
             mBinding = itemView;
        }
    }

}
