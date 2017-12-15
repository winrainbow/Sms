package com.u2sim.tellwechat.server.websocket.wsMsg;

import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.LogUtil;

import java.util.HashMap;

/**
 * Created by hanguojing on 2017/11/29 18:53
 * 正在发送的消息集合
 * 单例模式
 */

public class SendingMsg {
    private static final String TAG  = "SendingMsg";
    public volatile static SendingMsg instance;

    public static SendingMsg getInstance() {
        if (instance == null) {
            synchronized (SendingMsg.class) {
                if (instance == null) {
                    instance = new SendingMsg();
                }
            }
        }
        return instance;
    }

    private HashMap<String, String> smsHashMap, callHashMap,othersHashMap;


    /**
     * 发送消息时，需要调用该方法，加入记录中
     *
     * @param id
     * @param msgType
     * @param msg
     */
    public void add(String id, @EnumUtil.ActionType String action, String msg) {
        LogUtil.d(TAG,"addId:%s",id);
        switch (action) {
            case EnumUtil.ACTION2WS_CALL_NEW:
                if (callHashMap == null) {
                    callHashMap = new HashMap<>();
                }
                callHashMap.put(id, msg);
                break;
            case EnumUtil.ACTION2WS_SMS_NEW:
                if (smsHashMap == null) {
                    smsHashMap = new HashMap<>();
                }
                smsHashMap.put(id, msg);
                break;
            case EnumUtil.ACTION2WS_POWER:
            case EnumUtil.ACTION2WS_QR_CODE:
            case EnumUtil.ACTION2WS_USER_INFO_GET:
            case EnumUtil.ACTION2WS_USER_INFO_SET:
                if(othersHashMap == null){
                    othersHashMap = new HashMap<>();
                }
                othersHashMap.put(id,msg);
                break;
            default:
                break;
        }
    }
    public String getValue(String id,@EnumUtil.ActionType String action){
        LogUtil.d(TAG,"getValue:%s",id);
        String result = null;

        switch (action) {
            case EnumUtil.ACTION2WS_CALL_NEW:
                if (callHashMap != null) {
                    result = callHashMap.get(id);
                }
                break;
            case EnumUtil.ACTION2WS_SMS_NEW:
                if (smsHashMap != null) {
                    result = smsHashMap.get(id);
                }
                break;
            case EnumUtil.ACTION2WS_POWER:
            case EnumUtil.ACTION2WS_QR_CODE:
            case EnumUtil.ACTION2WS_USER_INFO_GET:
            case EnumUtil.ACTION2WS_USER_INFO_SET:
                if (othersHashMap != null) {
                    result = othersHashMap.get(id);
                }
                break;
            default:
                break;
        }
        return result;

    }


    /**
     * 在收到消息后，需要调用该方法，将发送消息记录中的记录删除
     *
     * @param id
     * @param msgType
     */
    public void remove(String id, @EnumUtil.ActionType String action) {
        LogUtil.d(TAG,"remove[id:%s,action:%s]",id,action);
        switch (action) {
            case EnumUtil.ACTION2WS_CALL_NEW:
                if (callHashMap != null) {
                    callHashMap.remove(id);
                }
                break;
            case EnumUtil.ACTION2WS_SMS_NEW:
                if (smsHashMap != null) {
                    smsHashMap.remove(id);
                }
                break;
            case EnumUtil.ACTION2WS_POWER:
            case EnumUtil.ACTION2WS_QR_CODE:
            case EnumUtil.ACTION2WS_USER_INFO_GET:
            case EnumUtil.ACTION2WS_USER_INFO_SET:
                if (othersHashMap != null) {
                    othersHashMap.remove(id);
                }
                break;
            default:
                break;
        }
    }

    public boolean isExist(String id ,@EnumUtil.ActionType String action){
        boolean isExist = false;
        LogUtil.d(TAG,"isExist:%s",id);
        switch (action) {
            case EnumUtil.ACTION2WS_CALL_NEW:
                if (callHashMap != null) {
                    isExist = callHashMap.containsKey(id);
                }
                break;
            case EnumUtil.ACTION2WS_SMS_NEW:
                if (smsHashMap != null) {
                    isExist = smsHashMap.containsKey(id);
                }
                break;
            case EnumUtil.ACTION2WS_POWER:
                break;
            case EnumUtil.ACTION2WS_QR_CODE:
                break;
            case EnumUtil.ACTION2WS_USER_INFO_GET:
                break;
            case EnumUtil.ACTION2WS_USER_INFO_SET:
                break;
            default:
                break;
        }
        return isExist;

    }


    private SendingMsg() {
    }
}
