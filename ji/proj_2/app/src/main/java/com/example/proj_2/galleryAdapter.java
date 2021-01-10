package com.example.proj_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class galleryAdapter extends RecyclerView.Adapter<galleryAdapter.ImageViewHolder> {
    private ArrayList<MediaFileData> dataset;
    List<Integer> countImages;

    galleryAdapter(ArrayList<MediaFileData> list){
        this.dataset = list;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView grdImg;
        public TextView gridName;
        public TextView gridSub;

        public ImageViewHolder(View view) {
            super(view);
            this.grdImg = view.findViewById(R.id.gridImg);
            this.gridName = view.findViewById(R.id.gridName);
            this.gridSub = view.findViewById(R.id.gridSub);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder_image, parent, false);
        return new ImageViewHolder(adapterLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        MediaFileData item = dataset.get(position);
        holder.gridName.setText(item.bucketId.toString());
        holder.gridSub.setText(countImages.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked", LENGTH_SHORT).show()
                //Intent intent = new Intent(context, OpenGalleryFolderActivity.class);
                //intent.putExtra("bucketId", item.bucketId);
                //intent.putExtra("bucketName", item.bucketName);
                //context.startActivity(intent);
            }
        });

        Glide.with(holder.itemView)
                .load(item.uri)
                .thumbnail(0.33f)
                .centerCrop()
                .into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}