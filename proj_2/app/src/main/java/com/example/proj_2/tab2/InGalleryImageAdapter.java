package com.example.proj_2.tab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;

public class InGalleryImageAdapter extends RecyclerView.Adapter<InGalleryImageAdapter.inGalleryImageHolder> {
    private ArrayList<MediaFileData> dataset;
    private Context context;

    InGalleryImageAdapter(ArrayList<MediaFileData> list){
        this.dataset = list;
    }

    public class inGalleryImageHolder extends RecyclerView.ViewHolder {
        public inGalleryImageHolder(View view) {
            super(view);
        }
    }

    @NonNull
    @Override
    public inGalleryImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new inGalleryImageHolder(adapterLayout);

    }

    @Override
    public void onBindViewHolder(@NonNull inGalleryImageHolder holder, int position) {
        MediaFileData item = dataset.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, showFullImage.class);
//                intent.putExtra("uri", item.uri.toString());
//                context.startActivity(intent);
//
//
//                Glide.with(holder.itemView)
//                        .load(item.uri)
//                        .thumbnail(0.33f)
//                        .centerCrop()
//                        .into((ImageView) holder.itemView);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}