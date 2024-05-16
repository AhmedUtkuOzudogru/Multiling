package com.example.multiling;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultPage extends AppCompatActivity {

    private ImageView resultImage;
    private ProgressBar resultProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve numberOfCorrectAnswers and numberOfQuestions from the Intent
        int numberOfCorrectAnswers = getIntent().getIntExtra("CORRECT_ANSWERS", 0);
        int numberOfQuestions = getIntent().getIntExtra("NUMBER_OF_QUESTIONS", 0);

        // Display the number of correct answers
        TextView resultTextView = findViewById(R.id.resultText);
        resultTextView.setText("You got " + numberOfCorrectAnswers + " out of " + numberOfQuestions + " questions correct!");

        // Determine the percentage of correct answers
        double percentageCorrect = (double) numberOfCorrectAnswers / numberOfQuestions * 100;

        // Set the star image based on the percentage of correct answers
        resultImage = findViewById(R.id.resultImage);
        if (percentageCorrect == 100) {
            resultImage.setImageResource(R.drawable.crown_icon);
        } else if (percentageCorrect > 80) {
            resultImage.setImageResource(R.drawable.three_star);
        } else if (percentageCorrect > 50) {
            resultImage.setImageResource(R.drawable.two_star);
        } else if (percentageCorrect > 30) {
            resultImage.setImageResource(R.drawable.one_star);
        } else {
            resultImage.setImageResource(R.drawable.lose);
        }

        // Set the progress bar based on the percentage of correct answers
        resultProgressBar = findViewById(R.id.resultProgressBar);
        int progress = (int) percentageCorrect; // Convert percentage to integer for progress bar
        resultProgressBar.setProgress(progress);

        // Ensure the progress value is within the range of 0 to 100 (assuming a percentage)
        // Adjust the maximum value of the progress bar if needed
        resultProgressBar.setMax(numberOfQuestions); // Assuming the maximum value of the progress bar is 100
    }
}
