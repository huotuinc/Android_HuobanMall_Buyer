package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.WindowUtils;
import com.tencent.utils.SystemUtils;

/**
 * 自定义弹出框
 */
public class MsgPopWindow extends PopupWindow {
    //标题
    private ImageView titleIcon;
    private TextView titleTxt;
    private ImageView titleClose;
    //内容
    private TextView tipsMsg;
    private Button btnSure;
    private Button btnCancel;
    private Activity context;
    private boolean isClose;
    private View popView;
    private RelativeLayout popTitle;
    private RelativeLayout popContext;

    public MsgPopWindow(Activity context, View.OnClickListener okOnClick, View.OnClickListener cancelOnClick, String title, String msg, boolean isClose) {
        super(context);
        this.context = context;
        this.isClose = isClose;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popwindow_ui, null);

        popTitle = (RelativeLayout) popView.findViewById(R.id.popTitle);
        popContext = (RelativeLayout) popView.findViewById(R.id.popCon);

        titleIcon = (ImageView) popView.findViewById(R.id.popTileIcon);
        titleTxt = (TextView) popView.findViewById(R.id.popTitleText);
        titleTxt.setText(title);

        titleClose = (ImageView) popView.findViewById(R.id.popTileClose);
        if (!isClose) {
            titleClose.setVisibility(View.GONE);
        } else {
            titleClose.setVisibility(View.VISIBLE);
        }

        tipsMsg = (TextView) popView.findViewById(R.id.popConMsg);
        tipsMsg.setText(msg);
        btnSure = (Button) popView.findViewById(R.id.btnSure);
        btnCancel = (Button) popView.findViewById(R.id.btnCancel);

        titleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        if (null == cancelOnClick) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            btnCancel.setOnClickListener(cancelOnClick);
        }

        if (null == okOnClick) {
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            btnSure.setOnClickListener(okOnClick);
        }

        //设置SelectPicPopupWindow的View
        this.setContentView(popView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);

        WindowUtils.backgroundAlpha(context, 0.4f);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popView.findViewById(R.id.popLayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        //dismiss();
                    }
                }
                return true;
            }
        });


    }

    public void setWindowsStyle() {
        popTitle.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) context.getApplication()).obtainMainColor()));
        popContext.setBackgroundColor(Color.WHITE);
        tipsMsg.setTextColor(Color.BLACK);

        StateListDrawable stateListDrawable = new StateListDrawable();
        float[] outR = {8f, 8f, 8f, 8f, 8f, 8f, 8f, 8f};
        RectF inRect = new RectF(1f, 1f, 1f, 1f);
        float[] inR = {8f, 8f, 8f, 8f, 8f, 8f, 8f, 8f};
        RoundRectShape roundRectShape1 = new RoundRectShape(outR, inRect, inR);
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape1);
        shapeDrawable1.setPadding(8, 8, 8, 8);
        shapeDrawable1.getPaint().setColor(SystemTools.obtainColor(((BaseApplication) context.getApplication()).obtainMainColor()));
        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);

        RoundRectShape roundRectShape2 = new RoundRectShape(outR, inRect, inR);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
        shapeDrawable1.setPadding(8, 8, 8, 8);
        shapeDrawable2.getPaint().setColor(SystemTools.obtainColor(((BaseApplication) context.getApplication()).obtainMainColor()));
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.setAlpha(50);

        int[] normalState = new int[]{};
        int[] pressState = new int[]{android.R.attr.state_pressed};
        int[] selectedState = new int[]{android.R.attr.state_selected};
        stateListDrawable.addState(normalState, shapeDrawable1);
        stateListDrawable.addState(pressState, shapeDrawable2);
        stateListDrawable.addState(selectedState, shapeDrawable1);

        btnSure.setTextColor(Color.BLACK);
        btnCancel.setTextColor(Color.BLACK);
        SystemTools.loadBackground(btnSure, stateListDrawable);
        SystemTools.loadBackground(btnCancel, stateListDrawable);
    }
}
