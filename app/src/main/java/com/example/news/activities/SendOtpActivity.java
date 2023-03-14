package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.AuthManager;
import com.example.news.R;
import com.example.news.classes.Validator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOtpActivity extends AppCompatActivity {

    AuthManager authManager;

    EditText inputMobile;

    Button getOTP;

    boolean inputMobileIsEmpty(String mobile){
        return mobile.isEmpty();
    }

    String startingDigitPhoneNumber(String phoneNumber){
        if(phoneNumber.startsWith("0")){
            phoneNumber = phoneNumber.substring(1);
        }
        phoneNumber = "+40" + phoneNumber;
        return phoneNumber;
    }

    void addPhoneNumberToCurrentUser(String phoneNumber){
        authManager = new AuthManager();
        Validator validator = new Validator();
        phoneNumber = startingDigitPhoneNumber(phoneNumber);
        if(inputMobileIsEmpty(phoneNumber)){
            Toast.makeText(SendOtpActivity.this, "Enter mobile", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!validator.isValidPhoneNumber(phoneNumber)){
            Toast.makeText(SendOtpActivity.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        authManager.addPhoneNumberToUser(phoneNumber);
        sendConfirmationCode(phoneNumber);
        Toast.makeText(SendOtpActivity.this, "Code sent!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SendOtpActivity.this, ReceiveOtpActivity.class);
        startActivity(intent);
    }

    void sendConfirmationCode(String phoneNumber){
        authManager = new AuthManager();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(authManager.getFirebaseAuth()).setPhoneNumber(phoneNumber).setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                            }
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                authManager.getCurrentUserOtpCode().setValue(s);
                            }
                        }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    @Override
    protected void onResume() {
        super.onResume();
        authManager = new AuthManager();
        if(authManager.getCurrentUser() == null){
            Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);


        authManager = new AuthManager();
        inputMobile = findViewById(R.id.inputMobile);
        getOTP = findViewById(R.id.getOTP);

        getOTP.setOnClickListener(v -> {
            String phoneNumber = inputMobile.getText().toString().trim();
            addPhoneNumberToCurrentUser(phoneNumber);

        });
    }
}