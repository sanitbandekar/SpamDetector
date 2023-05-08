package com.spamdetector;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_table")
public class Sms {



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sms_id")
    private int id;
    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "msg")
    private String msg;
    @ColumnInfo(name = "readState")
    private String readState; //"0" for have not read sms and "1" for have read sms
    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "folderName")
    private String folderName;
    @ColumnInfo(name = "isSpam")
    private Boolean isSpam;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Boolean getSpam() {
        return isSpam;
    }

    public void setSpam(Boolean spam) {
        isSpam = spam;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", msg='" + msg + '\'' +
                ", readState='" + readState + '\'' +
                ", time='" + time + '\'' +
                ", folderName='" + folderName + '\'' +
                ", isSpam=" + isSpam +
                '}';
    }
}