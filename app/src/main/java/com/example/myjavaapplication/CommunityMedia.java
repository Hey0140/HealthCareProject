package com.example.myjavaapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class CommunityMedia implements Serializable {
    private String uid;
    private String comid;
    private String title;
    private String body;
    private String imageUri;
    private String Timestamp;
    private boolean heart;
    private long heartNumber;
    private long commentNumber;

    CommunityMedia(){
        this.uid = "";
        this.comid = "";
        this.title = "";
        this.body = "";
        this.imageUri = "";
        this.Timestamp = "";
        this.heartNumber = 0;
        this.commentNumber = 0;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getComid() {
        return comid;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageUri() {
        return imageUri;
    }

    public long getHeartNumber() {
        return heartNumber;
    }

    public long getCommentNumber() {
        return commentNumber;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setCommentNumber(long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public void setHeartNumber(long heartNumber) {
        this.heartNumber = heartNumber;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public boolean isHeart() {
        return heart;
    }

    public void setHeart(boolean heart) {
        this.heart = heart;
    }
}
