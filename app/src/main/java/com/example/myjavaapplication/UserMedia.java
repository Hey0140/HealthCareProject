package com.example.myjavaapplication;

import java.io.Serializable;

public class UserMedia implements Serializable {
    private String email;
    private String image;
    private String name;
    private String number;
    private boolean pro;
    private String uid;
    private int count;

    public UserMedia(){
        this.email = "";
        this.image = "";
        this.name = "";
        this.number = "";
        this.pro = false;
        this.uid = "";
        this.count = 0;
    }

    public String getEmail() {
        return email;
    }

    public String getName(){
        return name;
    }

    public String getNumber() {
        return number;
    }
    public int getCount(){ return count;}

    public boolean isPro() {
        return pro;
    }
    public String getUid() {return uid;}
    public String getImage() {return image;}


    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String phoneNumber) {
        this.number = number;
    }

    public void setPro(boolean pro) { this.pro = pro; }
    public void setUid(String uid) {this.uid = uid;}
    public void setImage(String image){this.image = image;}

    public void setCount(int count){this.count = count; }
}
