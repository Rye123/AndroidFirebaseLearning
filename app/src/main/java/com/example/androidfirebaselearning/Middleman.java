package com.example.androidfirebaselearning;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.androidfirebaselearning.user.LocalDatabase;
import com.example.androidfirebaselearning.user.LocalDatabaseLegacy;
import com.example.androidfirebaselearning.user.User;
import com.example.androidfirebaselearning.user.UserDao;
import com.example.androidfirebaselearning.user.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

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

    boolean dataLoaded;

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
        fbDatabase = FirebaseDatabase.getInstance(/*url to database*/);
        DatabaseReference root = fbDatabase.getReference();
        dataLoaded = false;
        root.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot fbDbRecord = task.getResult();

                    // Firebase: GenericTypeIndicator needed to resolve types for generic collections
                    GenericTypeIndicator<ArrayList<User>> t = new GenericTypeIndicator<ArrayList<User>>(){};
                    ArrayList<User> users = fbDbRecord.getValue(t);

                    // Add unique data to localDatabase
                    if (users == null)
                        return;
                    UserDao userDao = localDatabase.userDao();
                    for (User user : users) {
                        if (userDao.getUserById(user.getId()).getType() == UserType.GUEST)
                            userDao.insert(user);
                    }

                    dataLoaded = true;
                } else {
                    Log.e("firebase", "Error getting data: ", task.getException());
                }
            }
        });
    }

    /**
     * Returns the User with the specified ID.
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

        // Local checking
        for (User user : userDao.getAllUsers()) {
            if (user.getId() == newUser.getId() || user.getName().equals(newUser.getName()))
                return;
        }

        // Add to localDatabase
        userDao.insert(newUser);

        // Add to DB
        fbDatabase.getReference().child("users").child(String.valueOf(newUser.getId())).setValue(newUser);
    }

    /**
     * Removes the User at the specified index.
     * @param index integer value of the position of the element to remove
     */
    public void remove (int index) {
        UserDao userDao = localDatabase.userDao();

        // Remove from localDatabase
        User user = get(index);
        userDao.delete(user);

        // Remove from DB
        fbDatabase.getReference().child("users").child(String.valueOf(index)).removeValue();
    }

    /**
     * Removes the first occurrence of the given User.
     * @param user User to remove
     */
    public void remove (User user) {
        int index = indexOf(user);
        remove(index);
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
        // Clear local data
        UserDao userDao = localDatabase.userDao();
        userDao.deleteAll();

        // Clear DB
        fbDatabase.getReference().child("users").removeValue();
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
