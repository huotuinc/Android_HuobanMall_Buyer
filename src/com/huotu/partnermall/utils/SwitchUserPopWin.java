package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.login.LoginActivity;

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
    private
    Handler mHandler;

    public
    SwitchUserPopWin ( Activity context, List< String > users, BaseApplication application, WindowManager wManager, Handler mHandler) {

        this.context = context;
        this.users = users;
        this.application = application;
        this.wManager = wManager;
        this.mHandler = mHandler;
    }

    public void initView()
    {
        LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        popView = inflater.inflate ( R.layout.switch_user_layout, null );

        LinearLayout userLayout = ( LinearLayout ) popView.findViewById ( R.id.userL );

        if ( null == users || users.isEmpty ( ) ) {
            /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                  .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
            LinearLayout userItem = ( LinearLayout ) LayoutInflater.from ( context ).inflate ( R.layout.switch_user_item, null );
            TextView account = ( TextView ) userItem.findViewById ( R.id.accountName );
            account.setText ( application.getUserName ( ) );
            userItem.setLayoutParams ( lp );
            userLayout.addView ( userItem );*/
        }
        else {
            int size = 0;
            if(5 > users.size ( ))
            {
                size = users.size ( );
            }
            else
            {
                size = 4;
            }

            for ( int i = 0 ; i < size ; i++ ) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                      .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
                LinearLayout userItem = ( LinearLayout ) LayoutInflater.from ( context ).inflate ( R.layout.switch_user_item, null );
                final TextView account = ( TextView ) userItem.findViewById ( R.id.accountName );
                //设置ID
                userItem.setId ( i );
                account.setText ( users.get ( i ) );
                userItem.setLayoutParams ( lp );
                account.setOnClickListener ( new View.OnClickListener ( ) {
                                                  @Override
                                                  public
                                                  void onClick ( View v ) {

                                                      //
                                                      dismiss ( );
                                                      ToastUtils.showShortToast ( context,
                                                                                  "3S后即将切换到账户：" +
                                                                                  account.getText
                                                                                          ( )
                                                                                         .toString ( ) );

                                                      mHandler.postDelayed ( new Runnable ( ) {
                                                                                 @Override
                                                                                 public
                                                                                 void run ( ) {

//鉴权失效
                                                                                     //清除登录信息
                                                                                     application.logout ();
                                                                                     application.titleStack.clear ();
                                                                                     //跳转到登录界面
                                                                                     ActivityUtils.getInstance ().skipActivity ( context, LoginActivity.class );

                                                                                 }
                                                                             }, 3000 );
                                                  }
                                              } );
                userLayout.addView ( userItem );

            }
        }

        // 设置SelectPicPopupWindow的View
        this.setContentView ( popView );
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( (wManager.getDefaultDisplay ().getWidth ()/4) * 3 );
        // 设置SelectPicPopupWindow弹出窗体的高
        switch ( users.size () )
        {
            case 0:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/3) * 1 );
            }
            break;
            case 1:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/2) * 1 );
            }
            break;
            case 2:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/5) * 3 );
            }
            break;
            case 3:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/5) * 4 );
            }
            break;
            case 4:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/5) * 4 );
            }
            break;
            case 5:
            {
                this.setHeight ( (wManager.getDefaultDisplay ().getWidth ()/9) * 8 );
            }
            break;
            default:
                break;
        }

        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPop);
        WindowUtils.backgroundAlpha ( context, 0.4f );
    }
}
