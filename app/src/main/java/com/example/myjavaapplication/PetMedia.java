package com.example.myjavaapplication;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PetMedia implements Serializable {
    private final int MALE = 11;
    private final int FEMALE = 12;
    private final int MALENEUTER = 13;
    private final int FEMALENEUTER = 14;
    private final int SMALL = 21;
    private final int MIDDLE = 22;
    private final int BIG = 23;

    public PetMedia() {
        this.image = "";
        this.birth = "";
        this.feat = "";
        this.feed = "";
        this.name = "";
        this.feedcal = 0;
        this.kind = 0;
        this.weight = 0;
        this.sex = 0;
        this.id = 0;
        this.uid = "";
        this.petVaccine = new HashMap<>();
        this.petLike = new HashMap<>();
    }
    private String uid;
    private int id;
    private int kind;
    private int sex;
    private int weight;
    private int feedcal;
    private String name;
    private String birth;
    private String image;
    private String feed;
    private String feat;
    private HashMap<String, Boolean> petLike;
    private HashMap<String, Boolean> petVaccine;

    public String getuId() {
        return uid;
    }
    public int getPetKind() {
        return kind;
    }
    public int getPetSex() {
        return sex;
    }
    public int getPetWeight() {
        return weight;
    }
    public int getPetFeedCalorie() {
        return feedcal;
    }
    public String getPetName() {
        return name;
    }
    public String getPetBirth() {
        return birth;
    }
    public String getPetFeed() {
        return feed;
    }
    public String getPetFeat() {
        return feat;
    }
    public String getImage() {
        return image;
    }
    public HashMap<String, Boolean> getPetLike() {
        return petLike;
    }
    public HashMap<String, Boolean> getPetVaccine() {
        return petVaccine;
    }

    public int getPetId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPetBirth(String petBirth) {
        this.birth = petBirth;
    }

    public void setPetFeat(String petFeat) {
        this.feat = petFeat;
    }

    public void setPetFeed(String petFeed) {
        this.feed = petFeed;
    }

    public void setPetFeedCalorie(int petFeedCalorie) {
        this.feedcal = petFeedCalorie;
    }

    public void setPetKind(int petKind) {
        this.kind = petKind;
    }

    public void setPetLike(HashMap<String, Boolean> petLike) {
        this.petLike = petLike;
    }

    public void setPetName(String petName) {
        this.name = petName;
    }

    public void setPetSex(int petSex) {
        this.sex = petSex;
    }

    public void setuId(String petUid) {
        this.uid = petUid;
    }

    public void setPetVaccine(HashMap<String, Boolean> petVaccine) {
        this.petVaccine = petVaccine;
    }
    public void setPetWeight(int petWeight) {
        this.weight = petWeight;
    }

    public void setPetId(int petId) {
        this.id = petId;
    }
}
