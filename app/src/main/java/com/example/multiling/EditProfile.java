package com.example.multiling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity
{
    private SharedPreferences sharedPreferences;
    ImageView profileImage;
    Button changeProfileButton, saveButton, resetPasswordButton, cancelButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    EditText nameEditText, surnameEditText;
    Spinner levelEditText;
    FirebaseUser user;


    String userID,name,surname,email,level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        userID=user.getUid();
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);


        profileImage = findViewById(R.id.profilePicture);
        changeProfileButton = findViewById(R.id.changeProfileButton);
        saveButton=findViewById(R.id.saveButton);
        resetPasswordButton=findViewById(R.id.resetPasswordButton);
        nameEditText=findViewById(R.id.nameEditText);
        surnameEditText =findViewById(R.id.surnameEditText);
        levelEditText = findViewById(R.id.editProfileLevel);

        // Define the list of proficiency levels
        String[] proficiencyLevels = {"Beginner", "Intermediate", "Advanced"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, proficiencyLevels);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        levelEditText.setAdapter(adapter);

        // Set a listener to handle item selection
        levelEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProficiencyLevel = parent.getItemAtPosition(position).toString();
                // Handle selected item (e.g., store in a variable or perform action)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });
        cancelButton=findViewById(R.id.cancelButton);



        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name=value.getString("name");
                surname=value.getString("surname");
                level=value.getString("proficiencyLevel");
                email=firebaseAuth.getCurrentUser().getEmail();
                nameEditText.setText(name);
                surnameEditText.setText(surname);
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).toString().equals(level)) {
                        levelEditText.setSelection(i);
                        break;
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = String.valueOf(nameEditText.getText());
                surname = String.valueOf(surnameEditText.getText());
                level = levelEditText.getSelectedItem().toString();
                if(name.isEmpty()||surname.isEmpty()){
                    Toast.makeText(EditProfile.this,"All fields should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> user = new HashMap<>();
                user.put("name",name);
                user.put("surname", surname);
                user.put("proficiencyLevel",level);
                user.put("email",firebaseAuth.getCurrentUser().getEmail());
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag","userProfile is updated for ID:"+ userID+"with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+level);

                    }
                });
                Toast.makeText(EditProfile.this, "userProfile is updated with Name:"+name+
                                " Surname:"+surname+" proficiencyLevel:"+level,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                /*user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(sharedPreferences.getString("username", "default_value"), sharedPreferences.getString("password", "default_value"));

                // Get auth credentials from the user for re-authentication
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                                user.updateEmail(email)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Account  updated successfully", Toast.LENGTH_SHORT).show();
                                                Map<String,Object> user = new HashMap<>();
                                                user.put("e-mail", email);
                                                user.put("name",name);
                                                user.put("surname", surname);
                                                user.put("proficiencyLevel",level);

                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Tag","userProfile is created for ID:"+ userID+"with Name:"+name+
                                                                " Surname:"+surname+" proficiencyLevel:"+level);

                                                    }
                                                });
                                                Intent intent = new Intent(getApplicationContext(), Profile.class);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Email update failed
                                                Toast.makeText(getApplicationContext(), "Account update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });*/


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, Profile.class));

            }
        });

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfile.this,"Reset Password Email Sent", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditProfile.this,"Unable to Send Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+userID+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);

            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) { //
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        StorageReference fileReference = storageReference.child("users/"+userID+"profile.jpg");
        //"users/"+mAuth.getCurrentUser().getUid()+"profile.jpg" should be used for unique file names
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(EditProfile.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }



}