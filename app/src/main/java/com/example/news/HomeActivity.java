package com.example.news;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    TextView logOut;

    AuthManager authManager;

    TextView welcomeTitle;


    public void logOut(){
        authManager = new AuthManager();
        authManager.logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(v -> logOut());
        welcomeTitle = findViewById(R.id.title);

        authManager = new AuthManager();
        authManager.getUsersReference().child(authManager.getCurrentUserUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String username = snapshot.getValue(String.class);
                    welcomeTitle.setText(getString(R.string.welcomeTitle) + username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}