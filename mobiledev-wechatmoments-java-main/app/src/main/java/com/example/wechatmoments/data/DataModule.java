package com.example.wechatmoments.data;

import static com.example.wechatmoments.data.Constants.LABEL;

import android.content.Context;
import android.util.Log;

import com.example.wechatmoments.data.entity.Tweet;
import com.example.wechatmoments.data.entity.User;
import com.example.wechatmoments.presenter.PresenterInterface;
import com.example.wechatmoments.view.MainActivity;

import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataModule implements ModuleInterface {

    //***********************CACHE SOLUTION*****************************
    private int cacheSize = 10 * 1024 * 1024;
    private Cache cache = new Cache(MainActivity.getMyContext().getCacheDir(), cacheSize);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(cache)
            .build();
    //************************************************************************
    private final PresenterInterface callback;
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2727/")
            .client(okHttpClient) //add cache solution here
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final WeChatService service = retrofit.create(WeChatService.class);

    public DataModule(PresenterInterface presenterInterface) {
        this.callback = presenterInterface;
    }

    @Override
    public void getTweets(String username) {

        service.getTweets(username).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Tweet>> call, Response<List<Tweet>> response) {
                Log.i(LABEL, "--------------------WEB SERVICE RESPONSE-----------------");
                Log.i(LABEL, "tweets response" + response);
                if(response != null && response.body() != null){
                    callback.loadTweetsSuccess(response.body());
                } else {
                    callback.loadTweetsFailed();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Tweet>> call, Throwable t) {
                callback.loadTweetsFailed();
            }
        });
    }

    @Override
    public void getUser(String username) {
        service.getUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                Log.i(LABEL, response.toString());
                if(response != null && response.body() != null){
                    callback.loadUserSuccess(response.body());
                } else {
                    callback.loadUserFailed();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                callback.loadUserFailed();
            }
        });
    }
}
