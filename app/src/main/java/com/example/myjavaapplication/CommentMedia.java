package com.example.myjavaapplication;

import java.io.Serializable;

public class CommentMedia implements Serializable {
    private String comId;
    private String userId;
    private String commentId;
    private String timeStamp;
    private String timeStampCheck;
    private String name;
    private String body;

    CommentMedia(){
        this.userId = "";
        this.commentId = "";
        this.timeStamp = "";
        this.timeStampCheck = "";
        this.name = "";
        this.body = "";
    }

    public String getUserId() {
        return userId;
    }

    public String getComId() {
        return comId;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTimeStampCheck() {
        return timeStampCheck;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimeStampCheck(String timeStampCheck) {
        this.timeStampCheck = timeStampCheck;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
