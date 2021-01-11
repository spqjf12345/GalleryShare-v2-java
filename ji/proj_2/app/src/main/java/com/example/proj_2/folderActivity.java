package com.example.proj_2;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class folderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
        recyclerView = (RecyclerView)findViewById(R.id.rv_folder);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<list_folder> items= new ArrayList<>();
        list_folder[] item=new list_folder[5];
        item[0]=new list_folder(R.drawable.minion1,"#1","제주도");
        item[1]=new list_folder(R.drawable.minion2,"#2", "과천");
        item[2]=new list_folder(R.drawable.minion3,"#3", "동두천");
        item[3]=new list_folder(R.drawable.minion4,"#4", "부산");


        for(int i=0;i<5;i++) items.add(item[i]);

        recyclerView.setAdapter(new folderAdapter(getApplicationContext(), items, R.layout.fragment_2));

    }
}
