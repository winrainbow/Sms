package com.u2sim.tellwechat.server;

/**
 * Created by hanguojing on 2017/12/6 16:26
 */

public class MsgIdUtil {

    /**
     * 已时间戳的 hex 串作为消息 id
     * @return
     */
    public static String getMsgId(){
        return Long.toHexString(System.currentTimeMillis());
    }


    private MsgIdUtil(){}
}
