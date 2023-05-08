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
    }
}
