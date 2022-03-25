package com.example.androidfirebaselearning;

import com.example.androidfirebaselearning.user.LocalDatabase;
import com.example.androidfirebaselearning.user.LocalDatabaseLegacy;
import com.example.androidfirebaselearning.user.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles interaction between database and app
 */
public class Middleman {
    LocalDatabaseLegacy localDatabaseLegacy;
    LocalDatabase localDatabase;
    FirebaseDatabase fbDatabase;

    public Middleman(LocalDatabaseLegacy database) {
        localDatabaseLegacy = database;
        // fbDatabase = FirebaseDatabase.getInstance().getReference; // root node
    }

    /**
     * Returns the User at the specified index.
     * @param index
     * @return User
     */
    public User get (int index) {
        return localDatabaseLegacy.get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified User, or -1 if the list doesn't contain it.
     * @param user
     * @return
     */
    public int indexOf (User user) {
        return localDatabaseLegacy.indexOf(user);
    }

    /**
     * Adds a new User to the database.
     * @param newUser
     */
    public void add (User newUser) {
        localDatabaseLegacy.add(newUser);
    }

    /**
     * Removes the User at the specified index.
     * @param index integer value of the position of the element to remove
     */
    public void remove (int index) {
        localDatabaseLegacy.remove(index);
    }

    /**
     * Removes the first occurrence of the given User.
     * @param u User to remove
     */
    public void remove (User u) {
        localDatabaseLegacy.remove(u);
    }

    /**
     * Returns true if database is empty.
     * @return
     */
    public boolean isEmpty() {
        return localDatabaseLegacy.isEmpty();
    }

    /**
     * Returns number of Users in the database.
     * @return
     */
    public int size () {
        return localDatabaseLegacy.size();
    }

    /**
     * Removes all elements from the LocalDatabase.
     */
    public void clear () {
        localDatabaseLegacy.clear();
    }

    /**
     * Returns the user if the given credentials are correct.
     * @param name string of the user's name
     * @param password string of the user's password
     * @return User if credentials are correct, null otherwise
     */
    public User authenticate (String name, String password) {
        for (User user : localDatabaseLegacy.getUsers()) {
            if (user.getName().equals(name)) {
                if (user.verifyPassword(password)) {
                    return user;
                } else {
                    return null; // avoids searching the rest of the list
                }
            }
        }
        return null;
    }

    /**
     * Returns the user based on the ID given.
     * @param id integer value of the user ID
     * @return User if the user exists in the user ArrayList, User.Guest otherwise
     */
    public User getUserById (int id) {
        return localDatabaseLegacy.getUserById(id);
    }

    /**
     * Returns an ArrayList of the users.
     */
    public ArrayList<User> getUsers () {
        return localDatabaseLegacy.getUsers();
    }
}
