package com.example.androidfirebaselearning.user;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities= {User.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
