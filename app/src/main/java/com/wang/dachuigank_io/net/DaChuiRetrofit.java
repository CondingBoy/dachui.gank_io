package com.wang.dachuigank_io.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.wang.dachuigank_io.net.api.GankApi;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2016/10/30.
 */
public class DaChuiRetrofit {
//    private static String baseUrl = "http://gank.io";
    private static String baseUrl = "http://gank.io";
    private static Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    private final GankApi mGankApi;

    public DaChuiRetrofit(){
        //设置okHttp的属性
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(20, TimeUnit.SECONDS);
        //设置retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mGankApi = retrofit.create(GankApi.class);

    }
    public GankApi getGankApi(){
        return mGankApi;
    }
}
