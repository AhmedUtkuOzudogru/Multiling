package com.example.multiling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    TextView userNameTextView, emailTextView, levelTextView;
    StorageReference storageReference;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    Button profileButton;
    ImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore=FirebaseFirestore.getInstance();
        profilePicture = findViewById(R.id.profilePicture);
        profileButton = findViewById(R.id.profileButton);
        userNameTextView = findViewById(R.id.userNameEdit);
        levelTextView = findViewById(R.id.levelEdit);
        emailTextView = findViewById(R.id.emailEdit);

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameTextView.setText(value.getString("name") + " " + value.getString("surname"));
                levelTextView.setText(value.getString("proficiencyLevel"));
                emailTextView.setText(value.getString("email"));
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profilePicture = findViewById(R.id.profilePicture);
        profileButton = findViewById(R.id.profileButton);





        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);


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


        BottomNavigationView bottomNavigation = findViewById(R.id.profileNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_profile);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigator_home)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_profile)
                {
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_settings)
                {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_flashcard)
                {
                    startActivity(new Intent(getApplicationContext(), Flashcard.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_writingexercises)
                {
                    startActivity(new Intent(getApplicationContext(), WritingExercise.class));
                    return true;
                }
                return false;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) { // Check if the returned result is from the gallery request
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                profilePicture.setImageURI(imageUri);
            }
        }
    }



}


