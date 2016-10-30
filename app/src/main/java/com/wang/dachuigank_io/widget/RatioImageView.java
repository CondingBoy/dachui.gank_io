package com.wang.dachuigank_io.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/10/30.
 * 可以设置宽高比例的ImageView
 */
public class RatioImageView extends ImageView {
    /**
     * 默认宽高比例为1:1
     */
    private int mRatioWidth = 1;
    private int mRatioHeght = 1;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRatio(int width, int height) {
        mRatioWidth = width;
        mRatioHeght = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mRatioWidth>0&&mRatioHeght>0){
            //计算宽高比例
            float ratio=(float)mRatioWidth/(float)mRatioHeght;

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height= MeasureSpec.getMode(heightMeasureSpec);
            //根据比例计算高度
            if(width>0){
                height= (int) (width/ratio);
            }
            setMeasuredDimension(width,height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
