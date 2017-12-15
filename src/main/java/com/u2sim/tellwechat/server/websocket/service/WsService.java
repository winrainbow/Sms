package com.u2sim.tellwechat.server.websocket.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.bean.server.data.DataCallTip;
import com.u2sim.tellwechat.bean.server.data.DataSms;
import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.CallTableBeanDao;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBeanDao;
import com.u2sim.tellwechat.server.websocket.wsMsg.HandleWsAppMsg;
import com.u2sim.tellwechat.server.websocket.wsMsg.MsgFactory;
import com.u2sim.tellwechat.server.websocket.wsMsg.SendReqMsgWrapper;
import com.u2sim.tellwechat.server.websocket.wsMsg.SendToWsController;
import com.u2sim.tellwechat.server.websocket.wsMsg.SendingMsg;
import com.u2sim.tellwechat.server.websocket.wsMsg.WebSocketManager;
import com.u2sim.tellwechat.server.websocket.wsMsg.WsStatus;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hanguojing on 2017/11/30 11:01
 */

public class WsService extends Service implements WebSocketActionInterface {
    private final static String TAG = "WsService";
    private IBinder wsBinder;
    private ExecutorService executorService;
    private volatile Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (wsBinder == null) {
            wsBinder = new WsBinder();
        }
        return wsBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        LogUtil.d(TAG, "onCreate");
        WebSocketManager.getInstance().init();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.d(TAG, "onStartCommand");
        WebSocketManager.getInstance().init();

        /** START_NOT_STICKY ; 当service运行的进程被Android系统强杀后不会重新创建，
         * START_STICKY ; service 运行的进程被强杀后，android系统会将该Service依然设置为started状态(运行状态)，但是不保存onStartCommand方法传入的intent对象。
         *                然后Android系统会尝试再次重新创建该service,并执行onStartCommand回调方法，但是onStartCommand方法中的intent参数为null, 如果不需要intent信息，则可以返回该值
         * START_REDELIVER_INTENT
         *
         */
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMsgTimeout(EventBusBase.SendTimeout sendTimeoutEvent) {
        LogUtil.d(TAG, "sendMsgTimeout");
        String id = sendTimeoutEvent.getId();
        String actionType = sendTimeoutEvent.getActionType();
        String value = SendingMsg.getInstance().getValue(id, actionType);
        SendReqMsgWrapper sendReqMsgWrapper = new SendReqMsgWrapper(id, actionType, value);
        SendToWsController.getInstance().executeSendReq(sendReqMsgWrapper);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveWsMsg(EventBusBase.ReceiveWsMsg receiveWsMsg) {
        String wsAppMsg = receiveWsMsg.getWsAppMsg();
        if (!TextUtils.isEmpty(wsAppMsg)) {
            HandleWsAppMsg.getInstance().handleWsAppMsg(wsAppMsg);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sys2AppComplete(EventBusBase.Sys2AppComplete sys2AppComplete) {
        switch (sys2AppComplete.getType()) {
            case EnumUtil.SYS_2_APP_Call:
                handleSendCall();
                break;
            case EnumUtil.SYS_2_APP_SMS:
                handleSendSms();
                break;
        }

    }

    private void handleSendSms() {
        initExecutorService().execute(() -> {
            List<SmsTableBean> list = InitApp.DaoSession.getSmsTableBeanDao().queryBuilder().where(SmsTableBeanDao.Properties.SendWsState.notEq(1))
                    .orderAsc(SmsTableBeanDao.Properties.ReceiveTime)
                    .build().forCurrentThread().list();
            if (list != null && !list.isEmpty()) {
                for (SmsTableBean bean : list) {
                    AppWsBean<DataSms> appWsSmsReq = MsgFactory.getInstance().getAppWsSmsReq(bean);
                    String toJson = GsonFormatUtil.getInstance().toJson(appWsSmsReq, new TypeToken<AppWsBean<DataSms>>() {
                    }.getType());
                    if (!TextUtils.isEmpty(toJson)) {
                        final SendReqMsgWrapper sendReqMsgWrapper = new SendReqMsgWrapper(appWsSmsReq.getId(), appWsSmsReq.getAction(), toJson);
                        initHandler().postDelayed(() -> SendToWsController.getInstance().executeSendReq(sendReqMsgWrapper), 1000);
                    }
                }

            }
        });
    }


    private void handleSendCall() {
        initExecutorService().execute(() -> {
            List<CallTableBean> list = InitApp.DaoSession.getCallTableBeanDao().queryBuilder()
                    .where(CallTableBeanDao.Properties.SendWsState.notEq(1))
                    .orderAsc(CallTableBeanDao.Properties.ReceiveTime)
                    .build().forCurrentThread().list();
            if (list != null && !list.isEmpty()) {
                for (CallTableBean bean : list) {
                    AppWsBean<DataCallTip> appWsCallReq = MsgFactory.getInstance().getAppWsCallReq(bean);
                    String toJson = GsonFormatUtil.getInstance().toJson(appWsCallReq, new TypeToken<AppWsBean<DataCallTip>>() {
                    }.getType());
                    if (!TextUtils.isEmpty(toJson)) {
                        final SendReqMsgWrapper sendReqMsgWrapper = new SendReqMsgWrapper(appWsCallReq.getId(), appWsCallReq.getAction(), toJson);
                        initHandler().postDelayed(() -> SendToWsController.getInstance().executeSendReq(sendReqMsgWrapper), 1000);
                    }
                }
            }
        });

    }

    private ExecutorService initExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    private Handler initHandler() {
        if (handler == null) {
            synchronized (this) {
                if (handler == null)
                    handler = new Handler(Looper.getMainLooper());
            }
        }
        return handler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        WebSocketManager.getInstance().init();

    }

    @Override
    public void reconnect() {
        WebSocketManager.getInstance().reconnect();

    }

    @Override
    public void sendTextMessage(String message) {
        WebSocketManager.getInstance().sendTextMessage(message);

    }

    @Override
    public void disconnect() {
        WebSocketManager.getInstance().disconnect();

    }

    @Override
    public void cancelReconnect() {
        WebSocketManager.getInstance().cancelReconnect();

    }

    @Override
    public WsStatus getStatus() {
        return WebSocketManager.getInstance().getStatus();
    }


    public class WsBinder extends Binder {
        public WsService getService() {
            return WsService.this;
        }

    }

}
