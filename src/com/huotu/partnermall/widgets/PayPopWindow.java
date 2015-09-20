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

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.WindowUtils;

/**
 * 支付弹出框
 */
public
class PayPopWindow extends PopupWindow {

    private
    Button wxPayBtn;
    private Button alipayBtn;
    private
    Button cancelBtn;
    private View payView;
    private Activity aty;

    public
    PayPopWindow ( Activity aty, View.OnClickListener wxPayListener, View.OnClickListener alipayListener) {
        super ( );
        this.aty = aty;
        LayoutInflater inflater = ( LayoutInflater ) aty.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );

        payView = inflater.inflate ( R.layout.pop_pay_ui, null );
        wxPayBtn = ( Button ) payView.findViewById ( R.id.wxPayBtn );
        alipayBtn = ( Button ) payView.findViewById ( R.id.alipayBtn );
        cancelBtn = ( Button ) payView.findViewById ( R.id.cancelBtn );

        wxPayBtn.setOnClickListener ( wxPayListener );
        alipayBtn.setOnClickListener ( alipayListener );
        cancelBtn.setOnClickListener ( new View.OnClickListener ( ) {
                                           @Override
                                           public
                                           void onClick ( View v ) {
                                               dismiss ();
                                           }
                                       } );

        //设置SelectPicPopupWindow的View
        this.setContentView ( payView );
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth ( LinearLayout.LayoutParams.MATCH_PARENT );
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight ( LinearLayout.LayoutParams.WRAP_CONTENT );
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable ( false );
        WindowUtils.backgroundAlpha ( aty, 0.4f );

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        payView.setOnTouchListener (
                new View.OnTouchListener ( ) {
                    @Override
                    public
                    boolean onTouch ( View v, MotionEvent event ) {
                        int height = payView.findViewById ( R.id.popLayout ).getTop ( );
                        int y      = ( int ) event.getY ( );
                        if ( event.getAction ( ) == MotionEvent.ACTION_UP ) {
                            if ( y < height ) {
                                dismiss ( );
                            }
                        }
                        return true;
                    }
                }
                                   );
    }
}
