package com.u2sim.tellwechat.ui.fragment.impl;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.u2sim.tellwechat.util.LogUtil;

/**
 * Created by hanguojing on 2017/12/5 18:30
 */

public class ListenRecyclerViewScroll extends RecyclerView.OnScrollListener {
    private static final String TAG = "ListenRecyclerViewScroll";
    private Handler handler;
    private int msgWhat;
    public ListenRecyclerViewScroll(Handler handler,int msgWhat){
        this.handler = handler;
        this.msgWhat = msgWhat;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){}

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled. This will be
     * called after the scroll has completed.
     * <p>
     * This callback will also be called if visible item range changes after a layout
     * calculation. In that case, dx and dy will be 0.
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        LogUtil.d(TAG,"[dx:%d,dy:%d]",dx,dy);
        Message message = Message.obtain();
        message.what = msgWhat;
        message.obj = dy;
        handler.removeMessages(msgWhat);
        handler.sendMessageDelayed(message,30);
        LogUtil.d(TAG,"发送："+dy);
    }
}
