package com.huotu.partnermall.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.WindowProgress;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

public class NativeBaseActivity extends AppCompatActivity {
    /**
     * 标记 是否 主界面
     */
    protected boolean isMainUI = false;
    protected WindowProgress windowProgress;
    protected long exitTime = 0l;

    public void setImmerseLayout(View view){
        if ( BaseApplication.single.isKITKAT()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = this.getStatusBarHeight ( this.getBaseContext ( ) );
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    public int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_base);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if( isMainUI ){
                QuitApp();
            }else{
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    protected void QuitApp(){
        //2秒以内按两次推出程序
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showLongToast( "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            try {
                this.finish();
                Runtime.getRuntime().exit(0);
            } catch (Exception e) {
                Runtime.getRuntime().exit(-1);
            }
        }
    }

    public  void showProgress(String message) {
        if (windowProgress == null) {
            windowProgress = new WindowProgress(this);
        }
        windowProgress.showProgress();
        //windowProgress.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void dismissProgress() {
        if (windowProgress != null) {
            windowProgress.dismissProgress();
        }
    }

    public void Register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void UnRegister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
