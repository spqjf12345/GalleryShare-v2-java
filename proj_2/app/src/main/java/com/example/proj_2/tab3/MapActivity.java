package com.example.proj_2.tab3;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proj_2.R;
import com.example.proj_2.tab2.folderActivity;
import com.example.proj_2.tab2.list_folder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        PlacesListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private String TAG = "googleMap";
    ArrayList<list_folder> getFolders;

//    private int GPS_ENABLE_REQUEST_CODE = 2001;
//    private int UPDATE_INTERVAL_MS = 1000;
//    private int FASTEST_UPDATE_INTERVAL_MS = 500;

    private GoogleMap mMap = null;
    String area = "";
    int image = 0;

    Marker currentMarker;
    LatLng latlong = null;

    //현재 위치 초기화
    LatLng myPosition = new LatLng(0, 0);


    /* map frame layout */
    private View mLayout = null;
    private int PERMISSIONS_REQUEST_CODE = 100;

    String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");

        //지도 객체 mMap
        mMap = googleMap;
        MarkerOptions makerOptions = new MarkerOptions();

        for(int i = 0; i <getFolders.size(); i++){
            final Geocoder geocoder = new Geocoder(this);
            List<Address> latlon = null;
            //area -> myPosition (latlon)
            try{
                Log.d("getFolder", String.valueOf(getFolders.size()));
                Log.d("getFolder", getFolders.get(i).area);

                latlon = geocoder.getFromLocationName(getFolders.get(i).area, 2);
            }catch (IOException e){
                e.printStackTrace();
            }

            /* area가 잘못된 주소일 때 */
            if (latlon != null) {
                if (latlon.size() == 0) {
                    Log.d(TAG, "해당 주소가 없습니다. 다시 입력해주세요");
                } else {
                    Address addr = latlon.get(0);
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();
                    Log.d("LatLong", String.valueOf(lat) + " " + lon);
                    myPosition = new LatLng(lat, lon);
                }
            }

            image = getFolders.get(i).folderImage;

            makerOptions.position(myPosition);
            makerOptions.title(String.valueOf(getFolders.get(i).folderImage));
            makerOptions.snippet(getFolders.get(i).folderName);
            makerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            mMap.addMarker(makerOptions);

            currentMarker = mMap.addMarker(makerOptions);
            currentMarker.showInfoWindow(); //아래의 infowindowadapter 실행
        }

        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        // camera move
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 10F));


//        // for loop를 통한 folder에 대한 마커 생성
//        for (int i = 0; i < getFolders.size(); i++) {
//            makerOptions.position(myPosition);
//            makerOptions.title("폴더 " + i);
//            makerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
//            mMap.addMarker(makerOptions);
//        }
//        currentMarker = mMap.addMarker(makerOptions);
//        currentMarker.showInfoWindow(); //아래의 infowindowadapter 실행
//
//        //MarkerOptions marker = new MarkerOptions().position(myPosition);
//        //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
//
//        //LatLng myArea = myPosition; //new LatLng(37.56, 126.97); 태평로 1가 35
//        //for 태평로
////       MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(myArea); //있어야 함
////        markerOptions.title(area); //use Image
////        markerOptions.snippet(area);
////        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
////        mMap.addMarker(markerOptions);
//
//        // info click
//        mMap.setOnInfoWindowClickListener(this);
//        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
//
//        // camera move
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10F));

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemaplayer);

        mLayout = findViewById(R.id.map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*dummy data */
        getFolders= new ArrayList<>();
        list_folder[] item=new list_folder[4];
        item[0]=new list_folder(R.drawable.minion1,"#1","태평로 1가 35");
        item[1]=new list_folder(R.drawable.minion2,"#2", "마석로 110");
        item[2]=new list_folder(R.drawable.minion3,"#3", "대학로 291");
        item[3]=new list_folder(R.drawable.minion4,"#4", "둔산3동");
        for(int i=0;i<4;i++) getFolders.add(item[i]);

    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {

    }

    @Override
    public void onPlacesFinished() {

    }


    //info 클릭 시
    @Override
    public void onInfoWindowClick(Marker marker) {
        //intent 생성해 ImageActivity 로 이동 (수정 필요) folderActivity -> imageActivity
        Intent intent = new Intent(getApplication(), folderActivity.class);
        startActivity(intent);

//        info click
//        Toast.makeText(this, "Info window clicked",
//                Toast.LENGTH_SHORT).show();
        //map marker click 시
        // marker click 시
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        //show custom info view
        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_map_info, null);
        }

        //entire info windows cutomized
        @Override
        public View getInfoWindow(Marker marker) {
            ImageView iv_title = ((ImageView) myContentsView.findViewById(R.id.iv_title));
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.tv_folder));
            iv_title.setImageResource(Integer.parseInt(marker.getTitle()));
            tvTitle.setText(marker.getSnippet());
            return myContentsView;
        }

        //default info window frame and background는 유지하면서 contents만 customizing
        @Override
        public View getInfoContents(Marker marker) {
        return null;
        }
    }
}


