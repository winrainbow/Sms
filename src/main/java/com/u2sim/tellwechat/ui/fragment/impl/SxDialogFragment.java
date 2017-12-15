package com.u2sim.tellwechat.ui.fragment.impl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.u2sim.tellwechat.util.LogUtil;

/**
 * Created by hanguojing on 2017/12/10 14:52
 */

public class SxDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = "SxDialogFragment";
    private static final String DIALOG_MESSAGE = "dialog_message";
    private AlertDialog dialog;

    public static SxDialogFragment newInstance(String dialog_msg) {

        Bundle args = new Bundle();
        args.putString(DIALOG_MESSAGE, dialog_msg);
        SxDialogFragment fragment = new SxDialogFragment();
        fragment.setArguments(args);
        fragment.setCancelable(false);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.d(TAG,"onCreateDialog");
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getActivity()).setTitle("权限申请")
                    .setMessage(getArguments().getString(DIALOG_MESSAGE, ""))
                    .setCancelable(false).setPositiveButton("确定", (dialogInterface, i) -> {
                        LogUtil.d(TAG, "确定onclick");
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        dismiss();
                    }).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        return dialog;
    }

}
