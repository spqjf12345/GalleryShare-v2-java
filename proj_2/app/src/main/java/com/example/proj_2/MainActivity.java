package com.example.proj_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tedpark.tedpermission.rx1.TedRxPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
    public String nickname;
    List<Map<String, Object>> invite_mms;
    int[] image = {R.drawable.mail, R.drawable.mail, R.drawable.mail};
    String[] text = {"조소정님이 가족 그룹에 초대 메세지를 보냈습니다", "이정인님이 친구1 그룹에 초대 메세지를 보냈습니다", "김철환님이 바다여행 그룹에 초대 메세지를 보냈습니다"};
    ApiService apiService;

    List<Map<String, Object>> recomnd_Friends;
    String [] friends = {"조소정 010-3245-2959", "김철환 010-2297-8999", "서승연 010-8212-2959", "이정인 010-7761-2959", "신정윤 010-2325-9682"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nickname = getIntent().getStringExtra("nickname");
        setContentView(R.layout.activity_main);
        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        apiService = RetroClient.getInstance().getApiService();

        getPermission();

        try {
            JSONArray contactJsonArray = getPhoneNumbers(null, "");

            Call<ResponseBody> req = apiService.getrecommendedUser(contactJsonArray);

            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Toast.makeText(MainActivity.this, response.body().string(), Toast.LENGTH_SHORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void setupViewPager(ViewPager viewPager){
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1(), "contacts");
        adapter.addFragment(new Fragment2(), "gallery");
        adapter.addFragment(new Fragment3(),"free");
        viewPager.setAdapter(adapter);
    }


    private void getPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
            }
        };

        TedRxPermission.with(this)
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                .request()
                .subscribe(tedPermissionResult -> {
                    if (tedPermissionResult.isGranted()) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }, throwable -> {
                });

/*
        TedPermission.with(this).setPermissionListener(permissionlistener);
        TedPermission.with(this).setRationaleMessage("카메라 사용을 위해 권한을 허용해주세요");
        TedPermission.with(this).setDeniedMessage("권한을 거부하였습니다.");
        TedPermission.with(this).setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        TedPermission.with(this).check();
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.notification){
            invite_mms = new ArrayList<>();
            for(int i=0;i<text.length;i++)
            {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put(TAG_IMAGE, image[i]);
                itemMap.put(TAG_TEXT, text[i]);
                invite_mms.add(itemMap);
            }
            showInvitemms();
        }

        if(item.getItemId() == R.id.find_freinds){
            recomnd_Friends = new ArrayList<>();
            for(int i=0; i < friends.length; i++){
                Map<String, Object> itemMap = new HashMap<>();
                //itemMap.put(TAG_IMAGE, image[i]);
                itemMap.put(TAG_FRIENDS, friends[i]);
                recomnd_Friends.add(itemMap);
            }
            showRecommendFriends();
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String TAG_FRIENDS = "friends";
    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";

    private void showInvitemms() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.noti_dialog, null);
        builder.setView(view);

        final ListView listView = (ListView)view.findViewById(R.id.lv_noti);
        final AlertDialog dialog = builder.create();
        SimpleAdapter simpleAdapter_mms = new SimpleAdapter(this, invite_mms,
                R.layout.list_noti_item,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});

        listView.setAdapter(simpleAdapter_mms);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showRecommendFriends(){
        AlertDialog.Builder builder_rf = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.re_friends_dialog, null);
        builder_rf.setView(view);

        final ListView listView = (ListView)view.findViewById(R.id.lv_rf);
        //final AlertDialog dialog = builder_rf.create();
        builder_rf.setTitle("추천 친구");
        SimpleAdapter simpleAdapter_rf = new SimpleAdapter(this, recomnd_Friends,
                R.layout.list_refriends_item,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemCheckBox, R.id.alertDialogItemTextView});

        listView.setAdapter(simpleAdapter_rf);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        builder_rf.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //for checked text send message
                //CheckBox chkBox = findViewById(R.id.alertDialogItemCheckBox);
                //Toast.makeText(view.getContext(), "메세지를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder_rf.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });


        builder_rf.setCancelable(false);
        builder_rf.show();

    }

    JSONArray getPhoneNumbers(String sort, String searchName) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();


        //주소 값
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Log.d("phoneUri",phoneUri.toString());
        String[] projections = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER};

        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.CONTACT_ID );
        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME );
        Log.d("Contract",ContactsContract.CommonDataKinds.Phone.NUMBER );

        ContentResolver resolver = this.getContentResolver();
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
        Log.d("list", jsonArray.toString());
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

            JSONObject contactJSON = new JSONObject();
            contactJSON.put("phonenum", number);

            jsonArray.put(contactJSON);


        }
        cursor.close();
        return jsonArray;
    }

}


