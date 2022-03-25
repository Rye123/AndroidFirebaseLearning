package com.example.androidfirebaselearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.androidfirebaselearning.user.User;
import com.example.androidfirebaselearning.user.UserType;

public class RegisterActivity extends AppCompatActivity {
    // SharedPreferences to store local data like current user
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String USER_KEY = "ID_KEY";
    public static final String RETURN_TO_MAIN_KEY = "RETURN_TO_MAIN_KEY";
    SharedPreferences userPreferences;

    // GUI items
    Spinner typeSpinner;
    EditText nameEditText;
    EditText pwEditText;
    Button registerButton;

    // Database stuff
    Middleman middleman;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // update users
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

        // if user is not a Guest, skip to the Users activity
        if (currentUser.getType() != UserType.GUEST) {
            Intent goToUsersActivityIntent = new Intent (RegisterActivity.this, UsersActivity.class);
            startActivity(goToUsersActivityIntent);
        }

        // initialise ids of GUI components
        typeSpinner = findViewById(R.id.typeSpinner);
        nameEditText = findViewById(R.id.nameEditText);
        pwEditText = findViewById(R.id.pwEditText);
        registerButton = findViewById(R.id.registerButton);

        // update name, if it was already there
        Intent fromMainActivityIntent = getIntent();
        String name = fromMainActivityIntent.getStringExtra(MainActivity.REGISTER_NAME_KEY);
        if (name == null)
            name = "";
        nameEditText.setText(name);

        // populate spinner with user types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item); // creates arrayadapter using default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // chooses layout to use when list of choices appears
        typeSpinner.setAdapter(adapter);

        // onselect handler for typeSpinner
        final String[] currentType = {""};
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentType[0] = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentType[0] = "Student";
            }
        });

        // onclick handler for registerButton
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String password = pwEditText.getText().toString();
                String typeStr = currentType[0];
                //TODO: Add check if name already exists

                switch(typeStr) {
                    case "Student":
                        middleman.add(User.Student(middleman.generateUniqueId(), name, password));
                        break;
                    case "Staff":
                        middleman.add(User.Staff(middleman.generateUniqueId(), name, password));
                        break;
                }


                // make an intent that goes back to the main page
                Intent goToMainActivityIntent = new Intent (RegisterActivity.this, MainActivity.class);
                goToMainActivityIntent.putExtra(RETURN_TO_MAIN_KEY, "Registration successful!");
                startActivity(goToMainActivityIntent);
            }
        });

    }
}