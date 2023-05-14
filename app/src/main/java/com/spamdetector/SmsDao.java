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


    @Query("SELECT * FROM sms_table GROUP BY address ORDER BY sms_id DESC")
    LiveData<List<Sms>> findAllSms();

    @Query("SELECT * FROM sms_table WHERE address = :num ")
    LiveData<List<Sms>> findAllSpecific(String num);
}
