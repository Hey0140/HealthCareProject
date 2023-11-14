package com.example.myjavaapplication;

public class MyPetInfoData {
    private int imageId;
    private String name;
    private int viewType;


    public void MypetInfoData(int vt, int id, String n) {
        imageId = id;
        name = n;
        viewType = vt;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
    public int getViewType() {return viewType;}

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setViewType(int vt) {this.viewType = vt;}
}
