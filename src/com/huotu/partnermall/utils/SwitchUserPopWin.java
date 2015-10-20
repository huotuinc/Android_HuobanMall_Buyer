package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.huotu.partnermall.inner.R;

/**
 * 切换用户面板
 */
public
class SwitchUserPopWin extends PopupWindow {

    private
    Activity context;
    private View popView;

    public
    SwitchUserPopWin ( ) {
        LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        popView = inflater.inflate ( R.layout.switch_user_layout, null );

    }


}
