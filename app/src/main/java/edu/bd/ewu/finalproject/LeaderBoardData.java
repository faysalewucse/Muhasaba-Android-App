package edu.bd.ewu.finalproject;

public class LeaderBoardData{
    String uid, points;

    public LeaderBoardData(String name, String points) {
        this.uid = name;
        this.points = points;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
