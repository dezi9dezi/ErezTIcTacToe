package com.example.ereztictactoe;

import androidx.annotation.NonNull;

public class User {

    long id;
    String username;
    String password;

    User(long id, String un, String pw) {
        this.id = id;
        this.username = un;
        this.password = pw;
    }
    User(String un, String pw) {
        this.id = -1;
        this.username = un;
        this.password = pw;
    }
    User() {
        this.id = -1;
        this.username = "empty";
        this.password = "empty";
    }

    public void setId(long id) {this.id = id;}
    public void setUsername(String un) {this.username = un;}
    public void setPassword(String pw) {this.password = pw;}

    public long getId() {return id;}
    public String getPassword() {return password;}
    public String getUsername() {return username;}

    @NonNull
    @Override
    public String toString() {
        return "id: " + this.id + ", username: " + this.username + ", password: " + this.password;
    }
}
