package com.spamdetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spamdetector.databinding.ActivityAllSpamBinding;
import com.spamdetector.databinding.AllSpamItemBinding;

import java.util.List;

public class AllSpamActivity extends AppCompatActivity {
    private static final String TAG = "AllSpamActivity";
    private AllSpamAdapter adapter;

    private ActivityAllSpamBinding binding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllSpamBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(ViewModel.class);


        viewModel.getAllSmsSpam().observe(this, new Observer<List<Sms>>() {
            @Override
            public void onChanged(List<Sms> sms) {
                Log.d(TAG, "onChanged: "+sms);
                if (sms.size() == 0){
                    binding.noFoundSpam.setVisibility(View.VISIBLE);
                }else {
                adapter = new AllSpamAdapter(sms);
                binding.allSmsRv.setAdapter(adapter);
                }
            }
        });
    }
}