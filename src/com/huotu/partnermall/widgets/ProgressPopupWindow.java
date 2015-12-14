package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.utils.WindowUtils;

/**
 * 延时加载时的进度条
 */
public
class ProgressPopupWindow extends PopupWindow {

    private Context       context;
    private Activity      aty;
    private WindowManager wManager;
    private View rootView;

    public ProgressPopupWindow ( Context context, Activity aty, WindowManager wManager ) {
        this.context = context;
        this.aty = aty;
        this.wManager = wManager;
        this.rootView = LayoutInflater.from ( context ).inflate ( R.layout.pop_progress, null );
    }

    public void showProgress ( String loadingText  ) {
        //View view = LayoutInflater.from ( context ).inflate ( R.layout.pop_progress, null );
        TextView loadingT = ( TextView ) rootView.findViewById ( R.id.loadingText );
        if( TextUtils.isEmpty ( loadingText )) {
            loadingText = "正在载入数据";
        }
        loadingT.setText ( loadingText );
        // 设置SelectPicPopupWindow的View
        this.setContentView ( rootView );
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( (wManager.getDefaultDisplay ().getWidth ()/10) * 8 );
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/10) * 2 );
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        WindowUtils.backgroundAlpha ( aty, 0.4f );
    }

    public void dismissView(){
        setOnDismissListener ( new PoponDismissListener ( aty ) );
        dismiss ();
    }
}
