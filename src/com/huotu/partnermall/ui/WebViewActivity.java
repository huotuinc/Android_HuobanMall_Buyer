package com.huotu.partnermall.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.widgets.KJWebView;

/**
 * 单张展示web页面
 */
public
class WebViewActivity extends BaseActivity implements View.OnClickListener {

    //获取资源文件对象
    private
    Resources resources;
    //application
    private
    BaseApplication application;
    //title
    //tilte组件
    private RelativeLayout homeTitle;
    //title左边图标
    //标题栏左侧图标
    private
    ImageView titleLeftImage;
    //标题栏标题文字
    private
    TextView  titleText;
    //标题栏右侧图标
    private ImageView titleRightImage;
    //web视图
    private
    KJWebView viewPage;

    @Override

    protected
    void onCreate ( Bundle savedInstanceState ) {

        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) this.getApplication ();
        resources = this.getResources ();
        this.setContentView ( R.layout.new_load_page );

        setImmerseLayout ( findViewById ( R.id.homeTitle ) );
    }

    @Override
    protected
    void findViewById ( ) {

        homeTitle = ( RelativeLayout ) this.findViewById ( R.id.homeTitle );
        titleLeftImage = ( ImageView ) this.findViewById ( R.id.titleLeftImage );
        titleLeftImage.setOnClickListener ( this );
        titleText = ( TextView ) this.findViewById ( R.id.titleText );
        titleRightImage = ( ImageView ) this.findViewById ( R.id.titleRightImage );
        titleRightImage.setOnClickListener ( this );

        viewPage = ( KJWebView ) this.findViewById ( R.id.viewPage );

    }

    @Override
    protected
    void initView ( ) {


    }

    @Override
    public
    void onClick ( View v ) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN) {
            // finish自身
            WebViewActivity.this.finish();
            return true;
        }
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
}
