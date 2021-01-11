package com.example.proj_2;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.List;

import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        PlacesListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private String TAG = "googleMap";

//    private int GPS_ENABLE_REQUEST_CODE = 2001;
//    private int UPDATE_INTERVAL_MS = 1000;
//    private int FASTEST_UPDATE_INTERVAL_MS = 500;

    private GoogleMap mMap = null;
    String area = "";
    Marker locationMarker;
    // for loop를 통한 n개의 마커 생성


//    Boolean needRequest = false;

    /* map frame layout */
    private View mLayout = null;
    private int PERMISSIONS_REQUEST_CODE = 100;

    Polyline polyline;

    String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //현재 위치 초기화
    LatLng myPosition = new LatLng(0, 0);


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");

        //지도 객체
        mMap = googleMap;
        MarkerOptions makerOptions = new MarkerOptions();
        LatLng latlong = null;
        // for loop를 통한 n개의 마커 생성
        for (int i = 0; i < 10; i++) {
            // 1. 마커 옵션 설정 (만드는 과정)
             // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
            latlong = new LatLng(37.52487 + i, 126.92723);
            makerOptions.position(latlong);
            makerOptions.title("마커" + i);
            makerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }
        locationMarker = mMap.addMarker(makerOptions);
        locationMarker.showInfoWindow(); //아래의 infowindowadapter 실행

        //현재 위치에 마커 설정
        //MarkerOptions marker = new MarkerOptions().position(myPosition);
        //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));

        //LatLng myArea = myPosition; //new LatLng(37.56, 126.97); 태평로 1가 35
        //for 태평로
//       MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(myArea); //있어야 함
//        markerOptions.title(area); //use Image
//        markerOptions.snippet(area);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
//        mMap.addMarker(markerOptions);

        // info click
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        // camera move
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10F));

        /*permission check - ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION*/
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
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
        final Geocoder geocoder = new Geocoder(this);
        List<Address> latlang = null;
        try {
            area = "태평로1가35";
            latlang = geocoder.getFromLocationName(area, 2);
            Log.d("latlang", latlang.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (latlang != null) {
            if (latlang.size() == 0) {
                Log.d(TAG, "해당 주소가 없습니다. 다시 입력해주세요");
            } else {
                Address addr = latlang.get(0);
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();
                //String address = addr.g
                Log.d("LatLong", String.valueOf(lat) + lon);
                myPosition = new LatLng(lat, lon);
            }

        }

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
            return null;
        }

        //default info window frame and background는 유지하면서 contents만 customizing
        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.tv_folder));
            tvTitle.setText(marker.getTitle());
            //TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            //tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }
    }
}


