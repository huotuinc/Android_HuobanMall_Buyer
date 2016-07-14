package com.huotu.partnermall.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.SwitchUserModel;
import com.huotu.partnermall.widgets.NoticePopWindow;

import java.util.List;

/**
 * 切换用户面板
 */
public class SwitchUserPopWin extends PopupWindow {
    private Activity context;
    private View popView;
    private List< SwitchUserModel.SwitchUser > users;
    private BaseApplication application;
    private WindowManager wManager;
    private Handler mHandler;
    //private View view;

    public SwitchUserPopWin ( Activity context, List< SwitchUserModel.SwitchUser > users, BaseApplication application,
                       WindowManager wManager, Handler mHandler ) {

        this.context = context;
        this.users = users;
        this.application = application;
        this.wManager = wManager;
        this.mHandler = mHandler;
        //this.view = view;
        this.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
    }

    public void initView ( ) {
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
            if(5 < users.size ( ))
            {
                users = users.subList ( 0, 5 );
            }

            for ( int i = 0 ; i < users.size () ; i++ ) {

                final SwitchUserModel.SwitchUser user = users.get ( i );
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
                LinearLayout userItem = ( LinearLayout ) LayoutInflater.from ( context ).inflate ( R.layout.switch_user_item, null );
                final TextView account = ( TextView ) userItem.findViewById ( R.id.accountName );
                ImageView tag = ( ImageView ) userItem.findViewById ( R.id.switchUserTag );
                if(application.readUserId ().equals ( String.valueOf ( user.getUserid( ) ) ))
                {
                    tag.setVisibility ( View.VISIBLE );
                }
                //设置ID
                userItem.setId ( i );
                account.setText ( user.getUsername ( ) );
                userItem.setLayoutParams ( lp );
                account.setOnClickListener (
                        new View.OnClickListener ( ) {
                            @Override
                            public void onClick ( View v ) {
                                dismiss();
                                //判断当前的用户
                                if ( application.readUserId().equals (String.valueOf (user.getUserid())) ) {
                                    NoticePopWindow noticePop = new NoticePopWindow ( context, "当前登录的是该用户，无需切换。" );
                                    noticePop.showNotice ( );
                                    noticePop.showAtLocation ( v , Gravity.CENTER, 0, 0 );
                                }
                                else {
                                    //切换用户通知
                                    Message msg = new Message ( );
                                    msg.what = Constants.SWITCH_USER_NOTIFY;
                                    msg.obj = user;
                                    mHandler.sendMessage ( msg );
                                }
                            }});
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
