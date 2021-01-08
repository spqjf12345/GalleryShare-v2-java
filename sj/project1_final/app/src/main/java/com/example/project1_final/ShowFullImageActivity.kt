package com.example.project1_final

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_show_full_image.*

class ShowFullImageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide();
        supportActionBar?.hide();

        setContentView(R.layout.activity_show_full_image)

        val uri = Uri.parse(intent.getStringExtra("uri"))
        photoview.setImageURI(uri)
    }
}