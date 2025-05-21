package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddPaypal extends AppCompatActivity {

    Button Continue;
    String username;
    String email;
    String password;
    EditText usernameEdit;
    EditText emailEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_paypal);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        usernameEdit = findViewById(R.id.username2);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password2);
        Continue = findViewById(R.id.Continue);
        Continue.setOnClickListener(v -> {
            username = usernameEdit.getText().toString();
            email = emailEdit.getText().toString();
            password = passwordEdit.getText().toString();
            Intent intent = new Intent(AddPaypal.this, PaymentSaved.class);
            intent.putExtra("type", "paypal");
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            Toast.makeText(AddPaypal.this, "Payment method added", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }
}