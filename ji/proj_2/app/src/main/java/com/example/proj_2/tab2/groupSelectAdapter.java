package com.example.proj_2.tab2;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.util.ArrayList;

public class groupSelectAdapter extends RecyclerView.Adapter<groupSelectAdapter.MyViewHolder> {
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    Context mContext;
    RecyclerView recyclerView;
    private ArrayList<String> groupselect = new ArrayList<String>();

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;



    public groupSelectAdapter(Context context, RecyclerView recyclerView, OnListItemSelectedInterface listener, ArrayList<String> group_){
        this.mContext = context;
        this.recyclerView = recyclerView;
        this.mListener = listener;
        this.groupselect = group_;
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView group_name;
        //아이템 클릭시 선택 상태 저장 및 선택 상태 표시
        public MyViewHolder(View view){
            super(view);
            this.group_name = view.findViewById(R.id.gp_name);
            Log.d("group_name", group_name.toString());
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int curPos = getAdapterPosition();
//                    if(mSelectedItems.get(curPos, false)){
//                        mSelectedItems.put(curPos,false);
//                        v.setBackgroundColor(Color.WHITE);
//                    }else{
//                        mSelectedItems.put(curPos, true);
//                        v.setBackgroundColor(Color.GRAY);
//                    }
                    toggleItemSelected(curPos);
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

    //아이템 바인딩 시 선택 상태 표시
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = groupselect.get(position);
        holder.group_name.setText(item);
        holder.itemView.setSelected(isItemSelected(position));

        if ( mSelectedItems.get(position, false) ){
            holder.itemView.setBackgroundColor(Color.GRAY);
            Log.d("holder.group_name", holder.group_name.getText().toString());
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return groupselect.size();
    }

    public void setData(ArrayList<String> data) {
        groupselect = data;
        notifyDataSetChanged();
    }


    public void toggleItemSelected(int position){
        if (mSelectedItems.get(position, false) == true) {
            mSelectedItems.delete(position);
            notifyItemChanged(position);
        } else {
            mSelectedItems.put(position, true);
            notifyItemChanged(position);
        }

    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

    public void clearSelectedItem() {
        int position;

        for (int i = 0; i < mSelectedItems.size(); i++) {
            position = mSelectedItems.keyAt(i);
            mSelectedItems.put(position, false);
            notifyItemChanged(position);
        }

        mSelectedItems.clear();
    }




}
