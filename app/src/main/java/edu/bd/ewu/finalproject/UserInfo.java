package edu.bd.ewu.finalproject;

public class UserInfo {
    String username;
    String email;
    String password;
    String pro_pic;

    public UserInfo(String username, String email, String password, String pro_pic) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pro_pic = pro_pic;
    }

    public String getPro_pic() {
        return pro_pic;
    }

    public void setPro_pic(String pro_pic) {
        this.pro_pic = pro_pic;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
