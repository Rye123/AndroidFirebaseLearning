package com.example.androidfirebaselearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfirebaselearning.user.LocalDatabaseLegacy;
import com.example.androidfirebaselearning.user.User;
import com.example.androidfirebaselearning.user.UserType;


public class MainActivity extends AppCompatActivity {
    // SharedPreferences to store local data like current user
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String USER_KEY = "ID_KEY";
    public static final String REGISTER_NAME_KEY = "REGISTER_NAME_KEY";
    SharedPreferences userPreferences;

    // GUI items
    EditText nameEditText;
    EditText pwEditText;
    Button loginButton;
    Button registerButton;

    LocalDatabaseLegacy dat = new LocalDatabaseLegacy();
    Middleman middleman = new Middleman(dat);
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
        setContentView(R.layout.activity_main);

        // TODO: Can probably refactor getting the current user into a separate class to handle database extraction and checking.

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

        // if user is not a Guest, skip to the Users activity
        if (currentUser.getType() != UserType.GUEST) {
            Intent goToUsersActivityIntent = new Intent (MainActivity.this, UsersActivity.class);
            startActivity(goToUsersActivityIntent);
        }

        // initialise ids of GUI components
        nameEditText = findViewById(R.id.nameEditText);
        pwEditText = findViewById(R.id.pwEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // onclick handler for loginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String password = pwEditText.getText().toString();

                User testUser = middleman.authenticate(name, password);
                if (testUser == null) {
                    Toast failedAuthToast = Toast.makeText(MainActivity.this, "Failed to log in!", Toast.LENGTH_LONG);
                    failedAuthToast.show();
                    pwEditText.setText("");
                } else {
                    // update SharedPrefs
                    currentUser = testUser;
                    SharedPreferences.Editor userPrefEdit = userPreferences.edit();
                    userPrefEdit.putInt(USER_KEY, currentUser.getId());
                    userPrefEdit.apply();

                    // go to users activity
                    Intent goToUsersActivityIntent = new Intent (MainActivity.this, UsersActivity.class);
                    startActivity(goToUsersActivityIntent);
                }
            }
        });

        // onclick handler for registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();

                // make an intent that goes to the register page, carrying over the name
                Intent goToRegisterActivityIntent = new Intent (MainActivity.this, RegisterActivity.class);
                goToRegisterActivityIntent.putExtra(REGISTER_NAME_KEY, name);
                startActivity(goToRegisterActivityIntent);
            }
        });
    }
}