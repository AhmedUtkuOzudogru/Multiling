package com.example.multiling;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigator_flashcard);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigator_settings) {
                // Handle settings navigation
                return true;
            } else if (itemId == R.id.navigator_profile) {
                // Handle profile navigation
                return true;
            } else if (itemId == R.id.navigator_home) {
                // Handle home navigation
                return true;
            } else if (itemId == R.id.navigator_flashcard) {
                // Handle flashcard navigation
                return true;
            } else if (itemId == R.id.navigator_writingexercises) {
                // Handle writing exercises navigation
                return true;
            }

            return false;
        });
    }
}