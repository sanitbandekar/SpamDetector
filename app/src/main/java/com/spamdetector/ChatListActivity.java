package com.spamdetector;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spamdetector.databinding.ActivityChatListBinding;

import java.util.List;


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



        recyclerView = binding.rvSmsList;

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllSms().observe(this, new Observer<List<Sms>>() {
            @Override
            public void onChanged(List<Sms> sms) {
                Log.d(TAG, "onChanged: "+sms);
                adapter = new SmsListAdapter(sms,ChatListActivity.this);
                binding.rvSmsList.setAdapter(adapter);
                binding.rvSmsList.addItemDecoration(new DividerItemDecoration(ChatListActivity.this, DividerItemDecoration.VERTICAL));
            }
        });






//        lvMsg = (ListView) findViewById(R.id.lvMsg);
//        Uri inboxURI = Uri.parse("content://sms");
//
//// List required columns
//        String[] reqCols = new String[]{"_id", "address", "body","date"};
//
//// Get Content Resolver object, which will deal with Content Provider
//        ContentResolver cr = getContentResolver();
//
//// Fetch Inbox SMS Message from Built-in Content Provider
//        Cursor c = cr.query(inboxURI, reqCols, null,null,null);
//
//
//        adapter = new SimpleCursorAdapter(this, R.layout.row, c,
//                new String[] { "address","body","date"} , new int[] {
//                R.id.lblMsg, R.id.lblNumber,R.id.date });
//
//        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
//
//            @Override
//            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                if (columnIndex == 1) {
//                    String createDate = c.getString(columnIndex);
//                    TextView textView = (TextView) view;
//                    textView.setText("Create date: " + createDate);
//                    Log.d(TAG, "setViewValue: "+createDate);
//                    return true;
//                }
//                return false;
//            }
//
//
//        });
//        lvMsg.setAdapter(adapter);
//
//        lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String info = ( (TextView) view.findViewById(R.id.lblNumber) ).getText().toString();
//                Log.d(TAG, "onItemClick: "+info);
//            }
//        });
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