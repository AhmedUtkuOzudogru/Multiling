package com.example.multiling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView userNameTextView, levelTextView, emailTextView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;

    Button goToProfilePageButton, goToWritingExerciseButton, goToFlashcardButton, goToSettingsButton;
    ImageButton goToNotificationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        userNameTextView = findViewById(R.id.mainUserName);
        levelTextView = findViewById(R.id.mainLevel);
        emailTextView = findViewById(R.id.mainEmail);
        goToProfilePageButton = findViewById(R.id.mainProfileButton);
        goToWritingExerciseButton = findViewById(R.id.mainWritingButton);
        goToFlashcardButton = findViewById(R.id.mainFlashButton);
        goToSettingsButton = findViewById(R.id.mainSettingsButton);
        goToNotificationsButton = findViewById(R.id.mainNotificationsButton);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameTextView.setText(value.getString("name") + " " + value.getString("surname"));
                levelTextView.setText(value.getString("proficiencyLevel"));
                emailTextView.setText(value.getString("email"));
            }
        });

        goToProfilePageButton.setOnClickListener(v -> {
            Intent intentProfilePage = new Intent(MainActivity.this, Profile.class);
            startActivity(intentProfilePage);
        });

        goToNotificationsButton.setOnClickListener(v -> {
            Intent intentNotifications = new Intent(MainActivity.this, NotificationPage.class);
            startActivity(intentNotifications);
        });

        goToWritingExerciseButton.setOnClickListener(v -> {
            Intent intentWritingExercise = new Intent(MainActivity.this, WritingExercise.class);
            startActivity(intentWritingExercise);
        });

        goToFlashcardButton.setOnClickListener(v -> {
            Intent intentFlashcard = new Intent(MainActivity.this, Flashcard.class);
            startActivity(intentFlashcard);
        });

        goToSettingsButton.setOnClickListener(v -> {
            Intent intentSettings = new Intent(MainActivity.this, Settings.class);
            startActivity(intentSettings);
        });
    }
}
