package com.example.proj_2.tab2;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;

public class groupAdapter extends RecyclerView.Adapter<groupAdapter.MyViewHolder> {
    private ArrayList<String> group = new ArrayList<String>();

    public groupAdapter(ArrayList<String> group_){ group = group_; }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView group_name;

        public MyViewHolder(View view){
            super(view);
            this.group_name = view.findViewById(R.id.gp_name);
            Log.d("group_name", group_name.toString());
            /* click */
            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Log.d("getClick", "getClick");
                    int curPos = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), folderActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
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
    }

    @Override
    public int getItemCount() {
        Log.d("group", group.toString());
        return group.size();
    }


}
