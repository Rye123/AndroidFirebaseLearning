package com.example.androidfirebaselearning.user;

public class User {
    private int id;
    private String name;
    private String password;
    private UserType type;

    private User (int id, String name, String password, UserType type) {
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

    public UserType getType() {
        return type;
    }

    public boolean verifyPassword (String pw) {
        return pw.equals(password);
    }
}
