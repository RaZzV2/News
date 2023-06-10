package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    RealtimeDatabaseManager realtimeDatabaseManager;

    boolean isButtonClickable = true;
    EditText email;
    EditText password;
    Button loginButton;

    TextView newUser;
    FirebaseAuth firebaseAuth;

    TextView forgotPassword;

    public void onStartVerification() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getUsersReference().child(realtimeDatabaseManager.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    boolean confirmedPhone = Boolean.TRUE.equals(snapshot.child("confirmedPhone").getValue(Boolean.class));

                    if (!snapshot.child("phoneNumber").exists() && !confirmedPhone) {
                        startActivity(new Intent(getApplicationContext(), SendOtpActivity.class));
                    } else if (snapshot.child("phoneNumber").exists() && !confirmedPhone) {
                        startActivity(new Intent(getApplicationContext(), ReceiveOtpActivity.class));
                    } else if (!realtimeDatabaseManager.getCurrentUser().isEmailVerified() && realtimeDatabaseManager.getCurrentUser().getEmail() != null) {
                        startActivity(new Intent(getApplicationContext(), ConfirmEmailActivity.class));
                    } else {
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                String token = task1.getResult();
                                realtimeDatabaseManager.addTokenToCurrentUser(token);
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void authenticate(String emailContent, String passwordContent) {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.login(emailContent, passwordContent, task -> {
            if (task.isSuccessful()) {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        String token = task1.getResult();
                        realtimeDatabaseManager.addTokenToCurrentUser(token);
                    }
                });
                secureAuthenticate();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong email or password!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void verifyAuthenticate(DataSnapshot snapshot) {

        if (snapshot.hasChild("confirmedPhone") && snapshot.child("confirmedPhone").getValue() != null) {
            boolean numberConfirmationColumnValue = Boolean.TRUE.equals(snapshot.child("confirmedPhone").getValue(Boolean.class));
            Intent intent;
            if (!numberConfirmationColumnValue) {
                intent = new Intent(MainActivity.this, SendOtpActivity.class);
            } else if (!realtimeDatabaseManager.getCurrentUser().isEmailVerified()) {
                Toast.makeText(MainActivity.this, "Email is not verified!", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, MainActivity.class);
            } else {
                intent = new Intent(MainActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }


    public void secureAuthenticate() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        FirebaseUser currentUser = realtimeDatabaseManager.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference currentUserDatabase = realtimeDatabaseManager.getCurrentUserReference();
            currentUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    verifyAuthenticate(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", error.getMessage(), error.toException());
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        FirebaseUser currentUser = realtimeDatabaseManager.getCurrentUser();
        if (currentUser != null) {
            realtimeDatabaseManager.reload();
            onStartVerification();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realtimeDatabaseManager = new RealtimeDatabaseManager();
        firebaseAuth = realtimeDatabaseManager.getFirebaseAuth();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        newUser = findViewById(R.id.new_user);
        forgotPassword = findViewById(R.id.forgot_password);

        newUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            if (isButtonClickable) {
                String emailContent = email.getText().toString();
                String passwordContent = password.getText().toString();
                if (TextUtils.isEmpty(emailContent)) {
                    Toast.makeText(this, "Email can't be empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordContent)) {
                    Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    authenticate(emailContent, passwordContent);
                }
                isButtonClickable = false;
                new Handler().postDelayed(() -> isButtonClickable = true, 2000);
            }
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}