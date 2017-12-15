package com.u2sim.tellwechat.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import com.u2sim.tellwechat.ui.act.impl.MainActivity;
import com.u2sim.tellwechat.util.LogUtil;

/**
 * Created by hanguojing on 2017/12/4 15:06
 * <p>
 * baseFragment 用来统一处理
 */

public abstract class BaseFragment extends Fragment {
    protected static final int MSG_WHAT_TAB_LAYOUT_VISIBILITY = 0x2001;
//    protected ListenRecyclerViewScroll listenRecyclerViewScroll;
    protected Activity baseActivity;
    protected boolean isVisible;
    protected String TAG = this.getClass().getSimpleName();
    protected Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_TAB_LAYOUT_VISIBILITY:
                    int dy = (int) msg.obj;
                    LogUtil.d(TAG,"发送接收："+dy);
                    setTabLayoutVisibility(dy);
                    break;
            }

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (Activity) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;

        if(isVisibleToUser){
            onShow();
        }else{
            onHidden();
        }
    }

    protected abstract void onHidden();
    protected abstract void onShow();


    private void setTabLayoutVisibility(int dy) {
        if (baseActivity != null) {
            if (dy > 0) {
                ((MainActivity) baseActivity).setTabLayoutVisibility(View.GONE);
            }else if(dy<0){
                ((MainActivity) baseActivity).setTabLayoutVisibility(View.VISIBLE);

            }
        } else {
            LogUtil.d(TAG, "baseActivity == null");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (baseActivity != null)
            baseActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
