package edu.bd.ewu.finalproject;

public class UserInfo {
    String username, email, password, user_key;

    public UserInfo(String username, String email, String password, String user_key) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.user_key = user_key;
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

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }
}
