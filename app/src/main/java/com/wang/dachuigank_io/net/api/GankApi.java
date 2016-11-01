package com.wang.dachuigank_io.net.api;

import com.wang.dachuigank_io.data.entity.MeiZhiData;


import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/27.
 */
public interface GankApi {
    @GET("/api/data/福利/10/{page}")
    Observable<MeiZhiData> getMeiZhi(@Path("page") int page);
    @GET("/api/data/休息视频/10/{page}")
    Observable<MeiZhiData> getVideo(@Path("page") int page);
    @GET("/api")
    Observable<String> getString();
}
