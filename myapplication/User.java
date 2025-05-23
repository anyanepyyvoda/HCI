package com.example.myapplication;

public class User {
    private int id;
    private String username;
    private String password;
    private boolean notifications;
    private short textSize;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
