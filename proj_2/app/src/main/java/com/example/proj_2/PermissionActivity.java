package com.example.proj_2;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class PermissionActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private int MY_PERMISSION_REQUEST_SMS = 1001;
    String[] REQUIRED_PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,  Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    public int[] grandResults = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Require Permission
        GetPermission();
        onRequestPermissionsResult(PERMISSIONS_REQUEST_CODE, REQUIRED_PERMISSIONS, grandResults);

        super.onCreate(savedInstanceState);
        startMainActivity();

    }
    private void startMainActivity(){
        Intent intent = new Intent(this, facebookloginActivity.class);
        startActivity(intent);
        finish();
    }

    public void GetPermission() {
        LinearLayout mLayout = (LinearLayout)findViewById(R.id.activity_permission);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[0]);
        int readContactPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[1]);
        int readCallPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[2]);
        int cameraPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[3]);
        int statePermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[4]);
        int writeContactPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[5]);
        int sendSMSPermission =  ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[6]);
        int readSMSPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[7]);
        int writeExternalPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[8]);

        int accessFineLoaction = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[9]);
        int accessCoarseLocation = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[10]);

        ActivityCompat.requestPermissions(PermissionActivity.this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);


        grandResults[0] = readExternalStoragePermission;
        grandResults[1] = readContactPermission;
        grandResults[2] = readCallPermission;
        grandResults[3] = cameraPermission;
        grandResults[4] = statePermission;
        grandResults[5] = writeContactPermission;
        grandResults[6] = sendSMSPermission;
        grandResults[7] = readSMSPermission;
        grandResults[8] = writeExternalPermission;
        grandResults[9] = accessFineLoaction;
        grandResults[10] = accessCoarseLocation;


        if (!(grandResults[0] == PackageManager.PERMISSION_GRANTED
                && grandResults[1] == PackageManager.PERMISSION_GRANTED
                && grandResults[2] == PackageManager.PERMISSION_GRANTED
                && grandResults[3] == PackageManager.PERMISSION_GRANTED
                && grandResults[4] == PackageManager.PERMISSION_GRANTED
                && grandResults[5] == PackageManager.PERMISSION_GRANTED
                && grandResults[6] == PackageManager.PERMISSION_GRANTED
                && grandResults[7] == PackageManager.PERMISSION_GRANTED
                && grandResults[8] == PackageManager.PERMISSION_GRANTED
                && grandResults[9] == PackageManager.PERMISSION_GRANTED
                && grandResults[10] == PackageManager.PERMISSION_GRANTED)) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[4])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[6])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[7])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[8])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[9])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[10])) {
                Snackbar.make(mLayout, "이 앱을 실행하려면 외부 저장소, 연락처, 전화 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 3-3. 사용자에게 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(PermissionActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }
}