package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ReceiveOTP extends AppCompatActivity {
    AuthManager authManager;

    Button verifyButton;

    EditText firstButton;
    EditText secondButton;
    EditText thirdButton;
    EditText fourthButton;
    EditText fifthButton;
    EditText sixthButton;


    void finishConfirmation(String code)
    {
        authManager = new AuthManager();
        authManager.getUsersReference().child(authManager.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String currentOTP = snapshot.child("otpCode").getValue(String.class);
                    phoneConfirmation(currentOTP, code);
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


        verifyButton.setOnClickListener(v -> {
            String code = firstButton.getText().toString() + secondButton.getText().toString() + thirdButton.getText().toString()
                    + fourthButton.getText().toString() + fifthButton.getText().toString() + sixthButton.getText().toString();
            finishConfirmation(code);
        });
    }
}