package com.huotu.partnermall.ui.sis;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.widgets.KJEditText;


public class SisLoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView header_title;
    private Button header_back;
    private KJEditText edtPhone;
    private KJEditText edtUseName;
    private KJEditText edtcode;
    private TextView btngetcode;
    private TextView tvinfo;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_sis_login);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        header_title = (TextView) findViewById(R.id.header_title);
        header_back = (Button) findViewById(R.id.header_back);
        edtPhone= (KJEditText) findViewById(R.id.edtPhone);
        edtUseName= (KJEditText) findViewById(R.id.edtUserName);
        edtcode= (KJEditText) findViewById(R.id.edtCode);
        btngetcode= (TextView) findViewById(R.id.btnGetcode);
        tvinfo= (TextView) findViewById(R.id.tvinfo);
        btnLogin= (Button) findViewById(R.id.btnLogin);
    }

    @Override
    protected void initView() {
        header_title.setText("申请开店");
        header_back.setOnClickListener(this);
        btngetcode.setOnClickListener(this);
        tvinfo.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:{
                finish();
            }
            break;
            case R.id.btnGetcode:{
                //getcode();
            }
            break;
            case R.id.btnLogin:{
                ActivityUtils.getInstance().skipActivity(SisLoginActivity.this, GoodManageActivity.class);
            }
            break;
        }
    }
}
