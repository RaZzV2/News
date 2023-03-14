package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.AuthManager;
import com.example.news.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    AuthManager authManager;
    EditText email;
    EditText password;
    Button loginButton;

    TextView newUser;
    FirebaseAuth firebaseAuth;

    TextView forgotPassword;

    @Override
    public void onStart() {
        super.onStart();
        authManager = new AuthManager();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if(currentUser != null) {
            authManager.reload();
            onStartVerification();
        }
    }

        public void onStartVerification(){
            authManager.getCurrentUserConfirmedPhoneReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Intent intent;
                        if (Boolean.FALSE.equals(snapshot.getValue(Boolean.class)))
                            intent = new Intent(getApplicationContext(), SendOtpActivity.class);
                        else if (!authManager.getCurrentUser().isEmailVerified() && authManager.getCurrentUser().getEmail() != null) {
                            intent = new Intent(getApplicationContext(), EmailOpenerActivity.class);
                        }
                        else
                            intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    public void authenticate(String emailContent, String passwordContent){
        authManager = new AuthManager();
        authManager.login(emailContent, passwordContent, task -> {
            if(task.isSuccessful()){
                secureAuthenticate();
            }
            else{
                Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void verifyAuthenticate(DataSnapshot snapshot){

        if (snapshot.hasChild("confirmedPhone") && snapshot.child("confirmedPhone").getValue() != null) {
            boolean numberConfirmationColumnValue = Boolean.TRUE.equals(snapshot.child("confirmedPhone").getValue(Boolean.class));
            Intent intent;
            if(!numberConfirmationColumnValue){
                intent = new Intent(MainActivity.this, SendOtpActivity.class);
            }
            else if(!authManager.getCurrentUser().isEmailVerified()){
                Toast.makeText(MainActivity.this, "Email is not verified!",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, MainActivity.class);
            }
            else {
                intent = new Intent(MainActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }


    public void secureAuthenticate(){
        authManager = new AuthManager();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if(currentUser != null){
            DatabaseReference currentUserDatabase = authManager.getCurrentUserReference();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authManager = new AuthManager();
        firebaseAuth = authManager.getFirebaseAuth();
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
            String emailContent = email.getText().toString();
            String passwordContent = password.getText().toString();

            if(TextUtils.isEmpty(emailContent)){
                Toast.makeText(this, "Email can't be empty!", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(passwordContent)) {
                Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show();
            }
            else {
                authenticate(emailContent, passwordContent);
            }
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}