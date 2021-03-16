package com.example.proj_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fileActivity extends AppCompatActivity {
    private int pickImage = 100;
    private int capturePhoto = 101;

    private String nickname;
    private String groupname;
    private String foldername;

    byte[] imageData;

    ApiService apiService;

    RecyclerView recyclerView;
    ArrayList<String> imageList = new ArrayList<>();

    fileAdapter fileAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nickname = getIntent().getStringExtra("nickname");
        groupname = getIntent().getStringExtra("groupname");
        foldername = getIntent().getStringExtra("foldername");

        apiService = RetroClient.getInstance().getApiService();



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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);

        nickname = getIntent().getStringExtra("nickname");
        groupname = getIntent().getStringExtra("groupname");
        foldername = getIntent().getStringExtra("foldername");

        Call<ResponseBody> imgreq = apiService.getimageurls(nickname, groupname, foldername);

        imgreq.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonobj = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonobj.getJSONArray("imageurls");

                    for (int i = 0; i < jsonArray.length();i++){
                        imageList.add(jsonArray.getString(i));
                    }

                    fileAdapter = new fileAdapter(fileActivity.this, imageList);
                    recyclerView.setAdapter(fileAdapter);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

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
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffer.length);

                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, len);
                        }
                        imageData = byteArrayOutputStream.toByteArray();

                        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), imageData);
                        String[] imagepathSplit = imageUri.getPath().split("/");
                        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", imagepathSplit[imagepathSplit.length-1], reqfile);
                        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

                        Call<ResponseBody> req = apiService.postImage(nickname, groupname, foldername, body, name);

                        req.enqueue(new Callback<ResponseBody>(){

                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code()==200){
                                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    imageList.add(jsonObject.getString("imageurl"));
                                    fileAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                /* camera load */
                if (requestCode == capturePhoto) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Uri changedUri = BitmapToUri(this, bitmap);
//                    imageList.add(new list_image(changedUri));
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

    }

