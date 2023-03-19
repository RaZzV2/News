package com.example.news.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.RealtimeDatabaseManager;
import com.example.news.classes.Validator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class ChangeEmailActivity extends AppCompatActivity {

    EditText email;

    RealtimeDatabaseManager realtimeDatabaseManager;
    ImageView close, done;


    @Override
    protected void onStart() {
        super.onStart();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getCurrentUserReference().child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    email.setText(snapshot.getValue(String.class));
                    email.setSelection(email.getText().length());
                    email.requestFocus();
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
        setContentView(R.layout.activity_change_email);

        email = findViewById(R.id.changeEmail);
        close = findViewById(R.id.close);
        done = findViewById(R.id.done);

        close.setOnClickListener(v -> {
            startActivity(new Intent(ChangeEmailActivity.this, ProfileActivity.class));
            finish();
        });


        done.setOnClickListener(v -> {
            realtimeDatabaseManager = new RealtimeDatabaseManager();
            realtimeDatabaseManager.reload();
            Validator validator = new Validator();
            if (validator.isValidEmail(email.getText().toString())) {
                realtimeDatabaseManager.getFirebaseAuth().fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> signInMethods = task.getResult().getSignInMethods();
                        if (signInMethods == null || signInMethods.isEmpty()) { //pun notificare
                            realtimeDatabaseManager.getCurrentUser().updateEmail(email.getText().toString().trim()).addOnCompleteListener(secondaryTask -> {
                                   if(secondaryTask.isSuccessful()){
                                       realtimeDatabaseManager.getCurrentUserReference().child("email").setValue(email.getText().toString());
                                       startActivity(new Intent(ChangeEmailActivity.this, MainActivity.class));
                                       finish();
                                       Toast.makeText(getApplicationContext(), "Email has been changed successfully!", Toast.LENGTH_SHORT).show();
                                       realtimeDatabaseManager.logOut();
                                   }
                                   else {
                                       Toast.makeText(this, Objects.requireNonNull(secondaryTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                            });
                        } else {
                            Toast.makeText(ChangeEmailActivity.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangeEmailActivity.this, "Error occurred while checking email!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ChangeEmailActivity.this, "The email address you entered is invalid!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}