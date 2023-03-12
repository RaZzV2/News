package com.example.news;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    AuthManager authManager;
    EditText email;
    Button sendEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        sendEmail = findViewById(R.id.sendEmail);
        authManager = new AuthManager();

        sendEmail.setOnClickListener(v -> authManager.getFirebaseAuth().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email!", Toast.LENGTH_SHORT).show();
            }
        }));



    }
}