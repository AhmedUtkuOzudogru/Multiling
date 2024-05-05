package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_page);

        // Apply window insets listener to adjust padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void openSignInPage(View view) {
        // TODO: handle the sign in page intent
        Intent intent = new Intent(StartPage.this, SignInActivity.class);
        startActivity(intent);
    }

    public void openSignUpPage(View view) {
        // TODO: handle the sign up page intent
        Intent intent = new Intent(StartPage.this, SignUpActivity.class);
        startActivity(intent);
    }
}