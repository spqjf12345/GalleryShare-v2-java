package com.example.facebook_login_retry;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public class RetroClient {

    private ApiService apiService;

    private static RetroClient retroClient = new RetroClient();

    private RetroClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder()
                .baseUrl("http://192.249.18.230:3000")
                .client(client)
                .build()
                .create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public static RetroClient getInstance(){
        return retroClient;
    }
}


interface ApiService {
    @GET("/login/{appId}/{token}/{userid}")
    Call<ResponseBody> login(@Path("appId") String appId, @Path("token") String token, @Path("userid") String userid);

    @POST("/register/{appId}/{token}/{userid}")
    Call<ResponseBody> register(@Path("appId") String appId, @Path("token") String token, @Path("userid") String userid);
}
