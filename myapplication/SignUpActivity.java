package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static androidx.constraintlayout.widget.Constraints.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Switch chip;
    Button creationButton;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load layout "sign_up.xml"
        setContentView(R.layout.sign_up);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        try {
            Thread.sleep(30);
        }
        catch (InterruptedException e) {
            Log.d(ConstraintLayoutStates.TAG, "Interrupted");
        }
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        creationButton = findViewById(R.id.creationButton);
        chip = findViewById(R.id.switch1);
        seekBar = findViewById(R.id.seekBar);

        creationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick called");
                if (usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")) {
                    //Pop-up message "There is no input"
                    Toast.makeText(getApplicationContext(), "There is no input", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Pass the data to the login activity so that a new user is created
                    String username = usernameInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    // Pass the data
                    Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("chip", chip.isChecked());
                    intent.putExtra("seekBar", seekBar.getProgress());
                    startActivity(intent);
                }
        }});

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = findViewById(R.id.fontSize);
                textView.setText(Integer.toString(getFontSize(progress)));
                textView.setTextSize(getFontSize(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public static int getFontSize(int progressValue){
        return 14 + 4 * progressValue / 100;
    }
}