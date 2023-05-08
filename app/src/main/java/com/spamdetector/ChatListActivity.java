package com.spamdetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.spamdetector.databinding.ActivityChatListBinding;

import java.util.List;


public class ChatListActivity extends AppCompatActivity {
    private static final String TAG = "ChatListActivity";
//    SimpleCursorAdapter adapter;
//    ListView lvMsg;

    ActivityChatListBinding binding;
    RecyclerView recyclerView;

    SmsListAdapter adapter;

    private List<Sms> smsList;
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
                adapter = new SmsListAdapter(sms);
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
}