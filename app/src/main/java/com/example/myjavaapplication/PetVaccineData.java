package com.example.myjavaapplication;

public class PetVaccineData {
    private boolean check;
    private String vaccineName;
    private int viewtype;


    public void PetVaccineData(boolean check, String vaccineName, int viewtype){
        this.check = check;
        this.vaccineName = vaccineName;
        this.viewtype = viewtype;
    }

    public boolean isCheck() {
        return check;
    }

    public int getViewtype() {
        return viewtype;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }
}
