package com.huotu.partnermall.ui.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.DataBase;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.UIUtils;
import com.huotu.partnermall.widgets.CountDownTimerButton;
import com.huotu.partnermall.widgets.KJEditText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity implements CountDownTimerButton.CountDownFinishListener{

    @Bind(R.id.edtPhone)
    KJEditText edtPhone;
    @Bind(R.id.edtCode)
    KJEditText edtCode;
    @Bind(R.id.tvGetCode)
    TextView tvGetCode;
    @Bind(R.id.btnBind)
    Button btnBind;
    @Bind(R.id.titleText)
    TextView tvTitle;
    @Bind(R.id.bindPhoneActivity_header)
    RelativeLayout rlHeader;
    @Bind(R.id.titleLeftImage)
    ImageView ivLeft;

    ProgressDialog progressDialog;
    CountDownTimerButton countDownBtn;
    boolean isVoiceSMS=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);

        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        btnBind.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        tvTitle.setText("绑定手机");
        rlHeader.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()) );
        ivLeft.setBackgroundResource( R.drawable.main_title_left_back );

    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.titleLeftImage)
    protected void onBack(){
        finish();
    }

    @OnClick(R.id.btnBind)
    protected void onBtnBindClick(){
        String phone = edtPhone.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtPhone , 0);
            return;
        }

        if(TextUtils.isEmpty(code)){
            edtCode.setError("请输入验证码");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode,0);
            return;
        }

        bindPhone(phone, code);
    }

    protected void bindPhone(String phone ,String code ){
        String url = Constants.getINTERFACE_PREFIX() + "Account/bindMobile";
        Map<String, String> map = new HashMap<>();
        map.put("userid", application.readMemberId());
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        map.put("code", code);
        map.put("pwd",  "" );
        AuthParamUtils authParamUtils = new AuthParamUtils(application,  System.currentTimeMillis() , url, this);
        Map<String, String> params = authParamUtils.obtainParams(map);
        GsonRequest<DataBase> request = new GsonRequest<>(
                Request.Method.POST,
                url,
                DataBase.class,
                null,
                params,
                new MyBindListener(this),
                new MyBindErrorListener(this)
        );

        VolleyUtil.getRequestQueue().add(request);

        if( progressDialog ==null){
            progressDialog=new ProgressDialog( BindPhoneActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage("正在绑定手机操作，请稍等...");
        progressDialog.show();
    }

    @OnClick(R.id.tvGetCode)
    protected void onTvGetCodeClick(){

        String phone = edtPhone.getText().toString().trim();
        if( TextUtils.isEmpty( phone ) ){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode, 0);
            return;
        }
        if( phone.length()<11 ){
            edtPhone.setError("请输入合法的手机号");
            edtPhone.setFocusable(true);
            return;
        }

        if( isVoiceSMS ) {
            getCode(true , phone);
            countDownBtn = new CountDownTimerButton(tvGetCode, "%dS", "获取语音验证码", 60000, this);
            countDownBtn.start();
        }else{
            getCode(false , phone);
            countDownBtn = new CountDownTimerButton(tvGetCode, "%dS", "获取验证码", 60000, this);
            countDownBtn.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( isVoiceSMS ) {
            tvGetCode.setText("获取语音验证码");
        }else{
            tvGetCode.setText("获取验证码");
        }

        tvGetCode.setClickable(true);
        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
    }

    @Override
    public void finish(){
        if( tvGetCode==null)return;
        if(countDownBtn!=null){
            countDownBtn.Stop();
            countDownBtn=null;
        }
        isVoiceSMS=true;
        tvGetCode.setText("获取语音验证码");
    }

    protected void getCode(boolean isVoice , String phone) {
        String url;
        if( isVoice ) {
            url = Constants.getINTERFACE_PREFIX() + "Account/sendVoiceCode";
        }else{
            url = Constants.getINTERFACE_PREFIX() + "Account/sendCode";
        }

        Map<String, String> map = new HashMap<>();
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        //map.put("second");

        AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url, this);
        Map<String, String> params = authParamUtils.obtainParams(map);

        GsonRequest<DataBase> request = new GsonRequest<DataBase>(
                Request.Method.POST,
                url,
                DataBase.class,
                null,
                params,
                new MyGetCodeListener(this),
                new MyGetCodeErrorListener(this)
        );

        VolleyUtil.getRequestQueue().add(request);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(null != countDownBtn){
            countDownBtn.Stop();
        }
    }


    static class MyGetCodeListener implements Response.Listener<DataBase> {
        WeakReference<BindPhoneActivity> ref;
        public MyGetCodeListener(BindPhoneActivity act) {
            ref=new WeakReference<BindPhoneActivity>(act);
        }
        @Override
        public void onResponse(DataBase dataBase) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( dataBase ==null ){
                ToastUtils.showShortToast(ref.get(), "获取验证码失败。");
                return;
            }
            if( dataBase.getCode() != 200) {
                ToastUtils.showShortToast(ref.get(), "获取验证码失败。");
                return;
            }
        }
    }

    static class MyGetCodeErrorListener implements Response.ErrorListener {
        WeakReference<BindPhoneActivity> ref;
        public MyGetCodeErrorListener(BindPhoneActivity act) {
            ref = new WeakReference<BindPhoneActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if(ref.get().progressDialog !=null ){
                ref.get().progressDialog.dismiss();
            }

            ToastUtils.showShortToast(ref.get(),"请求失败");
        }
    }

    static class MyBindListener implements Response.Listener<DataBase> {
        WeakReference<BindPhoneActivity> ref;
        public MyBindListener(BindPhoneActivity act) {
            ref=new WeakReference<BindPhoneActivity>(act);
        }
        @Override
        public void onResponse(DataBase dataBase) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( dataBase ==null ){
                ToastUtils.showShortToast( ref.get() ,"绑定失败。" );
                return;
            }
            if( dataBase.getCode() != 200) {
                String msg = "绑定失败";
                if( !TextUtils.isEmpty( dataBase.getMsg()  )){
                    msg = dataBase.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg );
                return;
            }

            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType( 1 );
            ref.get().application.writeMemberRelatedType(2);//重写 关联类型=2 已经绑定
            //ref.get().application.writePhoneLogin(model.getLoginName(), model.getRealName(), model.getRelatedType(), model.getAuthorizeCode(), String.valueOf(ref.get().secure));

            //动态加载侧滑菜单
            //UIUtils ui = new UIUtils ( ref.get().application, ref.get() , resources, mainMenuLayout, wManager, mHandler, am );
            //ui.loadMenus();
            ToastUtils.showShortToast( ref.get() ,"绑定手机成功" );
            ref.get().setResult( RESULT_OK );
            ref.get().finish();
        }
    }

    static class MyBindErrorListener implements Response.ErrorListener {
        WeakReference<BindPhoneActivity> ref;
        public MyBindErrorListener(BindPhoneActivity act) {
            ref = new WeakReference<BindPhoneActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progressDialog != null ){
                ref.get().progressDialog.dismiss();
            }

            ToastUtils.showShortToast(ref.get(), "绑定失败");
        }
    }


}
