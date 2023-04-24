package com.example.news.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.datastream.SendImageToServerAsyncTask;
import com.example.news.R;
import com.example.news.interfaces.OnTaskCompleteListener;
import com.example.news.models.SearchByImageModel.ImageQuery;

import java.io.ByteArrayOutputStream;

public class ImageSenderActivity extends AppCompatActivity {

    private ImageView currentPhoto;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sender);
        currentPhoto = findViewById(R.id.searchByImageView);
        Button sendButton = findViewById(R.id.sendButton);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri imageUri = data.getData();
                        currentPhoto.setImageURI(imageUri);
                    }
                });

        currentPhoto.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(gallery);
        });

        sendButton.setOnClickListener(v -> {
            Bitmap bitmap = ((BitmapDrawable) currentPhoto.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

            SendImageToServerAsyncTask sendImageToServerAsyncTask = new SendImageToServerAsyncTask();
            sendImageToServerAsyncTask.execute(base64String);

            sendImageToServerAsyncTask.setOnTaskCompletedListener(new OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery>() {
                @Override
                public void onTaskCompleted(ImageQuery result) {
                    Intent intent = new Intent(ImageSenderActivity.this, SearchByImageActivity.class);
                    intent.putExtra("imageQuery", result);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {

                }
            });



        });
    }
}