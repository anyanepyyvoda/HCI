package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Market extends AppCompatActivity {

    Button chat;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_market);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chat = findViewById(R.id.backToChat);
        chat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });
        menu = findViewById(R.id.menuButton);
        menu.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });
    }
}