package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentSaved extends AppCompatActivity {

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.payment_saved);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        add = findViewById(R.id.button);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSaved.this, AddPaymentMethod.class);
            startActivity(intent);
        });


        //If there are extras in the intent
        if (getIntent().getExtras() != null) {
            ScrollView scroll = findViewById(R.id.scroll);
            LinearLayout container = findViewById(R.id.container);
            //Retrieve the data from the Intent
            if (getIntent().getExtras().getString("type") != null) {
                if (getIntent().getExtras().getString("type").equals("mastercard")) {
                    //Get name, card number, expiration date from the extras
                    String name = getIntent().getExtras().getString("name");
                    String cardNumber = getIntent().getExtras().getString("cardNumber");
                    String expirationDate = getIntent().getExtras().getString("expirationDate");
                    //Inflate a new piece of the layout card_pay.xml with the correct data in the ScrollView "scroll"
                    LayoutInflater inflater = LayoutInflater.from(PaymentSaved.this);
                    View cardLayout =inflater.inflate(R.layout.card_pay, scroll, false);
                    TextView digits = cardLayout.findViewById(R.id.digits);
                    TextView expires = cardLayout.findViewById(R.id.expires);
                    TextView owner = cardLayout.findViewById(R.id.owner);
                    digits.setText(cardNumber);
                    expires.setText(expirationDate);
                    owner.setText(name);
                    container.addView(cardLayout);
                    Toast.makeText(this, "Mastercard Added", Toast.LENGTH_SHORT).show();
                }
                if (getIntent().getExtras().getString("type").equals("paypal")) {
                    // Get username, email from the intent
                    String username = getIntent().getExtras().getString("username");
                    String email = getIntent().getExtras().getString("email");
                    //Inflate a new piece of the layout paypal.xml with the correct data in the ScrollView "scroll"
                    LayoutInflater inflater = LayoutInflater.from(PaymentSaved.this);
                    View paypalLayout =inflater.inflate(R.layout.paypal, scroll, false);
                    TextView usernameText = paypalLayout.findViewById(R.id.owner);
                    TextView emailText = paypalLayout.findViewById(R.id.mail);
                    usernameText.setText(username);
                    emailText.setText(email);
                    container.addView(paypalLayout);
                    Toast.makeText(this, "Paypal Added", Toast.LENGTH_SHORT).show();
                }
            }
            //Inflate a new layout (mastercard or paypal)
            //TODO
        }

    }
}