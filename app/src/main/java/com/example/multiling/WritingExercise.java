package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class WritingExercise extends AppCompatActivity {
    private String[] questionComponents;
    private String ID;
    private String sentence;
    private String noSuffixAnswer;
    private String suffixAnswer;
    private String firstWrongAnswer;
    private String secondWrongAnswer;

    public WritingExercise()
    {
        this.questionComponents = createWriting("easy_writing.txt");//TODO:this should be changed to the var in the user class that will hold the file name
        this.ID = questionComponents[0];
        this.sentence = questionComponents[1];
        this.noSuffixAnswer = questionComponents[2];
        this.suffixAnswer = questionComponents[3];
        this.firstWrongAnswer = questionComponents[4];
        this.secondWrongAnswer = questionComponents[5];
    }

    public String[] createWriting(String filename)
    {
        Random random = new Random();
        String[] components = new String[6];
        //this array will contain the id, sentence with a blank, correct answer without any suffixes, suffixed correct answer, and 2 wrong answers respectively
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
        setContentView(R.layout.activity_writing_exercise);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
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