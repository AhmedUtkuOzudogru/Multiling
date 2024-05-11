package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FlashCard extends AppCompatActivity {
    private String front;
    private String[] questionComponents;
    private String ID;
    private String askedWord;
    private String correctAnswer;
    private String firstWrongAnswer;
    private String secondWrongAnswer;

    public FlashCard()
    {
        this.questionComponents = createFlashcard("easy_words.txt");//TODO:this should be changed to the var in the user class that will hold the file name
        this.ID = questionComponents[0];
        this.askedWord = questionComponents[1];
        this.correctAnswer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
    }

    public String[] createFlashcard(String filename)//TODO: declare a filename var in the user class in accordance with the proficiency level
    {
        Random random = new Random();
        String[] components = new String[5];
        //this array will contain the id, question, correct answer, and 2 wrong answers respectively
        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            for(int i = 0; i < random.nextInt(100) - 1; ++i)
            {
                reader.readLine();
            }
            String line = reader.readLine();
            components = line.trim().split(":");
            for(String i : components)
            {
                i.trim();
            }
        }
        catch (IOException e)
        {
            System.out.println("No file with such name");
        }
        return components;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);


        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_flashcard);
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
}