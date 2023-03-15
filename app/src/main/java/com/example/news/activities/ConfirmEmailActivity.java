package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.AuthManager;
import com.example.news.R;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmEmailActivity extends AppCompatActivity {

    Button openGmail;

    AuthManager authManager;


    @Override
    public void onStart() {
        super.onStart();
        authManager = new AuthManager();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if(currentUser != null) {
            if(currentUser.isEmailVerified()){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void sendEmailConfirmation() {
        authManager = new AuthManager();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if(currentUser != null) {
            currentUser.sendEmailVerification().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this, "An email has been sent!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "The email was not sent, please report the problem to the developer!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        authManager = new AuthManager();
        authManager.reload();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_opener);
        sendEmailConfirmation();
        openGmail = findViewById(R.id.getEmail);

        openGmail.setOnClickListener(v -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Gmail is not installed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}