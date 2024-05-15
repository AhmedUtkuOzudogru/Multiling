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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity
{
    private int writingNumber;
    private int flashcardNumber;
    private Switch notificationSwitch;
    private EditText writingNumEditText;
    private EditText flashcardNumEditText;
    private static Settings instance = null;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    String userID,noOfWritingExercise,noOfFlashcard;

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, MODE_PRIVATE);
        userID=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore=FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                noOfWritingExercise=value.getString("noOfWritingExercise");
                noOfFlashcard=value.getString("noOfFlashcard");
                flashcardNumEditText.setText(noOfFlashcard);
                writingNumEditText.setText(noOfWritingExercise);

            }
        });

        // Example logout button listener
        findViewById(R.id.settingsLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase
                firebaseAuth.signOut();

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
        notificationSwitch = findViewById(R.id.settingsSwitch1);
        setNotificationListener();

        getData(); // setting writingNumber and flashcardNumber to data in database

        writingNumEditText = findViewById(R.id.settingsWritingNum);
        flashcardNumEditText = findViewById(R.id.settingsFlashNum);
        setWritingNumListener();
        setFlashNumListener();




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
                    handleSwitchOn();
                } else {
                    // Switch is OFF
                    handleSwitchOff();
                }
            }
        });
    }

    // TODO: change the notification status in Notification Page.
    private void handleSwitchOn() {
        Toast.makeText(Settings.this, "Notifications ON", Toast.LENGTH_SHORT).show();
        // Perform actions when Switch is ON
    }

    private void handleSwitchOff() {
        Toast.makeText(Settings.this, "Notifications OFF", Toast.LENGTH_SHORT).show();
        // Perform actions when Switch is OFF
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
                    if (number >= 0 && number <= 20) {
                        writingNumber = number;
                        noOfWritingExercise=String.valueOf(number);
                        Map<String ,Object> map = new HashMap<>();
                        map.put("noOfWritingExercise",noOfWritingExercise);

                        documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }else {
                                    Toast.makeText(Settings.this, "Failed to updated",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        writingNumEditText.setText(String.valueOf(writingNumber));
                        Toast.makeText(Settings.this, "Please enter a number between 0 and 20", Toast.LENGTH_SHORT).show();
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

                    if (number >= 0 && number <= 20) {
                        flashcardNumber = number;
                        noOfFlashcard=String.valueOf(number);
                        Map<String ,Object> map = new HashMap<>();
                        map.put("noOfFlashcard",noOfFlashcard);

                        documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }else {
                                    Toast.makeText(Settings.this, "Failed to updated",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else {
                        flashcardNumEditText.setText(String.valueOf(flashcardNumber));
                        Toast.makeText(Settings.this, "Please enter a number between 0 and 20", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}
