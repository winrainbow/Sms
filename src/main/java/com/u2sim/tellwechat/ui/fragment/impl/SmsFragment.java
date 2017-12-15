package com.u2sim.tellwechat.ui.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.u2sim.tellwechat.R;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.greenDao.DbUtil;
import com.u2sim.tellwechat.ui.adapter.RecyclerAdapter;
import com.u2sim.tellwechat.ui.fragment.BaseFragment;
import com.u2sim.tellwechat.ui.view.DefaultView;
import com.u2sim.tellwechat.util.EnumUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hanguojing on 2017/12/4 16:21
 */

public class SmsFragment extends BaseFragment {

    private Unbinder bind;
    @BindView(R.id.smsRecyclerView)
    RecyclerView smsRecyclerView;

    @BindView(R.id.dv_no_sms)
    DefaultView dvNoSms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_fragment_sms, container, false);
        bind = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        showTip(true);
        dvNoSms.setIvShow(false,getString(R.string.db_loading));
        smsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }



    private void initData(){
        DbUtil.getInstance().getSmsList(list -> {
            if(list!=null && !list.isEmpty()){
                dvNoSms.setIvShow(true,getString(R.string.no_sms_tip));
                showTip(false);
                if(smsRecyclerView!=null){
                    if(smsRecyclerView.getAdapter() == null){
                        smsRecyclerView.setAdapter(new RecyclerAdapter(list));
                    }else{
                        ((RecyclerAdapter)smsRecyclerView.getAdapter()).setList(list);
                    }
                }
            }else{
                showTip(true);
                dvNoSms.setIvShow(true,getString(R.string.no_sms_tip));
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(EventBusBase.App2WsComplete event){
        if(isVisible && event.getType() == EnumUtil.APP2WS_RSP_SMS){
            initData();
        }

    }

    @Override
    protected void onHidden() {

    }

    @Override
    protected void onShow() {
        initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bind != null)
            bind.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void showTip(boolean showTip) {
        int flNoCallVisibility = dvNoSms.getVisibility();
        int recyclerViewVisibility = smsRecyclerView.getVisibility();
        if (showTip) {
            if (flNoCallVisibility != View.VISIBLE)
                dvNoSms.setVisibility(View.VISIBLE);
            if (recyclerViewVisibility != View.GONE)
                smsRecyclerView.setVisibility(View.GONE);
        } else {
            if (flNoCallVisibility != View.GONE)
                dvNoSms.setVisibility(View.GONE);
            if (recyclerViewVisibility != View.VISIBLE)
                smsRecyclerView.setVisibility(View.VISIBLE);
        }

    }
}
