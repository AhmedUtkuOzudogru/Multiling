package com.example.multiling;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    public Question[] questions;

    public WritingExercise(int num)
    {
        questions = new Question[num];
        for(int i = 0; i < num; i++)
        {
            questions[i] = new Question(this);
        }
    }

    public Question[] getQuestions()
    {
        return this.questions;
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

        for(int i = 0; i < this.questions.length; i++)
        {
            //code block that displays the question's sentence
            TextView sentenceText = findViewById(R.id.writingQuestion);
            StringBuffer sentenceBuffer = new StringBuffer();
            sentenceBuffer.append(this.questions[i].getQuestion());
            sentenceText.setText(sentenceBuffer);
            //code block that displays the first choice
            Button firstChoice = findViewById(R.id.writingAnswer1);
            Random rand = new Random();
            int ranIndex = rand.nextInt(3);
            StringBuffer firstChoiceBuffer = new StringBuffer();
            firstChoiceBuffer.append(this.questions[i].getAllAnswers()[ranIndex]);
            firstChoice.setText(firstChoiceBuffer);
            //removing the first choice from the array of choices and copying the remaining 2 words into another array
            for (int j = ranIndex; j < this.questions[i].getAllAnswers().length; i++)
            {
                this.questions[i].getAllAnswers()[j] = this.questions[i].getAllAnswers()[j + 1];
            }
            String[] twoAnswers = new String[2];
            System.arraycopy(this.questions[i].getAllAnswers(), 0, twoAnswers, 0, 2);
            //code block that displays the second and third choices
            Button secondChoice = findViewById(R.id.writingAnswer2);
            int secondRandIndex = rand.nextInt(2);
            StringBuffer secondChoiceBuffer = new StringBuffer();
            Button thirdChoice = findViewById(R.id.writingAnswer3);
            StringBuffer thirdChoiceBuffer = new StringBuffer();
            if (secondRandIndex == 0)
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

            ProgressBar progressBar = findViewById(R.id.writingProgressBar);

            int finalI = i;
            firstChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context firstContextInstance = firstChoice.getContext();
                    Context secContextInstance = secondChoice.getContext();
                    Context thirdContextInstance = thirdChoice.getContext();
                    if (firstChoice.getText().equals(getQuestions()[finalI].getAnswer())) {

                        firstChoice.setBackgroundTintList(firstContextInstance.getResources().getColorStateList(R.color.green));
                        secondChoice.setBackgroundTintList(secContextInstance.getResources().getColorStateList(R.color.red));
                        thirdChoice.setBackgroundTintList(thirdContextInstance.getResources().getColorStateList(R.color.red));
                    } else if (secondChoice.getText().equals(getQuestions()[finalI].getAnswer())) {
                        firstChoice.setBackgroundTintList(firstContextInstance.getResources().getColorStateList(R.color.red));
                        secondChoice.setBackgroundTintList(secContextInstance.getResources().getColorStateList(R.color.green));
                        thirdChoice.setBackgroundTintList(thirdContextInstance.getResources().getColorStateList(R.color.red));
                    }
                    else
                    {
                        firstChoice.setBackgroundTintList(firstContextInstance.getResources().getColorStateList(R.color.red));
                        secondChoice.setBackgroundTintList(secContextInstance.getResources().getColorStateList(R.color.red));
                        thirdChoice.setBackgroundTintList(thirdContextInstance.getResources().getColorStateList(R.color.green));
                    }
                    progressBar.incrementProgressBy(1);
                }
            });
        }
    }
}