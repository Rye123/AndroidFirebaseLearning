package com.example.androidfirebaselearning.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object to user database.
 */
@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE id LIKE :id")
    User getUserById(int id);

    @Query("SELECT * FROM users WHERE name LIKE :name AND password LIKE :password")
    User getUserByNameAndPassword(String name, String password);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}
