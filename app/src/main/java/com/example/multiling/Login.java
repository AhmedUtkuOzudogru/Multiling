package com.example.multiling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Login Class
 * Logins already existing Users to app with Firebase Authentication
 * Code done by Ahmed  06/05/2024 00:22
 * Xml done by Ibrahim
 */
public class Login extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextInputEditText editTextEmail, editTextPassword;
    private Button registerButton, logInButton, rememberMeButton, forgotPasswordButton;
    private CheckBox rememberMeCheckBox;
    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        // UI initialization
        editTextEmail = findViewById(R.id.email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextEmail  = findViewById(R.id.forgotPasswordEmail);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton2);
        logInButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        forgotPasswordButton = findViewById(R.id.loginForgotPassword);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            String email = sharedPreferences.getString(KEY_EMAIL, "");
            String password = sharedPreferences.getString(KEY_PASSWORD, "");
            signIn(email, password);
        }

        // Register button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        // Forgot password button listener
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Login button listener
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email!", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password!", Toast.LENGTH_LONG).show();
                    return;
                }

                signIn(email, password);
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Save login state if "Remember Me" is checked
                            if (rememberMeCheckBox.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                editor.putString(KEY_EMAIL, email);
                                editor.putString(KEY_PASSWORD, password);
                                editor.apply();
                            }

                            // Navigate to main activity
                            Toast.makeText(Login.this, "Authentication successful.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
