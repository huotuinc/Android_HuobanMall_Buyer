package com.huotu.partnermall.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huotu.partnermall.AppManager;

public abstract class BaseActivity extends Activity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    protected Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        //禁止横屏
        BaseActivity.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

    }

    public void setImmerseLayout(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = this.getStatusBarHeight ( this.getBaseContext ( ) );
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                                                              "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy ( );
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause ( );
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart ( );
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume ( );
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart ( );
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop ( );
    }

    /**
     * 绑定控件id
     */
    protected abstract void findViewById();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity ( intent );
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public void closeSelf(Activity aty)
    {
        aty.finish();
    }

}
