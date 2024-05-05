package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;

/**
 * Register Class
 * Register New Users with Firebase Authentication
 * Code dode by Ahmed and Saljug 05/05/2024 23:55
 * Xml done by Ibrahim
 */
public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextPasswordAgain ;
    Button registerButton , logInButton ;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextEmail  = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPasswordAgain = findViewById(R.id.password_Again);
        registerButton = findViewById(R.id.register);
        logInButton = findViewById(R.id.logInButton2);
        /**
         * logInButton's code and Intents
         * Right now Log in button fowards user to Login.class
         * To get in the app
         * */
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        /**
         * registerButton's code and Necesary methods to create a user via Firebase
         * */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, passwordAgain;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextEmail.getText());
                passwordAgain = String.valueOf(editTextPasswordAgain.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Enter email!", Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Enter password!", Toast.LENGTH_LONG).show();
                    return;
                }else if((passwordAgain.equals(password))){
                    Toast.makeText(Register.this,"Passwords does not match!", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Register.this, "Account  Created.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}