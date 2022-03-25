package com.example.androidfirebaselearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidfirebaselearning.user.LocalDatabaseLegacy;
import com.example.androidfirebaselearning.user.User;
import com.example.androidfirebaselearning.user.UserType;

public class UsersActivity extends AppCompatActivity {
    // SharedPreferences to store local data like current user
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String USER_KEY = "ID_KEY";
    SharedPreferences userPreferences;

    // GUI items
    TextView welcomeText;
    Button logoutButton;

    LocalDatabaseLegacy dat = new LocalDatabaseLegacy();
    Middleman middleman = new Middleman(getApplicationContext());
    User currentUser;

    /**
     * Initialises the test state.
     */
    private void initialState() {
        middleman.add(User.Student(0, "John", "password"));
        middleman.add(User.Student(1, "Jack", "pw"));
        middleman.add(User.Staff(2, "A staff", "asdf"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // update users ArrayList
        initialState();

        // Get the current user's preferences if any
        userPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int currentUserID = userPreferences.getInt(USER_KEY, -1);
        // TODO: refactor to remove if statement?
        if (currentUserID < 0) {
            currentUser = User.Guest();
        } else {
            currentUser = middleman.getUserById(currentUserID);
        }

        // if user is a Guest, go back to Log in
        if (currentUser.getType() == UserType.GUEST) {
            Intent goToMainActivityIntent = new Intent (UsersActivity.this, MainActivity.class);
            startActivity(goToMainActivityIntent);
        }

        // initialise IDs of GUI components
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        welcomeText.setText(
                String.format(
                    getString(R.string.welcome_text),
                        currentUser.getName()
                )
        );

        // onclick handler for logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update SharedPrefs
                SharedPreferences.Editor userPrefEdit = userPreferences.edit();
                userPrefEdit.clear();
                userPrefEdit.apply();

                // go to users activity
                Intent goToUsersActivityIntent = new Intent (UsersActivity.this, MainActivity.class);
                startActivity(goToUsersActivityIntent);
            }
        });
    }
}