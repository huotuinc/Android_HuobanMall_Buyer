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

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.WindowUtils;


/**
 * 延时加载时的进度条
 */
public class ProgressPopupWindow extends PopupWindow {
    private Activity aty;
    private View rootView;
    public ProgressPopupWindow ( Activity aty ) {
        this.aty = aty;
        this.rootView = LayoutInflater.from ( aty ).inflate ( R.layout.pop_progress, null );

        //this.rootView.setBackgroundColor( SystemTools.obtainColor(((BaseApplication) aty.getApplication()).obtainMainColor()) );
    }

    public void showProgress ( String loadingText  ) {
        TextView loadingT = ( TextView ) rootView.findViewById ( R.id.loadingText );
        if( TextUtils.isEmpty ( loadingText )) {
            loadingText = "正在载入数据";
        }
        loadingT.setText ( loadingText );
        // 设置SelectPicPopupWindow的View
        this.setContentView ( rootView );
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( (DensityUtils.getScreenW(aty)/10) * 8 );
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( (DensityUtils.getScreenW(aty)/10) * 2 );
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //WindowUtils.backgroundAlpha ( aty, 0.4f );
    }

    public void dismissView(){
        if(!this.isShowing()) return;

        setOnDismissListener ( new PoponDismissListener ( aty ) );
        dismiss ();
    }
}
