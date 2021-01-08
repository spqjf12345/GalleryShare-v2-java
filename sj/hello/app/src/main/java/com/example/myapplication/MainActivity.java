package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvData;
    TextView returnText;
    ImageView showImg;
    Bitmap pickedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView)findViewById(R.id.textView);
        returnText = (TextView)findViewById(R.id.returnText);
        showImg = (ImageView)findViewById(R.id.ImageSHow);
        Button btn = (Button)findViewById(R.id.httpTest);
        Button imagepick = (Button)findViewById(R.id.imagepickbtn);

        //버튼이 클릭되면 여기 리스너로 옴
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new JSONTask().execute("http://192.249.18.230:3000/post");//AsyncTask 시작시킴
                //volleypost();
                postimage();
            }
        });

        imagepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void volleypost(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.249.18.230:3000/post";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", "androidTest");
            jsonObject.put("name", "jungin");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                returnText.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });



        queue.add(jsonObjectRequest);
    }

    public void postimage(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.249.18.230:3000/post";

        JSONObject jsonObject = new JSONObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pickedImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodeImage = Base64.encodeToString(b, Base64.DEFAULT);
        System.out.println(encodeImage);

        try {
            jsonObject.put("id", "1");
            jsonObject.put("image", encodeImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                returnText.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
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
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                pickedImg = BitmapFactory.decodeFile(picturePath);
                                showImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


//
//    public class JSONTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("user_id", "androidTest");
//                jsonObject.put("name", "yun");
//
//                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                String url = "http://192.249.18.230:3000/";
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        returnText.setText(response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        returnText.setText("Nope");
//                    }
//                });
//
//
//
//                queue.add(stringRequest);
//
//
////                HttpURLConnection con = null;
////                BufferedReader reader = null;
//
////                try{
////                    //URL url = new URL("http://192.168.25.16:3000/users");
////                    URL url = new URL(urls[0]);
////                    //연결을 함
////                    con = (HttpURLConnection) url.openConnection();
////
////                    con.setRequestMethod("POST");//POST방식으로 보냄
////                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
////                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
////                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
////                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
////                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
////                    con.connect();
////
////                    //서버로 보내기위해서 스트림 만듬
////                    OutputStream outStream = con.getOutputStream();
////                    //버퍼를 생성하고 넣음
////                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
////                    writer.write(jsonObject.toString());
////                    writer.flush();
////                    writer.close();//버퍼를 받아줌
////
////                    //서버로 부터 데이터를 받음
////                    InputStream stream = con.getInputStream();
////
////                    reader = new BufferedReader(new InputStreamReader(stream));
////
////                    StringBuffer buffer = new StringBuffer();
////
////                    String line = "";
////                    while((line = reader.readLine()) != null){
////                        buffer.append(line);
////                    }
////
////                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
////
////                } catch (MalformedURLException e){
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } finally {
////                    if(con != null){
////                        con.disconnect();
////                    }
////                    try {
////                        if(reader != null){
////                            reader.close();//버퍼를 닫아줌
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }

//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            tvData.setText(result);//서버로 부터 받은 값을 출력해주는 부
//        }
//    }

}
