package com.example.news.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.classes.Validator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ChangeUsernameActivity extends AppCompatActivity {

    EditText username;

    RealtimeDatabaseManager realtimeDatabaseManager;

    ImageView close, done;
    @Override
    protected void onStart() {
        super.onStart();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getCurrentUserReference().child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username.setText(snapshot.getValue(String.class));
                    username.setSelection(username.getText().length());
                    username.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        username = findViewById(R.id.changeUsername);
        close = findViewById(R.id.close);
        done = findViewById(R.id.done);

        close.setOnClickListener(v -> {
            startActivity(new Intent(ChangeUsernameActivity.this, ProfileActivity.class));
            finish();
        });

        done.setOnClickListener(v -> {
            realtimeDatabaseManager = new RealtimeDatabaseManager();
            Validator validator = new Validator();
            if(validator.isValidUsername(username.getText().toString())){
                realtimeDatabaseManager.getCurrentUserReference().child("username").setValue(username.getText().toString());
                startActivity(new Intent(ChangeUsernameActivity.this, ProfileActivity.class));
                finish();
            }
            else {
                Toast.makeText(ChangeUsernameActivity.this, "Username is not valid!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}