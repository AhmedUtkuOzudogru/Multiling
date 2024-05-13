package com.example.multiling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView userNameTextView, levelTextView,emailTextView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;

    Button goToProfilePageButton, goToWritingExerciseButton, goToFlashcardButton,goToSettingsButton;
    AppCompatImageButton goToNotificationsButton;





    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();

        userNameTextView=findViewById(R.id.mainUserName);
        levelTextView=findViewById(R.id.mainLevel);
        emailTextView=findViewById(R.id.mainEmail);
        goToWritingExerciseButton=findViewById(R.id.mainWritingButton);
        goToFlashcardButton=findViewById(R.id.mainFlashButton);
        goToSettingsButton=findViewById(R.id.mainSettingsButton);
        goToProfilePageButton=findViewById(R.id.mainProfileButton);
        goToNotificationsButton=findViewById(R.id.mainNotificationsButton);
        setContentView(R.layout.activity_main);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameTextView.setText(value.getString("name")+" "+value.getString("surname"));
                levelTextView.setText(value.getString("proficiencyLevel"));
                emailTextView.setText(value.getString("email"));
            }
        });


        goToProfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfilePage = new Intent(getApplicationContext(), Profile.class);
                startActivity(intentProfilePage);

            }
        });
        goToNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationPage.class);
                startActivity(intent);
            }
        });

        goToWritingExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WritingExercise.class);
                startActivity(intent);

            }
        });
        goToFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Flashcard.class);
                startActivity(intent);

            }
        });
        goToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);

            }
        });

    }
}