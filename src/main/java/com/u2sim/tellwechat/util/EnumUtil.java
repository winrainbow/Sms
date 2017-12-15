package com.u2sim.tellwechat.util;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hanguojing on 2017/12/1 13:37
 */

public class EnumUtil {

    // msgAction : http 请求action
    public static final String ACTION_HTTP_CFG = "cfg";
    // msgAction : app->ws 请求action
    public static final String ACTION2WS_SMS_NEW = "smsNew";
    public static final String ACTION2WS_CALL_NEW = "callNew";
    public static final String ACTION2WS_POWER = "power";
    public static final String ACTION2WS_USER_INFO_SET = "userSet";
    public static final String ACTION2WS_QR_CODE = "qrCode";
    public static final String ACTION2WS_USER_INFO_GET = "userGet";

    // msgAction : ws->app 请求action
    public static final String ACTION2APP_WE_CHAT_BIND = "weChatBind";
    public static final String ACTION2APP_SEND_SMS = "smsSend";
    public static final String ACTION2APP_GET_RECENT_CALLS = "callRecentList";
    public static final String ACTION2APP_GET_RECENT_SMS = "smsRecentList";


    @StringDef({ACTION_HTTP_CFG,
            ACTION2WS_CALL_NEW, ACTION2WS_POWER, ACTION2WS_SMS_NEW, ACTION2WS_USER_INFO_SET, ACTION2WS_QR_CODE,ACTION2WS_USER_INFO_GET,
            ACTION2APP_WE_CHAT_BIND,ACTION2APP_SEND_SMS,ACTION2APP_GET_RECENT_CALLS,ACTION2APP_GET_RECENT_SMS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionType {
    }



    // 消息类型： 请求还是响应
    public static final int MSG_TYPE_REQ = 0;  // 请求类型消息
    public static final int MSG_TYPE_RSP = 1;  // 响应类型消息
    @IntDef({MSG_TYPE_REQ,MSG_TYPE_RSP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MsgType{}





    public static final String SP_KEY_USER_INFO = "sp_key_user_info";
    @StringDef({SP_KEY_USER_INFO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SpKey{}





    public final static int PHONE_ACTION_CALL_IN = 1;
    public final static int PHONE_ACTION_SMS_IN = 2;
    @IntDef({PHONE_ACTION_CALL_IN,PHONE_ACTION_SMS_IN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PhoneAction{}


    public final static int SYS_2_APP_SMS = 0x1001;
    public final static int SYS_2_APP_Call = 0x1002;
    @IntDef({SYS_2_APP_Call,SYS_2_APP_SMS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sys2AppType{}


    public final static int APP2WS_RSP_SMS = 0x1003;
    public final static int APP2WS_RSP_CALL = 0x1004;
    @IntDef({APP2WS_RSP_SMS,APP2WS_RSP_CALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface App2WsRspType{}


    private EnumUtil(){}
}
