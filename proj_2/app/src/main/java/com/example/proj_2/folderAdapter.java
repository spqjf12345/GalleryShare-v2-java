package com.example.proj_2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.ViewHolder> {
    Context context;
    ArrayList<JSONObject> items;
    String nickname;
    String groupname;

    public folderAdapter(Context context, ArrayList<JSONObject> items, String nickname, String groupname) {
        this.context=context;
        this.items=items;
        this.nickname=nickname;
        this.groupname=groupname;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_folder,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject jsonObject = items.get(position);
        //Drawable drawable= context.getResources().getDrawable(item.getImage());

        try {
            JSONArray array = jsonObject.getJSONArray("imageurls");

            String foldername = jsonObject.getString("foldername");
            holder.folderName.setText(foldername);

            holder.area.setText(jsonObject.getString("location"));

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, fileActivity.class);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("groupname", groupname);
                    intent.putExtra("foldername", foldername);
                    context.startActivity(intent);
                }
            });
            Glide.with(holder.folder)
                    .load("http://192.249.18.230:3000/"+array.get(0))
                    .into(holder.folder);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView folder;
        TextView folderName;
        TextView area;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            folder=(ImageView)itemView.findViewById(R.id.iv_folder);
            folderName=(TextView)itemView.findViewById(R.id.tv_folder);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
            area = (TextView)itemView.findViewById(R.id.tv_area);
        }
    }
}