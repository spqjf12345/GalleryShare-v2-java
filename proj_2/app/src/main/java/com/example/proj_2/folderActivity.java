package com.example.proj_2;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class folderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ApiService apiService;
    ArrayList<JSONObject> folderlist = new ArrayList<>();
    folderAdapter folderAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
        recyclerView = (RecyclerView)findViewById(R.id.rv_folder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        apiService = RetroClient.getInstance().getApiService();


        String nickname = getIntent().getStringExtra("nickname");
        String groupname = getIntent().getStringExtra("groupname");

        Call<ResponseBody> getfolderlistreq = apiService.getfolderlist(nickname,groupname);

        getfolderlistreq.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i= 0; i<jsonArray.length();i++){
                        folderlist.add(jsonArray.getJSONObject(i));
                    }
                    folderAdapter = new folderAdapter(folderActivity.this, folderlist, nickname, groupname);
                    recyclerView.setAdapter(folderAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



        findViewById(R.id.addfolderbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog02 = new Dialog(v.getContext());
                dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View dialogView = inflater.inflate(R.layout.add_folder_dialog, null);
                dialog02.setContentView(dialogView);
                dialog02.show();
                Button ADD_Button = dialogView.findViewById(R.id.folder_add);
                Button CANCEL_Button = dialogView.findViewById(R.id.folder_cancel);
                EditText folderName = dialogView.findViewById(R.id.tv_folderName);
                EditText location = dialogView.findViewById(R.id.tv_location);

                CANCEL_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog02.dismiss();
                    }

                });
                ADD_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String id = "";

                        //String dialogNumber = dialogView.findViewById(R.id.addNumber_).toString();
                        String foldername = folderName.getText().toString();
                        String locationname = location.getText().toString();
                        Call<ResponseBody> mkfolderreq = apiService.makefolder(nickname, groupname, foldername, locationname);

                        mkfolderreq.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    JSONObject responsejson = new JSONObject(response.body().string());
                                    if (responsejson.getInt("statuscode") == 100) {
                                        Toast.makeText(folderActivity.this, "folder created", Toast.LENGTH_SHORT);
                                        folderlist.add(responsejson.getJSONObject("folder"));
                                        dialog02.dismiss();
                                        folderAdapter.notifyDataSetChanged();
                                    } else if (responsejson.getInt("statuscode") == 200) {
                                        Toast.makeText(folderActivity.this, "duplcate foldername", Toast.LENGTH_SHORT);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    }

                });


            }
        });
    }





}

