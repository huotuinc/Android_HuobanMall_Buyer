package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;

import java.util.List;

/**
 * 切换用户面板
 */
public
class SwitchUserPopWin extends PopupWindow {

    private
    Activity context;
    private View popView;
    private
    List< String > users;
    private
    BaseApplication application;
    private WindowManager wManager;

    public
    SwitchUserPopWin ( Activity context, List< String > users, BaseApplication application, WindowManager wManager) {

        this.context = context;
        this.users = users;
        this.application = application;
        this.wManager = wManager;
    }

    public void initView()
    {
        LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        popView = inflater.inflate ( R.layout.switch_user_layout, null );

        LinearLayout userLayout = ( LinearLayout ) popView.findViewById ( R.id.userL );

        if ( null == users || users.isEmpty ( ) ) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                  .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
            LinearLayout userItem = ( LinearLayout ) LayoutInflater.from ( context ).inflate ( R.layout.switch_user_item, null );
            TextView account = ( TextView ) userItem.findViewById ( R.id.accountName );
            account.setText ( application.getUserName ( ) );
            userItem.setLayoutParams ( lp );
            userLayout.addView ( userItem );
        }
        else {
            int size = users.size ( );
            for ( int i = 0 ; i < size ; i++ ) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                      .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
                LinearLayout userItem = ( LinearLayout ) LayoutInflater.from ( context ).inflate ( R.layout.switch_user_item, null );
                TextView account = ( TextView ) userItem.findViewById ( R.id.accountName );
                account.setText ( users.get ( i ) );
                userItem.setLayoutParams ( lp );
                userLayout.addView ( userItem );

            }
        }

        // 设置SelectPicPopupWindow的View
        this.setContentView(popView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( (wManager.getDefaultDisplay ().getWidth ()/4) * 3 );
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/4) * 2 );
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPop);
        WindowUtils.backgroundAlpha ( context, 0.4f );
    }
}
