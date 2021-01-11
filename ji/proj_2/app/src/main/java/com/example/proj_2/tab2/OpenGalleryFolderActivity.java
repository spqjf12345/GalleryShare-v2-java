package com.example.proj_2.tab2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OpenGalleryFolderActivity extends AppCompatActivity {
    Intent intent = new Intent();
    Long bucketId = intent.getLongExtra("bucketId", -100);
    String bucketName = intent.getStringExtra("bucketName");
    public ArrayList<MediaFileData> list = new ArrayList<MediaFileData>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengalleryfolder);


        List<MediaFileData> ImageDataset = getFileList(this).stream().filter(a -> a.bucketId == bucketId).collect(Collectors.toList());


        //it.bucketId == bucketId;
        // }
        RecyclerView recyclerView1 = findViewById(R.id.opened_gallery);
        recyclerView1.setLayoutManager(new GridLayoutManager(this.getBaseContext(), 4));
        recyclerView1.setAdapter(new InGalleryImageAdapter(list));
// return MediaFileData which contains id, dateTaken, displayName, uri
        // Use MediaStore API

    }

    ArrayList<MediaFileData> getFileList(Context context){
        ArrayList<MediaFileData> folderList = new ArrayList<MediaFileData>();
        String[] projection = {MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_TAKEN,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.BUCKET_ID
        };


        String sortOrder = MediaStore.Files.FileColumns.DATE_TAKEN + " " + "DESC";

        //ContentResolver contentResolver1 = context.getContentResolver();
        ContentResolver contentResolver1 = context.getContentResolver();
        Cursor cursor = contentResolver1.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
        //Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        // cursor.use {
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
        int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN);
        int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        int bucketIDColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID);
        int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(idColumn);
            Date dateTaken = new Date(cursor.getLong(dateTakenColumn));
            String displayName = cursor.getString(displayNameColumn);
            Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString());
            Long bucketID = cursor.getLong(bucketIDColumn);
            String bucketName = cursor.getString(bucketNameColumn);

            MediaFileData MDF = new MediaFileData(bucketID, id, dateTaken, displayName, contentUri, bucketName);
            folderList.add(MDF);

        }
        // }

        return folderList;
    }


}

