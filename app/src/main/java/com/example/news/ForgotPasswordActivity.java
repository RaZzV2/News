package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    AuthManager authManager;
    EditText email;
    Button sendEmail;


    void sendPasswordResetEmail(){

        Validator validator = new Validator();
        String emailContent = email.getText().toString();
        if(!TextUtils.isEmpty(emailContent)) {
            if (validator.isValidEmail(emailContent)) {
                authManager.getFirebaseAuth().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
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
        authManager = new AuthManager();

        sendEmail.setOnClickListener(v -> sendPasswordResetEmail());
    }
}