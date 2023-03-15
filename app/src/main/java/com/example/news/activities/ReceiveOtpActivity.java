package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.AuthManager;
import com.example.news.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ReceiveOtpActivity extends AppCompatActivity {
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
        authManager.getUsersReference().child(authManager.getCurrentUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if (task.isSuccessful()) {
                    Toast.makeText(ReceiveOtpActivity.this, "Phone number has been verified!", Toast.LENGTH_SHORT).show();
                    authManager.getCurrentUserConfirmedPhoneReference().setValue(true);
                    Intent intent = new Intent(getApplicationContext(), ConfirmEmailActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ReceiveOtpActivity.this, "The OTP code entered is wrong!", Toast.LENGTH_SHORT).show();
                }
        });
    }

    void initialize() {
        firstButton.requestFocus();
        firstButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Character.isDigit(s.charAt(0))) {
                    secondButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        secondButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Character.isDigit(s.charAt(0))) {
                    thirdButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    secondButton.focusSearch(View.FOCUS_LEFT).requestFocus();
                }

            }
        });

        thirdButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Character.isDigit(s.charAt(0))) {
                    fourthButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    thirdButton.focusSearch(View.FOCUS_LEFT).requestFocus();
                }
            }
        });

        fourthButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Character.isDigit(s.charAt(0))) {
                    fifthButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    fourthButton.focusSearch(View.FOCUS_LEFT).requestFocus();
                }
            }
        });

        fifthButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Character.isDigit(s.charAt(0))) {
                    sixthButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    fifthButton.focusSearch(View.FOCUS_LEFT).requestFocus();
                }
            }
        });

        sixthButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    sixthButton.focusSearch(View.FOCUS_LEFT).requestFocus();
                }
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
                        .setTimeout(60L, TimeUnit.SECONDS).setActivity(ReceiveOtpActivity.this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                                Toast.makeText(ReceiveOtpActivity.this, "The code has been sent!", Toast.LENGTH_SHORT).show();
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

        initialize();

        resendOTP.setOnClickListener(v -> {
            resendCode();
            resendOTP.setEnabled(false);
            startTimer();
        });


        verifyButton.setOnClickListener(v -> {
            String code = firstButton.getText().toString() + secondButton.getText().toString() + thirdButton.getText().toString()
                    + fourthButton.getText().toString() + fifthButton.getText().toString() + sixthButton.getText().toString();
            if(code.length() == 6) {
                finishConfirmation(code);
            }
            else {
                Toast.makeText(getApplicationContext(), "The OTP code is not entered correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }
}