package com.example.proj_2.tab1;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {

    public ArrayList<list_contact> list = new ArrayList<list_contact>();

    RecyclerView recyclerView1;
    String[] permissions = {android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE};
    String searchText = "";
    String sortText = "";
    CharSequence serach = "";
    contactAdapter contactAdapter = new contactAdapter(list);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView =  inflater.inflate(R.layout.fragment_1, container, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isPermitted()) {
            recyclerView1 = rootView.findViewById(R.id.rv_json);
            recyclerView1.setLayoutManager(new LinearLayoutManager(this.getContext()));
            try {
                //list = getPhoneNumbers(sortText, searchText);
                list.addAll(getPhoneNumbers(sortText, searchText));
            } catch (IOException e) {
                e.printStackTrace();
            }


            contactAdapter = new contactAdapter(list);

            recyclerView1.setAdapter(contactAdapter);
            recyclerView1.setHasFixedSize(true);
            startProcess();
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permissions, 99);
        }

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 99){
            boolean check = true;
            for(int i =0; i< grantResults.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    check = false;
                    break;
                }
            }

            if(check) {
                startProcess();
            }
            else {
                Toast.makeText(this.getContext(), "권한 승인을 하셔야지만 앱을 사용할 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    void startProcess(){
        setList();
    }
    void setList(){
        recyclerView1.getAdapter().notifyDataSetChanged();
    }
    void changeList() throws IOException {
        List<list_contact> newList = getPhoneNumbers(sortText, searchText);
        Log.d("getPhoneNumbers", getPhoneNumbers(sortText, searchText).toString());
        list.clear();
        list.addAll(newList);

        //RecyclerView rv_json = recyclerView1.findViewById(R.id.rv_json);
        //rv_json.setAdapter(new contactAdapter());
        //rv_json.getAdapter().notifyDataSetChanged();
        recyclerView1.getAdapter().notifyDataSetChanged();
        //rv_json.setAdapter(new contactAdapter()).notifyDataSetChanged();
        //rv_json.setHasFixedSize(true);
    }

    ArrayList<list_contact> getPhoneNumbers(String sort, String searchName) throws IOException {
        ArrayList<list_contact> list_ = new ArrayList<list_contact>();


        //주소 값
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Log.d("phoneUri",phoneUri.toString());
        String[] projections = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER};

        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.CONTACT_ID );
        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME );
        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.NUMBER );

        ContentResolver resolver = getContext().getContentResolver();
        String wheneClause ="";
        String optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        String[] whereValues = null;
        if (!searchName.isEmpty() ){
            whereValues = new String[] {"%" + searchName + "%"};
        }

        Log.d("searchName", searchName);
        if(searchName.isEmpty() != true){
            wheneClause = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ?";
            Log.d("searchName", searchName);
            Log.d("wheneClause", wheneClause.toString());
            //Log.d("whereValues", whereValues.toString());

        }
        Log.d("list", list.toString());
        Cursor cursor = resolver.query(phoneUri, projections, wheneClause, whereValues, null);

        while(cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);

            // json 파일에 넣기
            // JSONObject main = JSONObject(jsonString);
            // jObject.put("person",main);
            // main.put("id", id);
            // main.put("name", name);
            // main.put("number", number);

            //넣은 값을 불러와서 list item 에 부여
            //for (i in 0 until jObject.length()) {
            //var jArray = jObject.getJSONArray("person")
            // val obj = jObject.getJSONObject("person")
            // Log.d("obj", obj.toString())
            //val json_id = obj.getString("id")
            //val json_name = obj.getString("name")
            //val json_number = obj.getString("number")
            //list_contact list = new list_contact();
            //list.setId(id);
            //list.setname(name);
            //list.setnumber(number);

            list_.add(new list_contact(id, name, number));
            for(int i=0; i< list_.size(); i++){
                Log.d("list_", String.valueOf(list_.indexOf(i)));
            }

        }
        cursor.close();
        return list_;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    boolean isPermitted(){
        for(int i=0; i< permissions.length; i++){
            if(ActivityCompat.checkSelfPermission(this.requireContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatImageView add_btn = view.findViewById(R.id.btn_add);

        add_btn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Dialog dialog01 = new Dialog(v.getContext());
                    dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.cutom_add_dialog, null);
                    dialog01.setContentView(dialogView);
                    dialog01.show();
                    Button ADD_Button = dialogView.findViewById(R.id.group_add);
                    Button CANCEL_Button = dialogView.findViewById(R.id.group_cancel);

                    CANCEL_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog01.dismiss();
                        }

                    });
                    ADD_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog01.dismiss();
                            String id = "";
                            TextView dialogName = dialogView.findViewById(R.id.tv_groupName);
                            TextView dialogNumber = dialogView.findViewById(R.id.addNumber_);
                            String sname = dialogName.getText().toString();
                            String snumber = dialogNumber.getText().toString();

                            list.add(new list_contact(id, sname, snumber));

                            dialog01.dismiss();

                            //contactAdapter.MyViewHolder.class.notify();
                            //contactAdapter.notifyItemInserted(0);
                            contactAdapter.notifyDataSetChanged();
                            //refreshFragment(Fragment1.this, getFragmentManager());

                        }

                    });


                }
            }
        });

        TextView contact_Filter = view.findViewById(R.id.contact_Filter);

        contact_Filter.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            //contactAdapter crAdapter = new contactAdapter(list);
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactAdapter.getFilter().filter(s);
                serach = s;
                Log.d("serach", serach.toString());
                searchText = s.toString();
                Log.d("searchText", searchText);
                try {
                    changeList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    void refreshFragment(Fragment fragment, FragmentManager fragmentManager){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(fragment).attach(fragment).commit();
        Log.v("dialog", "Do refresh");
    }
}
