package com.u2sim.tellwechat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.u2sim.tellwechat.service.GetSmsCallInfoService;
import com.u2sim.tellwechat.util.EnumUtil;

/**
 * Created by hanguojing on 2017/11/23 15:27
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, GetSmsCallInfoService.class);
        intentService.putExtra(GetSmsCallInfoService.INT_EXTRA_KEY, EnumUtil.PHONE_ACTION_SMS_IN);
        context.startService(intentService);
    }
}
