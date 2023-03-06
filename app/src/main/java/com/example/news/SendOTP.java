package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    AuthManager authManager;

    String mVerificationId;

    PhoneAuthProvider.ForceResendingToken mResendToken;

    PhoneAuthOptions phoneAuthOptions;

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
            Toast.makeText(SendOTP.this, "Enter mobile", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!validator.isValidPhoneNumber(phoneNumber)){
            Toast.makeText(SendOTP.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        authManager.addPhoneNumberToUser(phoneNumber);
        sendConfirmationCode(phoneNumber);
        Intent intent = new Intent(SendOTP.this, ReceiveOTP.class);
        startActivity(intent);
    }

    void sendConfirmationCode(String phoneNumber){

        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
            }
        };


       PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(authManager.getFirebaseAuth())
               .setPhoneNumber(phoneNumber).setTimeout(60L,TimeUnit.SECONDS)
               .setActivity(this).setCallbacks(callbacks).build());
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