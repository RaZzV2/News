package com.example.news.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sender);
        currentPhoto = findViewById(R.id.searchByImageView);
        Button sendButton = findViewById(R.id.sendButton);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            startActivity(new Intent(ImageSenderActivity.this, HomeActivity.class));
            finish();
        });

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
            try {
                Bitmap bitmap = ((BitmapDrawable) currentPhoto.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SendImageToServerAsyncTask sendImageToServerAsyncTask = new SendImageToServerAsyncTask(0, 5);
                sendImageToServerAsyncTask.execute(base64String);

                sendImageToServerAsyncTask.setOnTaskCompletedListener(new OnTaskCompleteListener.OnTaskCompletedListener<ImageQuery>() {
                    @Override
                    public void onTaskCompleted(ImageQuery result) {
                        if(result != null) {
                            Intent intent = new Intent(ImageSenderActivity.this, SearchByImageActivity.class);
                            intent.putExtra("imageQuery", result);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(ImageSenderActivity.this, "This image is corrupted or it doesn't exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }
            catch(Exception e) {
                Toast.makeText(this, "This image is corrupted or it doesn't exist!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}