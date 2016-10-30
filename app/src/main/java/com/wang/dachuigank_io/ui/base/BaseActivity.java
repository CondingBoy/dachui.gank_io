package com.wang.dachuigank_io.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.dachuigank_io.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {
    //用于c持有subscription，可持有多个，集体取消订阅
    private CompositeSubscription mCompositeSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    public CompositeSubscription getCompositeSubscription(){
        if(mCompositeSubscription==null){
            mCompositeSubscription=new CompositeSubscription();
        }
        return mCompositeSubscription;
    }
    // 持有subscription
    public void addSubscription(Subscription s){
        getCompositeSubscription().add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCompositeSubscription!=null){
            //取消已经持有的所有subscription，mCompositeSubscription一旦取消订阅，就不能再次使用，除非重新创建一个新的对象
            mCompositeSubscription.unsubscribe();
        }
    }
}
