package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.AsynchronousFileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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

public class Response {
    private boolean madeBooking;
    private Context context;
    private boolean reload;
    private int id;
    private String inputText;
    private String intent;
    private String outputText;

    static String requestType; //intent
    static String date;
    static String hour;
    static String room;
    static String amount;
    public Theater theater;
    SharedPreferences sharedPreferences;

    public boolean[][][][] getAvailability(int room) {
        if (room == 1) {
            return this.theater.getAvailability(1);
        } else if (room == 2) {
            return this.theater.getAvailability(2);
        } else {
            return null;
        }
    }

    public boolean reload(){
        return reload;
    }


    public Response(int id, String text, String intent, boolean[][][][] availability1, boolean[][][][] availability2) {
        Log.d("NLP", "Response called");
        this.reload = false;
        this.id = id;
        this.inputText = text;
        this.intent = intent;
        this.madeBooking = false;
        Log.d("NLP", "Intent: " + intent);
        this.theater = new Theater();
        Log.d("NLP", "Theater: " + theater.getName());
        this.theater.setAvailability(1, availability1);
        this.theater.setAvailability(2, availability2);
        Log.d("NLP", "Theater: " + theater.getName());
        NLP();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return outputText;
    }

    public void NLP(){
        Log.d("NLP", "NLP called");
        //TODO Convert message to readable
        // Each word mapped to its closest in the vocabulary
        // e.g. "βΙβλΙο" -> "βιβλίο"
        // Split message to array of words
        //String[] words = inputText.split(" ");
        //for (int i = 0; i < words.length; i++) {
            // TODO Map each word to its closest in the vocabulary
        //    words[i] = mapToClosest(words[i]);
        //}
        // TODO Find the appropriate response text
 // of tickets the client wants to buy

        requestType = intent;

        //Regex of dates = {dd mm, dd/mm, dd-mm, dd.mm}
        //Regex of hours = {h, hh, hh:mm, hh.mm}
        String datePattern = "\\b(\\d{1,2}[\\.\\-/\\s]\\d{1,2})\\b";
        String hourPattern = "\\b(\\d{1,2}[:\\.]?\\d{0,2})\\b";
        String[] possibleRooms = {"δωμάτιο 1", "παράσταση 1", "πρώτη παράσταση", "δωμάτιο 2", "παράσταση 2", "δεύτερη παράσταση"};
        // Combined room pattern
        String roomPattern = "\\b(δωμάτιο\\s*\\d+|παράσταση\\s*\\d+|πρώτη\\s*παράσταση|δεύτερη\\s*παράσταση|τρίτη\\s*παράσταση|τέταρτη\\s*παράσταση|πέμπτη\\s*παράσταση)\\b";
        String[] yesish = {"ναι", "ΝΑΙ", "Ναι", "σωστά", "Σωστά", "αυτό", "όντως", "επιβεβαιώνω", "οκ", "ΟΚ"};
        String[] noish = {"όχι", "ΟΧΙ", "Όχι", "δεν", "μην", "λάθος", "Λάθος"};
        String amountPattern = "\\b(\\d+)\\b";

        if (requestType.equals("BOOK_ONE")){
            if (id == 0){
                this.outputText = "Εντάξει! Πείτε μου ποια ημερομηνία και παράσταση θέλετε να κλείσετε!";
                return;
            }
            if (id == 1){
                Log.d("Received", inputText);
                //Find room, hour, date
                // Find date
                date = extractPattern(inputText, datePattern);
                // Find hour
                hour = extractPattern(inputText, hourPattern);
                // Find room
                room = extractPattern(inputText, roomPattern);
                for (String possibleRoom : possibleRooms) {
                    if (room.equals(possibleRoom)) {
                        if (room.equals("παράσταση 1") || room.equals("πρώτη παράσταση") || room.equals("δωμάτιο 1")){
                            room = "1";
                            break;
                        }
                        else if (room.equals("παράσταση 2") || room.equals("δεύτερη παράσταση") || room.equals("δωμάτιο 2")){
                            room = "2";
                            break;
                        }
                    }
                }
                if (dateToInt(date) < 0 || dateToInt(date) > 50) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ημερομηνία.";
                    reload = true;
                    return;
                }
                if (timeToInt(hour) != 0 && timeToInt(hour) != 1) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ώρα. Οι παραστάσεις προβάλλονται στις 6 μ.μ. και στις 10 μ.μ.";
                    reload = true;
                    return;
                }
                this.outputText = "Οπότε, θέλετε να κλείσετε ένα εισιτήριο στις " + date + ", την ώρα " + hour + " στον χώρο " + room + " όπου προβάλλεται η παράσταση \"" + theater.shows()[Integer.valueOf(room)-1] + "\". Σωστά;";
                return;
            }
            if (id == 2){
                //Detect yes-ish or no-ish words in the input text
                //Convert input text to array of words
                String[] words = inputText.split(" ");
                //Check for yes-ish words
                for (int i = 0; i < words.length; i++) {
                    for (int j = 0; j < yesish.length; j++) {
                        if (words[i].equals(yesish[j])) {
                            this.outputText = "Σωστά! Εντάξει! Ελέγχω επομένως για ένα εισιτήριο στις " + date + ", την ώρα " + hour + " στο " + room + ".";
                            Log.d("NLP", "Now to check availability");
                            boolean available = theater.hasAvailableSeats(dateToInt(date), timeToInt(hour), 1, Integer.parseInt(room));
                            Log.d("NLP", "Availability: " + available);
                            if (available) {
                                this.outputText += "\n \n Βρέθηκαν διαθέσιμα εισιτήρια. Πείτε αν θέλετε να πραγματοποιήσετε την αγορά των εισιτηρίων στην ημερομηνία " + date + " στην ώρα " + hour + " στον χώρο " + room + ".";
                                //Date, hour, room saved as static variables of the class
                                Log.d("NLP", "Available");
                            }
                            else {
                                this.outputText += " Δυστυχώς, δεν βρέθηκαν διαθέσιμα εισιτήρια. Προσπαθήστε ξανά.";
                                reload = true;
                            }
                            return;
                        }
                    }
                }
                //Check for no-ish words
                for (int i = 0; i < words.length; i++) {
                    for (int j = 0; j < noish.length; j++) {
                        if (words[i].equals(noish[j])) {
                            this.outputText = "Μάλιστα!";
                            return;
                        }
                    }
                }
                this.outputText = "Συγγνώμη, δεν κατάλαβα το αίτημά σας! Αναδιατυπώστε!";
                reload = true;
                return;
            }
            if (id == 3){
                //Search for yes-ish or no-ish words
                //Convert input text to array of words
                String[] words = inputText.split(" ");
                //Check for yes-ish words
                for (int i = 0; i < words.length; i++) {
                    for (int j = 0; j < yesish.length; j++) {
                        if (words[i].equals(yesish[j])) {
                            Log.d("Booking", "Yes");
                            if (theater != null) {
                                Log.d("Booking", "zone: " + "1" + ", date: " + dateToInt(date) + ", hour: " + timeToInt(hour) + ", room: " + room);
                                theater.makeBooking(room, 1, 1, timeToInt(hour), dateToInt(date));
                                this.outputText = "Πραγματοποιήθηκε η κράτησή σας στις " + date + ", την ώρα " + hour + " στο " + room + ".";
                                this.madeBooking = true;
                            }
                            else {
                                Log.d("Booking", "Theater is null");
                            }

                            return;
                        }
                    }
                }
            }
        }
        else if (requestType.equals("BOOK_MORE")){
            if (id == 0){
                this.outputText = "Εντάξει! Πείτε μου σε ποια ημερομηνία και παράσταση θέλετε να κλείσετε!";
                return;
            }
            if (id == 1){
                date = extractPattern(inputText, datePattern);
                hour = extractPattern(inputText, hourPattern);
                room = extractPattern(inputText, roomPattern);
                amount = extractPattern(inputText, amountPattern);
                for (String possibleRoom : possibleRooms) {
                    if (room.equals(possibleRoom)) {
                        if (room.equals("παράσταση 1") || room.equals("πρώτη παράσταση") || room.equals("δωμάτιο 1")){
                            room = "1";
                            break;
                        }
                        else if (room.equals("παράσταση 2") || room.equals("δεύτερη παράσταση") || room.equals("δωμάτιο 2")){
                            room = "2";
                            break;
                        }
                    }
                }
                if (dateToInt(date) < 0 || dateToInt(date) > 50) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ημερομηνία.";
                    reload = true;
                    return;
                }
                if (timeToInt(hour) != 0 && timeToInt(hour) != 1) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ώρα. Οι παραστάσεις προβάλλονται στις 6 μ.μ. και στις 10 μ.μ.";
                    reload = true;
                    return;
                }
                this.outputText = "Οπότε, θέλετε να κλείσετε ένα εισιτήριο στις " + date + ", την ώρα " + hour + " στον χώρο " + room + " όπου προβάλλεται η παράσταση \"" + theater.shows()[Integer.valueOf(room)-1] + "\" " + "για " + amount + " εισιτήρια. Σωστά;";
                return;
            }

        }
        else if (requestType.equals("CANCEL")){
            if (id == 0){
                this.outputText = "Εντάξει! Πείτε μου σε ποια ημερομηνία και σε ποια ώρα είναι το εισιτήριο που θέλετε να ακυρώσετε.";
                return;
            }
            if (id == 1){
                date = extractPattern(inputText, datePattern);
                hour = extractPattern(inputText, hourPattern);
                room = extractPattern(inputText, roomPattern);
                amount = extractPattern(inputText, amountPattern);
                for (String possibleRoom : possibleRooms) {
                    if (room.equals(possibleRoom)) {
                        if (room.equals("παράσταση 1") || room.equals("πρώτη παράσταση") || room.equals("δωμάτιο 1")){
                            room = "1";
                            break;
                        }
                        else if (room.equals("παράσταση 2") || room.equals("δεύτερη παράσταση") || room.equals("δωμάτιο 2")){
                            room = "2";
                            break;
                        }
                    }
                }
                if (dateToInt(date) < 0 || dateToInt(date) > 50) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ημερομηνία.";
                    reload = true;
                    return;
                }
                if (timeToInt(hour) != 0 && timeToInt(hour) != 1) {
                    this.outputText = "Παρακαλώ, αναζητήστε κράτηση σε άλλη ώρα. Οι παραστάσεις προβάλλονται στις 6 μ.μ. και στις 10 μ.μ.";
                    reload = true;
                    return;
                }
                this.outputText = "Οπότε, θέλετε να πραγματοποιήσετε ακύρωση κράτησης στις " + date + ", την ώρα " + hour + " στον χώρο " + room + " για " + amount + " εισιτήρια. Σωστά;";
                return;
            }

        }
        else if (requestType.equals("REPORT")){
            if (id == 0){
                this.outputText = "Λυπάμαι για την ταλαιπωρία. Μπορείτε να χρησιμοποιήσετε το εργαλείο \'Αναφορά\' ή το εργαλείο \'Βοήθεια και Επικοινωνία\' για να λάβετε βοήθεια.";
                return;
            }

        }
        else if (requestType.equals("HELP")){
            if (id == 0){
                this.outputText = "Το θέατρο βρίσκεται στην [Οδό] [Αριθμό], [ΤΚ], [Πόλη]. Προβάλλονται οι παραστάσεις [Όνομα] και [Όνομα].";
                return;
            }

        }

        //TODO Actions regarding booking or cancelling
        //Then create message
        this.outputText = "Συγγνώμη, δεν κατάλαβα το αίτημά σας! Αναδιατυπώστε.";
        reload = true;
    }

    private String extractPattern(String inputText, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(inputText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public String mapToClosest(String word){
        //Find closest word in the vocabulary (stored in "Greek.txt")
        List<String> vocabulary = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("Greek.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                vocabulary.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        String closestWord = null;
        int minDistance = Integer.MAX_VALUE;

        for (String vocabWord : vocabulary) {
            int distance = levenshteinDistance(word, vocabWord);
            if (distance < minDistance) {
                minDistance = distance;
                closestWord = vocabWord;
            }
        }

        return closestWord != null ? closestWord : "";
    }



    public int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + costOfSubstitution(a.charAt(i - 1), b.charAt(j - 1)),
                            Math.min(dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    public int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public int dateToInt(String date) {
        // Define possible date formats
        String[] dateFormats = {"dd MM", "dd/MM", "dd-MM", "dd.MM"};

        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Parse the input date string
        Date parsedDate = null;
        for (String format : dateFormats) {
            try {
                parsedDate = new SimpleDateFormat(format, Locale.getDefault()).parse(date);
                break;  // Break the loop if parsing is successful
            } catch (ParseException e) {
                // Continue trying the next format
            }
        }

        if (parsedDate == null) {
            throw new IllegalArgumentException("Date format is not recognized: " + date);
        }

        // Set the parsed date into a Calendar instance
        Calendar inputDate = Calendar.getInstance();
        inputDate.setTime(parsedDate);
        // Since only day and month are parsed, set the year to the current year
        inputDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));

        // Calculate the difference in days between the current date and the input date
        long diffInMillis = inputDate.getTimeInMillis() - currentDate.getTimeInMillis();
        int diffInDays = (int) (diffInMillis / (1000 * 60 * 60 * 24));

        return diffInDays;
    }

    public int timeToInt(String time) {
        //Time is in the form {h, hh, hh:mm, hh.mm}
        // if it is 6 p.m. then return 0
        // if it is 10 p.m. then return 1
        if (time.equals("6") || time.equals("18") || time.equals("18:00") || time.equals("18.00") || time.equals("6 p.m.") || time.equals("18 p.m.") || time.equals("18:00 p.m.") || time.equals("18.00 p.m.") || time.equals("6 pm") || time.equals("18 pm") || time.equals("18:00 pm") || time.equals("18.00 pm") || time.equals("6 μ.μ.") || time.equals("18 μ.μ.") || time.equals("18:00 μ.μ.") || time.equals("18.00 μ.μ.") || time.equals("6 μμ.") || time.equals("18 μμ.") || time.equals("18:00 μμ.") || time.equals("18.00 μμ.")) {
            return 0;
        }
        if (time.equals("10") || time.equals("10:00") || time.equals("10.00") || time.equals("10 pm") || time.equals("10 p.m.") || time.equals("10 μ.μ.") || time.equals("10 μμ")) {
            return 1;
        }
        return 2; //wrong input
    }

    public boolean getMadeBooking(){
        return madeBooking;
    }

}