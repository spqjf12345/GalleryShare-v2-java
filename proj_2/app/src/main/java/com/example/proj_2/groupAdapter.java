package com.example.proj_2;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.acl.Group;
import java.util.ArrayList;

public class groupAdapter extends RecyclerView.Adapter<groupAdapter.MyViewHolder> {
    private String nickname;
    private ArrayList<String> group = new ArrayList<String>();

    groupAdapter(ArrayList<String> group_, String nickname_){ group = group_; nickname = nickname_;}



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView group_name;

        public MyViewHolder(View view){
            super(view);
            this.group_name = view.findViewById(R.id.gp_name);
            Log.d("group_name", group_name.toString());

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group, parent, false);
        return new MyViewHolder(adapterLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = group.get(position);
        holder.group_name.setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //int curPos = getAdapterPosition();
                Intent intent = new Intent(v.getContext(), /*go into gallery folder activity*/ folderActivity.class);
                intent.putExtra("groupname", item);
                intent.putExtra("nickname", nickname);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("group", group.toString());
        return group.size();
    }


}
