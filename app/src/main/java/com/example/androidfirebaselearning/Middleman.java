package com.example.androidfirebaselearning;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.androidfirebaselearning.user.LocalDatabase;
import com.example.androidfirebaselearning.user.LocalDatabaseLegacy;
import com.example.androidfirebaselearning.user.User;
import com.example.androidfirebaselearning.user.UserDao;
import com.example.androidfirebaselearning.user.UserType;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles interaction between database and app
 */
public class Middleman {
    LocalDatabaseLegacy localDatabaseLegacy;
    LocalDatabase localDatabase;
    FirebaseDatabase fbDatabase;

    /**
     * Initialises the test state
     * @param middleman
     */
    public static void initialise(Middleman middleman) {
        middleman.add(User.Student(0, "John", "password"));
        middleman.add(User.Student(1, "Jack", "pw"));
        middleman.add(User.Staff(2, "A staff", "asdf"));
    }

    public Middleman(Context appContext) {
        //TODO: Refactor to avoid running on main thread -- need to learn about LiveData wrapper
        localDatabase = Room.databaseBuilder(appContext, LocalDatabase.class, "local_database").allowMainThreadQueries().build();
    }

    /**
     * Returns the User at the specified index.
     * @param index
     * @return User
     */
    public User get (int index) {
        UserDao userDao = localDatabase.userDao();
        return userDao.getUserById(index);
    }

    /**
     * Returns the index of the first occurrence of the specified User, or -1 if the list doesn't contain it.
     * @param user
     * @return
     */
    public int indexOf (User user) {
        UserDao userDao = localDatabase.userDao();
        List<User> users = userDao.getAllUsers();
        return users.indexOf(user);
    }

    /**
     * Adds a new User to the database.
     * @param newUser
     */
    public void add (User newUser) {
        UserDao userDao = localDatabase.userDao();
        userDao.insert(newUser);
    }

    /**
     * Removes the User at the specified index.
     * @param index integer value of the position of the element to remove
     */
    public void remove (int index) {
        UserDao userDao = localDatabase.userDao();
        User user = get(index);
        userDao.delete(user);
    }

    /**
     * Removes the first occurrence of the given User.
     * @param u User to remove
     */
    public void remove (User u) {
        UserDao userDao = localDatabase.userDao();
        userDao.delete(u);
    }

    /**
     * Returns true if database is empty.
     * @return
     */
    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
     * Returns number of Users in the database.
     * @return
     */
    public int size () {
        UserDao userDao = localDatabase.userDao();
        return userDao.size();
    }

    /**
     * Removes all elements from the LocalDatabase.
     */
    public void clear () {
        UserDao userDao = localDatabase.userDao();
        userDao.deleteAll();
    }

    /**
     * Returns the user if the given credentials are correct.
     * @param name string of the user's name
     * @param password string of the user's password
     * @return User if credentials are correct, null otherwise
     */
    public User authenticate (String name, String password) {
        for (User user : getUsers()) {
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
        UserDao userDao = localDatabase.userDao();
        User user = userDao.getUserById(id);
        if (user == null)
            return User.Guest();
        return user;
    }

    /**
     * Returns an ArrayList of the users.
     */
    public ArrayList<User> getUsers () {
        UserDao userDao = localDatabase.userDao();
        return (ArrayList<User>) userDao.getAllUsers();
    }

    /**
     * Generates a unique ID.
     * @return integer representing a unique ID.
     */
    public int generateUniqueId() {
        int test_id = size();
        while (!getUserById(test_id).getType().equals(UserType.GUEST))
            test_id++;

        return test_id;
    }
}
