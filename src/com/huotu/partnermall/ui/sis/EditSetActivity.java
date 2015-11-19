package com.huotu.partnermall.ui.sis;


import android.content.Intent;
import android.os.Bundle;
import android.sax.RootElement;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJEditText;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class EditSetActivity extends BaseActivity implements View.OnClickListener {

    public KJEditText ET;

    public TextView header_title;

    public Button header_back;

    public EditSetTypeEnum typeEnum;
    public
    ProgressPopupWindow progress;
    WindowManager wManager;

    private RelativeLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);
        findViewById();
        initView();
    }

    @Override
    protected void  findViewById() {
        ET= (KJEditText) findViewById(R.id.ET);
        header_title= (TextView) findViewById(R.id.header_title);
        header_back= (Button) findViewById(R.id.header_back);
        header_back.setOnClickListener(this);
        wManager = this.getWindowManager ( );
        progress = new ProgressPopupWindow ( EditSetActivity.this, EditSetActivity.this, wManager );
        header = (RelativeLayout)findViewById(R.id.header_bar);
        header.setBackgroundColor(SystemTools.obtainColor( application.obtainMainColor()) );
    }
    @Override
    protected void initView() {
        header_title.setText("修改");


        int temp = getIntent().getIntExtra("type", 0);
        typeEnum = EditSetTypeEnum.valueOf(temp);
        String tempName = EditSetTypeEnum.getName(temp);
        header_back.setOnClickListener(this);
        String context = getIntent().getExtras().getString("text");
        ET.setText(context);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){

            commit();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    protected void commit() {
        if (ET.getText().toString().trim() == "") {
            ToastUtils.showLongToast(this,"请输入内容");
            return;
        }
        String context = ET.getText().toString().trim();
        String url = SisConstant.INTERFACE_updateSisProfile;
        Map<String, String> paras = new HashMap<>();
        paras.put("profiletype", String.valueOf(typeEnum.getIndex()));
        paras.put("profiledata", context);
        paras.put("sisid", String.valueOf( SisConstant.SHOPINFO.getSisId() ));

        AuthParamUtils authParamUtils = new AuthParamUtils(application,System.currentTimeMillis(), url,this);
        Map<String, String> maps = authParamUtils.obtainParams(paras);
        GsonRequest<AppSisBaseinfoModel> updateRequest = new GsonRequest<>(
                Request.Method.POST,
                url,
                AppSisBaseinfoModel.class,
                null,
                maps,
                new UpdateListener(this),
                new ErrorListener(this,null,progress)
        );

        progress.showProgress("正在更新数据，请稍等...");
        progress.showAtLocation (
                findViewById ( R.id.editRL ),
                Gravity.CENTER, 0, 0
        );

        VolleyUtil.getRequestQueue().add(updateRequest);
    }

     static class UpdateListener implements Response.Listener<AppSisBaseinfoModel> {
        WeakReference<EditSetActivity> ref;
        public UpdateListener(EditSetActivity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisBaseinfoModel appSisBaseinfoModel) {
            if( ref.get()==null) return;
            if( ref.get().progress!=null ) {
                ref.get().progress.dismissView();
            }
            if (null == appSisBaseinfoModel) {
                ToastUtils.showLongToast( ref.get() , "出错");
                return;
            }
            if (appSisBaseinfoModel.getSystemResultCode() != 1) {
                ToastUtils.showLongToast( ref.get(), "出错");
                return;
            }

            if (appSisBaseinfoModel.getResultCode() != 1) {
                ToastUtils.showLongToast( ref.get() , "出错");
                return;
            }

            SisConstant.SHOPINFO = appSisBaseinfoModel.getResultData().getData();

            Intent intent = ref.get().getIntent();
            //intent.putExtra("text", ref.get().ET.getText().toString().trim() );
            ref.get().setResult(RESULT_OK , intent );
            ref.get().finish();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back: {
               commit();
            }
            break;

            default:
                break;
        }
    }

}
