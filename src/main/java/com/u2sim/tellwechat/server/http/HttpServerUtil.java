package com.u2sim.tellwechat.server.http;

import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.bean.server.data.DataCfgReq;
import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;
import com.u2sim.tellwechat.bean.server.wsApp.WsAppBean;
import com.u2sim.tellwechat.util.AppVersionUtil;
import com.u2sim.tellwechat.util.DeviceUuidGenerate;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hanguojing on 2017/11/29 15:49
 * http 请求获取webSocket 服务器的地址
 */

public class HttpServerUtil {
    private static final String TAG = "HttpServerUtil";


    public static Call<WsAppBean> getWsHost(Callback<WsAppBean> callBack){
        HttpApi httpApi = RetrofitFactory.getRetrofit().create(HttpApi.class);

        MediaType parse = MediaType.parse("application/json;charset=utf-8");
        AppWsBean<DataCfgReq> dataCfgAppWsBean = new AppWsBean<>();
        dataCfgAppWsBean.setAction(EnumUtil.ACTION_HTTP_CFG);
        final DataCfgReq dataCfgReq = new DataCfgReq();
        dataCfgReq.setDeviceID(DeviceUuidGenerate.getInstance().getDeviceUuid());
        dataCfgReq.setAppVersion(AppVersionUtil.getVersionName());
        dataCfgReq.setApiVersion("apiVersion");
        dataCfgReq.setPhone("13126657177");
        dataCfgAppWsBean.setData(dataCfgReq);
        String toJson = GsonFormatUtil.getInstance().toJson(dataCfgAppWsBean);
        LogUtil.d(TAG,"toJson:"+toJson);
        RequestBody requestBody = RequestBody.create(parse, toJson);

        Call<WsAppBean> wsHostCall = httpApi.getWebSocketHost(requestBody);
        wsHostCall.enqueue(callBack);
        return wsHostCall;
    }


    public static Call<ResponseBody> uploadLog(Callback<ResponseBody> callback){
        HttpApi httpApi = RetrofitFactory.getRetrofit().create(HttpApi.class);
        String deviceId = DeviceUuidGenerate.getInstance().getDeviceUuid();
        String filePath = new File(InitApp.AppContext.getFilesDir().getAbsolutePath(),"log").getPath() +File.separator+ getFormatTime("yyyy-MM-dd",System.currentTimeMillis());
        File logFile = new File(filePath);

        try {
            byte[] bytes = getBytes(logFile);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
            MultipartBody.Part partFile = MultipartBody.Part.createFormData("f1", logFile.getName(), requestBody);

            MultipartBody.Part id = MultipartBody.Part.createFormData("did", deviceId);



            Call<ResponseBody> uploadCall = httpApi.uploadLog(id, partFile);
            uploadCall.enqueue(callback);
            return uploadCall;
        } catch (Exception e) {
            return null;
        }




    }

    private static byte[] getBytes(File file) throws Exception
    {
        byte[] bytes = null;
        if(file!=null)
        {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if(length>Integer.MAX_VALUE)   //当文件的长度超过了int的最大值
            {
                System.out.println("this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while(offset<bytes.length&&(numRead=is.read(bytes,offset,bytes.length-offset))>=0)
            {
                offset+=numRead;
            }
            //如果得到的字节长度和file实际的长度不一致就可能出错了
            if(offset<bytes.length)
            {
                System.out.println("file length is error");
                return null;
            }
            is.close();
        }
        return bytes;
    }


    private static String getFormatTime(String pattern, long time) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String format = sdf.format(new Date(time));
        return format;
    }

    private HttpServerUtil(){}
}
