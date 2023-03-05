package com.example.news;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailVerification {
    private final DatabaseReference databaseReference;

    public EmailVerification() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public Task<Boolean> checkEmailExists(String email) {
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailExists = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserHelperClass userHelperClass = dataSnapshot.getValue(UserHelperClass.class);
                    if(userHelperClass != null && userHelperClass.getEmail().equals(email)){
                        emailExists = true;
                        break;
                    }
                }
                completionSource.setResult(emailExists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read value!", error.toException());
            }
        });
        return completionSource.getTask();
    }
}

