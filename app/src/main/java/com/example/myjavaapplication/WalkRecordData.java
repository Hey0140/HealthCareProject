package com.example.myjavaapplication;

public class WalkRecordData {
    private String startTime;
    private String endTime;
    private String duringTime;

    WalkRecordData(){
        startTime = "";
        endTime = "";
        duringTime = "";
    }

    public String getDuringTime() {
        return duringTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setDuringTime(String duringTime) {
        this.duringTime = duringTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
