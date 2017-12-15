package com.u2sim.tellwechat.alive;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.server.websocket.service.WsService;
import com.u2sim.tellwechat.util.LogUtil;

/**
 * Created by hanguojing on 2017/12/10 10:38
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class KeepLiveService extends JobService {

    private final static String TAG = "KeepLiveService";
    private Handler handler = new Handler(message -> {
        LogUtil.d(TAG,"收到handleMessage");
        JobParameters parameters = (JobParameters) message.obj;
        jobFinished(parameters,true);
        Intent intent = new Intent(InitApp.AppContext, WsService.class);
        startService(intent);
        return true;
    });

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG,"onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG,"onStartCommand");
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        LogUtil.d(TAG,"onStartJob");
        Message msg = Message.obtain();
        msg.obj = jobParameters;
        handler.sendMessage(msg);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        LogUtil.d(TAG,"onStopJob");
        handler.removeCallbacksAndMessages(null);
        return false;
    }
}
