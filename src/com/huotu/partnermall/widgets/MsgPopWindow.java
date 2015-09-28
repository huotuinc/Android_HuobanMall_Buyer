package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.WindowUtils;

/**
 * 自定义弹出框
 */
public
class MsgPopWindow extends PopupWindow {

    //标题
    private
    ImageView titleIcon;
    private
    TextView titleTxt;
    private ImageView titleClose;

    //内容
    private TextView tipsMsg;
    private
    Button btnSure;
    private Button btnCancel;
    private Activity context;
    private boolean isClose;

    private View popView;

    public MsgPopWindow(Activity context,View.OnClickListener okOnClick, View.OnClickListener cancelOnClick, String title, String msg, boolean isClose)
    {
        super ( context );
        this.context = context;
        this.isClose = isClose;
        LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        popView = inflater.inflate ( R.layout.popwindow_ui, null );

        titleIcon = ( ImageView ) popView.findViewById ( R.id.popTileIcon );
        titleTxt = ( TextView ) popView.findViewById ( R.id.popTitleText );
        titleTxt.setText ( title );

        titleClose = ( ImageView ) popView.findViewById ( R.id.popTileClose );
        if(!isClose)
        {
            titleClose.setVisibility ( View.GONE );
        }
        else
        {
            titleClose.setVisibility ( View.VISIBLE );
        }

        tipsMsg = ( TextView ) popView.findViewById ( R.id.popConMsg );
        tipsMsg.setText ( msg );
        btnSure = ( Button ) popView.findViewById ( R.id.btnSure );
        btnCancel = ( Button ) popView.findViewById ( R.id.btnCancel );

        titleClose.setOnClickListener ( new View.OnClickListener ( ) {
                                            @Override
                                            public
                                            void onClick ( View v ) {
                                                dismiss ();

                                            }
                                        } );
        if(null == cancelOnClick)
        {
            btnCancel.setOnClickListener ( new View.OnClickListener ( ) {
                                               @Override
                                               public
                                               void onClick ( View v ) {
                                                   dismiss ();
                                               }
                                           } );
        }
        else
        {
            btnCancel.setOnClickListener ( cancelOnClick );
        }

        if(null == okOnClick)
        {
            btnSure.setOnClickListener ( new View.OnClickListener ( ) {
                                             @Override
                                             public
                                             void onClick ( View v ) {
                                                 dismiss ();
                                             }
                                         } );
        }
        else
        {
            btnSure.setOnClickListener ( okOnClick );
        }

        //设置SelectPicPopupWindow的View
        this.setContentView ( popView );
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( LinearLayout.LayoutParams.WRAP_CONTENT );
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( LinearLayout.LayoutParams.WRAP_CONTENT );

        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable ( false );

        WindowUtils.backgroundAlpha ( context, 0.4f );

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        popView.setOnTouchListener ( new View.OnTouchListener ( ) {
                                         @Override
                                         public
                                         boolean onTouch ( View v, MotionEvent event ) {
                                             int height = popView.findViewById(R.id.popLayout).getTop();
                                             int y=(int) event.getY();
                                             if(event.getAction()==MotionEvent.ACTION_UP){
                                                 if(y<height){
                                                     //dismiss();
                                                 }
                                             }
                                             return true;
                                         }
                                     } );


    }

}
