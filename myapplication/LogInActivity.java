package com.example.myapplication;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;


public class LogInActivity extends AppCompatActivity {

    Button logInButton;
    Button visitorButton;
    Button signUpButton;
    EditText usernameInput;
    EditText passwordInput;
    User[] users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        users = new User[5];
        int usersC = 1;
        users[0] = new User(0, "admin", "admin");
        // Load other users
        // TODO
        // Check if there are extras (data) passed with the Intent
        if (intent != null && intent.getExtras() != null && usersC < 5) {
            // Retrieve the data from the Intent
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            boolean isChipChecked = intent.getBooleanExtra("chip", false);
            int seekBarProgress = intent.getIntExtra("seekBar", 0);
            users[usersC] = new User(usersC, username, password);
            usersC++;
        }


        logInButton = findViewById(R.id.logInButton);
        visitorButton = findViewById(R.id.visitorButton);
        signUpButton = findViewById(R.id.signUpButton);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        logInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick called");
                if (usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")) {
                    //Pop-up message "There is no input"
                    Toast.makeText(LogInActivity.this, "Λείπει το όνομα χρήστη ή ο κωδικός πρόσβασης!", Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int i = 0; i < users.length; i++) {
                        if (users[i] != null) {
                            if (users[i].getUsername().contentEquals(usernameInput.getText()) && users[i].getPassword().contentEquals(passwordInput.getText())) {
                                //Start activity 2
                                Intent intent = new Intent(LogInActivity.this, ChatActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                    Log.d("LogInFailed", "User tried to join with username: " + usernameInput.getText() + " and password: " + passwordInput.getText() + ".");
                    Toast.makeText(LogInActivity.this, "Λανθασμένα στοιχεία!", Toast.LENGTH_SHORT).show();
                }//Start activity 2
                //Intent intent = new Intent(LogInActivity.this, ChatActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //startActivity(intent);
            }
        });

        visitorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick called");
                //Start activity 2
                Intent intent = new Intent(LogInActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               Log.d(TAG, "onClick called");
               //Start sign up activity
               Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
               startActivity(intent);
           }
        });
    }



}