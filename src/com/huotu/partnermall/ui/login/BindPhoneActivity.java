package com.huotu.partnermall.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.android.library.libedittext.EditText;
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
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.widgets.CountDownTimerButton;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity implements CountDownTimerButton.CountDownFinishListener{

    @Bind(R.id.edtPhone) EditText edtPhone;
    @Bind(R.id.edtCode)  EditText edtCode;
    @Bind(R.id.tvGetCode) TextView tvGetCode;
    @Bind(R.id.btnBind) Button btnBind;
    @Bind(R.id.titleText) TextView tvTitle;
    @Bind(R.id.bindPhoneActivity_header) RelativeLayout rlHeader;
    @Bind(R.id.titleLeftImage) ImageView ivLeft;
    @Bind(R.id.tvNoCode) TextView tvNoCode;
    @Bind(R.id.titleRightText) TextView titleRightText;
    @Bind(R.id.titleRightImage) ImageView titleRightImage;
    @Bind(R.id.titleRightLeftImage) ImageView titleRightLeftImage;
    @Bind(R.id.rlroot) RelativeLayout rlRoot;

    ProgressDialog progressDialog;
    CountDownTimerButton countDownBtn;
    boolean forceBindPhone = false;
    boolean callBind=true;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);

        if( getIntent() !=null && getIntent().hasExtra("ForceBindPhone")){
            forceBindPhone = getIntent().getBooleanExtra("ForceBindPhone",false);
        }
        if(getIntent()!=null && getIntent().hasExtra("callBind")){
            callBind = getIntent().getBooleanExtra("callBind",true);
        }
        if(getIntent()!=null && getIntent().hasExtra("userid")){
            userId = getIntent().getStringExtra("userid");
        }

        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        tvGetCode.setTextColor( getResources().getColor( R.color.title_text_color ));
        btnBind.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        btnBind.setTextColor(getResources().getColor(R.color.title_text_color));
        tvTitle.setText("绑定手机");
        rlHeader.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()) );

        rlRoot.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));

        ivLeft.setBackgroundResource( R.drawable.main_title_left_back );

        titleRightImage.setVisibility(View.GONE);
        titleRightLeftImage.setVisibility(View.GONE);

        titleRightText.setVisibility( forceBindPhone? View.GONE:View.VISIBLE );
        titleRightText.setText("跳过");

        this.setImmerseLayout(rlHeader);
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.titleLeftImage, R.id.titleRightText} )
    protected void onBack(){

        if( this.getCurrentFocus() !=null ) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

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
        if(!Util.isPhone( phone )){
            edtPhone.setError("请输入正确的手机号码");
            edtPhone.setFocusable(true);
            return;
        }

        if( !callBind ){
            loginByMobileWithOauth(phone,code);
        }else {
            bindPhone( userId , phone, code);
        }
    }

    @OnClick(R.id.tvNoCode)
    protected void onSendVoiceCode() {
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
        if(!Util.isPhone( phone )){
            edtPhone.setError("请输入正确的手机号码");
            edtPhone.setFocusable(true);
            return;
        }

        getCode(true , phone);
    }

    protected void bindPhone(String userid ,  String phone ,String code ){
        String url = Constants.getINTERFACE_PREFIX() + "Account/bindMobile";
        Map<String, String> map = new HashMap<>();
        map.put("userid", userid );
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        map.put("code", code);
        map.put("pwd",  "" );
        AuthParamUtils authParamUtils = new AuthParamUtils(application,  System.currentTimeMillis() , url);
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
        if(!Util.isPhone( phone )){
            edtPhone.setError("请输入正确的手机号码");
            edtPhone.setFocusable(true);
            return;
        }

        tvGetCode.setClickable(false);
        getCode(false , phone);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvGetCode.setText("获取验证码");
        tvGetCode.setClickable(true);
        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));


    }

    @Override
    public void timeFinish(){
        if( tvGetCode==null)return;
        if(countDownBtn!=null){
            countDownBtn.Stop();
            countDownBtn=null;
        }
        tvGetCode.setText("获取验证码");
    }

    @Override
    public void timeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( tvNoCode ==null ) return;
                tvNoCode.setVisibility( View.VISIBLE );
            }
        });
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

        AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url);
        Map<String, String> params = authParamUtils.obtainParams(map);

        GsonRequest<DataBase> request = new GsonRequest<DataBase>(
                Request.Method.POST,
                url,
                DataBase.class,
                null,
                params,
                new MyGetCodeListener(this, isVoice ),
                new MyGetCodeErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
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
            countDownBtn=null;
        }
        if( progressDialog !=null ){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    static class MyGetCodeListener implements Response.Listener<DataBase> {
        WeakReference<BindPhoneActivity> ref;
        boolean isVoice=false;

        public MyGetCodeListener(BindPhoneActivity act , boolean isVoice ) {
            ref=new WeakReference<>(act);
            this.isVoice = isVoice;
        }
        @Override
        public void onResponse(DataBase dataBase) {
            if( ref.get()==null)return;
            if(ref.get().tvGetCode==null ) return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( dataBase ==null ){
                ref.get().tvGetCode.setClickable(true);
                ToastUtils.showShortToast( "获取验证码失败。");
                return;
            }
            if( dataBase.getCode() != 200) {
                ref.get().tvGetCode.setClickable(true);
                if( !TextUtils.isEmpty( dataBase.getMsg()) ){
                    ToastUtils.showShortToast( dataBase.getMsg() );
                }else {
                    ToastUtils.showShortToast( "获取验证码失败。");
                }
                return;
            }

            if(isVoice) {
                ToastUtils.showLongToast("语音验证码已经发送成功");
            }else{
                ToastUtils.showLongToast("短信验证码已经发送成功");

                if( ref.get().countDownBtn ==null ) {
                   ref.get().countDownBtn = new CountDownTimerButton( ref.get().tvGetCode, "%dS", "获取验证码", Constants.SMS_Wait_Time, ref.get() , Constants.SMS_SEND_VOICE_TIME);
                }
                ref.get().countDownBtn.start();
            }
        }
    }

    static class MyGetCodeErrorListener implements Response.ErrorListener {
        WeakReference<BindPhoneActivity> ref;
        public MyGetCodeErrorListener(BindPhoneActivity act) {
            ref = new WeakReference<>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if(ref.get().progressDialog !=null ){
                ref.get().progressDialog.dismiss();
            }

            if(ref.get().tvGetCode!=null){
                ref.get().tvGetCode.setClickable(true);
            }

            ToastUtils.showShortToast("请求服务失败");
        }
    }

    static class MyBindListener implements Response.Listener<DataBase> {
        WeakReference<BindPhoneActivity> ref;
        public MyBindListener(BindPhoneActivity act) {
            ref=new WeakReference<>(act);
        }
        @Override
        public void onResponse(DataBase dataBase) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( dataBase ==null ){
                ToastUtils.showShortToast( "绑定手机失败" );
                return;
            }
            if( dataBase.getCode() != 200) {
                String msg = "绑定手机失败";
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

            ToastUtils.showShortToast( "绑定手机成功" );
            ref.get().setResult( RESULT_OK , ref.get().getIntent() );
            ref.get().finish();
        }
    }

    static class MyBindErrorListener implements Response.ErrorListener {
        WeakReference<BindPhoneActivity> ref;
        public MyBindErrorListener(BindPhoneActivity act) {
            ref = new WeakReference<>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progressDialog != null ){
                ref.get().progressDialog.dismiss();
            }

            ToastUtils.showShortToast("绑定手机失败");
        }
    }

    /***
     *  登录
     * @param phone
     * @param code
     */
    void loginByMobileWithOauth(String phone ,String code){
        Intent intent = getIntent();
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        this.setResult( RESULT_OK , intent );
        this.finish();
    }
}
