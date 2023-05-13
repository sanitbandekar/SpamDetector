package com.spamdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.spamdetector.databinding.ActivityChatBoxBinding;


public class ChatBoxActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewModel viewModel;
    ChatBoxAdapter adapter;
    ActivityChatBoxBinding binding;

    String SENT_ACTION = "SMS_SENT_ACTION";
    String DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    String address;

    private static final String TAG = "ChatBoxActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.rvChatBox;

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        Intent intent = getIntent();
        address=  intent.getStringExtra("address");

        viewModel.getAllSmsSpecific(address).observe(this, sms -> {
            adapter = new ChatBoxAdapter(sms);
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            recyclerView.setAdapter(adapter);
        });



        binding.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              sendSms(address);
            }
        });


//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle(address);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void sendSms(String address) {
        String msg =  binding.threadTypeMessage.getText().toString();
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 100, new
                Intent(SENT_ACTION), PendingIntent.FLAG_IMMUTABLE);

        PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 200, new
                Intent(DELIVERED_ACTION), PendingIntent.FLAG_IMMUTABLE);

        // SMS sent receiver
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "SMS sent intent received.");
            }
        }, new IntentFilter(SENT_ACTION));

// SMS delivered receiver
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "SMS delivered intent received.");
            }
        }, new IntentFilter(DELIVERED_ACTION));

        Sms mSms =new Sms();
        mSms.setAddress(address);
        mSms.setMsg(msg);
        mSms.setFolderName("sent");
        mSms.setReadState("0");
        mSms.setTime("");
        mSms.setSpam(false);
        new Thread(new Runnable() {
            @Override
            public void run() {


                SmsRoomDatabase.getInstance(ChatBoxActivity.this)
                        .smsDao()
                        .insertSms(mSms);

                Log.d(TAG, "run: todos has been inserted...");
            }
        }).start();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(address, null, msg, sentIntent, deliveryIntent);
        binding.threadTypeMessage.setText("");
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thread, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.delete:
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
                                deleteSmsAll(address);
                                finish();
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

                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.dial_number:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+address));
                startActivity(callIntent);
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
}

    private void dialogCreate() {

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

    private void deleteSmsAll(String mAddress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmsRoomDatabase.getInstance(ChatBoxActivity.this)
                        .smsDao()
                        .deleteAllSpecific(mAddress);
            }
        }).start();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}