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


public class CartActivity extends BaseActivity implements View.OnClickListener {

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
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) CartActivity.this.getApplication ( );
        resources = CartActivity.this.getResources ( );
        setContentView ( R.layout.cart_ui );
        initView ( );
    }

    @Override
    protected
    void findViewById ( ) {
        // TODO Auto-generated method stub

    }

    @Override
    protected
    void initView ( ) {
        // TODO Auto-generated method stub

    }

    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId () )
        {
            default:
                break;
        }
    }
}
