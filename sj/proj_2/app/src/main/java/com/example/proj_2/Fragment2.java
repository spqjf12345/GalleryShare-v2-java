package com.example.proj_2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static java.nio.file.Paths.get;

public class Fragment2 extends Fragment {
    String name = "";
    private View view;
    @Nullable
    private Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        return view;
    }

    public boolean condition(MediaFileData md1, MediaFileData md2){
        if(md1.bucketId == md2.bucketId){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<MediaFileData> ImageDataset = getFileList(getContext());

        ArrayList<MediaFileData> folderDataset = new ArrayList<MediaFileData>();
        ArrayList<Integer> countImages = new ArrayList<Integer>();
        ArrayList<Long> folderId = new ArrayList<Long>();

        //Map<MediaFileData, MediaFileData> condition = new HashMap<>();
        //condition.put(mdf1, mdf2);

        //(MediaFileData,MediaFileData)->Boolean condition = { MediaFileData mdf1, MediaFileData mdf2 -> mdf1.bucketId == mdf2.bucketId }

        //MediaFileData mdf1 = new MediaFileData(;
        // MediaFileData mdf2 = new MediaFileData();
        for(Iterator<MediaFileData> mit = ImageDataset.iterator(); mit.hasNext();){
            MediaFileData mitr = mit.next();
            for(Iterator <MediaFileData> fit = folderDataset.iterator(); fit.hasNext();) {
                MediaFileData fitr = fit.next();
                if (condition(fitr, mitr)) {
                    folderDataset.add(mitr);
                    countImages.add(1);
                    folderId.add(mitr.bucketId);
                } else {
                    countImages.add(folderId.indexOf(mitr.bucketId), 1);
                }
            }
        }

            /*if(listContainsContitionedItem(folderDataset, mitr, condition(mdf1, mdf2))){
                folderDataset.add(mitr);
                countImages.add(1);
                folderId.add(mitr.bucketId);
            } else {
                countImages.add(folderId.indexOf(mitr.bucketId),1);
            }
        }*/

        /*for (int i = 0; i < ImageDataset.size(); i++) {
            if(listContainsContitionedItem(folderDataset, ImageDataset.indexOf(i), condition).not){
                forderDataset.add(i);
                countImages.add(1);
                folderId.add(ImageDataset.indexOf(i).bucketId);
            } else{
                countImages[folderId.indexOf(it.bucketId)] += 1
            }
        }
        ImageDataset.forEach {
            if (listContainsContitionedItem(folderDataset, it, condition).not()) {
                folderDataset.add(it)
                countImages.add(1)
                folderId.add(it.bucketId)
            } else {
                countImages[folderId.indexOf(it.bucketId)] += 1
            }
        }*/


        FloatingActionButton camerafab = view.findViewById(R.id.camerafab);
        camerafab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    takePicture();
                }}
        });

        RecyclerView recyclerView = view.findViewById(R.id.gallery);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(new galleryAdapter());
        //val recyclerView: RecyclerView = view.gallery
        //recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        //recyclerView.adapter = GalleryAdapter(requireContext(), folderDataset, countImages)


    }


   /* public boolean listContainsContitionedItem(ArrayList<MediaFileData> list, MediaFileData item, boolean condition(MediaFileData md1, MediaFileData md2)) {
        for(Iterator<MediaFileData> it = list.iterator(); it.hasNext();){
            if(condition(md1, md2)){
                return true;
            }
        }
        return false;

        //list.forEach { when (condition(it, item)){ true -> return true} }
        //return false
    }*/



    int REQUEST_TAKE_PHOTO = 1;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                Uri changedUri = BitmapToUri(this.requireContext(), bitmap);
            }
            refreshFragment(this, getFragmentManager());
        }
    }

    /*onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {

        if (requestCode == REQUEST_TAKE_PHOTO) {
        var bundle: Bundle? = data?.getExtras()
        var bitmap: Bitmap = bundle?.get("data") as Bitmap
        var changedUri: Uri = BitmapToUri(this.requireContext(), bitmap)
        //ImageDataset.add(MediaFileData(changedUri))
        //gallery.setImageBitmap(bitmap)
        }
        refreshFragment(this, activity?.supportFragmentManager!!)
        }
        }*/

    Uri BitmapToUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        ContentResolver contentResolver = context.getContentResolver();
        String path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null);
        return Uri.parse(path.toString());
    }

    private void takePicture(){
        //카메라 앱 실행
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(capture, REQUEST_TAKE_PHOTO);
    }

    void refreshFragment(Fragment fragment, FragmentManager fragmentManager){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(fragment).attach(fragment).commit();
    }


    String currentPhotoPath = "";


    List<MediaFileData> getFileList(Context context) {
        ArrayList<MediaFileData> folderList = new ArrayList<MediaFileData>();
        String[] projection = {MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_TAKEN,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.BUCKET_ID};


        //String sortOrder = MediaStore.Files.FileColumns.DATE_TAKEN + "DESC";


        ContentResolver contentResolver1 = context.getContentResolver();
        Cursor cursor = contentResolver1.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null
        );

        //cursor.use {
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
        int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN);
        int displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        int bucketIDColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID);
        int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            Date dateTaken = new Date(cursor.getLong(dateTakenColumn));
            String displayName = cursor.getString(displayNameColumn);
            Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

            long bucketID = cursor.getLong(bucketIDColumn);
            String bucketName = cursor.getString(bucketNameColumn);

            Log.d(
                    "test",
                    "id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri\n"
            );

            MediaFileData MDF = new MediaFileData(bucketID, id, dateTaken, displayName, contentUri, bucketName);
            folderList.add(MDF);

        }

        return folderList;
    }



    // class MediaStoreFileType(Uri externalContentUri, String mimeType, String pathByDCIM) {
    //     Uri externalContentUri;
    //    String mimeType;
    //    String pathByDCIM;

    //void IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*","/image");

    // void AUDIO(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*","/audio");

    // void VIDEO(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*","/video");
    // }
}








