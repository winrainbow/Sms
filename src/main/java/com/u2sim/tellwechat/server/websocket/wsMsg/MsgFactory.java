package com.u2sim.tellwechat.server.websocket.wsMsg;

import com.u2sim.tellwechat.bean.server.data.DataCallTip;
import com.u2sim.tellwechat.bean.server.data.DataSms;
import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;
import com.u2sim.tellwechat.server.MsgIdUtil;
import com.u2sim.tellwechat.util.EnumUtil;

/**
 * Created by hanguojing on 2017/12/7 11:40
 */

public class MsgFactory {
    private static volatile MsgFactory instance;
    private MsgFactory(){}
    public static MsgFactory getInstance(){
        if(instance == null){
            synchronized (MsgFactory.class){
                if(instance == null){
                    instance = new MsgFactory();
                }
            }
        }
        return instance;
    }

    public AppWsBean<DataSms> getAppWsSmsReq(SmsTableBean smsBean){

        AppWsBean<DataSms> smsAppWsBean = new AppWsBean<>();
        smsAppWsBean.setAction(EnumUtil.ACTION2WS_SMS_NEW);
        smsAppWsBean.setType(EnumUtil.MSG_TYPE_REQ);
        smsAppWsBean.setId(smsBean.getSmsId());
        DataSms dataSms = new DataSms();
        dataSms.setSenderPhone(smsBean.getSenderPhone());
        dataSms.setSmsId(smsBean.getSysId());
        dataSms.setSenderName(smsBean.getSenderName());
        dataSms.setReceiveTime(smsBean.getReceiveTime());
        dataSms.setContent(smsBean.getContent());
        smsAppWsBean.setData(dataSms);
        return smsAppWsBean;
    }

    public AppWsBean<DataCallTip> getAppWsCallReq(CallTableBean callBean){
        AppWsBean<DataCallTip> callAppWsBean = new AppWsBean<>();
        callAppWsBean.setAction(EnumUtil.ACTION2WS_CALL_NEW);
        callAppWsBean.setId(callBean.getCallId());
        callAppWsBean.setType(EnumUtil.MSG_TYPE_REQ);
        DataCallTip callTip = new DataCallTip();
        callTip.setCallID(callBean.getSysId());
        callTip.setReceiveTime(callBean.getReceiveTime());
        callTip.setSenderName(callBean.getSenderName());
        callTip.setSenderPhone(callBean.getSenderPhone());
        callAppWsBean.setData(callTip);
        return callAppWsBean;


    }


    public AppWsBean<Integer> getWxInfoReq(){
        AppWsBean<Integer> appWsBean = new AppWsBean<>();
        appWsBean.setAction(EnumUtil.ACTION2WS_USER_INFO_GET);
        appWsBean.setType(EnumUtil.MSG_TYPE_REQ);
        appWsBean.setId(MsgIdUtil.getMsgId());
        return appWsBean;
    }


}
