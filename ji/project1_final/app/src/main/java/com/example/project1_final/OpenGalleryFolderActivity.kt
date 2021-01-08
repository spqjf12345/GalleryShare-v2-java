package com.example.project1_final

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1_final.adapter.InGalleryImageAdapter
import com.example.project1_final.fragments.GalleryFragmentTab
import com.example.project1_final.model.MediaFileData
import kotlinx.android.synthetic.main.item_folder_image.*
import kotlinx.android.synthetic.main.activity_opengalleryfolder.*
import java.util.*

class OpenGalleryFolderActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val bucketId=intent.getLongExtra("bucketId", -100)
        val bucketName=intent.getStringExtra("bucketName")

        actionBar?.title = bucketName;
        supportActionBar?.title = bucketName;
        setContentView(R.layout.activity_opengalleryfolder)

        val ImageDataset = getFileList(this, GalleryFragmentTab.MediaStoreFileType.IMAGE).filter{ it.bucketId == bucketId }

        val recyclerView: RecyclerView = opened_gallery

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = InGalleryImageAdapter(this, ImageDataset)
    }

    fun getFileList(context: Context, type: GalleryFragmentTab.MediaStoreFileType): List<MediaFileData> {

        val folderList = mutableListOf<MediaFileData>()
        val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_TAKEN,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.BUCKET_ID
        )

        val sortOrder = "${MediaStore.Files.FileColumns.DATE_TAKEN} DESC"

        val cursor = context.contentResolver.query(
                type.externalContentUri,
                projection,
                null,
                null,
                sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)
            val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val bucketIDColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
            val bucketNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                        type.externalContentUri,
                        id.toString()
                )
                val bucketID = cursor.getLong(bucketIDColumn)
                val bucketName = cursor.getString(bucketNameColumn)

                Log.d(
                        "test",
                        "id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri\n"
                )

                val MDF = MediaFileData(id, dateTaken, displayName, contentUri, bucketID, bucketName)
                folderList.add(MDF)

            }
        }

        return folderList
    }

}