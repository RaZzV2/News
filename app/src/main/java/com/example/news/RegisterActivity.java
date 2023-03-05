package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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


    public void createAccountIfMailUnique(String emailContent, String passwordContent, String usernameContent, String phoneNumberContent){

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.checkEmailExists(emailContent).addOnSuccessListener(emailExists -> {
            if(emailExists){
                Toast.makeText(RegisterActivity.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
            }
            else {
                createAccount(emailContent, passwordContent, usernameContent, phoneNumberContent);
            }
        });
    }

    public void createAccount(String emailContent, String passwordContent, String usernameContent, String  phoneNumberContent){
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
                    else {
                        Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void checkValidatorsAndCreateAccount(String emailContent, String passwordContent, String usernameContent, String  phoneNumberContent){
        Validator validator = new Validator();

        if (!validator.isValidEmail(emailContent)) {
            Toast.makeText(this, "This email is invalid", Toast.LENGTH_SHORT).show();
        }

        else if (!validator.isValidUsername(usernameContent)) {
            Toast.makeText(this, "This username is invalid", Toast.LENGTH_SHORT).show();
            username.setError("The username:\n" +
                    "- It must start with a letter\n" +
                    "- It must have between 4 and 20 characters\n" +
                    "- Can also contain numbers");
        }
        else if (!validator.isValidPassword(passwordContent)) {
            Toast.makeText(this, "This password is invalid", Toast.LENGTH_SHORT).show();
            password.setError("The password\n" +
                    "- Must have at least 8 characters\n" +
                    "- Must contain at least one symbol\n" +
                    "- Must have at least one capital letter\n" +
                    "- Must contain at least one number");
        }
        else createAccountIfMailUnique(emailContent, passwordContent, usernameContent, phoneNumberContent);
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

            checkValidatorsAndCreateAccount(emailContent,passwordContent, usernameContent,phoneNumberContent);
        });


    }

}