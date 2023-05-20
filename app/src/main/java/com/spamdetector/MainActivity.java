package com.spamdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.spamdetector.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    final int PERMISSION_REQUEST_CODE =112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECEIVE_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            GotoChatActivity();
        }




        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (!shouldShowRequestPermissionRationale("112")){
                        getNotificationPermission();
                    }

            }
        });
    }

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE);
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){
            Log.e(TAG, "getNotificationPermission: ",e );
        }

    }

    private void loadOldMsg() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Sms> todoList = getAllSms();


                SmsRoomDatabase.getInstance(getApplicationContext())
                        .smsDao()
                        .insertMultipleTodo(todoList);
                Log.d(TAG, "inserted m....");
                GotoChatActivity();

            }
        });
        thread.start();
    }

    public List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        this.startManagingCursor(c);
        int totalSMS = c.getCount();


        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {


                objSms = new Sms();
                objSms.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                int indexDate = c.getColumnIndex("date");
                long date = c.getLong(indexDate);

                // convert millis value to proper format
                Date dateVal = new Date(date);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
                String dateText = format.format(dateVal);
                Log.d(TAG, "getAllSms: "+dateText);
                objSms.setTime(dateText);
                objSms.setReadState(c.getString(c.getColumnIndexOrThrow("read")));
                objSms.setSpam(false);
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                    loadOldMsg();


                }  else {
                    //deny
                    getNotificationPermission();
                }
                return;

        }

    }


    private void GotoChatActivity() {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }
}