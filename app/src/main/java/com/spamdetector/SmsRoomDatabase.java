package com.spamdetector;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Sms.class}, version = 1, exportSchema = false)
public abstract class SmsRoomDatabase extends RoomDatabase {

    public abstract SmsDao smsDao();

    private static volatile SmsRoomDatabase INSTANCE;

    static SmsRoomDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SmsRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SmsRoomDatabase.class, "Sms_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
