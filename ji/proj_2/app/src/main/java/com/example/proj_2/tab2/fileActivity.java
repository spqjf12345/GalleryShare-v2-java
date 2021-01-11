package com.example.proj_2.tab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class fileActivity extends AppCompatActivity {
    private int pickImage = 100;
    private int capturePhoto = 101;

    RecyclerView recyclerView;
    ArrayList<list_image> imageList = new ArrayList<list_image>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file);
        Button btn_camera = findViewById(R.id.btn_camera);
        Button btn_gallery = findViewById(R.id.btn_gallery);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

        recyclerView = findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setAdapter(new fileAdapter(getApplicationContext(), imageList, R.layout.file));
        recyclerView.setHasFixedSize(true);

    }


        private void loadImage () {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, pickImage);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                /* gallery load */
                if (requestCode == pickImage) {
                    Uri imageUri = data.getData();
                    imageList.add(new list_image(imageUri));
                }
                /* camera load */
                if (requestCode == capturePhoto) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Uri changedUri = BitmapToUri(this, bitmap);
                    imageList.add(new list_image(changedUri));
                }

                //refreshFragment(fileActivity.this, getSupportFragmentManager());
            }
        }

        public Uri BitmapToUri (Context context, Bitmap bitmap){
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        }

        private void takePicture () {
            Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(capture, capturePhoto);
        }

        void refreshFragment (Fragment fragment, FragmentManager fragmentManager){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.detach(fragment).attach(fragment).commit();
        }
    }

