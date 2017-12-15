package com.u2sim.tellwechat.greenDao;

import android.os.Handler;
import android.os.Looper;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.CallTableBeanDao;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBeanDao;
import com.u2sim.tellwechat.util.LogUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hanguojing on 2017/12/8 13:41
 */

public class DbUtil {
    private DbUtil() {
    }
    private static final String TAG = "DbUtil";
    private volatile ExecutorService dbExecutorService;
    private volatile Handler handler;
    private static volatile DbUtil instance = null;

    public static DbUtil getInstance() {
        if (instance == null) {
            synchronized (DbUtil.class) {
                if (instance == null) {
                    instance = new DbUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取转发未接来电处理
     *
     * @param callback
     */
    public void getCallList(final DbCallback callback) {
        initExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                final List<CallTableBean> callTableBeans = InitApp.DaoSession.getCallTableBeanDao().queryBuilder()
                        .where(CallTableBeanDao.Properties.SendWsState.eq(1))
                        .limit(500).orderDesc(CallTableBeanDao.Properties.CallId)
                        .build().forCurrentThread().list();
                initHandler().post(() -> callback.result(callTableBeans));

            }
        });
    }

    /**
     * 获取转发短信列表
     *
     * @param callback
     */
    public void getSmsList(final DbCallback callback) {
        initExecutorService().execute(() -> {
            final List<SmsTableBean> smsTableBeans = InitApp.DaoSession.getSmsTableBeanDao().queryBuilder()
                    .where(SmsTableBeanDao.Properties.SendWsState.eq(1))
                    .limit(500).orderDesc(SmsTableBeanDao.Properties.SmsId)
                    .build().forCurrentThread().list();
            LogUtil.d(TAG,smsTableBeans.size()+"");
            initHandler().post(()->callback.result(smsTableBeans));
        });

    }


    private Handler initHandler() {

        if (handler == null)
            synchronized (this) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        return handler;
    }

    private ExecutorService initExecutorService() {
        if (dbExecutorService == null) {
            synchronized (this) {
                if (dbExecutorService == null) {
                    dbExecutorService = Executors.newSingleThreadExecutor();
                }
            }
        }
        return dbExecutorService;
    }

}
