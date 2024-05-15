package com.example.multiling;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultPage extends AppCompatActivity {

    private ImageView resultStarImage;

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
        resultStarImage = findViewById(R.id.resultImage);
        if (percentageCorrect == 100) {
            resultStarImage.setImageResource(R.drawable.crown_icon);
        } else if (percentageCorrect > 80) {
            resultStarImage.setImageResource(R.drawable.three_star);
        } else if (percentageCorrect > 50) {
            resultStarImage.setImageResource(R.drawable.two_star);
        } else if (percentageCorrect > 30) {
            resultStarImage.setImageResource(R.drawable.one_star);
        } else {
            resultStarImage.setImageResource(R.drawable.lose);
        }
    }
}
