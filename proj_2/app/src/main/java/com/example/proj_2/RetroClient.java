package com.example.proj_2;


import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @GET("/login/{token}")
    Call<ResponseBody> login(@Path("token") String token);

    @POST("/register/{token}/{nickname}/{phonenum}")
    Call<ResponseBody> register(@Path("token") String token, @Path("nickname") String nickname, @Path("phonenum") String phonenum);

    @GET("/gallery/getfolderlist/{nickname}/{group}")
    Call<ResponseBody> getfolderlist(@Path("nickname") String nickname, @Path("group") String group);

    @POST("/gallery/makefolder/{nickname}/{groupname}/{foldername}/{location}")
    Call<ResponseBody> makefolder(
            @Path("nickname") String nickname,
            @Path("groupname") String groupname,
            @Path("foldername") String foldername,
            @Path("location") String location);

    @GET("/gallery/getgrouplist/{nickname}")
    Call<ResponseBody> getgrouplist(@Path("nickname") String nickname);

    @POST("/gallery/makegroup/{nickname}/{groupname}")
    Call<ResponseBody> makegroup(@Path("nickname") String nickname, @Path("groupname") String groupname);

    @PUT("/gallery/addUser2Group/{nickname}/{group}/{newusername}")
    Call<ResponseBody> addUser2Group(@Path("nickname") String nickname, @Path("group") String group, @Path("newusername") String newusername);

    @GET("/gallery/getimgurl/{nickname}/{group}/{folder}")
    Call<ResponseBody> getimageurls(@Path("nickname") String nickname, @Path("group") String group, @Path("folder") String folder);

    @Multipart
    @POST("/gallery/postimg/{nickname}/{groupname}/{foldername}")
    Call<ResponseBody> postImage(@Path("nickname") String nickname,@Path("groupname") String groupname, @Path("foldername") String foldername ,@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @GET("/contact/getrecommendedusers")
    Call<ResponseBody> getrecommendedUser(@Query("data") JSONArray contact);

    @POST("/invite/{nickname}/{groupname}/{friend}")
    Call<ResponseBody> inviteOne(@Path("nickname") String nickname, @Path("groupname") String groupname, @Path("friend") String friend);

    @GET("/invite/getmyinvite/{nickname}")
    Call<ResponseBody> getinvites(@Path("nickname") String nickname);
}

