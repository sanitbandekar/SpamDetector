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
    void deleteTodo(Sms sms);

    @Update
    void updateTodo(Sms sms);

    @Insert
    void insertMultipleTodo(List<Sms> smsList);


    @Query("SELECT * FROM sms_table")
    LiveData<List<Sms>> findAllSms();
}
