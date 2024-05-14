package com.example.multiling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    TextView userNameTextView, levelTextView, emailTextView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;
    StorageReference storageReference;

    Button goToProfilePageButton, goToWritingExerciseButton, goToFlashcardButton,goToSettingsButton;
    AppCompatImageButton goToNotificationsButton;





    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();



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
                userNameTextView.setText(value.getString("name") + " " + value.getString("surname"));
                levelTextView.setText(value.getString("proficiencyLevel"));
                emailTextView.setText(value.getString("email"));
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+userID+"profile.jpg");
        profileRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profilePicture.setImageBitmap(bitmap);


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
