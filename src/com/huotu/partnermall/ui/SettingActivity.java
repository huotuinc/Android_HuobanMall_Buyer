package com.huotu.partnermall.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;

/**
 * 用户设置界面
 */
public
class SettingActivity extends BaseActivity implements View.OnClickListener {

    public  BaseApplication application;
    private ImageView       searchImage;
    private ImageView       refreshBtn;
    private TextView        indexTitle;
    private
    WebView   viewPage;
    private
    Resources resources;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) SettingActivity.this.getApplication ();
        resources = SettingActivity.this.getResources ();
        setContentView ( R.layout.activity_setting );
        findViewById (  );
        initView ();
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
    }

    @Override
    protected
    void findViewById ( ) {

        searchImage = ( ImageView ) this.findViewById ( R.id.searchImage );
        refreshBtn = ( ImageView ) this.findViewById ( R.id.refreshBtn );
        indexTitle = ( TextView ) this.findViewById ( R.id.indexTitle );
    }

    @Override
    protected
    void initView ( ) {
        indexTitle.setText ( "设置中心" );
    }

    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId () )
        {
            case R.id.searchImage:
            {

            }
            break;
            case R.id.refreshBtn:
            {

            }
            break;
            default:
                break;
        }
    }
}
