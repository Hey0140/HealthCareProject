package com.example.myjavaapplication;

import java.io.Serializable;
import java.util.Map;

public class HospitalMedia implements Serializable {
    private int id;
    private String uid;
    private String name;
    private String doctor;
    private String address;
    private String number;
    private String feature;

    HospitalMedia(){
        this.id = 0;
        this.uid ="";
        this.name = "";
        this.doctor = "";
        this.address = "";
        this.number = "";
        this.feature = "";
    }

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getFeature() {
        return feature;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
