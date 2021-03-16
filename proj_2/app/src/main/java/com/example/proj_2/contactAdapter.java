package com.example.proj_2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proj_2.list_contact;

import java.util.ArrayList;


public class contactAdapter extends RecyclerView.Adapter<contactAdapter.MyViewHolder> implements Filterable {
    private ArrayList<list_contact> contact_list = new ArrayList<list_contact>();

    private ArrayList<list_contact> filteredList = new ArrayList<list_contact>();
    private ArrayList<list_contact> unfilterList = new ArrayList<list_contact>();

    contactAdapter(ArrayList<list_contact> list ){
        contact_list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public String phonenumber;
        public ImageButton callBtn;
        public ImageButton mmsBtn;

        public MyViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.tv_name);
            this.number = view.findViewById(R.id.tv_number);
            this.callBtn = view.findViewById(R.id.iv_call);
            this.mmsBtn = view.findViewById(R.id.iv_mms);


            /* delete */
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int curPos = getAdapterPosition() ;
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    TextView dialogText = dialogView.findViewById(R.id.dg_title);

                    dialogText.setText(contact_list.get(curPos).name + "\n" + contact_list.get(curPos).number + "\n 삭제하시겠습니까?");
                    builder.setView(dialogView)
                            .setTitle("연락처 삭제");
                    //.setMessage(dialogText.text.toString())contacts_list

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            contact_list.remove(contact_list.get(curPos));
                            notifyItemRemoved(curPos);
                            notifyItemRangeChanged(curPos, contact_list.size());
                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                    return true;


                }
            });

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflaterView;
        inflaterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflaterView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(contact_list.get(position).name);
        holder.number.setText(contact_list.get(position).number);

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    int curPos = holder.getAdapterPosition();
                    list_contact item = contact_list.get(curPos);
                    Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.number));
                    v.getContext().startActivity(intent_call);
                }
            }
        });

        holder.mmsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                {
                    int curPos = holder.getAdapterPosition();
                    list_contact item = contact_list.get(curPos);
                    Intent intent_mms = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + item.number));
                    v.getContext().startActivity(intent_mms);
                }}
        });

    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty()) {
                    filteredList = unfilterList;
                } else {
                    ArrayList<list_contact> filteringList = new ArrayList<>();

                    for (list_contact item : unfilterList) {
                        if(item.name.toLowerCase().contains(str))
                            filteringList.add(item);
                    }

                    filteredList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<list_contact>) results.values;
                notifyDataSetChanged();
            }
        };
    }






}
