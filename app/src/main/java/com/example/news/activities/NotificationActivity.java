package com.example.news.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.news.R;
import com.example.news.adapters.NotificationAdapter;
import com.example.news.classes.Notification;
import com.example.news.firebasemanager.RealtimeDatabaseManager;
import com.example.news.interfaces.OnItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;

    NotificationAdapter notificationAdapter;

    LinearLayoutManager layoutManager;

    List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        layoutManager = new LinearLayoutManager(NotificationActivity.this);
        notificationList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationAdapter(notificationList, NotificationActivity.this, this);

        RealtimeDatabaseManager realtimeDatabaseManager = new RealtimeDatabaseManager();
        realtimeDatabaseManager.getCurrentUserToken().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String token = Objects.requireNonNull(snapshot.getValue()).toString();
                    realtimeDatabaseManager.getHistoryReference(token).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String childKey = childSnapshot.getKey();
                                    String title = childSnapshot.child("title").getValue(String.class);
                                    String body = childSnapshot.child("body").getValue(String.class);
                                    notificationList.add(new Notification(title, childKey, body));
                                }
                            }
                            recyclerView.setAdapter(notificationAdapter);
                        }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                });
            }
        }

        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });
}

    @Override
    public void onItemClick(int position) {

    }
}