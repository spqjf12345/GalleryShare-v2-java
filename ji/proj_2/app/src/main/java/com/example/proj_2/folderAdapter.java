package com.example.proj_2;

import android.annotation.TargetApi;
import android.content.Context;
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

import java.util.ArrayList;

public class folderAdapter extends RecyclerView.Adapter<folderAdapter.ViewHolder> {
    Context context;
    ArrayList<list_folder> items;
    int item_layout;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        list_folder item = items.get(position);
        //Drawable drawable= context.getResources().getDrawable(item.getImage());
        Drawable d = context.getResources().getDrawable(item.getImage());

        holder.folder.setImageDrawable(d);
        holder.folderName.setText(item.getTitle());
        holder.area.setText(item.getTitle());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image
                Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView folder;
        TextView area;
        TextView folderName;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            folder=(ImageView)itemView.findViewById(R.id.iv_folder);
            folderName=(TextView)itemView.findViewById(R.id.tv_folder);
            area = (TextView)itemView.findViewById(R.id.tv_area);
            cardview=(CardView)itemView.findViewById(R.id.cardview);

        }
    }

    public interface OnMyItemCheckedChanged {
        public void onItemCheckedChanged(ArrayList<list_folder> item, boolean isChecked);
    }
    private OnMyItemCheckedChanged mOnMyItemCheckedChanged;

    public void setOnMyItemCheckedChanged(OnMyItemCheckedChanged  onMyItemCheckedChanged){
        this.mOnMyItemCheckedChanged = onMyItemCheckedChanged;
    }

}

