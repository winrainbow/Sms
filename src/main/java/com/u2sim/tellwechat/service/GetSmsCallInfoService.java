package com.u2sim.tellwechat.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.greenDao.bean.CallTableBean;
import com.u2sim.tellwechat.greenDao.bean.CallTableBeanDao;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBean;
import com.u2sim.tellwechat.greenDao.bean.SmsTableBeanDao;
import com.u2sim.tellwechat.server.MsgIdUtil;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hanguojing on 2017/11/23 15:46
 * <p>
 * 收到 短信 或电话后，去查找数据库，存储到 app 内部数据库
 */

public class GetSmsCallInfoService extends IntentService {

    public static final String INT_EXTRA_KEY = "int_extra_key";

    private static final String TAG = "GetSmsCallInfoService";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GetSmsCallInfoService() {
        super("getSmsCallInfoThread");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO: 2017/12/7 处理耗时操作：
        if (intent != null) {
            int intExtra = intent.getIntExtra(INT_EXTRA_KEY, -1);
            switch (intExtra) {
                case EnumUtil.PHONE_ACTION_CALL_IN:
                    // TODO: 2017/12/7 去查询手机的来电记录
                    handleQueryCallRecord();
                    break;
                case EnumUtil.PHONE_ACTION_SMS_IN:
                    // TODO: 2017/12/7 去查询手机的短信记录
                    handleQuerySmsRecord();
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 查询短信记录，并保存
     */
    private void handleQuerySmsRecord() {
        LogUtil.d(TAG, "处理短信记录查询");
        SystemClock.sleep(5000);
        Cursor cur = null;
        Cursor curName = null;
        if (InitApp.AppContext.getPackageManager().checkPermission(Manifest.permission.READ_SMS, InitApp.AppContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            try {
                String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "read"};
                cur = this.getContentResolver().query(Uri.parse("content://sms/inbox"), projection, " type = ? and read=?  ", new String[]{"1", "0"}, " date DESC ");
                if (cur != null && cur.moveToFirst()) {
                    String sysId = cur.getString(0);
                    String address = cur.getString(1);
                    String person = cur.getString(2);
                    String body = cur.getString(3);
                    Date date = new Date(cur.getLong(4));
                    int type = cur.getInt(5);
                    int read = cur.getInt(6);
                    String receiveTime = sdf.format(date);
                    cur.close();
                    String senderName = person;
                    if (!TextUtils.isEmpty(person)) {
                        curName = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, ContactsContract.Contacts._ID + " = ?", new String[]{person}, null);
                        if (curName != null && curName.moveToFirst()) {
                            senderName = curName.getString(0);
                            curName.close();
                        }
                    }

                    LogUtil.d(TAG, "[id:%s,addr:%s,person:%s,body:%s,time:%s,type:%d,read:%d]", sysId, address, senderName, body, receiveTime, type, read);

                    List<SmsTableBean> smsTableBeans = InitApp.DaoSession.getSmsTableBeanDao().queryBuilder()
                            .where(SmsTableBeanDao.Properties.SysId.eq(sysId),
                                    SmsTableBeanDao.Properties.SenderPhone.eq(address),
                                    SmsTableBeanDao.Properties.ReceiveTime.eq(receiveTime),
                                    SmsTableBeanDao.Properties.Content.eq(body))
                            .build().forCurrentThread().list();
                    if (smsTableBeans == null || smsTableBeans.isEmpty()) {
                        InitApp.DaoSession.getSmsTableBeanDao().insert(new SmsTableBean(null, MsgIdUtil.getMsgId(), sysId, address, senderName, body, receiveTime, 0, true));
                        EventBus.getDefault().post(new EventBusBase.Sys2AppComplete(EnumUtil.SYS_2_APP_SMS));
                    }

                }
            } catch (Exception exception) {
                LogUtil.d(TAG, "e:" + exception.getMessage());
            } finally {
                if (cur != null && !cur.isClosed()) {
                    cur.close();
                }
                if (curName != null && !curName.isClosed()) {
                    curName.close();
                }
            }

        } else {
            LogUtil.d(TAG, "未获取到读取短信权限");
        }

    }

    /**
     * 查询通话记录关保存
     */
    private void handleQueryCallRecord() {
        SystemClock.sleep(5000);
        LogUtil.d(TAG, "处理通话记录查询");
        Cursor cur = null;
        Cursor curName = null;
        if (InitApp.AppContext.getPackageManager().checkPermission(Manifest.permission.READ_CALL_LOG, InitApp.AppContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            try {
                LogUtil.d(TAG, "try");

                cur = this.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        new String[]{CallLog.Calls.CACHED_NAME,
                                CallLog.Calls.NUMBER,
                                CallLog.Calls.DATE, CallLog.Calls._ID},
                        " type = ? ", new String[]{Integer.toString(CallLog.Calls.MISSED_TYPE)},
                        CallLog.Calls.DATE + " DESC");
                if (cur != null && cur.moveToFirst()) {

                    LogUtil.d(TAG, "cur != null");
                    String callName = cur.getString(0);
                    LogUtil.d(TAG, "callName:" + callName);
                    String callNumber = cur.getString(1);
                    if (TextUtils.isEmpty(callName)) {
                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(callNumber));
                        curName = this.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                                null, null, null);
                        if (curName != null && curName.moveToFirst()) {
                            callName = curName.getString(0);
                            curName.close();
                        }
                    }
                    Date callDate = new Date(Long.parseLong(cur.getString(2)));
                    String sysId = cur.getString(3);
                    String callDateStr = sdf.format(callDate);
                    if (!cur.isClosed()) {
                        cur.close();
                    }


                    LogUtil.d(TAG, "[name:%s,sysId:%s,number:%s,date:%s]", callName, sysId, callNumber, callDateStr);
                    List<CallTableBean> callTableBeans = InitApp.DaoSession.getCallTableBeanDao().queryBuilder()
                            .where(CallTableBeanDao.Properties.SysId.eq(sysId),
                                    CallTableBeanDao.Properties.SenderPhone.eq(callNumber),
                                    CallTableBeanDao.Properties.ReceiveTime.eq(callDateStr))
                            .build().forCurrentThread().list();
                    if (callTableBeans == null || callTableBeans.isEmpty()) {
                        CallTableBean callTableBean = new CallTableBean(null, MsgIdUtil.getMsgId(), sysId, callNumber, callName, callDateStr, 0);
                        InitApp.DaoSession.getCallTableBeanDao().insert(callTableBean);
                        EventBus.getDefault().post(new EventBusBase.Sys2AppComplete(EnumUtil.SYS_2_APP_Call));
                    }

                }

            } catch (Exception exception) {
                LogUtil.d(TAG, "读取通话记录异常：%s", exception.getMessage());

            } finally {
                if (cur != null && !cur.isClosed()) {
                    cur.close();
                }
                if (curName != null && !curName.isClosed()) {
                    curName.close();
                }
            }
        } else {
            // TODO: 2017/12/7 发消息给websocket 通知服务器，
            LogUtil.d(TAG, "没有权限");
        }
    }


    private String getNameWithNum(String phoneNum) {
        String name = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNum));
        Cursor curName = this.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);
        if (curName != null && curName.moveToFirst()) {
            name = curName.getString(0);
            curName.close();
        }
        return name;
    }
}
