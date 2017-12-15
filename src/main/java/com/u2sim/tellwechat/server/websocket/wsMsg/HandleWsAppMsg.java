package com.u2sim.tellwechat.server.websocket.wsMsg;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.bean.server.wsApp.WsAppBean;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.CallTableBeanDao;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBeanDao;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hanguojing on 2017/12/1 14:08
 * <p>
 * 集中对 ws 发过来的消息进行处理
 */

public class HandleWsAppMsg {
    private static  final String TAG = "HandleWsAppMsg";
    private static volatile HandleWsAppMsg instance;
    private ExecutorService executorService;

    public static HandleWsAppMsg getInstance() {
        if (instance == null) {
            synchronized (HandleWsAppMsg.class) {
                if (instance == null) {
                    instance = new HandleWsAppMsg();
                }
            }
        }
        return instance;
    }

    public void handleWsAppMsg(String wsAppMsg) {
        // TODO: 2017/12/1 如果开启了加密，需要先进行解密处理
        WsAppBean wsAppBean = GsonFormatUtil.getInstance().fromJson(wsAppMsg, WsAppBean.class);
        if (wsAppBean != null) {

            String action = wsAppBean.getAction();
            if (action != null) {
                if (wsAppBean.getType() == EnumUtil.MSG_TYPE_REQ) {
                    // ws 主动发出的请求消息
                    switch (action) {
                        case EnumUtil.ACTION2APP_WE_CHAT_BIND:
                            JsonObject data = wsAppBean.getData();
                            EventBus.getDefault().post(new EventBusBase.WeChatBindResult(data == null ? "" : data.toString()));
                            break;
                    }
                } else {
                    // ws 返回的响应消息：
                    SendingMsg.getInstance().remove(wsAppBean.getId(), action);
                    switch (action) {
                        case EnumUtil.ACTION2WS_QR_CODE:
                            handleQrCode(wsAppBean);
                            break;
                        case EnumUtil.ACTION2WS_SMS_NEW:
                        case EnumUtil.ACTION2WS_CALL_NEW:
                            handleCallSmsNewRsp(wsAppBean);
                            break;
                        case EnumUtil.ACTION2WS_POWER:
                            break;
                        case EnumUtil.ACTION2WS_USER_INFO_SET:
                            break;
                        case EnumUtil.ACTION2WS_USER_INFO_GET:
                            handleGetUserInfo(wsAppBean);
                            break;
                        default:
                            break;
                    }
                }
            }

        }

    }



    private void handleCallSmsNewRsp(final WsAppBean wsAppBean) {
        initExecutorService().execute(() -> {

            switch (wsAppBean.getAction()){
                case EnumUtil.ACTION2WS_CALL_NEW:
                    CallTableBean callTableBean = InitApp.DaoSession.getCallTableBeanDao().queryBuilder().where(CallTableBeanDao.Properties.CallId.eq(wsAppBean.getId())).build().forCurrentThread().unique();
                    callTableBean.setSendWsState(1);
                    InitApp.DaoSession.getCallTableBeanDao().update(callTableBean);
                    EventBus.getDefault().post(new EventBusBase.App2WsComplete(EnumUtil.APP2WS_RSP_CALL));
                    break;
                case EnumUtil.ACTION2WS_SMS_NEW:
                    SmsTableBean smsTableBean = InitApp.DaoSession.getSmsTableBeanDao().queryBuilder().where(SmsTableBeanDao.Properties.SmsId.eq(wsAppBean.getId())).build().forCurrentThread().unique();
                    smsTableBean.setSendWsState(1);
                    LogUtil.d(TAG,smsTableBean.toString());
                    InitApp.DaoSession.getSmsTableBeanDao().update(smsTableBean);
                    EventBus.getDefault().post(new EventBusBase.App2WsComplete(EnumUtil.APP2WS_RSP_SMS));
                    break;
            }

        });

    }

    private ExecutorService initExecutorService(){
        if (executorService == null)
            executorService = Executors.newSingleThreadExecutor();
        return executorService;

    }

    private void handleGetUserInfo(WsAppBean wsAppBean) {
        JsonObject data = wsAppBean.getData();
        EventBus.getDefault().post(new EventBusBase.GetUserInfo(data));
    }

    private void handleQrCode(WsAppBean wsAppBean) {
        JsonObject data = wsAppBean.getData();
        if (data != null) {
            JsonElement jsonElement = data.get("url");
            if (jsonElement != null && !jsonElement.isJsonNull()) {
                EventBus.getDefault().post(new EventBusBase.GetQrCodeSuccess(jsonElement.getAsString()));
            }
        }

    }

    private HandleWsAppMsg() {
    }
}
