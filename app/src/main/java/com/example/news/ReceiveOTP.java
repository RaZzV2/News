package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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
                    String currentPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    phoneConfirmation(currentPhoneNumber, code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message;
            }
        });
    }

    void phoneConfirmation(String currentPhoneNumber, String code){
        authManager = new AuthManager();
        authManager.getOTPReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OneTimePassword oneTimePassword = dataSnapshot.getValue(OneTimePassword.class);
                    if(oneTimePassword != null && oneTimePassword.getPhoneNumber().equals(currentPhoneNumber)){
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(oneTimePassword.getOtpCode(), code);
                        authManager.getFirebaseAuth().checkActionCode(Objects.requireNonNull(phoneAuthCredential.getSmsCode())).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(ReceiveOTP.this,"Corect",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ReceiveOTP.this,"Incorect",Toast.LENGTH_SHORT).show();
                            }
                        });
                        }

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