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

public class Question extends AppCompatActivity
{
    private String[] questionComponents;
    private String ID;
    private String question;
    private String answer;
    private String firstWrongAnswer;
    private String secondWrongAnswer;
    private String[] allAnswers;

    public Question(WritingExercise writingExercise)
    {
        this.questionComponents = createQuestion("easy_writing.txt");//TODO:this should be changed to the var in the user class that will hold the file name
        this.ID = questionComponents[0];
        this.question = questionComponents[1];
        this.answer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
        System.arraycopy(questionComponents, 2, this.allAnswers, 0, 3);
    }

    public Question(Flashcard flashExercise)
    {
        this.questionComponents = createQuestion("easy_words.txt");//TODO:this should be changed to the var in the user class that will hold the file name
        this.ID = questionComponents[0];
        this.question = questionComponents[1];
        this.answer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
        System.arraycopy(questionComponents, 2, this.allAnswers, 0, 3);
    }

    // I created this constructor to suppress the warning given in AndroidManifest.xml
    public Question()
    {

    }

    public String[] createQuestion(String filename)
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

    public String getQuestion()
    {
        return this.question;
    }

    public String getAnswer()
    {
        return this.answer;
    }

    public String getFirstWrongAnswer()
    {
        return this.firstWrongAnswer;
    }

    public String getSecondWrongAnswer()
    {
        return this.secondWrongAnswer;
    }

    public String[] getAllAnswers()
    {
        return this.allAnswers;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}