package com.example.proj_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

class showFullImage extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //actionBar.hide();
        //supportActionBar.hide();

        setContentView(R.layout.activity_show_full_image);
        Intent intent = new Intent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        PhotoView photoview = findViewById(R.id.photoview);
        photoview.setImageURI(uri);
    }
}
