package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.RealtimeDatabaseManager;
import com.example.news.R;
import com.example.news.classes.Validator;

public class ForgotPasswordActivity extends AppCompatActivity {
    RealtimeDatabaseManager realtimeDatabaseManager;
    EditText email;
    Button sendEmail;

    boolean isButtonClickable = true;


    void sendPasswordResetEmail(){

        Validator validator = new Validator();
        String emailContent = email.getText().toString();
        if(!TextUtils.isEmpty(emailContent)) {
            if (validator.isValidEmail(emailContent)) {
                realtimeDatabaseManager.getFirebaseAuth().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(ForgotPasswordActivity.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(ForgotPasswordActivity.this, "Email can't be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        sendEmail = findViewById(R.id.sendEmail);
        realtimeDatabaseManager = new RealtimeDatabaseManager();

        sendEmail.setOnClickListener(v -> {
                if(isButtonClickable) {
                sendPasswordResetEmail();
                isButtonClickable = false;
                new Handler().postDelayed(() -> isButtonClickable = true, 2000);
        }
        });
    }
}