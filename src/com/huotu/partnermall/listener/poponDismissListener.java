package com.huotu.partnermall.listener;

import android.app.Activity;
import android.view.Window;
import android.widget.PopupWindow;

import com.huotu.partnermall.utils.WindowUtils;

/**
 * popwin 关闭后取消遮罩层监听器
 */
public
class poponDismissListener implements PopupWindow.OnDismissListener {

    private
    Activity aty;
    public poponDismissListener(Activity aty)
    {
        this.aty = aty;
    }
    @Override
    public
    void onDismiss ( ) {

        WindowUtils.backgroundAlpha ( aty, 1.0f );

    }
}
