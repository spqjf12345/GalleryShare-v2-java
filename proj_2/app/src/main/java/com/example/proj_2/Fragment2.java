package com.example.proj_2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class Fragment2 extends Fragment {
    public ArrayList<String> group = new ArrayList<String>();
    groupAdapter groupAdapter;
    ApiService apiService;
    String nickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_group, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nickname = getActivity().getIntent().getStringExtra("nickname");

        apiService = RetroClient.getInstance().getApiService();
//        groupAdapter= new groupAdapter(group, nickname);

        Call<ResponseBody> getgroupsreq = apiService.getgrouplist(nickname);



        getgroupsreq.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray responseJsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i< responseJsonArray.length();i++){
                        JSONObject groupJSON = responseJsonArray.getJSONObject(i);
                        group.add(groupJSON.getString("groupname"));
                        RecyclerView recyclerView = view.findViewById(R.id.rv_group);
                        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        groupAdapter =new groupAdapter(group, nickname);
                        recyclerView.setAdapter(groupAdapter);
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



        Button group_add = view.findViewById(R.id.gr_add);
        group_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                {
                    Dialog dialog02 = new Dialog(v.getContext());
                    dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.group_add_dialog, null);
                    dialog02.setContentView(dialogView);
                    dialog02.show();
                    Button ADD_Button = dialogView.findViewById(R.id.group_add);
                    Button CANCEL_Button = dialogView.findViewById(R.id.group_cancel);

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
                            TextView dialogName = dialogView.findViewById(R.id.tv_groupName);
                            //String dialogNumber = dialogView.findViewById(R.id.addNumber_).toString();
                            String sname = dialogName.getText().toString();
                            Call<ResponseBody> mkfolderreq = apiService.makegroup(nickname, sname);

                            mkfolderreq.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        JSONObject responsejson = new JSONObject(response.body().string());
                                        if (responsejson.getInt("statuscode")==100){
                                            Toast.makeText(getContext(), "folder created", Toast.LENGTH_SHORT);
                                            group.add(sname);
                                            dialog02.dismiss();
                                            groupAdapter.notifyDataSetChanged();
                                        }
                                        else if (responsejson.getInt("statuscode")==200){
                                            Toast.makeText(getContext(), "duplcate foldername", Toast.LENGTH_SHORT);
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
            }
        });

    }
}
