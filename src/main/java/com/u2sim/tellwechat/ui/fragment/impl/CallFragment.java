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

public class CallFragment extends BaseFragment {

    private Unbinder bind;
    @BindView(R.id.callRecyclerView)
    RecyclerView callRecyclerView;

    @BindView(R.id.dv_no_call)
    DefaultView dvNoCall;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_fragment_call, container, false);
        bind = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        showTip(true);
        dvNoCall.setIvShow(false,getString(R.string.db_loading));
        callRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }


    private void showTip(boolean showTip) {
        int flNoCallVisibility = dvNoCall.getVisibility();
        int recyclerViewVisibility = callRecyclerView.getVisibility();
        if (showTip) {
            if (flNoCallVisibility != View.VISIBLE)
                dvNoCall.setVisibility(View.VISIBLE);
            if (recyclerViewVisibility != View.GONE)
                callRecyclerView.setVisibility(View.GONE);
        } else {
            if (flNoCallVisibility != View.GONE)
                dvNoCall.setVisibility(View.GONE);
            if (recyclerViewVisibility != View.VISIBLE)
                callRecyclerView.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 初始化recyclerView 数据
     */
    private void initData() {
        DbUtil.getInstance().getCallList(list -> {
            if (list != null && !list.isEmpty()) {
                dvNoCall.setIvShow(true,getString(R.string.no_sms_tip));
                showTip(false);
                if (callRecyclerView != null) {
                    if (callRecyclerView.getAdapter() == null) {
                        callRecyclerView.setAdapter(new RecyclerAdapter(list));
                    } else {
                        ((RecyclerAdapter) callRecyclerView.getAdapter()).setList(list);
                    }
                }
            } else {
                showTip(true);
                dvNoCall.setIvShow(true,getString(R.string.no_sms_tip));
            }

        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(EventBusBase.App2WsComplete event) {
        if (isVisible && event.getType() == EnumUtil.APP2WS_RSP_CALL) {
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
        if (bind != null)
            bind.unbind();
        EventBus.getDefault().unregister(this);
    }
}
