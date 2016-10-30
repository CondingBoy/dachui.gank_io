package com.wang.dachuigank_io.ui.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.dachuigank_io.R;
import com.wang.dachuigank_io.data.entity.MeiZhi;
import com.wang.dachuigank_io.minterface.OnMeiZhiItemClick;
import com.wang.dachuigank_io.widget.RatioImageView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/30.
 */
public class MeiZhiListAdapter extends RecyclerView.Adapter<MeiZhiListAdapter.ViewHolder>{

    private final Context mContext;
    private final List<MeiZhi> mData;
    public  OnMeiZhiItemClick mOnMeiZhiItemClick;
    public MeiZhiListAdapter(Context context, List<MeiZhi> data){
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.meizhi_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int limit = 48;

        MeiZhi meiZhi = mData.get(position);
        String desc = meiZhi.getDesc().length()>limit ? meiZhi.getDesc().substring(0,limit)+"...":meiZhi.getDesc();
        holder.mTvDesc.setText(desc);
        holder.meiZhi=meiZhi;
        Glide.with(mContext)
                .load(meiZhi.getUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mMeizhi);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setOnMeizhiItemClick(OnMeiZhiItemClick onMeizhiItemClick){
        mOnMeiZhiItemClick = onMeizhiItemClick;
    }

    public   class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.card_meizhi)
        CardView mCard;
        @Bind(R.id.iv_meizhi)
        RatioImageView mMeizhi;
        @Bind(R.id.tv_desc)
        TextView mTvDesc;
        MeiZhi meiZhi;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.card_meizhi,R.id.iv_meizhi})
        void onItemClick(View view){
           if(mOnMeiZhiItemClick==null){
               return;
           }
            switch (view.getId()){
                case R.id.card_meizhi:
                    mOnMeiZhiItemClick.OnCardClic(view,meiZhi);
                    break;
                case R.id.iv_meizhi:
                    mOnMeiZhiItemClick.OnPicClick(view,meiZhi);
                    break;
            }
        }
    }
}
