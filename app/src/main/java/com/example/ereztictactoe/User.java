package com.example.ereztictactoe;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

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

    protected User(Parcel in) {
        id = in.readLong();
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(password);
    }
}
