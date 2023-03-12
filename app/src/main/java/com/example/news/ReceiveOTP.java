package com.example.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ReceiveOTP extends AppCompatActivity {
    AuthManager authManager;

    TextView resendOTP;

    Button verifyButton;

    EditText firstButton;
    EditText secondButton;
    EditText thirdButton;
    EditText fourthButton;
    EditText fifthButton;
    EditText sixthButton;

    String mVerificationId;

    String resendToken;

    void finishConfirmation(String code)
    {
        authManager = new AuthManager();
        authManager.getUsersReference().child(authManager.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mVerificationId = snapshot.child("otpCode").getValue(String.class);
                    resendToken = snapshot.child("resendCode").getValue(String.class);
                    phoneConfirmation(mVerificationId, code);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void phoneConfirmation(String currentOTP, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(currentOTP, code);
        authManager.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ReceiveOTP.this, "Phone number has been verified!", Toast.LENGTH_SHORT).show();
                authManager.getCurrentUserConfirmedPhoneReference().setValue(true);
                Intent intent = new Intent(getApplicationContext(), EmailOpener.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(ReceiveOTP.this, "The OTP code entered is wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resendCode(){
        authManager = new AuthManager();
        authManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                assert phoneNumber != null;
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(authManager.getFirebaseAuth()).setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS).setActivity(ReceiveOTP.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent (@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                                authManager = new AuthManager();
                                authManager.getCurrentUserReference().child("otpCode").setValue(s);
                                Toast.makeText(ReceiveOTP.this, "The code has been sent!", Toast.LENGTH_SHORT).show();
                            }
                        }).build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startTimer(){
        CountDownTimer countDownTimer = new CountDownTimer(60000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                resendOTP.setText("RESEND OTP in " + (millisUntilFinished/1000) + " seconds");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                resendOTP.setEnabled(true);
                resendOTP.setText("RESEND OTP");
            }
        };
        countDownTimer.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        resendOTP.setEnabled(false);
        startTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_otp);

        verifyButton = findViewById(R.id.verifyOTP);
        firstButton = findViewById(R.id.input1);
        secondButton = findViewById(R.id.input2);
        thirdButton = findViewById(R.id.input3);
        fourthButton = findViewById(R.id.input4);
        fifthButton = findViewById(R.id.input5);
        sixthButton = findViewById(R.id.input6);
        resendOTP = findViewById(R.id.resendOTP);


        resendOTP.setOnClickListener(v -> {
            resendCode();
            resendOTP.setEnabled(false);
            startTimer();
        });


        verifyButton.setOnClickListener(v -> {
            String code = firstButton.getText().toString() + secondButton.getText().toString() + thirdButton.getText().toString()
                    + fourthButton.getText().toString() + fifthButton.getText().toString() + sixthButton.getText().toString();
            finishConfirmation(code);
        });
    }
}