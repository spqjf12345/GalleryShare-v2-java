package com.example.madcamp_week2.soJeong;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_week2.R;
import com.example.madcamp_week2.soJeong.serverConnect;
import com.example.madcamp_week2.soJeong.MainActivity;
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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginbutton;
    private CallbackManager fcallbackManager;
    private AccessToken faccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginbutton = (LoginButton) findViewById(R.id.login_button);
        loginbutton.setReadPermissions("email");
        // If using in a fragment
        //loginbutton.setFragment(this.);

        fcallbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginbutton.registerCallback(fcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
             Log.d("success", "success");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
       
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                Log.d("onCompleted", "onCompleted");

            }

            @Override
            public void onFailure() {

            }



            @Override
            public void onError(Exception exception) {
                Log.d("onError", "onError");
            }

        });


        faccessToken = AccessToken.getCurrentAccessToken();

        boolean isLoggedIn = faccessToken != null && !faccessToken.isExpired();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        Button sub_button = (Button)findViewById(R.id.sub);
        sub_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent intent = new Intent(getApplicationContext(), serverConnect.class);
                startActivityForResult(intent, 1001);
            }
        });




    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            fcallbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }



    };


