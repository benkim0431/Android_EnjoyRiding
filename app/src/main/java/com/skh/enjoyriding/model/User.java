package com.skh.enjoyriding.model;

public class User {
    private int userId;
    private String username;
    private String password;

    public User(String username, String password) {
        this.userId = 0;
        this.username = username;
        this.password = password;
    }

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String validate(){
        String message = null;

        return message;
    }
}
