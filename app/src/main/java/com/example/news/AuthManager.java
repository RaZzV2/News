package com.example.news;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthManager {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseUser firebaseUser;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public AuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public String getCurrentUserUid (){
        return firebaseUser.getUid();
    }
    public FirebaseUser getCurrentUser() {
        return firebaseUser;
    }

    public void logOut() {
        firebaseAuth.signOut();
    }

    public void login(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void register(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void registerToRealtimeDatabase(UserHelperClass userHelperClass, String userId){
        getUsersReference().child(userId).setValue(userHelperClass);
    }

    public DatabaseReference getUsersReference(){
        return FirebaseDatabase.getInstance().getReference().child("users");
    }

    public DatabaseReference getCurrentUserReference(){
        return getUsersReference().child(getCurrentUserUid());
    }
}