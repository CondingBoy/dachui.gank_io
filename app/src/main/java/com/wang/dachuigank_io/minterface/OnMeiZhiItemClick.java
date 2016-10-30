package com.wang.dachuigank_io.minterface;

import android.view.View;

import com.wang.dachuigank_io.data.entity.MeiZhi;

/**
 * Created by Administrator on 2016/10/30.
 */
public interface OnMeiZhiItemClick {
    void OnPicClick(View v, MeiZhi meiZhi);
    void OnCardClic(View v, MeiZhi meiZhi);
}
