package edu.bd.ewu.finalproject;

public class JikirData {
    String id, name, meaning, benefit;

    public JikirData(String id, String name, String meaning, String benefit) {
        this.id = id;
        this.name = name;
        this.meaning = meaning;
        this.benefit = benefit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benifit) {
        this.benefit = benifit;
    }
}