package com.example.myapplication;

import android.util.Log;

public class Request {
    private int id;
    private String text;
    private String intention;
    double book = 0; //Κράτηση
    double many = 0; //Κράτηση - υποπερίπτωση με πολλά
    double cancel = 0; //Ακύρωση
    double report = 0; //Αναφορά
    double info = 0; //Πληροφόρηση

    public Request(int id, String text, String intention) {
        this.id = id;
        this.text = text;
        if (intention != null) {
            this.intention = intention;
        }
        else {
            findIntent(text);
        }
    }

    public void findIntent(String text){
        // TODO Find intent of the request
        //Split text to array of words
        String[] words = text.split(" ");

        Log.d("Intention", "text: " + text);

        String[] bookKeyWords = {
                "θέλω", "κλείσω", "αγοράσω", "κλείνω", "κλίνω", "αγορά", "κρατήσω", "κράτηση",
                "ένα", "εισιτήριο", "απόψε", "σήμερα", "κλείσεις", "κλείστε", "αγοράζω",
                "αγόρασα", "αγοράζεις", "αγοράζουμε", "κρατήσεις", "κρατάω", "κρατάμε", "νέα",
                "πραγματοποιήσω",
                "κρατάς", "θέλαμε", "θέλεις", "θέλω να κλείσω", "να αγοράσω", "κλείσω εισιτήριο"
        };

        String[] bookManyKeyWords = {
                "θέλουμε", "για", "και", "δύο", "2", "3", "τρία", "4", "τέσσερα", "τέσσερις",
                "τρεις", "απόψε", "θέλω", "πέντε", "5", "μένα", "άλλο", "άλλοι", "άλλων",
                "μαζί", "ομάδα", "παρέα", "πολλούς", "αρκετοί", "παραπάνω", "επιπλέον",
                "εμείς", "μας", "μας", "κλείσουμε", "ακόμα", "μερικούς", "μερικοί", "κάποιοι"
        };

        String[] cancelKeyWords = {
                "θέλω", "ακυρώνω", "ακύρωση", "ακύρωσε", "ακύρωσα", "ακύρωσες", "ακυρώσω", "ακυρώσει",
                "ακυρώσεις", "ακυρώσετε", "εισιτήριο", "κράτηση", "ακύρωση εισιτηρίου", "εισιτηρίου",
                "εισιτηρίων", "ακυρώνω εισιτήριο", "ακυρώνω κράτηση", "ακυρώνω παραγγελία", "ακυρωτικά"
        };

        String[] reportKeyWords = {
                "ζητώ", "ζητήσω", "δεν", "λάθος", "κακός", "κακή", "κακό", "λανθασμένο",
                "λανθασμένος", "λανθασμένη", "μπερδεύτηκα", "μπέρδεψε", "λάθη", "!", "δυστυχώς",
                "άθλιος", "άθλια", "άθλιο", "παράπονο", "παράπονα", "αναφορά", "καταγγελία",
                "πρόβλημα", "προβλήματα", "διαμαρτυρία", "αρνητική", "αναφορά προβλήματος"
        };

        String[] infoKeyWords = {
                "ζητώ", "πείτε", "πες", "ποιος", "πού", "πότε", "ποια", "ποιες", "ποια", "ποιοι",
                "ποιων", "τι", "ενημέρωσε", "ενημέρωση", "βρίσκεται", "τοποθεσία", "είναι",
                "μέρος", "διεύθυνση", "οδός", "αριθμός", "σήμερα", "παραστάσεις", "πληροφορίες",
                "πληροφορία", "λεπτομέρειες", "λεπτομέρεια", "στοιχεία", "πρόγραμμα", "ώρα",
                "χρόνος", "πότε γίνεται", "πότε είναι", "μέρος", "σε"
        };
        // Count probabilities
        for (String word : words) {
            for (String keyword : bookKeyWords) {

                if (word.contentEquals(keyword) || word.toLowerCase().contentEquals(keyword)) {
                    book++;
                    Log.d("Intention", "book: " + book);
                }
            }
            for (String keyword : bookManyKeyWords) {

                if (word.contentEquals(keyword) || word.toLowerCase().contentEquals(keyword)) {
                    many++;
                    Log.d("Intention", "many: " + many);
                }
            }
            for (String keyword : cancelKeyWords) {

                if (word.contentEquals(keyword) || word.toLowerCase().contentEquals(keyword)) {
                    cancel += 1.4;
                    Log.d("Intention", "cancel: " + cancel);
                }
            }
            for (String keyword : reportKeyWords) {

                if (word.contentEquals(keyword) || word.toLowerCase().contentEquals(keyword)) {
                    report++;
                    Log.d("Intention", "report: " + report);
                }
            }
            for (String keyword : infoKeyWords) {

                if (word.contentEquals(keyword) || word.toLowerCase().contentEquals(keyword)) {
                    info++;
                    Log.d("Intention", "info: " + info);
                }
            }
        }

        //Softmax
        //double sum = book + many + cancel + report + info;
        //book /= sum;
        //many /= sum;
        //cancel /= sum;
        //report /= sum;
        //info /= sum;
        //Print

        if (book > many && book > cancel && book > report && book > info && book > 0.13) {
            this.intention = "BOOK_ONE";
            return;
        }
        else if (many > book && many > cancel && many > report && many > info && many > 0.13) {
            this.intention = "BOOK_MANY";
            return;
        }
        else if (cancel > book && cancel > many && cancel > report && cancel > info && cancel > 0.13) {
            this.intention = "CANCEL";
            return;
        }
        else if (report > book && report > many && report > cancel && report > info && report > 0.13) {
            this.intention = "REPORT";
            return;
        }
        else if (info > book && info > many && info > cancel && info > report && info > 0.13) {
            this.intention = "INFO";
            return;
        }

        this.intention = "UNKNOWN";
        // this.intent = ...
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getIntent() {
        return intention;
    }
}
