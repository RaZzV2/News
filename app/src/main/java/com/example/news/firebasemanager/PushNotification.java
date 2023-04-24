package com.example.news.firebasemanager;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotification extends FirebaseMessagingService {

    RealtimeDatabaseManager realtimeDatabaseManager = new RealtimeDatabaseManager();
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        realtimeDatabaseManager.addTokenToCurrentUser(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
