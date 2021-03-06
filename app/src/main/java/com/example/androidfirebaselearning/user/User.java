package com.example.androidfirebaselearning.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    private int id;

    private String name;
    private String password;
    private UserType type;

    // Default constructor for Firebase
    public User () {
        this.id = -1;
        this.name = "";
        this.password = "";
        this.type = UserType.GUEST;
    }

    public User (int id, String name, String password, UserType type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public static User Student (int id, String name, String password) {
        return new User(id, name, password, UserType.STUDENT);
    }

    public static User Staff (int id, String name, String password) {
        return new User(id, name, password, UserType.STAFF);
    }

    public static User Guest () {
        return new User(-1, "Guest", "", UserType.GUEST);
    }

    public int getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    public boolean verifyPassword (String pw) {
        return pw.equals(password);
    }

    // setters: needed for FB
    public void setId (int id) {
        this.id = id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void setType (UserType type) {
        this.type = type;
    }
}
