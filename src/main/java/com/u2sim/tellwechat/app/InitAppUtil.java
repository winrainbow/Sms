package com.u2sim.tellwechat.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.u2sim.tellwechat.BuildConfig;
import com.u2sim.tellwechat.eventbusannotation.SmsEventBusIndex;
import com.u2sim.tellwechat.greenDao.SmsSQLiteOpenHelper;
import com.u2sim.tellwechat.netstate.NetStateReceiver;
import com.u2sim.tellwechat.server.websocket.service.WsService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by hanguojing on 2017/11/30 11:07
 */

public class InitAppUtil {

    /**
     * 初始化数据库： greenDao 3.2
     */
    static SQLiteDatabase initDatabase(Context context) {
        // 创建数据库 autoSms.db
        SmsSQLiteOpenHelper smsSQLiteOpenHelper = new SmsSQLiteOpenHelper(context, "autoSms.db", null);
        // 获取可写数据库

        return smsSQLiteOpenHelper.getWritableDatabase();
    }

    /**
     * 使用Subscribe index 增加EventBus 速度
     */
    static void initEventBus() {
        EventBus.builder().addIndex(new SmsEventBusIndex()).installDefaultEventBus();
    }

    /**
     * 注删网络监听
     *
     * @param context
     */
    static void registerNetReceiver(Context context) {
        NetStateReceiver.registerNetworkStateReceiver(context);
    }


    /**
     * 启动webService
     *
     * @param context
     */
    public static void startWbService(Context context) {
        context.startService(new Intent(context, WsService.class));
    }

    static void startJbService(Context context) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), KeepLiveService.class.getName()))
                    .setPeriodic(1000).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setPersisted(true).build();
            jobScheduler.schedule(jobInfo);
        }*/
    }

    public static FilePrinter initXlog(Context context){
        LogConfiguration configuration = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL:LogLevel.NONE)
                .tag("u2sim-LOG").build();
        AndroidPrinter androidPrinter = new AndroidPrinter();

        FilePrinter filePrinter = new FilePrinter.Builder(new File(context.getFilesDir().getAbsolutePath(), "log").getPath())
                .fileNameGenerator(new DateFileNameGenerator())
                .logFlattener(new ClassicFlattener())
                .build();
        XLog.init(configuration,androidPrinter,filePrinter);
        return filePrinter;

    }

    private InitAppUtil() {
    }
}
