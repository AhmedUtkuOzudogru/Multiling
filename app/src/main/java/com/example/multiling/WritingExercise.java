package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class WritingExercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_exercise);

        BottomNavigationView bottomNavigation = findViewById(R.id.writingExerciseNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_writingexercises);
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
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_settings)
                {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_flashcard)
                {
                    startActivity(new Intent(getApplicationContext(), FlashCard.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_writingexercises)
                {
                    return true;
                }
                return false;
            }
        });
    }
}