package com.wang.dachuigank_io.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wang.dachuigank_io.R;
import com.wang.dachuigank_io.data.entity.MeiZhi;
import com.wang.dachuigank_io.data.entity.MeiZhiData;
import com.wang.dachuigank_io.minterface.OnMeiZhiItemClick;
import com.wang.dachuigank_io.net.DaChuiRetrofit;
import com.wang.dachuigank_io.net.api.DaChuiFactiory;
import com.wang.dachuigank_io.net.api.GankApi;
import com.wang.dachuigank_io.ui.Adapter.MeiZhiListAdapter;
import com.wang.dachuigank_io.ui.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_miezhi)
    RecyclerView rvMiezhi;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.main_fb)
    FloatingActionButton mainFb;
    public boolean clear = false;
    private List<MeiZhi> mMeizhiListData;
    private MeiZhiListAdapter mListAdapter;
    private int mPage = 1;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setUpRecyclerView();
        loadData(mPage);
        swipeRefresh.setColorSchemeResources(R.color.color_252A2B, R.color.colorAccent, R.color.blue);
        swipeRefresh.setOnRefreshListener(this);
        rvMiezhi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //加载更多
                if(newState==RecyclerView.SCROLL_STATE_IDLE){//停止滚动
//                    Toast.makeText(MainActivity.this, "刷新1"+mPage, Toast.LENGTH_SHORT).show();
                    //返回最后可见的ites的位置，length为列数
                    int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
//                    for (int i :
//                            lastVisibleItemPositions) {
//                        Log.e("position:" ,i+"");
//                    }
//                    Log.e("check:",lastVisibleItemPositions[lastVisibleItemPositions.length-1]+";"+(staggeredGridLayoutManager.getItemCount()-1));
                    int maxPosition = 0;
                    for (int i :
                            lastVisibleItemPositions) {
                        if(maxPosition<i){
                            maxPosition=i;
                        }
                    }

                    if(maxPosition>=staggeredGridLayoutManager.getItemCount()-1){
                        mPage++;
                        clear=false;
                        loadData(mPage);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadData(int page) {
//        GankApi singleGankApi = DaChuiFactiory.getSingleGankApi();
//        Subscription subscription = Observable.zip(singleGankApi.getMeiZhi(page), singleGankApi.getVideo(page), new Func2<MeiZhiData, MeiZhiData, MeiZhiData>() {
//            @Override
//            public MeiZhiData call(MeiZhiData meiZhiData, MeiZhiData meiZhiData2) {
//                return replaceData(meiZhiData, meiZhiData2);
//            }
//        }).subscribeOn(Schedulers.io())
//                .map(new Func1<MeiZhiData, List<MeiZhi>>() {
//                    @Override
//                    public List<MeiZhi> call(MeiZhiData meiZhiData) {
//                        return meiZhiData.results;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
        Observable<List<MeiZhi>> listObservable = requestDataAfterHandle(page);
        Subscription subscription = listObservable.subscribe(new Action1<List<MeiZhi>>() {
            @Override
            public void call(List<MeiZhi> meiZhis) {
                if (mMeizhiListData != null) {

                    if (clear) {
                        //刷新逻辑
                        mMeizhiListData.clear();
                        clear=false;
                        swipeRefresh.setRefreshing(false);
                    }
                    mMeizhiListData.addAll(meiZhis);
                    mListAdapter.notifyDataSetChanged();
                } else {

                    mMeizhiListData = meiZhis;
                    mListAdapter = new MeiZhiListAdapter(MainActivity.this, mMeizhiListData);
                    mListAdapter.setOnMeizhiItemClick(new OnMeiZhiItemClick() {
                        @Override
                        public void OnPicClick(View v, MeiZhi meiZhi) {
                            Intent intent = PicktureActivity.getIntent(meiZhi.getUrl(), meiZhi.getDesc(), MainActivity.this);
                            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,v,"pic");
                            ActivityCompat.startActivity(MainActivity.this,intent,options.toBundle());
                        }

                        @Override
                        public void OnCardClic(View v, MeiZhi meiZhi) {

                        }
                    });
                    rvMiezhi.setAdapter(mListAdapter);
                }


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(MainActivity.this, "访问出错t！", Toast.LENGTH_SHORT).show();
                Log.e("ABC", throwable.getMessage() + ":" + throwable.getStackTrace().toString());
            }
        });
        addSubscription(subscription);
    }
    /**
     * 获取处理后的数据
     *
     * @param page 数据的页数
     * @return 返回已封装好的observeable对象
     */
    private Observable<List<MeiZhi>> requestDataAfterHandle(int page) {
        GankApi singleGankApi = DaChuiFactiory.getSingleGankApi();
        return Observable.zip(singleGankApi.getMeiZhi(page), singleGankApi.getVideo(page), new Func2<MeiZhiData, MeiZhiData, MeiZhiData>() {
            @Override
            public MeiZhiData call(MeiZhiData meiZhiData, MeiZhiData meiZhiData2) {
                return replaceData(meiZhiData, meiZhiData2);
            }
        }).subscribeOn(Schedulers.io())
                .map(new Func1<MeiZhiData, List<MeiZhi>>() {
                    @Override
                    public List<MeiZhi> call(MeiZhiData meiZhiData) {
                        return meiZhiData.results;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    private MeiZhiData replaceData(MeiZhiData meiZhiData, MeiZhiData meiZhiData2) {
        for (int i = 0; i < meiZhiData.results.size(); i++) {
            MeiZhi meiZhi = meiZhiData.results.get(i);
            MeiZhi meiZhi1 = meiZhiData2.results.get(i);
            String desc = meiZhi.getDesc() + " " + meiZhi1.getDesc();
            meiZhi.setDesc(desc);
        }
        return meiZhiData;
    }

    private void setUpRecyclerView() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvMiezhi.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onRefresh() {
        clear=true;
        mPage=1;
        loadData(mPage);
    }

}
