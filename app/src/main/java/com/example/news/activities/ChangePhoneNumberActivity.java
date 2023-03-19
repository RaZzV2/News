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
import com.example.news.interfaces.PhoneNumbersCallback;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChangePhoneNumberActivity extends AppCompatActivity {

    RealtimeDatabaseManager realtimeDatabaseManager;

    ImageView done, close;
    EditText phone;


    void sendConfirmationCode(String phoneNumber) {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(realtimeDatabaseManager.getFirebaseAuth()).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS)
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
                                realtimeDatabaseManager.getCurrentUserOtpCode().setValue(s);
                            }
                        }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    String startingDigitPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1);
            phoneNumber = "+40" + phoneNumber;
        }
        return phoneNumber;
    }

    @Override
    protected void onStart() {
        super.onStart();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getCurrentUserReference().child("phoneNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phone.setText(snapshot.getValue(String.class));
                    phone.setSelection(phone.getText().length());
                    phone.requestFocus();
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
        setContentView(R.layout.activity_change_phone_number);

        phone = findViewById(R.id.changePhoneNumber);
        close = findViewById(R.id.close);
        done = findViewById(R.id.done);

        close.setOnClickListener(v -> {
            startActivity(new Intent(ChangePhoneNumberActivity.this, ProfileActivity.class));
            finish();
        });

        done.setOnClickListener(v -> {
            realtimeDatabaseManager = new RealtimeDatabaseManager();
            Validator validator = new Validator();
            String phoneContent = startingDigitPhoneNumber(phone.getText().toString());
            if (validator.isValidPhoneNumber(phoneContent)) {
                realtimeDatabaseManager.getAllPhoneNumbers(phoneNumbers -> {
                    if (phoneNumbers.contains(phoneContent)) {
                        Toast.makeText(ChangePhoneNumberActivity.this, "The phone number already exists associated with an account!", Toast.LENGTH_SHORT).show();
                    } else {
                        realtimeDatabaseManager.getCurrentUserReference().child("phoneNumber").setValue(phoneContent);
                        realtimeDatabaseManager.getCurrentUserReference().child("confirmedPhone").setValue(false);
                        sendConfirmationCode(phoneContent);
                        startActivity(new Intent(ChangePhoneNumberActivity.this, ReceiveOtpActivity.class));
                        finish();
                    }
                });
            } else {
                Toast.makeText(ChangePhoneNumberActivity.this, "The phone number entered is invalid!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}