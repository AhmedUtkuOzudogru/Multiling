package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private boolean rememberMe;
    // TODO: modify other methods for rememberMe

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.singInEditText1);
        passwordEditText = findViewById(R.id.singUpEditText1);
        rememberMe = false;
    }

    // click listener for sign in button in activity_sign_in.xml
    private void signInClick() {
        signIn();
    }

    private void signIn() {
        // Get the entered email and password from EditText fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the input (e.g., check if email and password are not empty)
        if (isValidInput(email, password)) {
            // Perform login operation (simulated here)
            if (performLogin(email, password)) {
                // Login successful: navigate to HomeActivity
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finish current activity
            } else {
                // Display error message (e.g., incorrect credentials)
                Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Display error message (e.g., empty fields)
            Toast.makeText(SignInActivity.this, "Please enter valid email and password", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: this method should compare the input mail/password and database mail/password
    private boolean isValidInput(String email, String password) {
        // Perform input validation (e.g., check if email and password are not empty)
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }

    // TODO: this method should handle the login
    private boolean performLogin(String email, String password) {

        return false;
    }

    // TODO: this method should open the signUpPage if the user clicks on "Sign Up"
    public void openSignUpPage(View view) {
    }

    public void rememberMeClick(View view) {
        this.rememberMe = !this.rememberMe;
    }
}