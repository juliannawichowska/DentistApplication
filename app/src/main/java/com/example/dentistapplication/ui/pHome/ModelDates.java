package com.example.dentistapplication.ui.pHome;

public class ModelDates {
    String date, hour, free, uid, key;

    public ModelDates() {
    }

    public ModelDates(String date, String hour, String free, String uid, String key) {
        this.date = date;
        this.hour = hour;
        this.free = free;
        this.uid = uid;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }
}
