package com.huotu.partnermall.listener;

import android.content.Context;
import android.view.View;

import com.huotu.partnermall.ui.pay.AlipayActivity;
import com.huotu.partnermall.utils.ToastUtils;

/**
 * 支付宝支付点击事件
 */
public
class AliPayListener implements View.OnClickListener {

    private
    Context context;

    public AliPayListener(Context context)
    {
        this.context = context;
    }

    @Override
    public
    void onClick ( View v ) {
        ToastUtils.showShortToast ( context, "开始支付宝支付." );
    }
}
