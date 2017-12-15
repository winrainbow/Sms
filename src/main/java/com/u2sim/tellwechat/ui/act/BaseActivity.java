package com.u2sim.tellwechat.ui.act;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.u2sim.tellwechat.netstate.NetChangedObserver;
import com.u2sim.tellwechat.netstate.NetStateReceiver;
import com.u2sim.tellwechat.netstate.NetUtil;
import com.u2sim.tellwechat.util.BaseActManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hanguojing on 2017/12/5 15:14
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected NetChangedObserver netChangedObserver = null;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BaseActManager.getInstance().addActivity(this);
        netChangedObserver = new NetChangedObserver() {
            @Override
            public void onNetConnected(NetUtil.NetType type) {
                onNetworkConnected(type);

            }

            @Override
            public void onNetDisConnect() {
                onNetworkDisConnected();

            }
        };
        NetStateReceiver.registerObserver(netChangedObserver);
    }
    /**
     * network connected
     */
    protected abstract void onNetworkConnected(NetUtil.NetType type);

    /**
     * network disconnected
     */
    protected abstract void onNetworkDisConnected();

    @Override
    public void finish() {
        super.finish();
        BaseActManager.getInstance().removeActivity(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bind = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind != null)
            bind.unbind();

        NetStateReceiver.removeRegisterObserver(netChangedObserver);
    }
}
