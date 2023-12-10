package com.example.myjavaapplication;

public class MypageFriendItemData {
    private String myUid;
    private String frUid;
    private String frName;
    private String frImageUrl;
    private String frEmail;

    MypageFriendItemData() {
        this.myUid = "";
        this.frUid = "";
        this.frName = "";
        this.frEmail = "";
        this.frImageUrl = "";
    }

    public String getMyUid() {
        return myUid;
    }

    public String getFrUid() {
        return frUid;
    }

    public String getFrName() {
        return frName;
    }

    public String getFrEmail() {
        return frEmail;
    }

    public String getFrImageUrl() {
        return frImageUrl;
    }

    public void setFrUid(String frUid) {
        this.frUid = frUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public void setFrEmail(String frEmail) {
        this.frEmail = frEmail;
    }

    public void setFrImageUrl(String frImageUrl) {
        this.frImageUrl = frImageUrl;
    }
}
