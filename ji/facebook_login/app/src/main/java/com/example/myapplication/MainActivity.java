package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoginStatusCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginbutton;
    private CallbackManager fcallbackManager;
    private AccessToken faccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        loginbutton = (LoginButton) findViewById(R.id.login_button);
        //loginbutton.setReadPermissions("email");
        // If using in a fragment
        //loginbutton.setFragment(this.);

        fcallbackManager = CallbackManager.Factory.create();



        // Callback registration
        loginbutton.registerCallback(fcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(MainActivity.this, ServerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid", faccessToken.getUserId());
                bundle.putString("token", faccessToken.getToken());
                bundle.putString("appId", faccessToken.getApplicationId());
                intent.putExtras(bundle);
                startActivity(intent);
                Log.d("success", "success");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });

//        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
//            @Override
//            public void onCompleted(AccessToken accessToken) {
//                Log.d("onCompleted", "onCompleted");
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//
//            @Override
//            public void onError(Exception exception) {
//                Log.d("onError", "onError");
//            }
//
//        });




        boolean isLoggedIn = faccessToken != null && !faccessToken.isExpired();

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        Button sub_button = (Button)findViewById(R.id.sub);

        sub_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fcallbackManager.onActivityResult(requestCode, resultCode, data);
        faccessToken = AccessToken.getCurrentAccessToken();
        super.onActivityResult(requestCode, resultCode, data);
    }



};