package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "your_channel_id";


    SeekBar seekBar;
    Button applyButton;
    Switch chip;
    Button report;
    TextView paymentMethods;
    TextView help;
    Button menuButton;
    Button backToChat;
    ImageButton market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        createNotificationChannel();
        seekBar = findViewById(R.id.seekBar);
        applyButton = findViewById(R.id.applyButton);
        applyButton.setEnabled(false);
        //Make the background more gray-ish
        applyButton.setBackgroundColor(0xFFE0E0E0);
        chip = findViewById(R.id.switch1);
        report = findViewById(R.id.reportButton);
        paymentMethods = findViewById(R.id.paymentMethods);
        help = findViewById(R.id.help);
        menuButton = findViewById(R.id.menuButton);
        backToChat = findViewById(R.id.backToChat1);
        market = findViewById(R.id.imageButton3);

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        });
        backToChat.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        });

        paymentMethods.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PaymentSaved.class);
            startActivity(intent);
            finish();
        });

        help.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, HelpComActivity.class);
            startActivity(intent);
            finish();
        });

        //Set as default value of the progress of the seekbar the current font size preferred if this exists
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.contains("font_size")) {
            int fontSize = sharedPreferences.getInt("font_size", 16);
            TextView textView = findViewById(R.id.fontSize);
            textView.setText(Integer.toString(fontSize));
            //Set as value of the progress bar
            // 14 font size --> 0 %
            // 15 font size --> 20 %
            // 16 font size --> 50 %
            // 17 font size --> 75 %
            // 18 font size --> 100 %
            seekBar.setProgress((int) ((fontSize - 14) * 100 / 3.35));
        } else {
            Log.d(TAG, "Not found font size");
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                applyButton.setEnabled(true);
                //Set the background color to blue
                applyButton.setBackgroundColor(0xFF00AAFF);
                TextView textView = findViewById(R.id.fontSize);
                textView.setText(Integer.toString(getFontSize(progress)));
                textView.setTextSize(getFontSize(progress));
                // Set the preference of the font size
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("font_size", getFontSize(progress));
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            applyButton.setEnabled(true);
            applyButton.setBackgroundColor(0xFF00AAFF);
            createNotification();
        });

        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ChatActivity.class);
            intent.putExtra("chip", chip.isChecked());
            intent.putExtra("seekBar", seekBar.getProgress());
            Toast.makeText(MenuActivity.this, "Οι αλλαγές πραγματοποιήθηκαν.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });

        report.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, Report.class);
            startActivity(intent);
            finish();
        });
    }

    public static int getFontSize(int progressValue) {
        return 14 + 4 * progressValue / 100;
    }

    public void createNotification() {
        // Check if the notification permission is granted (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                return;
            }
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.theater);
        int newWidth = 48; // new width in pixels
        int newHeight = 48; // new height in pixels
        Bitmap scaledLargeIcon = Bitmap.createScaledBitmap(largeIcon, newWidth, newHeight, false);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MenuActivity.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.theater)
                .setLargeIcon(scaledLargeIcon)
                .setContentTitle("Ειδοποίηση από το MyTheater.")
                .setContentText("Όταν πλησιάζει η ώρα της παράστασης θα ενημερωθείτε. 😃")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MenuActivity.this);

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel Name";
            String description = "Your Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, create the notification
                createNotification();
            } else {
                // Permission denied, show a message to the user or handle accordingly
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}