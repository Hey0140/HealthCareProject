package com.example.myjavaapplication;

public class CommunityItemData {
    private String imageUri;
    private String comId;
    private String title;
    private String body;
    private boolean heart;
    private long heartNumber;
    private long commentNumber;
    private int viewType;


    CommunityItemData (int vt, String imageUri) {
        this.title = "";
        this.body = "";
        this.heart = false;
        this.imageUri = imageUri;
        this.heartNumber = 0;
        this.commentNumber = 0 ;
        this.viewType = vt;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getViewType() {return viewType;}

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getHeartNumber() {
        return heartNumber;
    }

    public long getCommentNumber() {
        return commentNumber;
    }

    public void setImageUri(String imageId) {
        this.imageUri = imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeart(boolean heart) {
        this.heart = heart;
    }

    public void setHeartNumber(long heartNumber) {
        this.heartNumber = heartNumber;
    }

    public void setCommentNumber(long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public boolean isHeart() {
        return heart;
    }

    public void setViewType(int vt) {this.viewType = vt;}

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }
}
