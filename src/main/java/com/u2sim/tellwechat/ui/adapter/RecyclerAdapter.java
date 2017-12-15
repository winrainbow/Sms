package com.u2sim.tellwechat.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.u2sim.tellwechat.R;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hanguojing on 2017/12/4 16:42
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.InnerViewHolder> {
    private List list;
    public RecyclerAdapter(List list){
        this.list = list;
    }
    @Override
    public RecyclerAdapter.InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(InitApp.AppContext).inflate(R.layout.item_recycler_view, parent, false);
        return new InnerViewHolder(view);
    }

    public void setList(List list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.InnerViewHolder holder, int position) {

        Object obj = list.get(position);
        if(obj instanceof CallTableBean){
            CallTableBean bean = (CallTableBean) obj;
            holder.ivIcon.setImageResource(R.drawable.icon_missedcall);
            holder.tvDes.setText(String.format("未接来电[%s]", TextUtils.isEmpty(bean.getSenderName())?bean.getSenderPhone():bean.getSenderName()));
            holder.tvTime.setText(TextUtils.isEmpty(bean.getReceiveTime()) ? "":bean.getReceiveTime());
            holder.llContentBg.setBackgroundResource(R.drawable.img_card_white);
            holder.tvContent.setText(TextUtils.isEmpty(bean.getSenderPhone())?"空":bean.getSenderPhone());
        }else if(obj instanceof SmsTableBean){
            SmsTableBean bean = (SmsTableBean) obj;
            holder.ivIcon.setImageResource(R.drawable.icon_sms);
            holder.tvDes.setText(String.format("来自%s[%s]",bean.getSenderName()==null?"":bean.getSenderName(),bean.getSenderPhone()));
            holder.tvTime.setText(TextUtils.isEmpty(bean.getReceiveTime()) ? "":bean.getReceiveTime());
            holder.llContentBg.setBackgroundResource(R.drawable.img_card_white);
            holder.tvContent.setText(bean.getContent()==null?"":bean.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class InnerViewHolder extends RecyclerView.ViewHolder{

       @BindView(R.id.iv_icon)
       ImageView ivIcon;
       @BindView(R.id.tv_des)
       TextView tvDes;
       @BindView(R.id.tv_time)
       TextView tvTime;
       @BindView(R.id.ll_content)
       LinearLayout llContentBg;
       @BindView(R.id.tv_content)
       TextView tvContent;

        public InnerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
