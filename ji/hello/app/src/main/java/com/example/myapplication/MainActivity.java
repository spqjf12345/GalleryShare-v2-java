package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ApiService apiService;
    TextView tvData;
    TextView returnText;
    TextView filename;
    ImageView showImg;
    Bitmap pickedImg;
    Uri selectedUri;
    byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView)findViewById(R.id.textView);
        returnText = (TextView)findViewById(R.id.returnText);
        showImg = (ImageView)findViewById(R.id.ImageSHow);
        filename = findViewById(R.id.filename);
        Button btn = (Button)findViewById(R.id.httpTest);
        Button imagepick = (Button)findViewById(R.id.imagepickbtn);

        initRetrofitClient();

        //버튼이 클릭되면 여기 리스너로 옴
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new JSONTask().execute("http://192.249.18.230:3000/post");//AsyncTask 시작시킴
                //volleypost();
                try {
                    postimage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imagepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void initRetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://192.249.18.230:3000").client(client).build().create(ApiService.class);
    }

    public void postimage() throws IOException {

        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), imageData);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", "abc", reqfile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        Call<ResponseBody> req = apiService.postImage(body, name);

        req.enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    returnText.setText("Uploaded");
                    returnText.setTextColor(Color.BLUE);
                }

                Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                returnText.setText("Uploaded Failed!");
                returnText.setTextColor(Color.RED);
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        }

        );
    }



    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        pickedImg = selectedImage;
                        showImg.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        selectedUri = selectedImage;
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                            byte[] buffer = new byte[1024];
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffer.length);

                            int len = 0;
                            while ((len = inputStream.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }
                            imageData = byteArrayOutputStream.toByteArray();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

}
