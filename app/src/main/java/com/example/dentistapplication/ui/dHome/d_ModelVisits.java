package com.example.dentistapplication.ui.dHome;

public class d_ModelVisits {

    String date, hour, uidPatient, free;

    public d_ModelVisits() {
    }

    public d_ModelVisits(String date, String hour, String uidPatient, String free) {
        this.date = date;
        this.hour = hour;
        this.uidPatient = uidPatient;
        this.free = free;
    }

    public static void setBackgroundColor(int color) {
    }

    public String getDate() {
        return date;
    }

    public void setDay(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUidPatient() { return uidPatient;}

    public void setUidPatient(String uidPatient) {
        this.uidPatient = uidPatient;
    }

    public String getFree() {return free; }

    public void setFree(String free) {
        this.free = free;
    }

}
