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
    private String flashcardFilename;
    private String writingFilename;

    public Question(WritingExercise writingExercise)
    {
        this.flashcardFilename = determineFlashcardFilename();
        this.writingFilename = determineWritingFilename();
        this.questionComponents = new String[5];
        this.questionComponents = createQuestion(this.writingFilename);
        this.ID = questionComponents[0];
        this.question = questionComponents[1];
        this.answer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
        System.arraycopy(questionComponents, 2, this.allAnswers, 0, 3);
    }

    public Question(Flashcard flashExercise)
    {

        this.flashcardFilename = determineFlashcardFilename();
        this.writingFilename = determineWritingFilename();
        this.questionComponents = new String[5];
        this.questionComponents = createQuestion(this.flashcardFilename);
        this.ID = questionComponents[0];
        this.question = questionComponents[1];
        this.answer = questionComponents[2];
        this.firstWrongAnswer = questionComponents[3];
        this.secondWrongAnswer = questionComponents[4];
        System.arraycopy(questionComponents, 2, this.allAnswers, 0, 3);
    }

    public String determineFlashcardFilename()
    {
        if(FillProfile.getInstance().getSelectedProficiencyLevel().equals("Beginner"))
        {
            return "easy_words.txt";
        }
        else if (FillProfile.getInstance().getSelectedProficiencyLevel().equals("Intermediate"))
        {
            return "intermediate_words.txt";
        }
        else
        {
            return "advanced_words.txt";
        }
    }

    public String determineWritingFilename()
    {
        if(FillProfile.getInstance().getSelectedProficiencyLevel().equals("Beginner"))
        {
            return "easy_writing.txt";
        }
        else if(FillProfile.getInstance().getSelectedProficiencyLevel().equals("Intermediate"))
        {
            return "intermediate_writing.txt";
        }
        else
        {
            return "advanced_writing.txt";
        }
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