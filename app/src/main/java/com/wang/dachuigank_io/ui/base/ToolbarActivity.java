package com.wang.dachuigank_io.ui.base;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;

import com.wang.dachuigank_io.R;

public abstract class ToolbarActivity extends AppCompatActivity {

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mAppBar ==null|| mToolbar ==null){
            new IllegalStateException("The subclass of ToolbarActivity must include toobar_layout");
        }
        setSupportActionBar(mToolbar);
        if(canBack()){
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if(Build.VERSION.SDK_INT>=21){
            mAppBar.setElevation(10.6f);
        }
    }
    public boolean canBack(){
        return false;
    }

    /**
     * 返回布局文件id
     * @return
     */
    public abstract int provideLayoutId();
    protected void setAppBarAlpha(float alpha){
        mAppBar.setAlpha(alpha);
    }
    protected  void hideOrShowToolbar(){
        mAppBar.animate().translationY(mIsHide?0:-mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHide=!mIsHide;
    }
}
