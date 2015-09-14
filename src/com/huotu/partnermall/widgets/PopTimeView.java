package com.huotu.partnermall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.DensityUtils;

/**
 * 弹出去选择时间控件
 */
public
class PopTimeView {

    public interface OnDateBackListener
    {
        void onDateBack(String date);
    }

    private OnDateBackListener listener;

    private Dialog dialog;

    private
    BaseApplication application;

    private View mainView;

    private Context mContext;

    private int minYear; // 最小年份

    private int curDay;

    private String result;

    private TimeView wheelYear;

    private TimeView wheelMonth;

    private TimeView wheelDay;

    public PopTimeView(Context context, BaseApplication application)
    {
        this.mContext = context;
        this.application = application;
        initView(context);
    }

    public void setOnDateBackListener(OnDateBackListener listener)
    {
        this.listener = listener;
    }

    private void initView(Context context)
    {

        if (dialog == null)
        {
            mainView = LayoutInflater.from ( context ).inflate(
                    R.layout.pop_wheelview, null);
            dialog = new Dialog(context, R.style.PopDialog);
            dialog.setContentView(mainView);
            Window window = dialog.getWindow();
            window.setGravity( Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.AnimationPop); // 添加动画

            // 设置视图充满屏幕宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            int[] size = DensityUtils.getSize ( mContext );
            lp.width = size[0]; // 设置宽度
            // lp.height = (int) (size[1]*0.8);
            window.setAttributes(lp);
        }

        // if(pop == null){
        // mainView =
        // LayoutInflater.from(context).inflate(R.layout.pop_wheelview, null);
        // pop = new PopupWindow(mainView,
        // LinearLayout.LayoutParams.MATCH_PARENT,
        // LinearLayout.LayoutParams.MATCH_PARENT, true);
        // pop.setAnimationStyle(R.style.AnimationPop);
        // }

        wheelYear = (TimeView) mainView.findViewById(R.id.wheelYear);
        wheelYear.setCyclic(true);
        wheelMonth = (TimeView) mainView.findViewById(R.id.wheelMonth);
        wheelMonth.setCyclic(true);
        wheelDay = (TimeView) mainView.findViewById(R.id.wheelDay);
        wheelDay.setCyclic(true);
        // final TextView txtDate = (TextView)
        // mainView.findViewById(R.id.txtDate);

        mainView.findViewById(R.id.btnCancel).setOnClickListener(
                new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        if (listener != null)
                            listener.onDateBack(null);
                        dialog.dismiss();

                    }
                });
        mainView.findViewById(R.id.btnSure).setOnClickListener(
                new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        /*
                         * if(listener != null){ listener.onDateBack(result); }
                         */
                        // 保存生日信息
                        //new ModifyBrithdayAsyncTask().execute();

                    }
                });

        if (mainView.getBackground() == null)
            mainView.setBackgroundColor( Color.BLACK);
        mainView.getBackground().setAlpha(150);

        mainView.setFocusableInTouchMode(true);
        mainView.setOnKeyListener(new View.OnKeyListener()
                                  {
                                      @Override
                                      public boolean onKey(View v, int keyCode, KeyEvent event)
                                      {
                                          if (keyCode == KeyEvent.KEYCODE_BACK)
                                          {
                                              if (listener != null)
                                                  listener.onDateBack(null);
                                              dialog.dismiss();
                                          }
                                          return false;
                                      }
                                  });
        mainView.findViewById(R.id.layAll).setOnClickListener(
                new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        if (listener != null)
                            listener.onDateBack(null);
                        dialog.dismiss();

                    }
                });
        mainView.findViewById(R.id.layMain).setOnClickListener(
                new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                    }
                });

    }

}
