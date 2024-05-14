package com.example.multiling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Flashcard extends AppCompatActivity
{

    private Question[] questions;
    private int correctAnswers;
    private int currentQuestionIndex;
    public Flashcard()
    {
        questions = new Question[Settings.getInstance().getFlashcardNumber()];
        for(int i = 0; i < this.questions.length; i++)
        {
            questions[i] = new Question(this);
        }

        this.correctAnswers = 0;
        this.currentQuestionIndex = 0;
    }

    private String[] shuffleAnswers(String[] answers)
    {
        List<String> list = new ArrayList<>(Arrays.asList(answers));
        Collections.shuffle(list);
        String[] shuffled = list.toArray(new String[0]);
        return shuffled;
    }

    private void displayCurrentQuestion()
    {
        TextView question = findViewById(R.id.flashcardQuestionText);
        Question currentQuestion = questions[currentQuestionIndex];
        question.setText(currentQuestion.getQuestion());

        Button firstChoice = findViewById(R.id.flashcardAnswer1);
        Button secondChoice = findViewById(R.id.flashcardAnswer2);
        Button thirdChoice = findViewById(R.id.flashcardAnswer3);
        String[] shuffledAnswers = shuffleAnswers(currentQuestion.getAllAnswers());
        firstChoice.setText(shuffledAnswers[0]);
        secondChoice.setText(shuffledAnswers[1]);
        thirdChoice.setText(shuffledAnswers[2]);
    }

    private void checkAnswer(Button selectedAnswer)
    {
        String selectedText = selectedAnswer.getText().toString();
        Question currentQuestion = questions[currentQuestionIndex];
        if (selectedText.equals(currentQuestion.getAnswer()))
        {
            selectedAnswer.setBackgroundColor(getResources().getColor(R.color.green));
            incrementScore();
        }
        else
        {
            selectedAnswer.setBackgroundColor(getResources().getColor(R.color.red));
        }
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                moveToNextQuestion();
            }
        }, 1000);
    }

    private void resetAnswerButtons()
    {
        Button firstChoice = findViewById(R.id.flashcardAnswer1);
        Button secondChoice = findViewById(R.id.flashcardAnswer2);
        Button thirdChoice = findViewById(R.id.flashcardAnswer3);
        firstChoice.setBackgroundColor(getResources().getColor(R.color.black));
        secondChoice.setBackgroundColor(getResources().getColor(R.color.black));
        thirdChoice.setBackgroundColor(getResources().getColor(R.color.black));
    }

    private void moveToNextQuestion()
    {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length)
        {
            displayCurrentQuestion();
            resetAnswerButtons();
        }
    }

    public Question[] getQuestions()
    {
        return this.questions;
    }

    public void incrementScore()
    {
        this.correctAnswers++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        BottomNavigationView bottomNavigation = findViewById(R.id.flashcardNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_flashcard);


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

        displayCurrentQuestion();
    }
}