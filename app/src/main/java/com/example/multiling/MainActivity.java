package com.example.multiling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements NotificationListener{

    TextView userNameTextView, levelTextView,emailTextView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userID;
    StorageReference storageReference;

    ImageView profilePicture, notificationIcon;
    SharedPreferences sharedPreferences;

    Button goToProfilePageButton, goToWritingExerciseButton, goToFlashcardButton,goToSettingsButton, goToNotificationsButton;

    boolean notificationStatus;






    @SuppressLint("WrongViewCast")
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
        profilePicture=findViewById(R.id.mainProfileView);


        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameTextView.setText(value.getString("name")+" "+value.getString("surname"));
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

            }
        });
        notificationIcon = findViewById(R.id.mainNotificationsButton);
        sharedPreferences = getSharedPreferences("Notifications", MODE_PRIVATE);
        notificationStatus = sharedPreferences.getBoolean("notificationStatus", true); // Default is true

        updateNotificationIcon();


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
                Intent intent = new Intent(getApplicationContext(), NotificationPage.class);
                startActivity(intent);
            }
        });

        goToProfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
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
    private void updateNotificationIcon() {
        if (notificationStatus) {
            notificationIcon.setImageResource(R.drawable.notification_icon1);
        } else {
            notificationIcon.setImageResource(R.drawable.notification_icon2);
        }
    }

    @Override
    public void onNotificationStatusChanged(boolean status) {
        notificationStatus = status;
        updateNotificationIcon();
    }
}
