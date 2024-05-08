package com.example.multiling;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
/**
 * FillProfile Class
 * Continues The Registration process with data taken from New Users with FireStore Database
 * Code done by Ahmed 09/05/2024 01:05
 * Xml done by Ahmed
 * TODO: The proficiencyLevel level should be Combo box needs implementation
 */
public class FillProfile extends AppCompatActivity {
    String userID,name,surname,proficiencyLevel;
    TextInputEditText nameTextField, surnameTextField, proficiencyLevelTextField ;
    Button startButton,skipButton;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fill_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userID = getIntent().getStringExtra("userID");
        nameTextField = findViewById(R.id.nameTextField);
        surnameTextField = findViewById(R.id.surnameTextField);
        proficiencyLevelTextField = findViewById(R.id.proficiencyLevel);
        skipButton = findViewById(R.id.skipButton2);
        startButton = findViewById(R.id.start);
        name="aName";
        surname="aSurname";
        proficiencyLevel="aProficiencyLevel";
        firestore = FirebaseFirestore.getInstance();


        /*
         * SkipButton's code and Intents
         * Right now this button fowards user to MainActivity.class
         * To get in the app and creates&send empty data's to FireStore DataBase
         * */
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = firestore.collection("users").document(userID);
                // Storing data using hash map
                Map<String,Object> user = new HashMap<>();
                user.put("name",name);
                user.put("surname", surname);
                user.put("proficiencyLevel",proficiencyLevel);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag","userProfile is created for ID:"+ userID+"with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+proficiencyLevel);

                    }
                });
                Toast.makeText(FillProfile.this, "userProfile is created with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+proficiencyLevel,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });
        /*
         * startButton's code and Intents
         * Right now this button fowards user to MainActivity.class
         * To get in the app and sends the data that is retrieved from user to FireStore DataBase
         * */
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=String.valueOf(nameTextField.getText());
                surname=String.valueOf(surnameTextField.getText());
                proficiencyLevel=String.valueOf(proficiencyLevelTextField.getText());

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(FillProfile.this,"Enter a Name!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(surname)){
                    Toast.makeText(FillProfile.this,"Enter a Surname!", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(proficiencyLevel)){
                    Toast.makeText(FillProfile.this,"Enter a proficiencyLevel", Toast.LENGTH_SHORT).show();
                    return;
                }
                DocumentReference documentReference = firestore.collection("users").document(userID);
                // Storing data using hash map
                Map<String,Object> user = new HashMap<>();
                user.put("name",name);
                user.put("surname", surname);
                user.put("proficiencyLevel",proficiencyLevel);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag","userProfile is created with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+proficiencyLevel);

                    }
                });
                Toast.makeText(FillProfile.this, "userProfile is created with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+proficiencyLevel,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);


            }
        });

    }
}