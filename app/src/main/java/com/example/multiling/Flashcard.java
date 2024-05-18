package com.example.multiling;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Flashcard extends AppCompatActivity {

    private TextView flashcardQuestionText;
    private Button flashcardAnswer1;
    private Button flashcardAnswer2;
    private Button flashcardAnswer3;
    private Button nextButton;
    private ProgressBar flashcardProgressBar;

    private int currentQuestion = 1; // The current question number

    private List<FlashcardData> flashcards; // List of flashcards
    private int currentIndex = 0; // Current index of the flashcard being displayed
    private String level; // User's proficiency level
    private FirebaseAuth firebaseAuth; // Firebase authentication instance
    private FirebaseFirestore firebaseFirestore; // Firebase Firestore instance
    private boolean isAttemptingToNavigate = false; // Flag to check if the user is attempting to navigate away
    private int numberOfFlashcards; // Total number of flashcards to be shown
    private int numberOfCorrectAnswers = 0; // Counter for the number of correct answers

    private String userID; // User ID of the authenticated user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        // Initialize views
        flashcardQuestionText = findViewById(R.id.flashcardQuestionText);
        flashcardAnswer1 = findViewById(R.id.flashcardAnswer1);
        flashcardAnswer2 = findViewById(R.id.flashcardAnswer2);
        flashcardAnswer3 = findViewById(R.id.flashcardAnswer3);
        nextButton = findViewById(R.id.nextButton);
        flashcardProgressBar = findViewById(R.id.flashcardProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Setup bottom navigation view
        BottomNavigationView bottomNavigation = findViewById(R.id.flashcardNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_flashcard);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item selection
                if (item.getItemId() == R.id.navigator_home) {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_profile) {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), Profile.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_settings) {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), Settings.class));
                    return true;
                } else if (item.getItemId() == R.id.navigator_writingexercises) {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), WritingExercise.class));
                    return true;
                }
                return false;
            }
        });

        // Copy initial data to internal storage if not already copied
        if (!fileExists("easy_words.json")) {
            copyRawResourceToInternalStorage(R.raw.easy_words, "easy_words.json");
        }
        if (!fileExists("intermediate_words.json")) {
            copyRawResourceToInternalStorage(R.raw.intermediate_words, "intermediate_words.json");
        }
        if (!fileExists("advanced_words.json")) {
            copyRawResourceToInternalStorage(R.raw.advanced_words, "advanced_words.json");
        }

        // Register onBackPressed callback using OnBackPressedDispatcher
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if user is attempting to navigate away
                if (isAttemptingToNavigate) {
                    showLeaveWarningDialog(null); // Pass null Intent to stay on current activity
                } else {
                    isEnabled(); // Allow default back button handling
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        showStartConfirmationDialog(); // Show confirmation dialog to start flashcards

        if (currentUser != null) {
            userID = currentUser.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();

            // Listen for changes in user's document in Firestore
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        level = value.getString("proficiencyLevel");
                        String noOfFlashcard = value.getString("noOfFlashcard");

                        if (noOfFlashcard != null) {
                            Toast.makeText(Flashcard.this, "Number of flashcards: " + noOfFlashcard, Toast.LENGTH_SHORT).show();
                            numberOfFlashcards = Integer.parseInt(noOfFlashcard);
                        } else {
                            Toast.makeText(Flashcard.this, "Number of flashcards not found", Toast.LENGTH_SHORT).show();
                            numberOfFlashcards = 10; // Default value if not set
                        }

                        if (level != null) {
                            Toast.makeText(Flashcard.this, "User level: " + level, Toast.LENGTH_SHORT).show();
                            flashcards = loadFlashcards(level); // Load flashcards based on user level
                            if (numberOfFlashcards > flashcards.size()) {
                                numberOfFlashcards = flashcards.size();
                            }
                            flashcards = flashcards.subList(0, numberOfFlashcards);
                            flashcardProgressBar.setMax(numberOfFlashcards);

                            loadFlashcard(); // Load the first flashcard
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

        // Set up click listeners for answer buttons
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

        // Set up click listener for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion < numberOfFlashcards) {
                    flashcardProgressBar.setProgress(currentQuestion);
                    currentQuestion++;
                    loadNextFlashcard();
                } else {
                    // Go to result page when all flashcards are shown
                    Intent intent = new Intent(Flashcard.this, ResultPage.class);
                    intent.putExtra("CORRECT_ANSWERS", numberOfCorrectAnswers); // Pass numberOfCorrectAnswers as an extra
                    intent.putExtra("NUMBER_OF_QUESTIONS", numberOfFlashcards); // Pass numberOfFlashcards as an extra
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // Load flashcards from JSON resource based on user level
    private List<FlashcardData> loadFlashcards(String level) {
        List<FlashcardData> flashcardList = new ArrayList<>();
        String fileName = getFileNameByLevel(level);

        try {
            FileInputStream fis = openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            // Parse JSON data and create flashcard objects
            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObject = wordsArray.getJSONObject(i);
                String english = wordObject.getString("english");
                String correct = wordObject.getString("correct");
                int priority = wordObject.getInt("priority");
                JSONArray incorrectArray = wordObject.getJSONArray("incorrect");

                List<String> options = new ArrayList<>();
                options.add(correct);
                for (int j = 0; j < incorrectArray.length(); j++) {
                    options.add(incorrectArray.getString(j));
                }
                Collections.shuffle(options);

                flashcardList.add(new FlashcardData(english, correct, options, priority));
            }

            // Sort flashcards by priority
            flashcardList.sort(Comparator.comparingInt(FlashcardData::getPriority));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flashcardList;
    }

    // Save flashcards back to file
    private void saveFlashcards() {
        try {
            JSONArray wordsArray = new JSONArray();
            for (FlashcardData card : flashcards) {
                JSONObject wordObject = new JSONObject();
                wordObject.put("english", card.getEnglishWord());
                wordObject.put("correct", card.getCorrectTranslation());
                wordObject.put("priority", card.getPriority());

                // Remove the correct answer from the incorrect options
                List<String> incorrectOptions = new ArrayList<>(card.getOptions());
                incorrectOptions.remove(card.getCorrectTranslation());
                JSONArray incorrectArray = new JSONArray(incorrectOptions);

                wordObject.put("incorrect", incorrectArray);
                wordsArray.put(wordObject);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("words", wordsArray);

            // Write the JSON data to the file
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    openFileOutput(getFileNameByLevel(level), MODE_PRIVATE), StandardCharsets.UTF_8);
            outputStreamWriter.write(jsonObject.toString());
            outputStreamWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyRawResourceToInternalStorage(int resourceId, String fileName) {
        try {
            InputStream is = getResources().openRawResource(resourceId);
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileNameByLevel(String level) {
        switch (level.toLowerCase()) {
            case "intermediate":
                return "intermediate_words.json";
            case "advanced":
                return "advanced_words.json";
            case "beginner":
            default:
                return "easy_words.json";
        }
    }

    private boolean fileExists(String fileName) {
        File file = new File(getFilesDir(), fileName);
        return file.exists();
    }

    // Load the current flashcard onto the screen
    private void loadFlashcard() {
        if (flashcards != null && !flashcards.isEmpty() && currentIndex < flashcards.size()) {
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
        } else {
            // No more flashcards to display, go to result page
            Intent intent = new Intent(Flashcard.this, ResultPage.class);
            intent.putExtra("CORRECT_ANSWERS", numberOfCorrectAnswers); // Pass numberOfCorrectAnswers as an extra
            intent.putExtra("NUMBER_OF_QUESTIONS", numberOfFlashcards); // Pass numberOfFlashcards as an extra
            startActivity(intent);
            finish();
        }
    }

    // Check the user's answer and update flashcard priority
    private void checkAnswer(Button selectedButton) {
        if (flashcards != null && !flashcards.isEmpty() && currentIndex < flashcards.size()) {
            FlashcardData currentFlashcard = flashcards.get(currentIndex);
            String selectedOption = selectedButton.getText().toString();

            // Check if the selected answer is correct
            if (selectedOption.equals(currentFlashcard.getCorrectTranslation())) {
                selectedButton.setBackgroundColor(Color.GREEN);
                this.numberOfCorrectAnswers++;
                currentFlashcard.incrementPriority();
            } else {
                selectedButton.setBackgroundColor(Color.RED);
                currentFlashcard.decrementPriority();
            }

            // Save changes to the file
            saveFlashcards();

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

            // Sort flashcards by priority for the next round
            flashcards.sort(Comparator.comparingInt(FlashcardData::getPriority));
        }
    }

    // Load the next flashcard
    private void loadNextFlashcard() {
        currentIndex++;
        loadFlashcard();
    }

    // Show a confirmation dialog before starting the flashcards
    private void showStartConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Flashcards");
        builder.setMessage("Do you want to start?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked Yes, start loading flashcards
            loadFlashcardsForUser();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User clicked No, close the activity
            finish();
        });
        builder.setOnCancelListener(dialog -> {
            // Dialog was cancelled (e.g., back button pressed), close the activity
            finish();
        });
        builder.setCancelable(false); // Disable dismissing dialog by tapping outside or back button
        builder.show();
    }

    // Load flashcards for the user
    private void loadFlashcardsForUser() {
        // Retrieve user's proficiency level and load flashcards accordingly...
        if (level != null) {
            Toast.makeText(Flashcard.this, "User level: " + level, Toast.LENGTH_SHORT).show();
            flashcards = loadFlashcards(level);
            flashcardProgressBar.setMax(numberOfFlashcards);
            loadFlashcard();
        } else {
            Toast.makeText(Flashcard.this, "Level field not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Show a warning dialog when the user tries to navigate away
    private void showLeaveWarningDialog(Intent intentToStart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Your progress will be terminated if you leave now.");
        builder.setPositiveButton("Leave", (dialog, which) -> {
            // User clicked Leave, navigate to the selected activity
            startActivity(intentToStart);
            finish(); // Finish current activity
        });
        builder.setNegativeButton("Stay", (dialog, which) -> {
            // User clicked Stay, dismiss the dialog
            // Do nothing
        });
        builder.setOnCancelListener(dialog -> {
            // Dialog was cancelled (e.g., back button pressed), dismiss the dialog
            // Do nothing
        });
        builder.show();
    }

    // Class representing flashcard data
    private static class FlashcardData {
        private final String englishWord;
        private final String correctTranslation;
        private final List<String> options;
        private int priority;

        public FlashcardData(String englishWord, String correctTranslation, List<String> options, int priority) {
            this.englishWord = englishWord;
            this.correctTranslation = correctTranslation;
            this.options = options;
            this.priority = priority;
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

        public int getPriority() {
            return priority;
        }

        public void incrementPriority() {
            if (priority < 5) {
                priority++;
            }
        }

        public void decrementPriority() {
            if (priority > 1) {
                priority--;
            }
        }
    }
}