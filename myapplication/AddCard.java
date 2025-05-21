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

public class AddCard extends AppCompatActivity {

    Button Continue;
    String name;
    String cardNumber;
    String expiryDate;

    EditText name1;
    EditText cardNumber1;
    EditText expiryDate1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_card);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name1 = findViewById(R.id.name);
        cardNumber1 = findViewById(R.id.number);
        expiryDate1 = findViewById(R.id.date);

        name = name1.getText().toString();
        cardNumber = cardNumber1.getText().toString();
        expiryDate = expiryDate1.getText().toString();
        Continue = findViewById(R.id.Continue);
        Continue.setOnClickListener(v -> {

            Intent intent = new Intent(AddCard.this, PaymentSaved.class);
            //Add the extras
            intent.putExtra("type", "mastercard");
            intent.putExtra("name", name);
            intent.putExtra("cardNumber", cardNumber);
            intent.putExtra("expiryDate", expiryDate);
            Toast.makeText(AddCard.this, "Payment method added", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }
}