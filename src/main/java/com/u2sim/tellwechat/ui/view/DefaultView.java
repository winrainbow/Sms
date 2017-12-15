package com.u2sim.tellwechat.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.u2sim.tellwechat.R;

/**
 * Created by hanguojing on 2017/12/11 17:37
 */

public class DefaultView extends FrameLayout {


    private TextView tvDefault;
    private ImageView ivDefault;
    private ContentLoadingProgressBar clpDefault;

    public DefaultView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public DefaultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DefaultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DefaultView, defStyleAttr, 0);
        int len = typedArray.getIndexCount();
        for (int i = 0; i < len; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.DefaultView_defaultText:
                    tvDefault.setText(typedArray.getString(attr));
                    break;
                case R.styleable.DefaultView_defaultIvSrc:
                    ivDefault.setImageDrawable(typedArray.getDrawable(attr));
                    break;
            }
        }
        typedArray.recycle();

    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.default_view, this, true);
        tvDefault = view.findViewById(R.id.tv_default);
        ivDefault = view.findViewById(R.id.iv_default);
        clpDefault = view.findViewById(R.id.clp_default);
    }
    public void setIvShow(boolean ivShow, String defaultText) {
        tvDefault.setText(defaultText);
        ivDefault.setVisibility(ivShow ? View.VISIBLE : View.GONE);

        if (ivShow) {
            if (clpDefault.isShown()) {
                clpDefault.hide();
            }
            clpDefault.setVisibility(View.GONE);
        } else {
            clpDefault.setVisibility(View.VISIBLE);
            if (!clpDefault.isShown()) {
                clpDefault.show();
            }
        }
    }


}
