package com.example.myjavaapplication;

public class CommentItemData {
    private String comId;
    private String userId;
    private String commentId;
    private String timeStamp;
    private String name;
    private String body;

    CommentItemData(){
        this.userId = "";
        this.commentId = "";
        this.timeStamp = "";
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

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
