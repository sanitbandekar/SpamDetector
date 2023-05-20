package com.spamdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spamdetector.databinding.ActivityChatListBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatListActivity extends AppCompatActivity implements SmsListAdapter.OnItemClickInterface {
    private static final String TAG = "ChatListActivity";
//    SimpleCursorAdapter adapter;
//    ListView lvMsg;

    ActivityChatListBinding binding;
    RecyclerView recyclerView;

    SmsListAdapter adapter;

    private ViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        recyclerView = binding.rvSmsList;

        viewModel = new ViewModelProvider(this).get(ViewModel.class);


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm a", Locale.getDefault());
//        String currentDateandTime = sdf.format(new Date());
//        Log.d(TAG, "currentDateandTime: "+currentDateandTime);



        viewModel.getAllSms().observe(this, new Observer<List<Sms>>() {
            @Override
            public void onChanged(List<Sms> sms) {
                Log.d(TAG, "onChanged: "+sms);
                adapter = new SmsListAdapter(sms,ChatListActivity.this);
                binding.rvSmsList.setAdapter(adapter);
                binding.rvSmsList.addItemDecoration(new DividerItemDecoration(ChatListActivity.this, DividerItemDecoration.VERTICAL));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: "+id);
        if (id == R.id.scan){
            Intent intent = new Intent(this, ImgToTextActivity.class);
            startActivity(intent);
        }else if (id==R.id.allSpamMenu){
            Intent intent = new Intent(this, AllSpamActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.getAllSms().removeObservers(this);
    }

    @Override
    public void OnItemClick(Sms sms) {
//        Toast.makeText(this,sms.getAddress(), Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(this,ChatBoxActivity.class);
        intent.putExtra("address", sms.getAddress());
        startActivity(intent);
    }

    @Override
    public void longPress(Sms sms) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                    //set title
                .setTitle("Delete this conversation?")
                    //set message
//                .setMessage("Delete all messages?")
                    //set positive button
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SmsRoomDatabase.getInstance(ChatListActivity.this)
                                        .smsDao()
                                        .deleteAllSpecific(sms.getAddress());
                            }
                        }).start();
                        Toast.makeText(ChatListActivity.this, sms.getAddress(), Toast.LENGTH_SHORT).show();
                    }
                })
//set negative button
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })
                .show();

    }
}