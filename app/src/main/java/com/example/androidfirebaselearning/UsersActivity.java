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
    public static final String RETURN_TO_MAIN_KEY = "RETURN_TO_MAIN_KEY";
    SharedPreferences userPreferences;

    // GUI items
    TextView welcomeText;
    Button logoutButton;
    Button clearDatabaseButton;

    Middleman middleman;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // update users ArrayList
        middleman = new Middleman(getApplicationContext());

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
        clearDatabaseButton = findViewById(R.id.clearDatabaseButton);

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

                // return to main activity
                Intent goToMainActivityIntent = new Intent (UsersActivity.this, MainActivity.class);
                goToMainActivityIntent.putExtra(RETURN_TO_MAIN_KEY, "Log out successful!");
                startActivity(goToMainActivityIntent);
            }
        });

        // onclick handler for debug clearDatabaseButton
        clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update SharedPrefs
                SharedPreferences.Editor userPrefEdit = userPreferences.edit();
                userPrefEdit.clear();
                userPrefEdit.apply();

                // update database
                middleman.clear();

                // return to main activity
                Intent goToMainActivityIntent = new Intent (UsersActivity.this, MainActivity.class);
                goToMainActivityIntent.putExtra(RETURN_TO_MAIN_KEY, "All user data cleared!");
                startActivity(goToMainActivityIntent);
            }
        });
    }
}