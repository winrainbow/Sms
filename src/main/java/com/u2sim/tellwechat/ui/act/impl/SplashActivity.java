package com.u2sim.tellwechat.ui.act.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.u2sim.tellwechat.R;
import com.u2sim.tellwechat.netstate.NetUtil;
import com.u2sim.tellwechat.ui.act.BaseActivity;

import butterknife.BindView;

/**
 * Created by hanguojing on 2017/12/10 12:50
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_act_splash);
        llParent.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }, 2000);

    }

    @Override
    protected void onNetworkConnected(NetUtil.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
