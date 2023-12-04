package com.example.myjavaapplication;

import java.io.Serializable;

public class WeekStatusData implements Serializable {
    private long monday;
    private long tuesday;
    private long wednesday;
    private long thursday;
    private long friday;
    private long saturday;
    private long sunday;

    WeekStatusData(){
        this.monday = 0;
        this.tuesday = 0;
        this.wednesday = 0;
        this.thursday = 0;
        this.friday = 0;
        this.saturday = 0;
        this.sunday = 0;
    }

    public void setMonday(long monday) {
        this.monday = monday;
    }

    public void setTuesday(long tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(long wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(long thursday) {
        this.thursday = thursday;
    }

    public void setFriday(long friday) {
        this.friday = friday;
    }

    public void setSaturday(long saturday) {
        this.saturday = saturday;
    }

    public void setSunday(long sunday) {
        this.sunday = sunday;
    }

    public long getMonday() {
        return monday;
    }

    public long getTuesday() {
        return tuesday;
    }

    public long getWednesday() {
        return wednesday;
    }

    public long getThursday() {
        return thursday;
    }

    public long getFriday() {
        return friday;
    }

    public long getSaturday() {
        return saturday;
    }

    public long getSunday() {
        return sunday;
    }
}

