package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    AuthManager authManager;
    EditText email;
    EditText username;
    EditText password;
    EditText phone_number;
    Button registerButton;

    public void createAccountIfMailUnique(String emailContent, String passwordContent, String usernameContent){

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.checkEmailExists(emailContent).addOnSuccessListener(emailExists -> {
            if(emailExists){
                Toast.makeText(RegisterActivity.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
            }
            else {
                createAccount(emailContent, passwordContent, usernameContent);
            }
        });
    }

    public void createAccount(String emailContent, String passwordContent, String usernameContent){
        final UserHelperClass userHelperClass = new UserHelperClass(emailContent, usernameContent, false, false);
        authManager = new AuthManager();
        authManager.register(emailContent, passwordContent, task -> {
            if(task.isSuccessful()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                user.sendEmailVerification().addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        authManager.registerToRealtimeDatabase(userHelperClass, userId);
                        Intent intent = new Intent(getApplicationContext(), SendOTP.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });


                }
            }
            else {
                Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void checkValidatorsAndCreateAccount(String emailContent, String passwordContent, String usernameContent){
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
        else createAccountIfMailUnique(emailContent, passwordContent, usernameContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        authManager = new AuthManager();
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        phone_number = findViewById(R.id.phone_number);

        registerButton.setOnClickListener(v -> {
            final String emailContent = email.getText().toString();
            final String usernameContent = username.getText().toString();
            final String passwordContent = password.getText().toString();

            checkValidatorsAndCreateAccount(emailContent,passwordContent, usernameContent);
        });


    }

}