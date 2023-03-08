package com.example.news;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        authManager.getOTPReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {

                        String[] currentUserPhone = new String[1];
                        authManager.getUsersReference().child(authManager.getCurrentUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotAux) {
                                currentUserPhone[0] = snapshotAux.child("phoneNumber").getValue(String.class);
                                String currentPhoneNumber = snapshot1.child("phoneNumber").getValue(String.class);
                                String currentOTP = snapshot1.child("otpCode").getValue(String.class);
                                assert currentPhoneNumber != null;
                                if(currentPhoneNumber.equals(currentUserPhone[0])) {
                                    phoneConfirmation(currentOTP, code);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message;
            }
        });
    }

    void phoneConfirmation(String currentPhoneNumber, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(currentPhoneNumber, code);
        authManager.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ReceiveOTP.this, "Succes", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ReceiveOTP.this, "Failed", Toast.LENGTH_SHORT).show();
                }
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