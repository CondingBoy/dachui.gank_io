package com.wang.dachuigank_io.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.wang.dachuigank_io.R;
import com.wang.dachuigank_io.data.entity.MeiZhi;
import com.wang.dachuigank_io.data.entity.MeiZhiData;
import com.wang.dachuigank_io.net.api.DaChuiFactiory;
import com.wang.dachuigank_io.net.api.GankApi;
import com.wang.dachuigank_io.ui.Adapter.MeiZhiListAdapter;
import com.wang.dachuigank_io.ui.base.ToolbarActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends ToolbarActivity {

    @Bind(R.id.rv_miezhi)
    RecyclerView rvMiezhi;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.main_fb)
    FloatingActionButton mainFb;
    public boolean clear=false;
    private List<MeiZhi> mMeizhiListData;
    private MeiZhiListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setUpRecyclerView();
        loadData();
    }

    private void loadData() {
        GankApi singleGankApi = DaChuiFactiory.getSingleGankApi();
        Observable.zip(singleGankApi.getMeiZhi(1), singleGankApi.getVideo(1), new Func2<MeiZhiData, MeiZhiData, MeiZhiData>() {
            @Override
            public MeiZhiData call(MeiZhiData meiZhiData, MeiZhiData meiZhiData2) {
                return replaceData(meiZhiData,meiZhiData2);
            }
        }).map(new Func1<MeiZhiData, List<MeiZhi>>() {
            @Override
            public List<MeiZhi> call(MeiZhiData meiZhiData) {
                return meiZhiData.results;
            }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<MeiZhi>>() {
            @Override
            public void call(List<MeiZhi> meiZhis) {
                if(mMeizhiListData!=null){

                    if(clear){
                        mMeizhiListData.clear();
                    }
                    mMeizhiListData.addAll(meiZhis);
                    mListAdapter.notifyDataSetChanged();
                }else{

                    mMeizhiListData = meiZhis;
                    mListAdapter = new MeiZhiListAdapter(MainActivity.this,mMeizhiListData);
                    rvMiezhi.setAdapter(mListAdapter);
                }
               
                    
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Toast.makeText(MainActivity.this, "访问出错！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MeiZhiData replaceData(MeiZhiData meiZhiData, MeiZhiData meiZhiData2) {
       for(int i=0;i<meiZhiData.results.size();i++){
           MeiZhi meiZhi = meiZhiData.results.get(i);
           MeiZhi meiZhi1 = meiZhiData2.results.get(i);
           String desc = meiZhi.getDesc()+" "+meiZhi1.getDesc();
           meiZhi.setDesc(desc);
       }
        return meiZhiData;
    }

    private void setUpRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvMiezhi.setLayoutManager(layoutManager);
    }

    @Override
    public int provideLayoutId() {
        return R.layout.activity_main;
    }
}
