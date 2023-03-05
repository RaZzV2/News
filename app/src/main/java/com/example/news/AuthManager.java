package com.example.news;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthManager {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;

    public AuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
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

    public DatabaseReference usersReference(){
        return FirebaseDatabase.getInstance().getReference().child("users");
    }

}
