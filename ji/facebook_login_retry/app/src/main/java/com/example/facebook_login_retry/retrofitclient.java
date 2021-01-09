package com.example.facebook_login_retry;

public class retrofitclient {
    private static retrofitclient uniqueClient = new retrofitclient();

    private retrofitclient(){}

    public static retrofitclient getInstance(){
        return uniqueClient;
    }
}
