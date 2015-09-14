package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;

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

    private View popView;

    public MsgPopWindow(Activity context,View.OnClickListener itemsOnClick, String title, String msg)
    {
        super ( context );
        LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
        popView = inflater.inflate ( R.layout.popwindow_ui, null );

        titleIcon = ( ImageView ) popView.findViewById ( R.id.popTileIcon );
        titleTxt = ( TextView ) popView.findViewById ( R.id.popTitleText );
        titleTxt.setText ( title );

        titleClose = ( ImageView ) popView.findViewById ( R.id.popTileClose );

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

        btnCancel.setOnClickListener (
                new View.OnClickListener ( ) {
                    @Override
                    public
                    void onClick ( View v ) {

                        dismiss ( );
                    }
                }
                                     );

        btnSure.setOnClickListener ( itemsOnClick );

        //设置SelectPicPopupWindow的View
        this.setContentView ( popView );
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( LinearLayout.LayoutParams.WRAP_CONTENT );
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( LinearLayout.LayoutParams.WRAP_CONTENT );
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable ( true );
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        popView.setOnTouchListener ( new View.OnTouchListener ( ) {
                                         @Override
                                         public
                                         boolean onTouch ( View v, MotionEvent event ) {
                                             int height = popView.findViewById(R.id.popLayout).getTop();
                                             int y=(int) event.getY();
                                             if(event.getAction()==MotionEvent.ACTION_UP){
                                                 if(y<height){
                                                     dismiss();
                                                 }
                                             }
                                             return true;
                                         }
                                     } );


    }

}
