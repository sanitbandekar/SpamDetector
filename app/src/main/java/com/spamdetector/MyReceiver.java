package com.spamdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private NotificationHelper notificationHelper;
    final int spam_threshhold = 10;
    HashMap<String, Integer> SpamWord = new HashMap<String, Integer>();
    private Sms mSms;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHelper = new NotificationHelper(context.getApplicationContext());
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from="";
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm a", Locale.getDefault());
        String currentDateAndTime = sdf.format(currentTime);
            if (bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    String msgBody="";
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody += msgs[i].getMessageBody();
                        mSms =new Sms();
                        mSms.setAddress(msgs[i].getOriginatingAddress());
                        mSms.setMsg(msgs[i].getMessageBody());
                        mSms.setFolderName("inbox");
                        mSms.setReadState("0");
                        mSms.setTime(currentDateAndTime);
                        mSms.setSpam(isSpam(msgBody, msg_from));
                    }

                    Log.d(TAG, "onReceive: "+mSms.toString());
                    storeToDb(context,mSms);


                    SpamWord=loadSpamWords(context);

                    //Check if the sms is spam or not
                    if(!isSpam(msgBody, msg_from)){
                        //if it is not a spam notify the user and store the sms in inbox
                        this.clearAbortBroadcast();
                    }
                    else{
                        Log.d(TAG, "onReceive: isSpam word");
                        notificationHelper.sendHighPriorityNotification(msg_from,msgBody, MainActivity.class);
                        //else store the sms in a db and not in inbox and do not notify the user
//                        storeToDb();
                        saveSpam(msg_from, context);
                    }//end if else
                    Log.d(TAG, "onReceive: "+msg_from+" "+msgBody);


                }
                catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    private void storeToDb(Context context, Sms sms) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                SmsRoomDatabase.getInstance(context.getApplicationContext())
                        .smsDao()
                        .insertSms(sms);

                Log.d(TAG, "run: todos has been inserted...");
            }
        }).start();
    }

    public boolean isSpam(String msg, String sender)
    {
        int weight=0;
        boolean flag=false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";



        //sender="abc";
//        if(!sender.matches("\\d{11}")) {
//            return true;
//        }



        //work remaining:
        //1. work on URL shortners and domains

        //if(containsURL(msg))
        //	return true;



        StringTokenizer st= new StringTokenizer(msg);


        String word="";

        while(st.hasMoreElements())
        {
            word = st.nextElement().toString();

            if(word.matches("(.*%)|(.*!+)") || word.contains("$"))
                weight=weight+4;

            if(word.matches(regex))
                weight=weight+8;

            word=word.toLowerCase();

            if(SpamWord.containsKey(word)){
                weight=weight+SpamWord.get(word);
            }
        }

        if(weight<=spam_threshhold)
            flag=false;
        else
            flag=true;

        return flag;
    }

    public boolean containsURL(String msg)
    {
        boolean flag=false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        StringTokenizer st= new StringTokenizer(msg);
        String word="";

        while(st.hasMoreElements())
        {
            word = st.nextElement().toString();
            Pattern patt = Pattern.compile(regex);
            Matcher matcher = patt.matcher(word);
            Log.d(TAG, "containsURL: "+matcher.matches());
            flag=matcher.matches();
            if(flag)
                break;
        }
        return flag;

    }

    public void saveSpam(String fmsg, Context context)
    {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("SPAM_SMS_DATA",context.MODE_APPEND)));
            writer.write("\r"+fmsg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, Integer> loadSpamWords(Context context)
    {
        HashMap<String, Integer> SpamWord = new HashMap<String, Integer>();
        String line, word="";
        int weight=0;
        try
        {
            BufferedReader reader;
            final InputStream file = context.getAssets().open("spam_keywords.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            while((line = reader.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreElements())
                {
                    word=st.nextElement().toString();
                    weight= Integer.parseInt(st.nextElement().toString());
                }
                SpamWord.put(word, weight);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(SpamWord);
        return SpamWord;
    }



}