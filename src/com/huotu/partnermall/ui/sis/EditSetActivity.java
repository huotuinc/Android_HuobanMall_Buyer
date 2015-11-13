package com.huotu.partnermall.ui.sis;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.widgets.KJEditText;


public class EditSetActivity extends BaseActivity implements View.OnClickListener {

    public KJEditText ET;

    public TextView header_title;

    public Button header_back;



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


    }
    @Override
    protected void initView() {
        header_title.setText("修改");
//

        //int temp = getIntent().getIntExtra("type", 1);
        //String tempName = EditSetTypeEnum.getName(temp);
        header_back.setOnClickListener(this);
        //String context = getIntent().getExtras().getString("text");
       // ET.setText(context);
    }






//    protected void commit() {
//        if (ET.getText().toString().trim() == "") {
//            return;
//        }
//        String context = ET.getText().toString().trim();
//        String url = Constant.UPDATEPROFILE_INTERFACE;
//        Map<String, String> paras = new HashMap<>();
//        paras.put("profileType", String.valueOf(typeEnum.getIndex()));
//        paras.put("profileData", context);
//
//        HttpParaUtils httpParaUtils = new HttpParaUtils();
//        Map<String, String> maps = httpParaUtils.getHttpPost(paras);
//        GsonRequest<HTMerchantModel> updateRequest = new GsonRequest<>(
//                Request.Method.POST,
//                url,
//                HTMerchantModel.class,
//                null,
//                maps,
//                updateListener,
//                new MJErrorListener(this)
//        );
//
//        this.showProgressDialog("", "正在更新数据，请稍等...");
//
//        VolleyRequestManager.getRequestQueue().add(updateRequest);
//    }
//
//    Response.Listener<HTMerchantModel> updateListener = new Response.Listener<HTMerchantModel>() {
//        @Override
//        public void onResponse(HTMerchantModel htMerchantModel) {
//            EditSetActivity.this.closeProgressDialog();
//            if (null == htMerchantModel) {
//                DialogUtils.showDialog(EditSetActivity.this, EditSetActivity.this.getSupportFragmentManager(), "错误信息", "更新失败", "关闭");
//                return;
//            }
//            if (htMerchantModel.getSystemResultCode() != 1) {
//                DialogUtils.showDialog(EditSetActivity.this, EditSetActivity.this.getSupportFragmentManager(), "错误信息", htMerchantModel.getSystemResultDescription(), "关闭");
//                return;
//            }
//            if (htMerchantModel.getResultCode() == Constant.TOKEN_OVERDUE) {
//                ActivityUtils.getInstance().skipActivity(EditSetActivity.this, LoginActivity.class);
//                return;
//            }
//            if (htMerchantModel.getResultCode() != 1) {
//                DialogUtils.showDialog(EditSetActivity.this, EditSetActivity.this.getSupportFragmentManager(), "错误信息", htMerchantModel.getResultDescription(), "关闭");
//                return;
//            }
//
//            SellerApplication.getInstance().writeMerchantInfo(htMerchantModel.getResultData().getUser());
//            //刷新界面数据
//            EventBus.getDefault().post(new RefreshSettingEvent());
//            EditSetActivity.this.finish();
//        }
//    };

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
