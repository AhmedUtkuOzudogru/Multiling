package com.example.multiling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
    private Button nextButton;
    private ProgressBar flashcardProgressBar;

    private List<FlashcardData> flashcards;
    private int currentIndex = 0;
    private String level;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    private String userID,noOfFlashcard;
    private  int numberOfFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        flashcardQuestionText = findViewById(R.id.flashcardQuestionText);
        flashcardAnswer1 = findViewById(R.id.flashcardAnswer1);
        flashcardAnswer2 = findViewById(R.id.flashcardAnswer2);
        flashcardAnswer3 = findViewById(R.id.flashcardAnswer3);
        nextButton = findViewById(R.id.nextButton);
        flashcardProgressBar = findViewById(R.id.flashcardProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        BottomNavigationView bottomNavigation = findViewById(R.id.flashcardNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_writingexercises);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigator_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_profile) {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_settings) {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_flashcard) {
                    return true;
                } else if (item.getItemId() == R.id.navigator_writingexercises) {
                    startActivity(new Intent(getApplicationContext(), WritingExercise.class));
                    return true;
                }
                return false;
            }
        });

        if (currentUser != null) {
            userID = currentUser.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        level = value.getString("proficiencyLevel");
                        noOfFlashcard= value.getString("noOfFlashcard");
                        numberOfFlashcard = Integer.parseInt(noOfFlashcard);

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
                checkAnswer(selectedButton);
            }
        };

        flashcardAnswer1.setOnClickListener(answerClickListener);
        flashcardAnswer2.setOnClickListener(answerClickListener);
        flashcardAnswer3.setOnClickListener(answerClickListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextFlashcard();
            }
        });
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

            // Reset button colors
            flashcardAnswer1.setBackgroundColor(Color.parseColor("#800080")); // Purple
            flashcardAnswer2.setBackgroundColor(Color.parseColor("#800080")); // Purple
            flashcardAnswer3.setBackgroundColor(Color.parseColor("#800080")); // Purple

            // Enable buttons
            flashcardAnswer1.setEnabled(true);
            flashcardAnswer2.setEnabled(true);
            flashcardAnswer3.setEnabled(true);
        }
    }

    private void checkAnswer(Button selectedButton) {
        FlashcardData currentFlashcard = flashcards.get(currentIndex);
        String selectedOption = selectedButton.getText().toString();

        if (selectedOption.equals(currentFlashcard.getCorrectTranslation())) {
            selectedButton.setBackgroundColor(Color.GREEN);
        } else {
            selectedButton.setBackgroundColor(Color.RED);
        }

        // Highlight correct answer in green
        if (flashcardAnswer1.getText().toString().equals(currentFlashcard.getCorrectTranslation())) {
            flashcardAnswer1.setBackgroundColor(Color.GREEN);
        }
        if (flashcardAnswer2.getText().toString().equals(currentFlashcard.getCorrectTranslation())) {
            flashcardAnswer2.setBackgroundColor(Color.GREEN);
        }
        if (flashcardAnswer3.getText().toString().equals(currentFlashcard.getCorrectTranslation())) {
            flashcardAnswer3.setBackgroundColor(Color.GREEN);
        }

        // Disable buttons after selection
        flashcardAnswer1.setEnabled(false);
        flashcardAnswer2.setEnabled(false);
        flashcardAnswer3.setEnabled(false);
    }

    private void loadNextFlashcard() {
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
