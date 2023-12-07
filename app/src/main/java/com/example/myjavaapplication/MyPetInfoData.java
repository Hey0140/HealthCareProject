package com.example.myjavaapplication;

public class MyPetInfoData {
    private String imageId;
    private String name;
    private int viewType;


    public void MypetInfoData(int vt, String id, String n) {
        imageId = id;
        name = n;
        viewType = vt;
    }

    public String getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
    public int getViewType() {return viewType;}

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setViewType(int vt) {this.viewType = vt;}
}
