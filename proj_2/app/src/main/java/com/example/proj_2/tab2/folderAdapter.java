package com.example.proj_2.tab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.proj_2.R;

import java.util.ArrayList;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.ViewHolder> {
    Context context;
    ArrayList<list_folder> items;
    int item_layout;
    public RequestManager mRequestManager;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView folderImage;
        TextView area;
        TextView folderName;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            folderImage=(ImageView)itemView.findViewById(R.id.iv_folder);
            folderName=(TextView)itemView.findViewById(R.id.tv_folder);
            area = (TextView)itemView.findViewById(R.id.tv_area);
            cardview=(CardView)itemView.findViewById(R.id.cardview);



            /* go to file */
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_file = new Intent(v.getContext(), fileActivity.class);
                    v.getContext().startActivity(intent_file);
                }
            });;

        }
    }

    public folderAdapter(Context context, ArrayList<list_folder> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_folder,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final list_folder item = items.get(position);
        Drawable d = context.getResources().getDrawable(item.folderImage);

        //Drawable drawable= context.getDrawable(R.id.); //getResources().getDrawable(item.folderImage, null);

        holder.folderImage.setImageDrawable(d);
        holder.folderName.setText(item.getTitle());
        holder.area.setText(item.getArea());



        //Log.d("item", items.get(position).folderName + item.area + item.folderImage);
//        Drawable d = context.getResources().getDrawable(item.getImage());
//        Drawable drawable= context.getDrawable(R.id.); //getResources().getDrawable(item.folderImage, null);
//        holder.folderImage.setImageDrawable(d);
//        holder.folderName.setText(item.folderName);
//        holder.area.setText(item.area);

//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show image
//                Log.d("folder_click", "folder_click");
//                Intent intent_file = new Intent(v.getContext(), OpenGalleryFolderActivity.class);
//                v.getContext().startActivity(intent_file);
//                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public interface OnMyItemCheckedChanged {
        public void onItemCheckedChanged(ArrayList<list_folder> item, boolean isChecked);
    }
    private OnMyItemCheckedChanged mOnMyItemCheckedChanged;

    public void setOnMyItemCheckedChanged(OnMyItemCheckedChanged  onMyItemCheckedChanged){
        this.mOnMyItemCheckedChanged = onMyItemCheckedChanged;
    }

}

