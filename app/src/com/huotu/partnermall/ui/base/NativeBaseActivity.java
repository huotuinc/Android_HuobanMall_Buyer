package com.huotu.partnermall.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.WindowProgress;

public class NativeBaseActivity extends AppCompatActivity {
    //protected ProgressPopupWindow progressPopupWindow;
    protected WindowProgress windowProgress;

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
}
