package com.example.proj_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class apploginActivity extends AppCompatActivity {
    ApiService apiService;
    String appId;
    String token;
    String userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applogin);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        apiService = RetroClient.getInstance().getApiService();

        EditText nicknameEditText = findViewById(R.id.nicknameEditText);

        Call<ResponseBody> req = apiService.login(token);
//        System.out.println(req);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if  (response.code()==100){
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(apploginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (response.code()==200){
                    Toast.makeText(getApplicationContext(), "need to register", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_applogin);

                    Button registerBtn = findViewById(R.id.registerBtn);
                    registerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String nickname = nicknameEditText.getText().toString();
                            Call<ResponseBody> registerreq = apiService.register(nickname);

                            registerreq.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.code()==100){
                                        Toast.makeText(getApplicationContext(), "registered, and logged in", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(apploginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (response.code()==200){
                                        Toast.makeText(getApplicationContext(), "duplicate nickname, try another one", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
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
}
