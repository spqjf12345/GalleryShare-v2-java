package com.example.proj_2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    public ArrayList<String> group = new ArrayList<String>();
    groupAdapter groupAdapter = new groupAdapter(group);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_group, container, false);
        group.add("Family");
        group.add("Friend1");
        group.add("Friend2");
        Button group_add = view.findViewById(R.id.gr_add);
        group_add.setOnClickListener(new View.OnClickListener() {
            /* group name add */
            @Override
            public void onClick(View v) {
                {
                    Dialog dialog02 = new Dialog(v.getContext());
                    dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.group_add_dialog, null);
                    dialog02.setContentView(dialogView);
                    dialog02.show();
                    Button ADD_Button = dialogView.findViewById(R.id.group_add);
                    Button CANCEL_Button = dialogView.findViewById(R.id.group_cancel);

                    CANCEL_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog02.dismiss();
                        }

                    });
                    ADD_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog02.dismiss();
                            String id = "";
                            TextView dialogName = dialogView.findViewById(R.id.tv_groupName);
                            //String dialogNumber = dialogView.findViewById(R.id.addNumber_).toString();
                            String sname = dialogName.getText().toString();
                            group.add(sname);
                            dialog02.dismiss();
                            groupAdapter.notifyDataSetChanged();

                        }

                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(groupAdapter);
    }


}
