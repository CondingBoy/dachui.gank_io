package com.wang.dachuigank_io.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.dachuigank_io.R;
import com.wang.dachuigank_io.ui.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PicktureActivity extends ToolbarActivity {

    @Bind(R.id.iv_pic)
    ImageView ivPic;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        parseIntent(getIntent());
        ViewCompat.setTransitionName(ivPic,"pic");
        Glide.with(this).load(url).into(ivPic);
        setTitle(title);
        setAppBarAlpha(0.7f);
        PhotoViewAttacher viewAttacher = new PhotoViewAttacher(ivPic);
        viewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                hideOrShowToolbar();
            }
        });
        viewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(PicktureActivity.this, "保存图片", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void parseIntent(Intent intent) {
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

    }

    @Override
    public int provideLayoutId() {
        return R.layout.activity_pickture;
    }
    public static Intent getIntent(String url, String title, Context context){
        Intent intent = new Intent(context,PicktureActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }else{

            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean canBack() {
        return true;
    }
}
