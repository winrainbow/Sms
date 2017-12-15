package com.u2sim.tellwechat.bean.server.data;

/**
 * Created by hanguojing on 2017/11/27 16:09
 */

public class DataCfgRsp {
    private String wsUrl;

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    @Override
    public String toString() {
        return "DataCfgRsp{" +
                "wsUrl='" + wsUrl + '\'' +
                '}';
    }
}
