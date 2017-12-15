package com.u2sim.tellwechat.server.websocket.wsMsg;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.server.ResultCode;
import com.u2sim.tellwechat.bean.server.data.DataCfgRsp;
import com.u2sim.tellwechat.bean.server.wsApp.WsAppBean;
import com.u2sim.tellwechat.server.http.HttpServerUtil;
import com.u2sim.tellwechat.server.websocket.service.WebSocketActionInterface;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;
import com.u2sim.tellwechat.util.NetWorkUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hanguojing on 2017/11/29 13:45
 * 统一管理 webSocket 连接
 */

public class WebSocketManager implements WebSocketActionInterface {

    private volatile static WebSocketManager instance;
    private final String TAG = "WebSocketManager";


    private static final int FRAME_QUEUE_SIZE = 5;
    private static final int CONNECT_TIMEOUT = 5000;


    private int reconnectCount = 0; // 重连次数
    private long minInterval = 5000; // 重连最小时间间隔
    private long maxInterval = 60000; // 重连最大时间间隔

    private Runnable reconnectTask = () -> init();

    private WsStatus wsStatus;
    private WebSocket ws;
    private WsListener wsListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());  // 主线程 handler

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (instance == null) {
            synchronized (WebSocketManager.class) {
                if (instance == null) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        //todo 首先http 去获取URl
        if (NetWorkUtil.isNetworkConnected(InitApp.AppContext) &&
                ((wsStatus != null && wsStatus == WsStatus.CONNECT_FAILURE) || wsStatus == null)) {
            LogUtil.d(TAG, "start http req ");
            wsStatus = WsStatus.CONNECT_ING;
            HttpServerUtil.getWsHost(new Callback<WsAppBean>() {
                @Override
                public void onResponse(Call<WsAppBean> call, Response<WsAppBean> response) {
                    if (response.isSuccessful()) {
                        WsAppBean dataCfgRspWsAppBean = response.body();
                        LogUtil.d(TAG, "httpRsp[%s]", dataCfgRspWsAppBean.toString());
                        if (dataCfgRspWsAppBean.getResult() == ResultCode.CODE_1_SUCCESS) {
                            JsonObject data = dataCfgRspWsAppBean.getData();
                            if (data != null) {
                                DataCfgRsp dataCfgRsp = GsonFormatUtil.getInstance().fromJson(data, DataCfgRsp.class);
                                LogUtil.d(TAG, "dataCfgResp:%s", dataCfgRsp.toString());
                                if (!TextUtils.isEmpty(dataCfgRsp.getWsUrl())) {
                                    connectWs(dataCfgRsp.getWsUrl());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<WsAppBean> call, Throwable t) {
                    // TODO: 2017/11/30 处理http 失败的处理
                }
            });
        }
    }

    private void connectWs(String url) {
        try {
            ws = new WebSocketFactory().createSocket(url, CONNECT_TIMEOUT)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)
//                    .setMissingCloseFrameAllowed(false)
                    .addListener(wsListener = new WsListener())
                    .connectAsynchronously();
            setStatus(WsStatus.CONNECT_ING);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d(TAG,"connectWs url:"+url);
            LogUtil.d(TAG,"connectWs exception:"+e.getMessage());
        }
    }

    public void sendTextMessage(final String message) {
        LogUtil.d(TAG, "wsStatus:" + wsStatus.toString());
        if (wsStatus == WsStatus.CONNECT_SUCCESS) {
            ws.sendText(message);
        } else {
            // TODO: 2017/11/29 需要先去重连
            if (ws == null) {
                init();
            } else {

                reconnect();
            }

            mHandler.postDelayed(new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    LogUtil.d(TAG, Thread.currentThread().getName() + ":" + Integer.toString(++i));
                    if (ws != null && wsStatus == WsStatus.CONNECT_SUCCESS) {
                        ws.sendText(message);
                    } else {
                        if (i < 5) {
                            if (ws == null) {
                                init();
                            } else {
                                if (wsStatus == WsStatus.CONNECT_FAILURE) {
                                    reconnect();
                                }
                            }
                            mHandler.postDelayed(this, 5000);
                        }
                    }
                }
            }, 5000);

        }
    }


    /**
     * webSocket 重连
     */
    @Override
    public synchronized void reconnect() {
        LogUtil.d(TAG,"reconnect");
        LogUtil.d(TAG,"reconnect Status:"+wsStatus.toString());
        if (NetWorkUtil.isNetworkConnected(InitApp.AppContext)) {
            if (ws != null && !ws.isOpen() && getStatus() != WsStatus.CONNECT_ING) {
                reconnectCount++;
                long reconnectTime = minInterval;
//                if (reconnectCount > 3) {
//                    long temp = minInterval * (reconnectCount - 2);
//                    reconnectTime = temp > maxInterval ? maxInterval : temp;
//                }
                mHandler.postDelayed(reconnectTask, reconnectTime);
            }else{
                LogUtil.d(TAG,"不满足条件");
            }

        } else {
            LogUtil.d(TAG, "reconnect : 重连失败网络不可用");
        }
    }

    @Override
    public void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(reconnectTask);
    }

    /**
     * 设置webSocket的连接状态
     *
     * @param status
     */
    void setStatus(WsStatus status) {
        this.wsStatus = status;
    }

    public WsStatus getStatus() {
        return this.wsStatus;
    }

    /**
     * 断开webSocket 连接
     */
    @Override
    public void disconnect() {
        if (ws != null) {
            wsStatus = WsStatus.CONNECT_FAILURE;
            ws.disconnect();
        }
    }


}



