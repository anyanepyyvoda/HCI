package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class AddPaymentMethod extends AppCompatActivity {
    Button backButton;
    Button mastercardButton;
    Button paypalButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_payment_method);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddPaymentMethod.this, PaymentSaved.class);
            startActivity(intent);
        });
        mastercardButton = findViewById(R.id.mastercardButton);
        mastercardButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddPaymentMethod.this, AddCard.class);
            startActivity(intent);
        });
        paypalButton = findViewById(R.id.paypalButton);
        paypalButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddPaymentMethod.this, AddPaypal.class);
            startActivity(intent);
        });

    }
}