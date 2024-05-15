package com.example.multiling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flashcard extends AppCompatActivity {

    private TextView flashcardQuestionText;
    private Button flashcardAnswer1;
    private Button flashcardAnswer2;
    private Button flashcardAnswer3;
    private ProgressBar flashcardProgressBar;

    private List<FlashcardData> flashcards;
    private int currentIndex = 0;
    private String level;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        flashcardQuestionText = findViewById(R.id.flashcardQuestionText);
        flashcardAnswer1 = findViewById(R.id.flashcardAnswer1);
        flashcardAnswer2 = findViewById(R.id.flashcardAnswer2);
        flashcardAnswer3 = findViewById(R.id.flashcardAnswer3);
        flashcardProgressBar = findViewById(R.id.flashcardProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userID = currentUser.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        level = value.getString("proficiencyLevel");
                        if (level != null) {
                            Toast.makeText(Flashcard.this, "User level: " + level, Toast.LENGTH_SHORT).show();
                            flashcards = loadFlashcards(level);
                            flashcardProgressBar.setMax(flashcards.size());
                            flashcardProgressBar.setProgress(currentIndex + 1);
                            loadFlashcard();
                        } else {
                            Toast.makeText(Flashcard.this, "Level field not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Flashcard.this, "User level not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if the user is not authenticated
        }

        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button selectedButton = (Button) v;
                checkAnswer(selectedButton.getText().toString());
            }
        };

        flashcardAnswer1.setOnClickListener(answerClickListener);
        flashcardAnswer2.setOnClickListener(answerClickListener);
        flashcardAnswer3.setOnClickListener(answerClickListener);
    }

    private List<FlashcardData> loadFlashcards(String level) {
        List<FlashcardData> flashcardList = new ArrayList<>();
        int resourceId = R.raw.easy_words; // Default to easy words

        switch (level.toLowerCase()) {
            case "intermediate":
                resourceId = R.raw.intermediate_words;
                break;
            case "advanced":
                resourceId = R.raw.advanced_words;
                break;
            case "beginner":
            default:
                resourceId = R.raw.easy_words;
                break;
        }

        try {
            InputStream is = getResources().openRawResource(resourceId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObject = wordsArray.getJSONObject(i);
                String english = wordObject.getString("english");
                String correct = wordObject.getString("correct");
                JSONArray incorrectArray = wordObject.getJSONArray("incorrect");

                List<String> options = new ArrayList<>();
                options.add(correct);
                for (int j = 0; j < incorrectArray.length(); j++) {
                    options.add(incorrectArray.getString(j));
                }
                Collections.shuffle(options);

                flashcardList.add(new FlashcardData(english, correct, options));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flashcardList;
    }

    private void loadFlashcard() {
        if (flashcards != null && !flashcards.isEmpty()) {
            FlashcardData currentFlashcard = flashcards.get(currentIndex);
            flashcardQuestionText.setText(currentFlashcard.getEnglishWord());
            List<String> options = currentFlashcard.getOptions();
            flashcardAnswer1.setText(options.get(0));
            flashcardAnswer2.setText(options.get(1));
            flashcardAnswer3.setText(options.get(2));
        }
    }

    private void checkAnswer(String selectedOption) {
        FlashcardData currentFlashcard = flashcards.get(currentIndex);
        if (selectedOption.equals(currentFlashcard.getCorrectTranslation())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect! The correct answer is: " + currentFlashcard.getCorrectTranslation(), Toast.LENGTH_LONG).show();
        }

        currentIndex++;
        if (currentIndex >= flashcards.size()) {
            currentIndex = 0;
        }
        flashcardProgressBar.setProgress(currentIndex + 1);
        loadFlashcard();
    }

    private static class FlashcardData {
        private final String englishWord;
        private final String correctTranslation;
        private final List<String> options;

        public FlashcardData(String englishWord, String correctTranslation, List<String> options) {
            this.englishWord = englishWord;
            this.correctTranslation = correctTranslation;
            this.options = options;
        }

        public String getEnglishWord() {
            return englishWord;
        }

        public String getCorrectTranslation() {
            return correctTranslation;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
