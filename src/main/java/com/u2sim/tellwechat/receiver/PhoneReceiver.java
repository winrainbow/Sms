package com.u2sim.tellwechat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.u2sim.tellwechat.service.GetSmsCallInfoService;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.LogUtil;

import java.util.Date;

/**
 * Created by hanguojing on 2017/11/23 16:17
 */

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneReceiver";
    private TelephonyManager telephonyManager = null;
    private static long endRingTime = -1;
    private static int lastCallState = 0;
    private static PhoneListener phoneListener = null;
    private static long startRingTime = -1;

    //    private String incomingNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        phoneListener = new PhoneListener(context);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    class PhoneListener extends PhoneStateListener {
        private Context context;

        public PhoneListener(Context context) {
            this.context = context;

        }

        @Override
        public void onCallStateChanged(int currentCallState, String incomingNumber) {
//            PowerManager.WakeLock wakelock = ((PowerManager) this.context.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, TAG + "_onCallStateChanged");
//            wakelock.acquire();
            LogUtil.d(TAG, incomingNumber + ",currentCallState:" + currentCallState);
            if (currentCallState != TelephonyManager.CALL_STATE_IDLE) {
                if (currentCallState == TelephonyManager.CALL_STATE_RINGING) {
                    // 正在响铃
                    LogUtil.d(TAG, "Ringing");
                    startRingTime = new Date().getTime();
                } else if (currentCallState == TelephonyManager.CALL_STATE_OFFHOOK) {
                    LogUtil.d(TAG, "offHook");
                }
            }
            if (currentCallState == 0) {
                Intent intentService = new Intent(context, GetSmsCallInfoService.class);
                intentService.putExtra(GetSmsCallInfoService.INT_EXTRA_KEY, EnumUtil.PHONE_ACTION_CALL_IN);
                context.startService(intentService);
            }
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
//            if (wakelock.isHeld()) {
//                wakelock.release();
//            }
        }
    }
}
