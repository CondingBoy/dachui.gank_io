package com.wang.dachuigank_io.net.api;


import com.wang.dachuigank_io.net.DaChuiRetrofit;

/**
 * Created by Administrator on 2016/10/30.
 */
public class DaChuiFactiory {
    public static final int PAGESIZE=10;
    public static  GankApi mGankApi=null;
    public static final Object monitor = new Object();
    public static GankApi getSingleGankApi(){
        synchronized (monitor){
            if(mGankApi==null){
                mGankApi = new DaChuiRetrofit().getGankApi();
            }
            return mGankApi;
        }
    }
}
