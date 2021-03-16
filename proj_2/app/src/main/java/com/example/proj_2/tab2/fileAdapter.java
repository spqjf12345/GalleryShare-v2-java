package com.example.proj_2.tab2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;

public class fileAdapter extends RecyclerView.Adapter<fileAdapter.ViewHolder> {
    ArrayList<list_image> ImageList;
    fileAdapter(ArrayList<list_image> imgList ){
        ImageList = imgList;
    }
    Context context;
    int item_layout;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gallery);


        }
    }

    public fileAdapter(Context context, ArrayList<list_image> items, int item_layout) {
        this.context=context;
        this.ImageList = items;
        this.item_layout = item_layout;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        list_image item = ImageList.get(position);
        //Log.d("item", item.folderName + item.area + item.folderImage);
        //Drawable d = context.getResources().getDrawable(item.img);
        //Drawable drawable= context.getDrawable(R.id.); //getResources().getDrawable(item.folderImage, null);
        holder.image.setImageURI(ImageList.get(position).img);
        //holder.image.setImageResource(item.img);


    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }


}

