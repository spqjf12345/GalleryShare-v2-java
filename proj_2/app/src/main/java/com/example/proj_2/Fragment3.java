package com.example.proj_2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;



public class Fragment3 extends Fragment implements OnMapReadyCallback
        , groupSelectAdapter.OnListItemSelectedInterface {


    public ArrayList<String> groupselect = new ArrayList<String>();
    RecyclerView recyclerView;
    //groupSelectAdapter groupSelectAdapter = new groupSelectAdapter(this, recyclerView, this,this); //new groupSelectAdapter(groupselect);



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupselect.add("Family");
        groupselect.add("Friend1");
        groupselect.add("Friend2");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        recyclerView = view.findViewById(R.id.rv_frag3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        final groupSelectAdapter groupselectAdapter = new groupSelectAdapter(this.getContext(), recyclerView, this, groupselect);
        recyclerView.setAdapter(groupselectAdapter);

        groupselectAdapter.setData(groupselect);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btn_map = view.findViewById(R.id.btn_getMap);
        btn_map.setOnClickListener(new View.OnClickListener() {
            //선택한 그룹의 folder1, folder2, .. 내의 이미지를 put ->
            @Override
            public void onClick(View v) {
                {
                    Intent intent_map = new Intent(getContext(), MapActivity.class);
                    startActivity(intent_map);
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    public void onItemSelected(View v, int position) {
        groupSelectAdapter.MyViewHolder viewHolder = (groupSelectAdapter.MyViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        Toast.makeText(getContext(), "make", Toast.LENGTH_SHORT).show();
    }
}

