package com.example.multiling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity
{
    private int writingNumber;
    private int flashcardNumber;
    private Switch notificationSwitch;
    private NotificationListener notificationListener;
    private EditText writingNumEditText;
    private EditText flashcardNumEditText;
    private static Settings instance = null;

    public static Settings getInstance()
    {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    public int getWritingNumber()
    {
        return writingNumber;
    }

    public int getFlashcardNumber()
    {
        return flashcardNumber;
    }
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, MODE_PRIVATE);

        // Example logout button listener
        findViewById(R.id.settingsLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase
                mAuth.signOut();

                // Clear login state
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to login activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });



        BottomNavigationView bottomNavigation = findViewById(R.id.settingsNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_settings);
        setNotificationListener();

        getData(); // setting writingNumber and flashcardNumber to data in database

        writingNumEditText = findViewById(R.id.settingsWritingNum);
        flashcardNumEditText = findViewById(R.id.settingsFlashNum);
        setWritingNumListener();
        setFlashNumListener();

        notificationSwitch = findViewById(R.id.settingsSwitch1);
        setNotificationListener();




        // Set listener for navigation bar
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigator_home)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_profile)
                {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_settings)
                {
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_flashcard)
                {
                    startActivity(new Intent(getApplicationContext(), Flashcard.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_writingexercises)
                {
                    startActivity(new Intent(getApplicationContext(), WritingExercise.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void getData()
    {
        // TODO: get data from database
        writingNumber = 10;
        flashcardNumber = 10;
    }

    private void setNotificationListener() {
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch is ON
                    Toast.makeText(Settings.this, "Notifications ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is OFF
                    Toast.makeText(Settings.this, "Notifications OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setWritingNumListener() {
        writingNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
                String enteredText = s.toString();
                if (!enteredText.isEmpty()) {
                    int number = Integer.parseInt(enteredText);
                    if (number >= 0 && number <= 99) {
                        writingNumber = number;
                    } else {
                        writingNumEditText.setText(String.valueOf(writingNumber));
                        Toast.makeText(Settings.this, "Please enter a number between 0 and 99", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setFlashNumListener() {
        flashcardNumEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called when the text is changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
                String enteredText = s.toString();
                if (!enteredText.isEmpty()) {
                    int number = Integer.parseInt(enteredText);
                    if (number >= 0 && number <= 99) {
                        flashcardNumber = number;
                    }
                    else {
                        flashcardNumEditText.setText(String.valueOf(flashcardNumber));
                        Toast.makeText(Settings.this, "Please enter a number between 0 and 99", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}
