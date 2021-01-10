package com.example.facebook_login_retry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewActivity extends AppCompatActivity {
    ApiService apiService;
    String appId;
    String token;
    String userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Intent intent = getIntent();
        appId = intent.getStringExtra("appId");
        token = intent.getStringExtra("token");
        userid = intent.getStringExtra("userid");

        apiService = RetroClient.getInstance().getApiService();

        Call<ResponseBody> req = apiService.login(appId, token, userid);
//        System.out.println(req);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if  (response.code()==100){
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                }
                else if (response.code()==200){
                    Toast.makeText(getApplicationContext(), "need to register", Toast.LENGTH_SHORT).show();
                }
                else if (response.code()==300){
                    Toast.makeText(getApplicationContext(), "invalid token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });



    }

    private void login(){

    }
}
