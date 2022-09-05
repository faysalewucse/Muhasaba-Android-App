package edu.bd.ewu.finalproject;

import java.util.ArrayList;

public class HistoryData {
    String date;
    ArrayList<JikirData> jikirData;

    public HistoryData(String date, ArrayList<JikirData> jikirData) {
        this.date = date;
        this.jikirData = jikirData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<JikirData> getJikirData() {
        return jikirData;
    }

    public void setJikirData(ArrayList<JikirData> jikirData) {
        this.jikirData = jikirData;
    }
}
