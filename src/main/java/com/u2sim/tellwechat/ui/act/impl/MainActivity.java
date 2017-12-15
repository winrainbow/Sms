package com.u2sim.tellwechat.ui.act.impl;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.u2sim.tellwechat.R;
import com.u2sim.tellwechat.app.InitApp;
import com.u2sim.tellwechat.bean.UserInfo;
import com.u2sim.tellwechat.bean.server.wsApp.AppWsBean;
import com.u2sim.tellwechat.event.EventBusBase;
import com.u2sim.tellwechat.netstate.NetUtil;
import com.u2sim.tellwechat.server.MsgIdUtil;
import com.u2sim.tellwechat.server.http.HttpServerUtil;
import com.u2sim.tellwechat.server.websocket.wsMsg.MsgFactory;
import com.u2sim.tellwechat.server.websocket.wsMsg.SendReqMsgWrapper;
import com.u2sim.tellwechat.server.websocket.wsMsg.SendToWsController;
import com.u2sim.tellwechat.ui.act.BaseActivity;
import com.u2sim.tellwechat.ui.adapter.MainFragmentAdapter;
import com.u2sim.tellwechat.ui.fragment.impl.SxDialogFragment;
import com.u2sim.tellwechat.util.DisplayUtil;
import com.u2sim.tellwechat.util.EnumUtil;
import com.u2sim.tellwechat.util.GsonFormatUtil;
import com.u2sim.tellwechat.util.LogUtil;
import com.u2sim.tellwechat.util.PermissionUtil;
import com.u2sim.tellwechat.util.SPUtils;
import com.u2sim.tellwechat.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hanguojing on 2017/12/4 14:30
 */

public class MainActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final int REQUEST_CODE_LOGIN_ACT = 0x1001;
    public static final String KEY_WE_CHAT_BIND_RESULT = "key_we_chat_bind_result";

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.iv_photo)
    CircleImageView ivPhoto;

    @BindView(R.id.iv_bg)
    CircleImageView ivBg;
    @BindView(R.id.tv_start)
    TextView tvStart;

    @BindView(R.id.ll)
    RelativeLayout fl;

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_bind)
    LinearLayout llBind;
    private SxDialogFragment sxDialogFragment;
    private Drawable drawableLeft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_act_main);
        setSupportActionBar(toolbar);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtil.getInstance().notificationPermissionEnable()) {

            if (sxDialogFragment == null) {
                sxDialogFragment = SxDialogFragment.newInstance("打开监听通知权限后，应用更稳定");
            }

            if (!sxDialogFragment.isAdded()) {
                sxDialogFragment.show(getSupportFragmentManager(), "NOTIFICATION_LISTENER");
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNetworkConnected(NetUtil.NetType type) {
        LogUtil.d(TAG, type.toString());
    }

    @Override
    protected void onNetworkDisConnected() {
        LogUtil.d(TAG, "onNetWorkDisConnected");

    }

    private void initView() {


        drawableLeft = ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_wechat);
        drawableLeft.setBounds(0,0,DisplayUtil.dp2px(14),DisplayUtil.dp2px(12));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout.addOnOffsetChangedListener(this);
        if (InitApp.getLoginState()) {
            String userInfoStr = (String) SPUtils.get(EnumUtil.SP_KEY_USER_INFO, "");
            if (!TextUtils.isEmpty(userInfoStr)) {
                UserInfo userInfo = GsonFormatUtil.getInstance().fromJson(userInfoStr, UserInfo.class);
                tvStart.setVisibility(View.GONE);
                ivPhoto.setVisibility(View.VISIBLE);
                // TODO: 2017/12/11 开启旋转动画
                tvDesc.setVisibility(View.GONE);
                llBind.setVisibility(View.VISIBLE);
                tvName.setText(userInfo.getNickName());
                tvName.setCompoundDrawables(drawableLeft,null,null,null);
                Glide.with(this).load(userInfo.getPhotoUrl()).into(ivPhoto).onLoadFailed(ContextCompat.getDrawable(InitApp.AppContext, R.mipmap.icon));
            }
        } else {
            tvDesc.setVisibility(View.VISIBLE);
            llBind.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.GONE);
            tvStart.setVisibility(View.VISIBLE);
        }


    }

    @OnClick({R.id.iv_photo, R.id.iv_bg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bg:
                // TODO: 2017/12/11 获取二维码
                getQrCode();
                break;
            case R.id.iv_photo:
                // TODO: 2017/12/11 退出登录
                break;
        }
    }


    /**
     * 获取二维码
     */
    private void getQrCode() {
        AppWsBean<Object> appWsBean = new AppWsBean<>();
        appWsBean.setType(EnumUtil.MSG_TYPE_REQ);
        appWsBean.setResult(1);
        appWsBean.setAction(EnumUtil.ACTION2WS_QR_CODE);
        appWsBean.setId(MsgIdUtil.getMsgId());

        SendReqMsgWrapper sendReqMsgWrapper = new SendReqMsgWrapper(appWsBean);
        SendToWsController.getInstance().executeSendReq(sendReqMsgWrapper);


    }


    /**
     * 动态控制 tablayout的显示与隐藏
     *
     * @param visibility
     */
    public void setTabLayoutVisibility(int visibility) {
        if (tabLayout != null) {
            if (tabLayout.getVisibility() != visibility) {
                tabLayout.setVisibility(visibility);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_help:
                break;
            case R.id.menu_forward:
                break;
            case R.id.menu_permission:
                break;
            case R.id.menu_update:
                break;
            case R.id.menu_about:
                HttpServerUtil.uploadLog(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            if (body != null) {
                                try {
                                    String result = new String(body.bytes());
                                    if("ok".equals(result)){
                                        ToastUtil.shortToast(MainActivity.this,"log upload success");
                                    }else{
                                        ToastUtil.shortToast(MainActivity.this,"log upload failure:"+result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    ToastUtil.shortToast(MainActivity.this,"log upload failure:"+e.getMessage());
                                }
                            }else {
                                ToastUtil.shortToast(MainActivity.this,"log upload failure:response body is null");
                            }
                        }else{
                            ToastUtil.shortToast(MainActivity.this,"log upload failure:response is not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        ToastUtil.shortToast(MainActivity.this,"log upload failure:"+t.getMessage());
                    }
                });
                break;
            case android.R.id.home:
                appBarLayout.setExpanded(true, false);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getQrUrl(EventBusBase.GetQrCodeSuccess qrCodeSuccess) {
        String qrCodeUrl = qrCodeSuccess.getQrCodeUrl();
        if (!TextUtils.isEmpty(qrCodeUrl)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.KEY_QR_CODE_URL, qrCodeUrl);
            startActivityForResult(intent, REQUEST_CODE_LOGIN_ACT);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserInfoResult(EventBusBase.GetUserInfo getUserInfo) {
        JsonObject data = getUserInfo.getData();
        if (data != null) {
            UserInfo userInfo = new UserInfo();
            JsonElement deviceName = data.get("deviceName");
            if (deviceName != null && !deviceName.isJsonNull()) {
                userInfo.setDeviceName(deviceName.getAsString());
            }
            JsonObject weChat = data.getAsJsonObject("weChat");
            if (weChat != null && !weChat.isJsonNull()) {
                JsonElement isBind = weChat.get("isBind");
                userInfo.setBind(isBind != null && !isBind.isJsonNull() && isBind.getAsInt() == 1);
                JsonElement weChatID = weChat.get("weChatID");
                userInfo.setWeChatID(weChatID != null && !weChatID.isJsonNull() ? weChatID.getAsString() : "");
                JsonElement nickName = weChat.get("nickName");
                JsonElement photoUrl = weChat.get("photoUrl");
                if (userInfo.isBind()) {
                    tvDesc.setVisibility(View.GONE);
                    llBind.setVisibility(View.VISIBLE);

                    tvStart.setVisibility(View.GONE);
                    ivPhoto.setVisibility(View.VISIBLE);

                    if (nickName != null && !nickName.isJsonNull()) {
                        userInfo.setNickName(nickName.getAsString());
                        tvName.setText(nickName.getAsString());
                        tvName.setCompoundDrawables(drawableLeft,null,null,null);
                    } else {
                        tvName.setText(getString(R.string.app_name));
                        tvName.setCompoundDrawables(null,null,null,null);
                    }

                    if (photoUrl != null && !photoUrl.isJsonNull()) {
                        userInfo.setPhotoUrl(photoUrl.getAsString());
                        Glide.with(this).load(userInfo.getPhotoUrl()).apply(RequestOptions.errorOf(R.mipmap.icon)).into(ivPhoto);
                    }

                } else {
                    tvDesc.setVisibility(View.VISIBLE);
                    llBind.setVisibility(View.GONE);
                    ivPhoto.setVisibility(View.GONE);
                }

            }
            String toJson = GsonFormatUtil.getInstance().toJson(userInfo, UserInfo.class);
            InitApp.setLoginState(userInfo.isBind());
            SPUtils.putCommit(EnumUtil.SP_KEY_USER_INFO, toJson);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActResult[requestCode:%d,resultCode:%d]", requestCode, resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN_ACT:
                    if (data != null && data.getBooleanExtra(KEY_WE_CHAT_BIND_RESULT, false)) {
                        SendToWsController.getInstance().executeSendReq(new SendReqMsgWrapper(MsgFactory.getInstance().getWxInfoReq()));
                    }
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {

            String userInfoStr = (String) SPUtils.get(EnumUtil.SP_KEY_USER_INFO, "");
            if (!TextUtils.isEmpty(userInfoStr)) {
                UserInfo userInfo = GsonFormatUtil.getInstance().fromJson(userInfoStr, UserInfo.class);
                collapsingToolbarLayout.setTitle(userInfo.isBind() ? userInfo.getNickName() : getString(R.string.app_name));
                if (userInfo.isBind()) {

                    int height = toolbar.getHeight() - DisplayUtil.dp2px(16);
                    LogUtil.d(TAG, "height dp :" + DisplayUtil.px2dp(height));
                    Glide.with(this).load(userInfo.getPhotoUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).apply(RequestOptions.overrideOf(height)).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            toolbar.setNavigationIcon(resource);
                        }
                    }).onLoadFailed(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.icon));
                } else {
                    toolbar.setNavigationIcon(R.mipmap.icon);
                }

            } else {
                collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                toolbar.setNavigationIcon(R.mipmap.icon);
            }
            fl.setVisibility(View.GONE);
            if (ivPhoto.getVisibility() == View.VISIBLE) {
                // TODO: 2017/12/11 关闭动画
            }
        } else {
            toolbar.setNavigationIcon(null);
            collapsingToolbarLayout.setTitle("");
            fl.setVisibility(View.VISIBLE);
            if (ivPhoto.getVisibility() == View.VISIBLE) {
                // TODO: 2017/12/11 开启动画 
            }
        }
    }


}
