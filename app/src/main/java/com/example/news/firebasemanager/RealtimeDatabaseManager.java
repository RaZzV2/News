package com.example.news.firebasemanager;

import androidx.annotation.NonNull;

import com.example.news.classes.UserHelperClass;
import com.example.news.interfaces.PhoneNumbersCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RealtimeDatabaseManager {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseUser firebaseUser;
    private final FirebaseDatabase firebaseDatabase;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public RealtimeDatabaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public String getCurrentUserUid() {
        return firebaseUser.getUid();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseUser;
    }

    public void getAllPhoneNumbers(PhoneNumbersCallback callback) {
        DatabaseReference ref = firebaseDatabase.getReference().child("users");
        List<String> phoneNumbers = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                for (DataSnapshot childSnapshot : snapshot1.getChildren()) {
                    String uid = childSnapshot.getKey();
                    assert uid != null;
                    DatabaseReference phoneRef = ref.child(uid).child("phoneNumber");
                    phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            phoneNumbers.add(snapshot.getValue(String.class));
                            callback.onPhoneNumbersRetrieved(phoneNumbers);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logOut() {
        firebaseAuth.signOut();
    }

    public void reload() {
        firebaseUser.reload();
    }

    public void login(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void register(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void registerToRealtimeDatabase(UserHelperClass userHelperClass, String userId) {
        getUsersReference().child(userId).setValue(userHelperClass);
    }

    public void addPhoneNumberToUser(String phoneNumber) {
        getUsersReference().child(getCurrentUserUid()).child("phoneNumber").setValue(phoneNumber);
    }


    public void getCurrentUserImageProfile() {
        getCurrentUserReference().child("image");
    }

    public void addImageToCurrentUser(String downloadURL) {
        getCurrentUserReference().child("image").setValue(downloadURL);
    }

    public void addTokenToCurrentUser(String token){
        getCurrentUserReference().child("token").setValue(token);
    }

    public DatabaseReference getUsersReference() {
        return firebaseDatabase.getReference().child("users");
    }

    public DatabaseReference getCurrentUserToken() {
        return firebaseDatabase.getReference().child("users").child(getCurrentUserUid()).child("token");
    }

    public DatabaseReference getHistoryReference(String token) {
        return firebaseDatabase.getReference().child("notifications").child(token);
    }

    public DatabaseReference getCurrentUserConfirmedPhoneReference() {
        return getUsersReference().child(getCurrentUserUid()).child("confirmedPhone");
    }

    public DatabaseReference getCurrentUserOtpCode() {
        return getUsersReference().child(getCurrentUserUid()).child("otpCode");
    }

    public DatabaseReference getCurrentUserResendCode() {
        return getUsersReference().child(getCurrentUserUid()).child("resendCode");
    }

    public DatabaseReference getOTPReference() {
        return firebaseDatabase.getReference().child("otp");
    }

    public DatabaseReference getCurrentUserReference() {
        return getUsersReference().child(getCurrentUserUid());
    }

    public DatabaseReference getCurrentUserPhoneNumberReference() {
        return getUsersReference().child(getCurrentUserUid()).child("phoneNumber");
    }
}
