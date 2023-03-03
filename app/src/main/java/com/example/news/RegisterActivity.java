package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText email;
    EditText username;
    EditText password;
    EditText phone_number;
    Button registerButton;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReferenceUsers;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        phone_number = findViewById(R.id.phone_number);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");

        registerButton.setOnClickListener(v -> {
            final String emailContent = email.getText().toString();
            final String usernameContent = username.getText().toString();
            final String phoneNumberContent = phone_number.getText().toString();
            final String passwordContent = password.getText().toString();

            final UserHelperClass userHelperClass = new UserHelperClass(emailContent, usernameContent, phoneNumberContent);


            firebaseAuth.createUserWithEmailAndPassword(emailContent, passwordContent)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if(task.isSuccessful()){
                            String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            DatabaseReference newUserRef = databaseReferenceUsers.child(userId);
                            newUserRef.setValue(userHelperClass);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
        });


    }
}