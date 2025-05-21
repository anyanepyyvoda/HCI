package com.example.myapplication;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintSet;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "chat";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    public Theater theater;
    private static final String TAG = "ChatActivity";
    public static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageButton[] speakerButtons = new ImageButton[500];
    TextView[] texts = new TextView[500];
    TextToSpeech textToSpeech;
    ImageButton sendButton;
    ConstraintLayout responseLayout;
    ConstraintLayout requestLayout;
    LinearLayout mainLayout;
    Button menuButton;
    ImageButton marketButton;
    Request[] requests = new Request[500];
    String[] allTextRequests = new String[500];
    String[] allTextResponses = new String[500];
    Response[] responses = new Response[500];
    int requestCount;
    int responseCount;
    int speakerCount = 0;
    Locale greekLocale;
    short reload;

    HashMap<String, String> speakerTextMap = new HashMap<>();
    String currentIntention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_SPEECH_INPUT);
        }

        // Initialize shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        theater = new Theater();
        Log.d("NLP", "Theater initialized" + theater.toString());
        // if there is a value for the text size
        if (sharedPreferences.contains("font_size")) {
            int textSize = sharedPreferences.getInt("font_size", 16);
            TextView textView = findViewById(R.id.text1);
            textView.setTextSize(textSize);
            //Confirm the change of the value
            Log.d(TAG, "Font size changed to " + sharedPreferences.getInt("font_size", 16));
        }

        // Initialize UI components
        reload = 0;
        requestCount = 0;
        responseCount = 0;
        responseLayout = findViewById(R.id.ResponseA);
        requestLayout = findViewById(R.id.RequestA);
        mainLayout = findViewById(R.id.MainLinear);
        menuButton = findViewById(R.id.menuButton);
        marketButton = findViewById(R.id.imageButton3);
        greekLocale = new Locale("el", "GR");
        sendButton = findViewById(R.id.sendButton);
        ImageButton imageButton4 = findViewById(R.id.imageButton4); //microphone

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        //Sleep for 30ms
        try {
            Thread.sleep(30);
        }
        catch (InterruptedException e) {
            Log.d(TAG, "Interrupted");
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
        //
        currentIntention = null;
        requestCount = 0;
        responseCount = 0;
        responseLayout = findViewById(R.id.ResponseA);
        requestLayout = findViewById(R.id.RequestA);
        mainLayout = findViewById(R.id.MainLinear);
        menuButton = findViewById(R.id.menuButton);
        greekLocale = new Locale("el", "GR");
        //
        Log.d(TAG, "Resource ID: " + requestLayout.getId());
        sendButton = findViewById(R.id.sendButton);
        //
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick called");
                //Create a new message on the screen
                try {
                    LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
                    View messageLayout = inflater.inflate(R.layout.request, mainLayout, false);
                    // Modify the text
                    TextView textView = messageLayout.findViewById(R.id.text3);
                    TextView textView2 = findViewById(R.id.keyboardInput);
                    ImageButton requestSpeakerButton = messageLayout.findViewById(R.id.speakerButton1);
                    Log.d("ID", String.valueOf(requestSpeakerButton.getId()) + " " + Integer.toString(requestSpeakerButton.getId()));
                    requestSpeakerButton.setTag("req" + String.valueOf(requestCount));
                    textView.setText(textView2.getText().toString());
                    speakerTextMap.put("req" + String.valueOf(requestCount), textView.getText().toString());
                    Log.d("AddedHash", "Added tag = " + "req" + String.valueOf(requestCount) + " " + speakerTextMap.get("req" + String.valueOf(requestCount)));
                    requests[requestCount] = new Request(requestCount, textView.getText().toString(), currentIntention);
                    requestCount++;
                    Log.d(TAG, String.valueOf(requestCount));
                    //Get the preferred font size and set it to the text view
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    int textSize = sharedPreferences.getInt("font_size", 16);
                    textView.setTextSize(textSize);
                    //Check if the text is too long to be displayed in the textView
                    if (textView.getLineCount() > 3) {
                        //Make the textview taller
                        textView.setHeight(textView.getLineHeight() * 2);

                    }
                    mainLayout.addView(messageLayout);
                    Log.d(TAG, "Added request layout");
                    // Scroll to the bottom to show the new message
                    //Sleep for 0.5sec
                    try {
                        Thread.sleep(500);
                    }
                    catch (Exception e) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                    View anotherLayout = inflater.inflate(R.layout.response, mainLayout, false);
                    // Create response and modify text
                    TextView textView3 = anotherLayout.findViewById(R.id.text3);
                    ImageButton responseSpeakerButton = anotherLayout.findViewById(R.id.speakerButton3);
                    Log.d("ID", Integer.toString(responseSpeakerButton.getId()));
                    responseSpeakerButton.setTag("res" + String.valueOf(responseCount));
                    Log.d("Reload", Integer.toString(reload));
                    if (reload > 0){
                        Log.d("Reload", "Creating response with id : " + Integer.toString(reload - 1));
                        responses[responseCount] = new Response(reload-1, textView2.getText().toString(), requests[requestCount - 1].getIntent(), theater.getAvailability(1), theater.getAvailability(2));
                        reload++;
                    }
                    else {
                        responses[responseCount] = new Response(responseCount, textView2.getText().toString(), requests[requestCount - 1].getIntent(), theater.getAvailability(1), theater.getAvailability(2));
                    }

                    theater.setAvailability(1, responses[responseCount].getAvailability(1));
                    theater.setAvailability(2, responses[responseCount].getAvailability(2));//Reload the theater with the new seats etc.
                    Log.d("Intention", requests[requestCount - 1].getIntent().toString());
                    currentIntention = requests[requestCount - 1].getIntent();
                    if (responses[responseCount].getText().toString() != null) {
                        Log.d("ResponseText", responses[responseCount].getText().toString());
                    }
                    else {
                        Log.d("ResponseText", "null");
                    }
                    textView3.setText(responses[responseCount].getText());
                    speakerTextMap.put("res" + String.valueOf(responseCount), textView3.getText().toString());
                    Log.d("AddedHash", "Added hash to tag = " + "res" + String.valueOf(responseCount) + " " + speakerTextMap.get("res" + String.valueOf(responseCount)));
                    Log.d("AddedResponseText", "Added response text");

                    responseCount++;
                    Log.d(TAG, String.valueOf(responseCount));
                    //Get the preferred font size and set it to the text view
                    textSize = sharedPreferences.getInt("font_size", 16);
                    //Check if the text is too long to be displayed in the textView
                    if (extractNumberOfCharacters(textView3) > 90) {
                        Log.d("CHAR", "Text is too long to be displayed in the textView");

                        int newHeight = textView3.getLayoutParams().height + 25 * extractNumberOfCharacters(textView3) / 18;
                        Log.d("CHAR", "Old height: " + textView3.getLayoutParams().height);
                        Log.d("CHAR", "New height: " + newHeight);
                        // Set the new height for the TextView
                        textView3.setLayoutParams(new LinearLayout.LayoutParams(textView3.getLayoutParams().width, newHeight));
                        // Set the layout_marginStart to 16 dp
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView3.getLayoutParams();
                        layoutParams.setMarginStart(30);
                        textView3.setLayoutParams(layoutParams);
                    }
                    textView3.setTextSize(textSize);
                    mainLayout.addView(anotherLayout);
                    Log.d(TAG, "Added response layout");
                    // Remove text from keyboardInput
                    textView2.setText("");
                    if (responses[responseCount - 1].getMadeBooking()) {
                        //Create notification "A booking has been made"
                        createNotificationChannel();
                        createNotification();
                    }

                    // Add text-to-speech functionality in both response and request
                    // Find speakers

                    // Create buttons
                    speakerButtons[speakerCount] = requestSpeakerButton;
                    speakerButtons[speakerCount + 1] = responseSpeakerButton;
                    speakerCount = speakerCount + 2;
                    for (int i = 0; i < speakerCount; i++) {
                        Log.d("TAG", speakerButtons[i].getTag().toString());
                        for (int j = 0; j < speakerCount; j++) {
                            if (speakerButtons[i].getId() == speakerButtons[j].getId() && i!= j) {
                                Log.d("Duplicate", "Duplicate speaker button found");
                                speakerButtons[i].setId(getUniqueId(speakerButtons[i].getId()));
                            }
                            if (speakerButtons[i].getTag() == speakerButtons[j].getTag() && i!= j) {
                                Log.d("Duplicate", "Duplicate speaker button found - same tag");
                                speakerButtons[i].setId(getUniqueId(speakerButtons[i].getId()));
                            }
                        }
                        Log.d("ID", Integer.toString(speakerButtons[i].getId()));
                    }

                    String tag = "req"+String.valueOf(requestCount - 1);
                    for (int i = 0; i < speakerCount; i++) {
                        if (speakerButtons[i].getId() == requestSpeakerButton.getId() && tag.equals(speakerButtons[i].getTag().toString())) {
                            Log.d("AddedHash", "Found a speaker button with id " + requestSpeakerButton.getId() + " and tag " + requestSpeakerButton.getTag().toString());

                        }
                    }


                    requestSpeakerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.d("Speaker", "Clicked speaker button with id = " + speakerButtons[speakerCount - 2].getId() + " and tag = " + speakerButtons[speakerCount - 2].getTag().toString());
                            //Convert text to voice
                            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if(status != TextToSpeech.ERROR) {
                                        int result = textToSpeech.setLanguage(greekLocale);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.d("Speaker", "Language not supported");
                                        }
                                        // Find text from the hashmap
                                        for (int i = 0; i < speakerButtons.length; i++) {
                                            if (requestSpeakerButton.getTag().toString().equals("req" + String.valueOf(i))) {
                                                textToSpeech.speak(speakerTextMap.get("req" + String.valueOf(i)), TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        }


                                        //textToSpeech.speak(requests[requestCount - 1].getText(), TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            });
                            Log.d("Speaker", "Text to speech ended");
                        }
                    });
                    responseSpeakerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.d("Speaker", "Clicked speaker button with id = " + speakerButtons[speakerCount - 1].getId() + " and tag = " + speakerButtons[speakerCount - 1].getTag().toString());
                            //Convert text to voice
                            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if(status != TextToSpeech.ERROR) {
                                        int result = textToSpeech.setLanguage(greekLocale);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.d("Speaker", "Language not supported");
                                        }
                                        for (int i = 0; i < speakerButtons.length; i++) {
                                            if (responseSpeakerButton.getTag().toString().equals("res" + String.valueOf(i))) {
                                                textToSpeech.speak(speakerTextMap.get("res" + String.valueOf(i)), TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        }
                                        //textToSpeech.speak(responses[responseCount - 1].getText(), TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            });
                    }});
                }
                catch (Exception e) {
                    Log.d(TAG, "Exception: " + e.getMessage());
                }
                if (responses[responseCount - 1].reload()) {
                    Log.d("Reload", "Reloading");
                    reload = 1;

                }
            }
        });

        // Setup menu button click listener
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        texts[0] = findViewById(R.id.text1);
        Log.d(TAG, "Text view found");
        ImageButton speaker = findViewById(R.id.speakerButton);
        Log.d(TAG, "Speaker button found");
        //speakerButtons[0] = speaker;
        speaker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Speaker", "Clicked speaker button");
                //Convert text to voice
                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(ChatActivity.this.getResources().getConfiguration().locale);
                            textToSpeech.speak(texts[0].getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
                Log.d("Speaker", "Text to speech ended");
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Menu", "Clicked menu button");
                Intent intent = new Intent(ChatActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return;
            }
        });

        marketButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Market", "Clicked market button");
                Intent intent = new Intent(ChatActivity.this, Market.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return;
            }
        });

    }

    public ImageButton findSpeakerByIdAndOrder(int id, int order) {
        int count = 0;
        for (int i = 0; i < speakerCount; i++) {
            if (speakerButtons[i].getId() == id) {
                if (count == order) {
                    return speakerButtons[i];
                }
                count++;
            }
        }
        return null;
    }

    public int getUniqueId(long currentId) {
        try {
            long currentTimeMillis = System.currentTimeMillis() / 300;
            currentId = currentId + currentTimeMillis;
            // Convert integer to byte array
            byte[] idBytes = intToBytes(currentId);

            // Get SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Calculate hash value
            byte[] hash = digest.digest(idBytes);

            // Convert hash bytes to integer
            int uniqueId = bytesToInt(hash);

            return uniqueId;
        } catch (NoSuchAlgorithmException e) {
            // Handle NoSuchAlgorithmException
            e.printStackTrace();
            return -1; // Return -1 as error indicator
        }
    }

    private byte[] intToBytes(long value) {
        byte[] result = new byte[4]; // Assuming 4-byte integers
        result[0] = (byte) (value >> 24);
        result[1] = (byte) (value >> 16);
        result[2] = (byte) (value >> 8);
        result[3] = (byte) value;
        return result;
    }

    // Convert byte array to integer
    private int bytesToInt(byte[] bytes) {
        return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                // Do something with the spokenText, for example, set it to a TextView or use it as an input
                TextView textView2 = findViewById(R.id.keyboardInput);
                textView2.setText(spokenText);
            }
        }
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "el-GR");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "el-GR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Μιλήστε τώρα...");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition is not supported on your device", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUniqueId(int id) {
        int uniqueId = id;
        boolean isUnique = false;
        while (!isUnique) {
            uniqueId++;
            isUnique = true;
            for (int i = 0; i < speakerCount; i++) {
                if (speakerButtons[i].getId() == uniqueId) {
                    isUnique = false;
                    break;
                }
            }
        }
        return uniqueId;
    }

    public int extractNumberOfCharacters(TextView txt){
        return txt.getText().length();
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatActivity.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.theater)
                .setLargeIcon(scaledLargeIcon)
                .setContentTitle("Ειδοποίηση από το MyTheater.")
                .setContentText("Μόλις πραγματοποιήσατε μία κράτηση. Εξαιρετικά! Μπορείτε να δείτε τα εισιτήριά σας στο \"Καλάθι\". 😃")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ChatActivity.this);

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
