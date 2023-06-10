package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;
import com.example.news.classes.UserHelperClass;
import com.example.news.classes.Validator;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    RealtimeDatabaseManager realtimeDatabaseManager;

    boolean isButtonClickable = true;

    EditText email;
    EditText username;
    EditText password;
    EditText phone_number;
    Button registerButton;

    TextView alreadyHaveAnAccount;

    public void mailDuplicateVerification(String emailContent, String passwordContent, String usernameContent) {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getFirebaseAuth().fetchSignInMethodsForEmail(emailContent).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods == null || signInMethods.isEmpty()) {
                    createAccount(emailContent, passwordContent, usernameContent);
                } else {
                    Toast.makeText(RegisterActivity.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Error occurred while checking email!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAccount(String emailContent, String passwordContent, String usernameContent) {
        final UserHelperClass userHelperClass = new UserHelperClass(emailContent, usernameContent, false);
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.register(emailContent, passwordContent, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    realtimeDatabaseManager.registerToRealtimeDatabase(userHelperClass, userId);
                    Intent intent = new Intent(getApplicationContext(), SendOtpActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkValidatorsAndCreateAccount(String emailContent, String passwordContent, String usernameContent) {
        Validator validator = new Validator();

        if (!validator.isValidEmail(emailContent)) {
            Toast.makeText(this, "This email is invalid", Toast.LENGTH_SHORT).show();
        } else if (!validator.isValidUsername(usernameContent)) {
            Toast.makeText(this, "This username is invalid", Toast.LENGTH_SHORT).show();
            username.setError("The username:\n" +
                    "- It must start with a letter\n" +
                    "- It must have between 4 and 20 characters\n" +
                    "- Can also contain numbers");
        } else if (!validator.isValidPassword(passwordContent)) {
            Toast.makeText(this, "This password is invalid", Toast.LENGTH_SHORT).show();
            password.setError("The password\n" +
                    "- Must have at least 8 characters\n" +
                    "- Must contain at least one symbol\n" +
                    "- Must have at least one capital letter\n" +
                    "- Must contain at least one number");
        } else mailDuplicateVerification(emailContent, passwordContent, usernameContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        realtimeDatabaseManager = new RealtimeDatabaseManager();
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        phone_number = findViewById(R.id.phone_number);
        alreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAccount);

        registerButton.setOnClickListener(v -> {
            if (isButtonClickable) {
                final String emailContent = email.getText().toString();
                final String usernameContent = username.getText().toString();
                final String passwordContent = password.getText().toString();

                checkValidatorsAndCreateAccount(emailContent, passwordContent, usernameContent);
                isButtonClickable = false;
                new Handler().postDelayed(() -> isButtonClickable = true, 2000);
            }
        });


        alreadyHaveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

}