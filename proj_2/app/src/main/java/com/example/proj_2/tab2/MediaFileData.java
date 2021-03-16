package com.example.proj_2.tab2;

import android.net.Uri;

import java.util.Date;

public class MediaFileData {
    public Long bucketId;
    public Long id;
    public Date dateTaken;
    public String displayName;
    public Uri uri;
    public String bucketName;

    public MediaFileData(Long bucketId, Long id, Date dateTaken, String displayName, Uri uri, String bucketName){
        this.bucketId = bucketId;
        this.id = id;
        this.dateTaken = dateTaken;
        this.displayName = displayName;
        this.uri = uri;
        this.bucketName = bucketName;
    }


}
