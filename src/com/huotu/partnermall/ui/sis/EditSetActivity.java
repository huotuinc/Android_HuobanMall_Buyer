package com.huotu.partnermall.ui.sis;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.KJEditText;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

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
        wManager = this.getWindowManager ( );
        progress = new ProgressPopupWindow ( EditSetActivity.this, EditSetActivity.this, wManager );

    }
    @Override
    protected void initView() {
        header_title.setText("修改");


        int temp = getIntent().getIntExtra("type", 1);
        String tempName = EditSetTypeEnum.getName(temp);
        header_back.setOnClickListener(this);
        String context = getIntent().getExtras().getString("text");
        ET.setText(context);
    }






    protected void commit() {
        if (ET.getText().toString().trim() == "") {
            return;
        }
        String context = ET.getText().toString().trim();
        String url = SisConstant.INTERFACE_updateSisProfile;
        Map<String, String> paras = new HashMap<>();
        paras.put("profileType", String.valueOf(typeEnum.getIndex()));
        paras.put("profileData", context);

        AuthParamUtils authParamUtils = new AuthParamUtils(application,System.currentTimeMillis(), url,this);
        Map<String, String> maps = authParamUtils.obtainParams(paras);
        GsonRequest<BaseModel> updateRequest = new GsonRequest<>(
                Request.Method.POST,
                url,
                BaseModel.class,
                null,
                maps,
                updateListener,
                new ErrorListener(this,null)
        );

        progress.showProgress("正在更新数据，请稍等...");
        progress.showAtLocation (
                findViewById ( R.id.editRL ),
                Gravity.CENTER, 0, 0
        );

        VolleyUtil.getRequestQueue().add(updateRequest);
    }

    Response.Listener<BaseModel> updateListener = new Response.Listener<BaseModel>() {
        @Override
        public void onResponse(BaseModel baseModel) {
            progress.dismissView();
            if (null == baseModel) {
                ToastUtils.showLongToast( EditSetActivity.this, "出错");
                return;
            }
            if (baseModel.getSystemResultCode() != 1) {
                ToastUtils.showLongToast(EditSetActivity.this, "出错");
                return;
            }

            if (baseModel.getResultCode() != 1) {
                ToastUtils.showLongToast(EditSetActivity.this, "出错");
                return;
            }

           // SellerApplication.getInstance().writeMerchantInfo(baseModel.getResultData().getUser());
            //刷新界面数据
           // EventBus.getDefault().post(new RefreshSettingEvent());
            EditSetActivity.this.finish();
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back: {
               // commit();
            }
            break;

            default:
                break;
        }
    }

}
