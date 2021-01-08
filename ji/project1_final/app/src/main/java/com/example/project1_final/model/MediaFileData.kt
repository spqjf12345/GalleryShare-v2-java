package com.example.project1_final.model

import android.net.Uri
import java.util.*

data class MediaFileData(
    val id:Long,
    val dateTaken: Date,
    val displayName: String,
    val uri: Uri,
    val bucketId: Long,
    val bucketName : String
)