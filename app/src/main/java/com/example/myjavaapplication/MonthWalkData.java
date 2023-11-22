package com.example.myjavaapplication;

import android.graphics.Bitmap;

public class MonthWalkData {
    private String image;
    private Bitmap bitmap;
    private int type;

    public String getImage() {
        return image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
