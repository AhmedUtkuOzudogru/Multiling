package com.example.multiling;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class WritingExercise extends AppCompatActivity
{
    private String[] questionComponents;
    private String ID;
    private String sentence;
    private String answer;
    private String firstWrongAnswer;
    private String secondWrongAnswer;
    private String[] allAnswers;

    public WritingExercise()
    {
        this.questionComponents = createWriting("easy_writing.txt");//TODO:this should be changed to the var in the user class that will hold the file name
        this.ID = questionComponents[0];
        this.sentence = questionComponents[1];
        this.answer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
        System.arraycopy(questionComponents, 2, this.allAnswers, 0, 3);
    }

    public String[] createWriting(String filename)
    {
        Random random = new Random();
        String[] components = new String[5];
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_exercise);

        BottomNavigationView bottomNavigation = findViewById(R.id.writingExerciseNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_writingexercises);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
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
                    startActivity(new Intent(getApplicationContext(), Flashcard.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_writingexercises)
                {
                    return true;
                }
                return false;
            }
        });
        //code block that displays the question's sentence
        TextView sentenceText = findViewById(R.id.writingQuestion);
        StringBuffer sentenceBuffer = new StringBuffer();
        sentenceBuffer.append(this.sentence);
        sentenceText.setText(sentenceBuffer);
        //code block that displays the first choice
        Button firstChoice = findViewById(R.id.writingAnswer1);
        Random rand = new Random();
        int ranIndex = rand.nextInt(3);
        StringBuffer firstChoiceBuffer = new StringBuffer();
        firstChoiceBuffer.append(allAnswers[ranIndex]);
        firstChoice.setText(firstChoiceBuffer);
        firstChoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(firstChoice.getContext().equals(answer))
                {
                    
                }
            }
        });
        //removing the first choice from the array of choices and copying the remaining 2 words into another array
        for(int i = ranIndex; i < allAnswers.length; i++)
        {
            allAnswers[i] = allAnswers[i + 1];
        }
        String[] twoAnswers = new String[2];
        System.arraycopy(allAnswers, 0, twoAnswers, 0, 2);
        //code block that displays the second and third choices
        Button secondChoice = findViewById(R.id.writingAnswer2);
        int secondRandIndex = rand.nextInt(2);
        StringBuffer secondChoiceBuffer = new StringBuffer();
        Button thirdChoice = findViewById(R.id.writingAnswer3);
        StringBuffer thirdChoiceBuffer = new StringBuffer();
        if(secondRandIndex == 0)
        {
            secondChoiceBuffer.append(twoAnswers[0]);
            thirdChoiceBuffer.append(twoAnswers[1]);
            secondChoice.setText(secondChoiceBuffer);
            thirdChoice.setText(thirdChoiceBuffer);
        }
        else
        {
            secondChoiceBuffer.append(twoAnswers[1]);
            thirdChoiceBuffer.append(twoAnswers[0]);
            secondChoice.setText(secondChoiceBuffer);
            thirdChoice.setText(thirdChoiceBuffer);
        }
    }
}