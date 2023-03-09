package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class EmailOpener extends AppCompatActivity {

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

    @Override
    public void onResume() {
        super.onResume();
        authManager = new AuthManager();
        FirebaseUser currentUser = authManager.getCurrentUser();
        authManager.reload();
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

        openGmail = findViewById(R.id.getEmail);

        openGmail.setOnClickListener(v -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                //finish();
            } else {
                Toast.makeText(this, "Gmail is not installed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}