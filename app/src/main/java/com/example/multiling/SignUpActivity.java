package com.example.multiling;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void signUpClick(View view) {
        // Find the EditText views by their IDs
        EditText emailInput = findViewById(R.id.singUpEditText1);
        EditText passwordInput = findViewById(R.id.signUpEditText2);
        EditText passwordAgainInput = findViewById(R.id.signUpEditText3);

        // Retrieve the text input from EditText fields
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordAgain = passwordAgainInput.getText().toString();

        // Check if password valid
        isPasswordValid(password, passwordAgain);
        // Check if mail is valid
        isEmailValid(email);


        // TODO: Implement sign-up logic using 'email' and 'password'
        performSignUp(email, password);
    }

    private void performSignUp(String email, String password) {
        //TODO: connect to the database
        Toast.makeText(this, "Sign-up successful for " + email, Toast.LENGTH_SHORT).show();
    }

    public void openSignInPage(View view) {
        // TODO: Handle opening sign-in page when either button is clicked
        //Intent signInIntent = new Intent(this, SignIn.class);
        //startActivity(signInIntent);
    }

    // TODO: this method should check if user actually entered an email.
    private boolean isEmailValid(String email) {

        return false;
    }

    // TODO: this method should check if the passwords match and the passwords contains the minimum
    // password requirements.
    private boolean isPasswordValid(String password, String passwordAgain) {

        return false;
    }

}