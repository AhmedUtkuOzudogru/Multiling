package com.example.multiling;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Flashcard extends AppCompatActivity
{
    private String front;
    private String flashcardID;
    private String askedWord;
    private String correctAnswer;
    private String firstWrongAnswer;
    private String secondWrongAnswer;

    public Flashcard(){}

    public void displayQuestion()
    {
        Random random = new Random();
        String[] questionComponents = createQuestion("easy_words.txt", random.nextInt(100));
        this.flashcardID = questionComponents[0];
        this.askedWord = questionComponents[1];
        this.correctAnswer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
    }

    public String[] createQuestion(String filename, int randNum)//TODO: declare a filename var in the user class in accordance with the proficiency level
    {
        String[] components = new String[5];
        //this array will contain the id, question, correct answer, and 2 wrong answers respectively
        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            for(int i = 0; i < randNum - 1; ++i)
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flashcard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}