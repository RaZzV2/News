package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.news.RealtimeDatabaseManager;
import com.example.news.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    RealtimeDatabaseManager realtimeDatabaseManager;

    TextView welcomeTitle;

    TextView searchBar;

    Toolbar toolbar;
    NavigationView navigationView;
    View headerView;
    TextView currentUsername;


    public void logOut() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setCurrentUsername() {
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getUsersReference().child(realtimeDatabaseManager.getCurrentUserUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.getValue(String.class);
                    currentUsername.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    protected void onStart() {
        super.onStart();
        realtimeDatabaseManager = new RealtimeDatabaseManager();
        if (realtimeDatabaseManager.getCurrentUser() == null) {
            realtimeDatabaseManager.logOut();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        searchBar = findViewById(R.id.searchBar);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        currentUsername = headerView.findViewById(R.id.usernameNavigation);

        findViewById(R.id.menu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));


        setCurrentUsername();

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.menuLogout:
                    logOut();
                    break;

                case R.id.menuProfile:
                    intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });


        searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        });

    }
}