package com.huotu.partnermall.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.SystemTools;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;

import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends Activity {
    public BaseApplication application;
    protected Handler mHandler = null;
    protected static final String NULL_NETWORK = "无网络或当前网络不可用!";
    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) this.getApplication ();
        //禁止横屏
        //BaseActivity.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
    }

    public void setImmerseLayout(View view){
        if (application.isKITKAT ()) {

            int bgColor = SystemTools.obtainColor(application.obtainMainColor());
            if( bgColor == Color.WHITE ){//当背景色为白色时，不启用沉浸模式
                //return;
                if( !setStatusBarTextBlackColor( bgColor )) return;
            }

            setStatusBarTextColor();

            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setStatusBarTextColor(){
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.M ) return;

        boolean flag_statusbar_text_color = getResources().getBoolean(R.bool.flag_statusbar_text_color_black);
        if( !flag_statusbar_text_color ) return;

        int bgColor = SystemTools.obtainColor(application.obtainMainColor());
        setStatusBarTextBlackColor( bgColor );
    }

    /**
     *  此功能在 android 6.0以上系统适用
     *设置白底黑字的状态栏
     */
    private boolean setStatusBarTextBlackColor( @ColorInt int backgroundColor ){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int newUiVisibility = this.getWindow().getDecorView().getSystemUiVisibility();
            newUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            this.getWindow().getDecorView().setSystemUiVisibility(newUiVisibility);
            getWindow().setStatusBarColor( backgroundColor );
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );

        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    protected boolean canConnect(){
//        //网络访问前先检测网络是否可用
//        if(!Util.isConnect(BaseActivity.this)){
//            ToastUtils.showLongToast(this, NULL_NETWORK);
//            return false;
//        }
//        return true;
//    }

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

    public void closeSelf(Activity aty){
        aty.finish();
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
