package com.spamdetector;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SmsDao {

    @Insert
    void insertSms(Sms sms);

    @Query("SELECT * FROM sms_table")
    List<Sms> getAllTodos();

    @Query("SELECT * FROM sms_table WHERE sms_id LIKE :id")
    Sms findTodoById(int id);

    @Delete
    void deleteSms(Sms sms);

    @Query("DELETE FROM sms_table WHERE address = :address")
    void deleteAllSpecific(String address);

    @Update
    void updateTodo(Sms sms);

    @Insert
    void insertMultipleTodo(List<Sms> smsList);


    @Query("SELECT * FROM sms_table where sms_id in (SELECT max(sms_id) FROM sms_table GROUP BY address ) order by sms_id desc")
    LiveData<List<Sms>> findAllSms();

    @Query("SELECT * FROM sms_table WHERE address = :num ")
    LiveData<List<Sms>> findAllSpecific(String num);
    @Query("SELECT * FROM sms_table WHERE isSpam = 1 ")
    LiveData<List<Sms>> findAllSpam();
}
