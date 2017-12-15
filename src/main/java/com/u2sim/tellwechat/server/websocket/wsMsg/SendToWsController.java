package com.u2sim.tellwechat.server.websocket.wsMsg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;

import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.server.websocket.service.WsService;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hanguojing on 2017/11/29 17:42
 * 负责 向ws 发送消息
 */

public class SendToWsController {

    private final static String TAG = "SendToWsController";
    private static final int SEND_REQ = 0x1001;
    private static final int SEND_RSP = 0x1002;
    private WsService wsService;

    @IntDef({SEND_REQ, SEND_RSP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SendType {

    }

    private volatile static SendToWsController instance;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MsgWhat.SEND_MSG_TIMEOUT:
                    Bundle bundle = (Bundle) msg.obj;
                    String id = bundle.getString(BundleKey.KEY_ID);
                    String actionType = bundle.getString(BundleKey.KEY_ACTION_TYPE);
                    boolean exist = SendingMsg.getInstance().isExist(id, actionType);
                    LogUtil.d(TAG,"exist:"+exist);

                    if (exist) {
                        EventBus.getDefault().post(new EventBusBase.SendTimeout(id, actionType));
                    }
                    break;
            }
        }
    };

    private SendReqMsgWrapper sendReqMsgWrapper;
    private SendRspMsgWrapper sendRspMsgWrapper;

    private @SendType
    int sendType;

    private ServiceConnection serviceConnection;

    public static SendToWsController getInstance() {
        if (instance == null) {
            synchronized (SendToWsController.class) {
                if (instance == null) {
                    instance = new SendToWsController();
                }
            }
        }
        return instance;
    }

    public Handler getHandler() {
        return this.handler;
    }

    /**
     * 单例： 私有
     */
    private SendToWsController() {
    }

    /**
     * 执行发送 app 发起的请求消息
     */
    public void executeSendReq(SendReqMsgWrapper sendReqMsgWrapper) {
        this.sendType = SEND_REQ;
        this.sendReqMsgWrapper = sendReqMsgWrapper;
        LogUtil.d(TAG,sendReqMsgWrapper.getSendMsg());
        SendingMsg.getInstance().add(sendReqMsgWrapper.getId(), sendReqMsgWrapper.getActionType(), sendReqMsgWrapper.getSendMsg());
        InitApp.AppContext.bindService(new Intent(InitApp.AppContext, WsService.class), getServiceConnection(), Context.BIND_AUTO_CREATE);
        Message msg = Message.obtain();
        msg.what = MsgWhat.SEND_MSG_TIMEOUT;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKey.KEY_ID, sendReqMsgWrapper.getId());
        bundle.putString(BundleKey.KEY_ACTION_TYPE, sendReqMsgWrapper.getActionType());
        msg.obj = bundle;
        handler.sendMessageDelayed(msg, sendReqMsgWrapper.getTimeout());
    }

    public void executeSendRsp() {
        this.sendType = SEND_RSP;
    }


    private ServiceConnection getServiceConnection() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUtil.d(TAG, "onServiceConnected");
                    wsService = ((WsService.WsBinder) service).getService();
                    assert wsService == null;
                    sendMsg();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    LogUtil.d(TAG, "onServiceDisconnected");
                    serviceConnection = null;
                    wsService = null;
                }
            };
        } else {
            if (wsService != null) {
                sendMsg();
            }
        }
        return serviceConnection;

    }

    private void sendMsg() {
        switch (sendType) {
            case SEND_REQ:
                if (sendReqMsgWrapper != null)
                    wsService.sendTextMessage(sendReqMsgWrapper.getSendMsg());
                break;
            case SEND_RSP:
                if (sendRspMsgWrapper != null)
                    wsService.sendTextMessage(sendRspMsgWrapper.getSendMsg());
                break;
            default:
                break;
        }
    }
}
