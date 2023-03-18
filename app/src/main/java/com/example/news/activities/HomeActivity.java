package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.news.AuthManager;
import com.example.news.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    AuthManager authManager;

    TextView welcomeTitle;

    TextView searchBar;

    Toolbar toolbar;

    NavigationView navigationView;



    public void logOut(){
        authManager = new AuthManager();
        authManager.logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onStart() {
        super.onStart();
        authManager = new AuthManager();
        if(authManager.getCurrentUser() == null) {
            authManager.logOut();
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

        findViewById(R.id.menu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

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

        /*authManager.getUsersReference().child(authManager.getCurrentUserUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String username = snapshot.getValue(String.class);
                    welcomeTitle.setText(String.format("Welcome %s", username));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}