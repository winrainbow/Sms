package com.u2sim.tellwechat.netstate;

/**
 * Created by hanguojing on 2017/12/5 16:12
 */

public interface NetChangedObserver {

        /**
         * when network connected callback
         */
        void onNetConnected(NetUtil.NetType type) ;

        /**
         *  when network disconnected callback
         */
        void onNetDisConnect() ;

}
