package com.example.proj_2.tab2;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;

public class folderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder);
        recyclerView = (RecyclerView)findViewById(R.id.rv_folder);


        LinearLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<list_folder> items= new ArrayList<>();
        list_folder[] item=new list_folder[4];
        item[0]=new list_folder(R.drawable.minion1,"#1","태평로 1가 35");
        item[1]=new list_folder(R.drawable.minion2,"#2", "마석로 110");
        item[2]=new list_folder(R.drawable.minion3,"#3", "대학로 291");
        item[3]=new list_folder(R.drawable.minion4,"#4", "둔산 3동");


        for(int i=0;i<4;i++) items.add(item[i]);

        recyclerView.setAdapter(new folderAdapter(getApplicationContext(), items, R.layout.fragment_2));

    }
}
