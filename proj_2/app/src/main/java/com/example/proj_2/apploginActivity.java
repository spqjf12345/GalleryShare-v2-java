package com.example.proj_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class apploginActivity extends AppCompatActivity {
    ApiService apiService;
    String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applogin);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        apiService = RetroClient.getInstance().getApiService();

        EditText nicknameEditText = findViewById(R.id.nicknameEditText);
        EditText phonenumEditText = findViewById(R.id.phonenumEditText);

        Call<ResponseBody> req = apiService.login(token);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject responsejson = new JSONObject(response.body().string());
                    if (responsejson.getInt("statuscode")==100){
                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(apploginActivity.this, MainActivity.class);
                        intent.putExtra("nickname", responsejson.getJSONObject("user").getString("nickname"));
                        startActivity(intent);
                    }
                    else if (responsejson.getInt("statuscode")==200){
                        Toast.makeText(getApplicationContext(), "need to register", Toast.LENGTH_SHORT).show();
                    }
                    else if (responsejson.getInt("statuscode")==300){
                        Toast.makeText(getApplicationContext(), "invalid token", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "what?", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
//        System.out.println(req);

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEditText.getText().toString();
                String phonenum = phonenumEditText.getText().toString();
                Call<ResponseBody> registerreq = apiService.register(token, nickname, phonenum);

                registerreq.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject responsejson = new JSONObject(response.body().string());
                            if (responsejson.getInt("statuscode")==100){ //100
                                Toast.makeText(getApplicationContext(), "registered, and logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(apploginActivity.this, MainActivity.class);
                                intent.putExtra("nickname", responsejson.getJSONObject("user").getString("nickname"));
                                startActivity(intent);
                                finish();
                            }
                            else if (responsejson.getInt("statuscode")==200){
                                Toast.makeText(getApplicationContext(), "duplicate nickname, try another one", Toast.LENGTH_SHORT).show();
                            }
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
            }
        });


    }
}
