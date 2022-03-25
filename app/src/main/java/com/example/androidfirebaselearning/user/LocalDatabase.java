package com.example.androidfirebaselearning.user;

import com.example.androidfirebaselearning.user.User;

import java.util.ArrayList;

public class LocalDatabase{
    private ArrayList<User> users;

    public LocalDatabase () {
        users = new ArrayList<>();
    }

    /**
     * Returns the User at the specified index.
     * @param index
     * @return User
     */
    public User get (int index) {
        return users.get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified User, or -1 if the list doesn't contain it.
     * @param user
     * @return
     */
    public int indexOf (User user) {
        return users.indexOf(user);
    }

    /**
     * Adds a new User to the list.
     * @param newUser
     */
    public void add (User newUser) {
        // ensure ID is unique
        for (User user : users) {
            if (user.getId() == newUser.getId())
                throw new IllegalArgumentException("ID " + user.getId() + " already exists -- cannot add new user " + newUser.getName() + " with ID " + newUser.getId());
        }
        users.add(newUser);
    }

    /**
     * Removes the User at the specified index.
     * @param index integer value of the position of the element to remove
     */
    public void remove (int index) {
        users.remove(index);
    }

    /**
     * Removes the first occurrence of the given User.
     * @param u User to remove
     */
    public void remove (User u) {
        users.remove(u);
    }

    /**
     * Returns true if database is empty.
     * @return
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }

    /**
     * Returns number of Users in the database.
     * @return
     */
    public int size () {
        return users.size();
    }

    /**
     * Removes all elements from the LocalDatabase.
     */
    public void clear () {
        users.clear();
    }

    /**
     * Returns the user based on the ID given.
     * @param id integer value of the user ID
     * @return User if the user exists in the user ArrayList, User.Guest otherwise
     */
    public User getUserById (int id) {
        for (User user : users) {
            if (user.getId() == id)
                return user;
        }
        return User.Guest();
    }

    /**
     * Returns an ArrayList of the users.
     */
    public ArrayList<User> getUsers () {
        return users;
    }
}
