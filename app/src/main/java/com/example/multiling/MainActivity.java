package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button goToProfilePageButton;
    Button goToWritingExercisePageButton;
    Button goToFlashCardPageButton;
    Button goToSettingsPageButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        goToProfilePageButton=findViewById(R.id.mainProfileButton);
        goToProfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfilePage = new Intent(getApplicationContext(), Profile.class);
                startActivity(intentProfilePage);

            }
        });

        goToWritingExercisePageButton =findViewById(R.id.mainWritingButton);

        goToWritingExercisePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(getApplicationContext(), WritingExercise.class);
                startActivity(intentProfile);
            }
        });

        goToFlashCardPageButton = findViewById(R.id.mainFlashButton);
        goToFlashCardPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFlashCard = new Intent(getApplicationContext(), FlashCard.class);
                startActivity(intentFlashCard);
            }
        });

        goToSettingsPageButton = findViewById(R.id.mainSettingsButton);
        goToSettingsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(getApplicationContext(), Settings.class);
                startActivity(intentSettings);
            }
        });









    }
}