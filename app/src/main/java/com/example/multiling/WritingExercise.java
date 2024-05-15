package com.example.multiling;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class WritingExercise extends AppCompatActivity {

    private TextView writingQuestionText;
    private Button writingAnswer1;
    private Button writingAnswer2;
    private Button writingAnswer3;
    private Button nextButton;
    private ProgressBar writingProgressBar;

    private List<WritingData> writings;
    private int currentIndex = 0;
    private int numberOfCorrectAnswers = 0;
    private String level;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private boolean isAttemptingToNavigate = false;
    private int numberOfWritings;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_exercise);

        writingQuestionText = findViewById(R.id.writingQuestionText);
        writingAnswer1 = findViewById(R.id.writingAnswer1);
        writingAnswer2 = findViewById(R.id.writingAnswer2);
        writingAnswer3 = findViewById(R.id.writingAnswer3);
        nextButton = findViewById(R.id.nextButton);
        writingProgressBar = findViewById(R.id.writingProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        BottomNavigationView bottomNavigation = findViewById(R.id.writingExerciseNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_writingexercises);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigator_home)
                {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_profile)
                {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_settings)
                {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), Settings.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_flashcard)
                {
                    showLeaveWarningDialog(new Intent(getApplicationContext(), Flashcard.class));
                    return true;
                }
                return false;
            }
        });

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

        showStartConfirmationDialog();

        if (currentUser != null) {
            userID = currentUser.getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {

                        level = value.getString("proficiencyLevel");
                        String noOfWritings = value.getString("noOfWritings");

                        if (noOfWritings != null) {
                            Toast.makeText(WritingExercise.this, "Number of writings: " + noOfWritings, Toast.LENGTH_SHORT).show();
                            numberOfWritings = Integer.parseInt(noOfWritings);
                        } else {
                            Toast.makeText(WritingExercise.this, "Number of writings not found", Toast.LENGTH_SHORT).show();
                            numberOfWritings = 10; // Default value if not set
                        }

                        if (level != null) {
                            Toast.makeText(WritingExercise.this, "User level: " + level, Toast.LENGTH_SHORT).show();
                            writings = loadWritings(level);
                            if (numberOfWritings > writings.size()) {
                                numberOfWritings = writings.size();
                            }
                            writings = writings.subList(0, numberOfWritings);
                            writingProgressBar.setMax(numberOfWritings);

                            loadWriting();
                        } else {
                            Toast.makeText(WritingExercise.this, "Level field not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WritingExercise.this, "User level not found", Toast.LENGTH_SHORT).show();
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

        writingAnswer1.setOnClickListener(answerClickListener);
        writingAnswer2.setOnClickListener(answerClickListener);
        writingAnswer3.setOnClickListener(answerClickListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex < numberOfWritings) {
                    loadWriting();
                } else {
                    Intent intent = new Intent(WritingExercise.this, ResultPage.class);
                    intent.putExtra("CORRECT_ANSWERS", numberOfCorrectAnswers); // Pass numberOfCorrectAnswers as an extra
                    intent.putExtra("NUMBER_OF_QUESTIONS", numberOfWritings); // Pass numberOfCorrectAnswers as an extra
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private List<WritingData> loadWritings(String level) {
        List<WritingData> writingList = new ArrayList<>();
        int resourceId = R.raw.easy_writing; // Default to easy writing

        switch (level.toLowerCase()) {
            case "intermediate":
                resourceId = R.raw.intermediate_writing;
                break;
            case "advanced":
                resourceId = R.raw.advanced_writing;
                break;
            case "beginner":
            default:
                resourceId = R.raw.easy_writing;
                break;
        }

        try {
            InputStream is = getResources().openRawResource(resourceId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(json);
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                String sentence = questionObject.getString("sentence");
                String correct = questionObject.getString("correct");
                JSONArray incorrectArray = questionObject.getJSONArray("incorrect");

                List<String> options = new ArrayList<>();
                options.add(correct);
                for (int j = 0; j < incorrectArray.length(); j++) {
                    options.add(incorrectArray.getString(j));
                }
                Collections.shuffle(options);

                writingList.add(new WritingData(sentence, correct, options));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writingList;
    }

    private void loadWriting() {
        if (writings != null && !writings.isEmpty()) {
            WritingData currentWriting = writings.get(currentIndex);
            writingQuestionText.setText(currentWriting.getSentence());
            List<String> options = currentWriting.getOptions();
            writingAnswer1.setText(options.get(0));
            writingAnswer2.setText(options.get(1));
            writingAnswer3.setText(options.get(2));

            // Reset button colors
            writingAnswer1.setBackgroundColor(Color.parseColor("#800080")); // Purple
            writingAnswer2.setBackgroundColor(Color.parseColor("#800080")); // Purple
            writingAnswer3.setBackgroundColor(Color.parseColor("#800080")); // Purple

            // Enable buttons
            writingAnswer1.setEnabled(true);
            writingAnswer2.setEnabled(true);
            writingAnswer3.setEnabled(true);

            // Update progress bar
            writingProgressBar.setProgress(currentIndex + 1);
        }
    }

    private void checkAnswer(Button selectedButton) {
        WritingData currentWriting = writings.get(currentIndex);
        String selectedOption = selectedButton.getText().toString();

        if (selectedOption.equals(currentWriting.getCorrectOption())) {
            selectedButton.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            numberOfCorrectAnswers++;
        } else {
            selectedButton.setBackgroundColor(Color.RED);
        }

        // Highlight correct answer in green
        if (writingAnswer1.getText().toString().equals(currentWriting.getCorrectOption())) {
            writingAnswer1.setBackgroundColor(Color.GREEN);
        }
        if (writingAnswer2.getText().toString().equals(currentWriting.getCorrectOption())) {
            writingAnswer2.setBackgroundColor(Color.GREEN);
        }
        if (writingAnswer3.getText().toString().equals(currentWriting.getCorrectOption())) {
            writingAnswer3.setBackgroundColor(Color.GREEN);
        }

        // Disable buttons after selection
        writingAnswer1.setEnabled(false);
        writingAnswer2.setEnabled(false);
        writingAnswer3.setEnabled(false);
    }

    private static class WritingData {
        private final String sentence;
        private final String correctOption;
        private final List<String> options;

        public WritingData(String sentence, String correctOption, List<String> options) {
            this.sentence = sentence;
            this.correctOption = correctOption;
            this.options = options;
        }

        public String getSentence() {
            return sentence;
        }

        public String getCorrectOption() {
            return correctOption;
        }

        public List<String> getOptions() {
            return options;
        }
    }

    private void showStartConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Writing Exercises");
        builder.setMessage("Do you want to start?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked Yes, start loading writings
            loadWritingsForUser();
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

    private void loadWritingsForUser() {
        // Retrieve user's proficiency level and load writings accordingly...

        // Retrieve writings and start loading the first writing
        if (level != null) {
            Toast.makeText(WritingExercise.this, "User level: " + level, Toast.LENGTH_SHORT).show();
            writings = loadWritings(level);
            writingProgressBar.setMax(numberOfWritings);
            writingProgressBar.setProgress(currentIndex + 1);
            loadWriting();
        } else {
            Toast.makeText(WritingExercise.this, "Level field not found", Toast.LENGTH_SHORT).show();
        }
    }

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
}

