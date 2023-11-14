package com.example.myjavaapplication;

public class UserMedia {
    private String email;
    private String name;
    private String phoneNumber;
    private boolean professional;

    public String getEmail() {
        return email;
    }

    public String getName(){
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isProfessional() {
        return professional;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfessional(boolean professional) {
        this.professional = professional;
    }
}
