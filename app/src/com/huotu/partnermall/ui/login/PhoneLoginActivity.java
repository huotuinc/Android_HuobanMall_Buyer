package com.huotu.partnermall.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.DataBase;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.PhoneLoginModel;
import com.huotu.partnermall.model.UpdateLeftInfoModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.CountDownTimerButton;
import com.huotu.partnermall.widgets.KJEditText;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 手机注册并登录
 */
public class PhoneLoginActivity extends BaseActivity {

    @Bind(R.id.edtPhone)
    KJEditText edtPhone;
    @Bind(R.id.edtCode)
    KJEditText edtCode;
    @Bind(R.id.tvGetCode)
    TextView tvGetCode;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.tvAuthorise)
    TextView tvAuthorise;
    ProgressDialog progressDialog;
    Long secure;
    CountDownTimerButton countDownBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);

        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        btnLogin.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        tvAuthorise.setTextColor( SystemTools.obtainColor(application.obtainMainColor()));
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.btnLogin)
    protected void onBtnLoginClick(){
        String phone = edtPhone.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        if(TextUtils.isEmpty( phone )){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtPhone , 0);
            return;
        }

        if(TextUtils.isEmpty(code)){
            edtCode.setError("请输入验证码");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode,0);
            return;
        }

        login( phone , code );
    }

    protected void login(String phone ,String code ){
        secure = System.currentTimeMillis();
        String url = Constants.getINTERFACE_PREFIX() + "Account/loginAuthorize";
        Map<String, String> map = new HashMap<>();
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        map.put("code", code);
        map.put("secure",  String.valueOf( secure ));
        AuthParamUtils authParamUtils = new AuthParamUtils(application,  secure , url, this);
        Map<String, String> params = authParamUtils.obtainParams(map);
        GsonRequest<PhoneLoginModel> request = new GsonRequest<>(
                Request.Method.POST,
                url,
                PhoneLoginModel.class,
                null,
                params,
                new MyLoginListener(this),
                new MyLoginErrorListener(this)
        );

        VolleyUtil.getRequestQueue().add(request);

        if( progressDialog ==null){
            progressDialog=new ProgressDialog( PhoneLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage("正在登录，请稍等...");
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

        getCode( phone );
        countDownBtn = new CountDownTimerButton(tvGetCode, "%dS", "获取验证码", 60000,null);
        countDownBtn.start();
    }

    protected void getCode(String phone) {

        String url = Constants.getINTERFACE_PREFIX() + "Account/sendCode";
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
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    重新授权事件
     */
    @OnClick(R.id.tvAuthorise)
    protected void onTvAuthoriseClick(){
        this.startActivity(new Intent(this,LoginActivity.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvGetCode.setText("获取验证码");
        tvGetCode.setClickable(true);
        tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if(null != countDownBtn){
            countDownBtn.Stop();
        }
    }

    protected void getLeftMenuData(){
        String url = Constants.getINTERFACE_PREFIX() + "weixin/UpdateLeftInfo";
        url +="?customerId="+application.readMerchantId();
        url +="&userId=" + application.readMemberId();
        url +="&clientUserType=" + application.readMemberType();
        AuthParamUtils authParamUtils = new AuthParamUtils(application , System.currentTimeMillis(), url , PhoneLoginActivity.this );
        url = authParamUtils.obtainUrlName();

        GsonRequest<UpdateLeftInfoModel> request = new GsonRequest<UpdateLeftInfoModel>(
                Request.Method.GET,
                url,
                UpdateLeftInfoModel.class,
                null,
                new MyGetMenuListener(this),
                new MyGetCodeErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);

        if( progressDialog ==null){
            progressDialog=new ProgressDialog( PhoneLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage("正在获取菜单信息，请稍等...");
        progressDialog.show();
    }

    static class MyGetMenuListener implements Response.Listener<UpdateLeftInfoModel>{
        WeakReference<PhoneLoginActivity> ref;
        public MyGetMenuListener(PhoneLoginActivity act) {
            ref=new WeakReference<PhoneLoginActivity>(act);
        }
        @Override
        public void onResponse(UpdateLeftInfoModel updateLeftInfoModel) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( updateLeftInfoModel ==null ){
                ToastUtils.showShortToast(ref.get(), "获取菜单信息失败。");
                return;
            }
            if( updateLeftInfoModel.getCode() != 200) {
                String  msg = "获取菜单信息失败";
                if( !TextUtils.isEmpty( updateLeftInfoModel.getMsg() )){
                    msg = updateLeftInfoModel.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg );
                return;
            }
            if( updateLeftInfoModel.getData() ==null || updateLeftInfoModel.getData().getHome_menus() ==null){
                ToastUtils.showShortToast(ref.get(),"获取菜单信息失败");
                return;
            }

            //设置侧滑栏菜单
            List<MenuBean > menus = new ArrayList< MenuBean >(  );
            MenuBean menu = null;
            List<UpdateLeftInfoModel.MenuModel> home_menus = updateLeftInfoModel.getData().getHome_menus();
            for(UpdateLeftInfoModel.MenuModel home_menu:home_menus)
            {
                menu = new MenuBean ();
                menu.setMenuGroup ( String.valueOf ( home_menu.getMenu_group () ) );
                menu.setMenuIcon ( home_menu.getMenu_icon ( ) );
                menu.setMenuName ( home_menu.getMenu_name ( ) );
                menu.setMenuUrl ( home_menu.getMenu_url ( ) );
                menus.add ( menu );
            }
            if(null != menus && !menus.isEmpty ())
            {
                ref.get().application.writeMenus(menus);
                //跳转到首页
                ActivityUtils.getInstance ().skipActivity ( ref.get(), HomeActivity.class );
            }
            else
            {
                ToastUtils.showShortToast(ref.get(),"获取菜单信息失败。");
            }
        }
    }

    static class MyGetCodeListener implements Response.Listener<DataBase> {
        WeakReference<PhoneLoginActivity> ref;
        public MyGetCodeListener(PhoneLoginActivity act) {
            ref=new WeakReference<PhoneLoginActivity>(act);
        }
        @Override
        public void onResponse(DataBase dataBase) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( dataBase ==null ){
                ToastUtils.showShortToast( ref.get() ,"获取验证码失败。" );
                return;
            }
            if( dataBase.getCode() != 200) {
                ToastUtils.showShortToast(ref.get(), "获取验证码失败。");
                return;
            }
        }
    }

    static class MyGetCodeErrorListener implements Response.ErrorListener {
        WeakReference<PhoneLoginActivity> ref;
        public MyGetCodeErrorListener(PhoneLoginActivity act) {
            ref = new WeakReference<PhoneLoginActivity>(act);
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

    static class MyLoginListener implements Response.Listener<PhoneLoginModel> {
        WeakReference<PhoneLoginActivity> ref;
        public MyLoginListener(PhoneLoginActivity act) {
            ref=new WeakReference<PhoneLoginActivity>(act);
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel) {
            if( ref.get()==null)return;

            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }

            if( phoneLoginModel ==null ){
                ToastUtils.showShortToast( ref.get() ,"登录失败。" );
                return;
            }
            if( phoneLoginModel.getCode() != 200) {
                String msg = "登录失败";
                if( !TextUtils.isEmpty( phoneLoginModel.getMsg()  )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast(ref.get(), msg );
                return;
            }

            PhoneLoginModel.PhoneModel model = phoneLoginModel.getData();

            //写入userID
            //phoneLoginModel.getData().setAccountId ( String.valueOf ( mall.getUserid ( ) ) );
            //account.setAccountName ( mall.getNickName ( ) );
            //account.setAccountIcon ( mall.getHeadImgUrl ( ) );
            //和商城用户系统交互
            ref.get().application.writeMemberInfo(
                    model.getNickName(), String.valueOf(model.getUserid()),
                    model.getHeadImgUrl(), String.valueOf( ref.get().secure )  , model.getAuthorizeCode());
            ref.get().application.writeMemberLevel(model.getLevelName());

            ref.get().application.writePhoneLogin(model.getLoginName(), model.getRealName(), model.getRelatedType(), model.getAuthorizeCode() , String.valueOf( ref.get().secure ) );
            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType( 2 );

            ref.get().getLeftMenuData();
        }
    }

    static class MyLoginErrorListener implements Response.ErrorListener {
        WeakReference<PhoneLoginActivity> ref;
        public MyLoginErrorListener(PhoneLoginActivity act) {
            ref = new WeakReference<PhoneLoginActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progressDialog != null ){
                ref.get().progressDialog.dismiss();
            }

            ToastUtils.showShortToast(ref.get(),"登录失败");
        }
    }

}
