package com.example.dentistapplication.ui.pHome;

public class ModelDates {
    String date, hour, free;

    public ModelDates() {
    }

    public ModelDates(String date, String hour, String free) {
        this.date = date;
        this.hour = hour;
        this.free = free;
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
