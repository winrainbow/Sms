package com.u2sim.tellwechat.server;

/**
 * Created by hanguojing on 2017/11/27 14:12
 */

public interface ResultCode {
    int CODE_1_SUCCESS = 1;
    int CODE_0_FAILURE = 0;
    int CODE_100_WRONG_PARAMS = -100;
    int CODE_101_JOSN_ERROR = -101;
    int CODE_102_UNKOWN_ACTION = -102;
    int CODE_103_DATA_ERROR = -103;
    int CODE_201_URL_ERROR = -201;
    int CODE_202_DEVICE_DISABLE = -202;
    int CODE_211_FORWARD_ERROR = -211;
    int CODE_500_SERVER_ERROR  = -500;
}
