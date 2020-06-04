package com.example.dentistapplication.ui.pCalendar;

public class ModelVisits {
    String day, hour, uidDoctor;

    public ModelVisits() {
    }

    public ModelVisits(String day, String hour, String uidDoctor) {
        this.day = day;
        this.hour = hour;
        this.uidDoctor = uidDoctor;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUidDoctor() {
        return uidDoctor;
    }

    public void setUidDoctor(String uidDoctor) {
        this.uidDoctor = uidDoctor;
    }
}