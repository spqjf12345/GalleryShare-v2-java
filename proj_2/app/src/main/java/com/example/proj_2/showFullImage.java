package com.example.proj_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class showFullImage extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //actionBar.hide();
        //supportActionBar.hide();

        setContentView(R.layout.activity_show_full_image);
        String url = getIntent().getStringExtra("url");
        PhotoView photoview = findViewById(R.id.photoview);

        Glide.with(this)
                .load("http://192.249.18.230:3000/"+url)
                .into(photoview);
    }
}
