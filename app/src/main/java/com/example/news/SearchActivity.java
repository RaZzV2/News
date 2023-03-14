package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends AppCompatActivity {

    ImageView back;

    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.back);
        searchBar = findViewById(R.id.searchBar);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}