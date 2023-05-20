package com.spamdetector;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    SmsRoomDatabase smsRoomDatabase;
    public ViewModel(@NonNull Application application) {
        super(application);

        smsRoomDatabase = SmsRoomDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Sms>> getAllSms(){
        return smsRoomDatabase.smsDao().findAllSms();
    }    public LiveData<List<Sms>> getAllSmsSpecific(String num){
        return smsRoomDatabase.smsDao().findAllSpecific(num);
    }

    public LiveData<List<Sms>> getAllSmsSpam(){
        return smsRoomDatabase.smsDao().findAllSpam();
    }
}
