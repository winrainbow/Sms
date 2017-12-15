package com.u2sim.tellwechat.ui.act.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.u2sim.tellwechat.R;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.bean.UserInfo;
import com.u2sim.tellwechat.bean.server.data.DataWeChatBindInfo;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.netstate.NetUtil;
import com.u2sim.tellwechat.ui.act.BaseActivity;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.SPUtils;
import com.u2sim.tellwechat.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by hanguojing on 2017/12/6 11:30
 */

public class LoginActivity extends BaseActivity  {


    public static final String KEY_QR_CODE_URL = "KEY_QR_CODE_URL";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_act_login);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        Intent intent = getIntent();
        final String stringExtra = intent.getStringExtra(KEY_QR_CODE_URL);
        Glide.with(this).load(stringExtra).into(ivQrCode);

        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weChatBind(EventBusBase.WeChatBindResult weChatBindResult){
        if(TextUtils.isEmpty(weChatBindResult.getResultJsonStr())){
            ToastUtil.shortToast(this,"返回了空的微信信息");
        }else{
            DataWeChatBindInfo weChatInfo = GsonFormatUtil.getInstance().fromJson(weChatBindResult.getResultJsonStr(), DataWeChatBindInfo.class);
            int isBind = weChatInfo.getIsBind();
            UserInfo userInfo = new UserInfo();
            userInfo.setBind(isBind == 1);
            InitApp.setLoginState(isBind ==1);
            SPUtils.putCommit(EnumUtil.SP_KEY_USER_INFO,GsonFormatUtil.getInstance().toJson(userInfo,UserInfo.class));
            if(isBind == 1){
                ToastUtil.shortToast(this,"登录成功");
                Intent intent = new Intent();
                intent.putExtra(MainActivity.KEY_WE_CHAT_BIND_RESULT,true);
                setResult(RESULT_OK,intent);
                finish();
            }else{
                ToastUtil.shortToast(this,"登录失败");
            }

        }
    }

    @Override
    protected void onNetworkConnected(NetUtil.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
