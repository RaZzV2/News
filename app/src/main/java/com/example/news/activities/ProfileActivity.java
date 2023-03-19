package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.RealtimeDatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    RealtimeDatabaseManager realtimeDatabaseManager;

    TextView changeEmail;

    ImageView back;

    TextView changePhoneNumber;

    ImageView changeProfilePicture;

    TextView changeUsername;

    LinearLayout changeEmailLayout;

    LinearLayout changeUsernameLayout;

    LinearLayout changePhoneNumberLayout;



    void initialize() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        changeEmail.setHint(realtimeDatabaseManager.getCurrentUser().getEmail());
        changePhoneNumber.setHint(realtimeDatabaseManager.getCurrentUser().getPhoneNumber());
        realtimeDatabaseManager.getCurrentUserReference().child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                changeUsername.setHint(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        realtimeDatabaseManager.getCurrentUserReference().child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isDestroyed()) {
                    Glide.with(ProfileActivity.this)
                            .load(snapshot.getValue(String.class))
                            .into(changeProfilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        changeEmail = findViewById(R.id.changeEmail);
        changePhoneNumber = findViewById(R.id.changePhoneNumber);
        changeUsername = findViewById(R.id.changeUsername);


        changeProfilePicture = findViewById(R.id.changeProfilePicture);
        back = findViewById(R.id.back);
        changeEmailLayout = findViewById(R.id.changeEmailLayout);
        changeUsernameLayout = findViewById(R.id.changeUsernameLayout);
        changePhoneNumberLayout = findViewById(R.id.changePhoneNumberLayout);

        changeEmailLayout.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangeEmailActivity.class)));
        changeUsernameLayout.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangeUsernameActivity.class)));
        changePhoneNumberLayout.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangePhoneNumberActivity.class)));

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), result -> {
                    if (result != null) {
                        realtimeDatabaseManager = new RealtimeDatabaseManager();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference().child("images");
                        StorageReference imageRef = storageRef.child(result.getLastPathSegment());
                        UploadTask uploadTask = imageRef.putFile(result);
                        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            realtimeDatabaseManager.addImageToCurrentUser(downloadUrl);
                        }));
                    }
                });

        changeProfilePicture.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }
}