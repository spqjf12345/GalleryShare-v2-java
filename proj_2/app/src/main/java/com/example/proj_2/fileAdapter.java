package com.example.proj_2;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

public class fileAdapter extends RecyclerView.Adapter<fileAdapter.ViewHolder> {
    ArrayList<String> ImageList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gridImg_infolder);
        }
    }

    public fileAdapter(Context context, ArrayList<String> items) {
        this.context=context;
        this.ImageList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = ImageList.get(position);
        //Log.d("item", item.folderName + item.area + item.folderImage);
        //Drawable d = context.getResources().getDrawable(item.img);
        //Drawable drawable= context.getDrawable(R.id.); //getResources().getDrawable(item.folderImage, null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, showFullImage.class);
                intent.putExtra("url", item);
                context.startActivity(intent);
            }
        });


        Glide.with(holder.image)
                .load("http://192.249.18.230:3000/"+item)
                .centerCrop()
                .into(holder.image);
        //holder.image.setImageResource(item.img);


    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }


}

