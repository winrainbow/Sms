package com.u2sim.tellwechat.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.u2sim.tellwechat.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by hanguojing on 2017/12/5 16:02
 */

public class NetStateReceiver extends BroadcastReceiver {

    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String TAG = NetStateReceiver.class.getSimpleName();
    private static boolean isNetAvailable = false;
    private static NetUtil.NetType mNetType;
    private static ArrayList<NetChangedObserver> mNetChangeObservers = new ArrayList<>();
    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = NetStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            if (!NetUtil.isNetworkAvailable(context)) {
                LogUtil.i(TAG, "<--- network disconnected --->");
                isNetAvailable = false;
            } else {
                LogUtil.i(TAG, "<--- network connected --->");
                isNetAvailable = true;
                mNetType = NetUtil.getAPNType(context);
            }
            notifyObserver();
        }
    }

    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }


    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                LogUtil.d(TAG, e.getMessage());
            }
        }

    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public static NetUtil.NetType getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangedObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    public static void registerObserver(NetChangedObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<>();
        }
        mNetChangeObservers.add(observer);
    }

    public static void removeRegisterObserver(NetChangedObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }

}
