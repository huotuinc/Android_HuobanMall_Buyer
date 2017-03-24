package com.huotu.partnermall.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.huotu.android.library.libedittext.EditText;
import com.huotu.android.library.libpush.PushHelper;
import com.huotu.android.library.libpush.SetAliasEvent;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.AuthMallModel;
import com.huotu.partnermall.model.DataBase;
import com.huotu.partnermall.model.GoIndexEvent;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.PhoneLoginModel;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
import com.huotu.partnermall.model.RefreshMenuEvent;
import com.huotu.partnermall.model.UpdateLeftInfoModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.widgets.CountDownTimerButton;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static android.R.attr.data;
import static com.huotu.partnermall.ui.HomeActivity.BINDPHONE_REQUESTCODE;

/*
 * 手机注册并登录
 */
public class PhoneLoginActivity extends BaseActivity implements Handler.Callback , CountDownTimerButton.CountDownFinishListener{
    @Bind(R.id.edtPhone) EditText edtPhone;
    @Bind(R.id.edtCode) EditText edtCode;
    @Bind(R.id.tvGetCode) TextView tvGetCode;
    @Bind(R.id.btnLogin) Button btnLogin;
    @Bind(R.id.llWechat) LinearLayout llWechat;
    @Bind(R.id.titleLeftImage) ImageView ivLeft;
    @Bind(R.id.titleText) TextView tvTitle;
    @Bind(R.id.activity_phone_header) RelativeLayout rlHeader;
    @Bind(R.id.PhoneLoginActivity_phone_weixin) RelativeLayout rlPhoneWeixin;
    @Bind(R.id.PhoneLoginActivity_weixin)  LinearLayout rlWeixin;
    @Bind(R.id.llSpace)  FrameLayout llSpace;
    @Bind(R.id.tvNoCode) TextView tvNoCode;
    @Bind(R.id.rlLogin) RelativeLayout rlLogin;
    @Bind(R.id.rl_phone_login_root) RelativeLayout rlRoot;
    public static int WEIXIN_BINDPHONE_REQUESTCODE=1100;

    ProgressPopupWindow progressPopupWindow;
    Long secure;
    CountDownTimerButton countDownBtn;
    ShareSDKLogin shareSDKLogin;
    Handler handler;
    Bundle bundlePush;
    String redirectUrl;
    int loginMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);

        initPush();

        initView();

        handler = new Handler(this);
        shareSDKLogin = new ShareSDKLogin(handler);

    }

    /**
     * 检测是否安装微信客户端
     */
    protected void checkWechatClient(){
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        if( null == platform || !platform.isClientValid()){
            llWechat.setVisibility(View.GONE);
        }else{
            llWechat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {
        loginMethod = BaseApplication.single.readLoginMethod();
        if( loginMethod == 0 ){
            rlPhoneWeixin.setVisibility(View.VISIBLE);
            llSpace.setVisibility(View.VISIBLE);
            llWechat.setVisibility(View.VISIBLE);
            rlWeixin.setVisibility(View.GONE);
        }else if( loginMethod==1){
            rlPhoneWeixin.setVisibility(View.VISIBLE);
            llSpace.setVisibility(View.GONE);
            llWechat.setVisibility(View.GONE);
            rlWeixin.setVisibility(View.GONE);
        }else if(loginMethod==2){
            rlPhoneWeixin.setVisibility(View.GONE);
            llSpace.setVisibility(View.GONE);
            llWechat.setVisibility(View.GONE);
            rlWeixin.setVisibility(View.VISIBLE);
        }else if( loginMethod == 3){
//            rlPhoneWeixin.setVisibility(View.GONE);
//            llSpace.setVisibility(View.GONE);
//            llWechat.setVisibility(View.GONE);
//            rlWeixin.setVisibility(View.VISIBLE);

            rlPhoneWeixin.setVisibility(View.VISIBLE);
            llSpace.setVisibility(View.VISIBLE);
            llWechat.setVisibility(View.VISIBLE);
            rlWeixin.setVisibility(View.GONE);
        }

        //tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        SystemTools.setWindowsStyle(tvGetCode , 10f, 8 , SystemTools.obtainColor( BaseApplication.single.obtainMainColor() ));
        //btnLogin.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        SystemTools.setWindowsStyle(rlLogin, 15f, 0 , getResources().getColor( R.color.lightgray));
        SystemTools.setWindowsStyle(btnLogin , 15f , 8 , SystemTools.obtainColor( BaseApplication.single.obtainMainColor() ));
        //tvAuthorise.setTextColor( SystemTools.obtainColor(application.obtainMainColor()));
        tvTitle.setText("登录");
        rlHeader.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()) );
        ivLeft.setBackgroundResource( R.drawable.main_title_left_back );

        //checkWechatClient();
        rlRoot.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        setImmerseLayout(rlHeader);

       redirectUrl =  getIntent().getExtras() ==null? "" : getIntent().getExtras().getString("redirecturl");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initPush();

        redirectUrl = intent.getExtras() ==null? "" : intent.getExtras().getString("redirecturl");
    }

    protected void initPush(){
        //获得推送信息
        if(null!= getIntent() && getIntent().hasExtra(Constants.PUSH_KEY)){
            bundlePush = getIntent().getBundleExtra(Constants.PUSH_KEY);
        }
    }

    @OnClick(R.id.titleLeftImage)
    protected void onBack(){

        EventBus.getDefault().post(new GoIndexEvent());

        this.finish();
    }

    /***
     * 手机登录
     */
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

        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(loginMethod == 3 ){
            loginWithWechatPhone( phone , code , null );
        }else {
            login(phone, code);
        }
    }

    /***
     *
     * @param phone
     * @param code
     */
    protected void login(String phone ,String code ){
        secure = System.currentTimeMillis();
        String url = Constants.getINTERFACE_PREFIX() + "Account/loginAuthorize";
        Map<String, String> map = new HashMap<>();
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        map.put("code", code);
        map.put("secure",  String.valueOf( secure ));
        AuthParamUtils authParamUtils = new AuthParamUtils(application,  secure , url  );
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

        if( progressPopupWindow ==null){
            progressPopupWindow=new ProgressPopupWindow( PhoneLoginActivity.this);
        }
        progressPopupWindow.showProgress("正在登录，请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER , 0 , 0);
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

        getCode(false , phone);
    }

    @OnClick(R.id.tvNoCode)
    protected void onSendVoiceCode(){
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
        if(! Util.isPhone( phone )){
            edtPhone.setError("请输入正确的手机号码");
            edtPhone.setFocusable(true);
            return;
        }

        getCode(true , phone);
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

    protected void getCode(final boolean isVoice , String phone) {
        String url ;
        if( isVoice) {
            url = Constants.getINTERFACE_PREFIX() + "Account/sendVoiceCode";
        }else{
            url = Constants.getINTERFACE_PREFIX() + "Account/sendCode";
        }

        Map<String, String> map = new HashMap<>();
        map.put("customerid", application.readMerchantId());
        map.put("mobile", phone);
        AuthParamUtils authParamUtils = new AuthParamUtils( url);
        Map<String, String> params = authParamUtils.obtainParams(map);

        GsonRequest<DataBase> request = new GsonRequest<DataBase>(Request.Method.POST,
                url, DataBase.class, null, params, new Response.Listener<DataBase>() {
            @Override
            public void onResponse(DataBase dataBase) {
                if (progressPopupWindow != null) {
                    progressPopupWindow.dismissView();
                }
                if(tvGetCode==null ) return;

                if (dataBase == null || dataBase.getCode() != 200 ) {

                    if( dataBase!=null && !TextUtils.isEmpty(dataBase.getMsg()) ){
                        ToastUtils.showShortToast( dataBase.getMsg() );
                    }else {
                        ToastUtils.showShortToast("获取验证码失败。");
                    }
                    return;
                }

                if(  isVoice) {
                    ToastUtils.showLongToast("语音验证码已经发送成功");
                }else{

                    if( countDownBtn==null) {
                        countDownBtn = new CountDownTimerButton(tvGetCode, "%dS", "获取验证码", Constants.SMS_Wait_Time, PhoneLoginActivity.this , Constants.SMS_SEND_VOICE_TIME);
                    }
                    countDownBtn.start();

                    ToastUtils.showLongToast("短信验证码已经发送成功");
                }

                if(edtCode!=null) {
                    edtCode.requestFocus();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressPopupWindow != null) {
                    progressPopupWindow.dismissView();
                }
                ToastUtils.showShortToast("请求失败");
            }
        });

        VolleyUtil.getRequestQueue().add( request );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            EventBus.getDefault().post(new GoIndexEvent());

            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /***
     * 微信授权登录
     */
    @OnClick({R.id.llWechat , R.id.PhoneLoginActivity_weixin_login })
    protected void onWechat(){
        if( null == progressPopupWindow){
            progressPopupWindow = new ProgressPopupWindow(this);
        }
        progressPopupWindow.showProgress("正在调用微信登录,请稍等...");

        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(),Gravity.CENTER , 0,0 );

        llWechat.setEnabled(false);
        rlPhoneWeixin.setEnabled(false);

        Platform platform = ShareSDK.getPlatform( Wechat.NAME);
        shareSDKLogin.authorize(platform);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvGetCode.setText("获取验证码");

        tvGetCode.setClickable(true);
        //tvGetCode.setBackgroundColor(SystemTools.obtainColor(application.obtainMainColor()));
        SystemTools.setWindowsStyle(tvGetCode, 10f , 8 , SystemTools.obtainColor(application.obtainMainColor()) );

        llWechat.setEnabled(true);
        rlPhoneWeixin.setEnabled(true);

        if( progressPopupWindow!=null){
            progressPopupWindow.dismissView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if (null != countDownBtn) {
            countDownBtn.Stop();
        }
        if (null != progressPopupWindow) {
            progressPopupWindow.dismissView();
            progressPopupWindow = null;
        }

        handler.removeCallbacksAndMessages(null);
    }

    /***
     * 获得左侧菜单数据
     */
    protected void getLeftMenuData(){
        String url = Constants.getINTERFACE_PREFIX() + "/weixin/UpdateLeftInfo";
        String customerId = BaseApplication.single.readMerchantId();
        String userId= BaseApplication.single.readMemberId();
        String userType = String.valueOf( BaseApplication.single.readMemberType());
        url +="?customerId="+customerId+"&userId="+userId +"&userType="+userType;

        AuthParamUtils authParamUtils = new AuthParamUtils(BaseApplication.single ,System.currentTimeMillis() , url );
        url = authParamUtils.obtainUrl();
        GsonRequest<UpdateLeftInfoModel> request = new GsonRequest<UpdateLeftInfoModel>(Request.Method.GET
                , url, UpdateLeftInfoModel.class, null, new Response.Listener<UpdateLeftInfoModel>() {
            @Override
            public void onResponse(UpdateLeftInfoModel updateLeftInfoModel) {
                getLeftMenu( updateLeftInfoModel );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressPopupWindow!=null) progressPopupWindow.dismissView();
            }
        });

        VolleyUtil.getRequestQueue().add(request);

        if( progressPopupWindow ==null){
            progressPopupWindow=new ProgressPopupWindow( PhoneLoginActivity.this);
        }
        progressPopupWindow.showProgress("正在获取菜单信息,请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(),Gravity.CENTER,0,0);




//        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
//        String version = BaseApplication.getAppVersion();
//        String operation = Constants.OPERATION_CODE;
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String appId="";
//        String customerId = BaseApplication.single.readMerchantId();
//        String userId= BaseApplication.single.readMemberId();
//        String userType = String.valueOf( BaseApplication.single.readMemberType());

//        try {
//            appId = URLEncoder.encode(Constants.getAPP_ID(), "UTF-8");
//        }catch (UnsupportedEncodingException ex){}

//        Map<String,String> map = new HashMap<>();
//        map.put("version", version);
//        map.put("operation",operation);
//        map.put("timestamp",timestamp);
//        map.put("appid",appId);
//        map.put("customerid",customerId);
//        map.put("userId", userId );
//        map.put("clientUserType", userType );

//        String sign = BuyerSignUtil.getSign(map);
//
//        Call<UpdateLeftInfoModel> call=apiService.updateLeftInfo(version,operation,timestamp,appId,sign,customerId, userId , userType);
//        call.enqueue(new Callback<UpdateLeftInfoModel>() {
//            @Override
//            public void onResponse(Call<UpdateLeftInfoModel> call, retrofit2.Response<UpdateLeftInfoModel> response) {
//                if (progressPopupWindow != null) {
//                    progressPopupWindow.dismissView();
//                }
//                if (response.body() == null) {
//                    ToastUtils.showShortToast( "获取菜单信息失败。");
//                    return;
//                }
//                if (response.body().getCode() != 200) {
//                    String msg = "获取菜单信息失败";
//                    if (!TextUtils.isEmpty(response.body().getMsg())) {
//                        msg = response.body().getMsg();
//                    }
//                    ToastUtils.showShortToast( msg);
//                    return;
//                }
//                if (response.body().getData() == null || response.body().getData().getHome_menus() == null) {
//                    ToastUtils.showShortToast( "获取菜单信息失败");
//                    return;
//                }
//
//                //设置侧滑栏菜单
//                List<MenuBean> menus = new ArrayList<>();
//                MenuBean menu;
//                List<UpdateLeftInfoModel.MenuModel> home_menus = response.body().getData().getHome_menus();
//                for (UpdateLeftInfoModel.MenuModel home_menu : home_menus) {
//                    menu = new MenuBean();
//                    menu.setMenuGroup(String.valueOf(home_menu.getMenu_group()));
//                    menu.setMenuIcon(home_menu.getMenu_icon());
//                    menu.setMenuName(home_menu.getMenu_name());
//                    menu.setMenuUrl(home_menu.getMenu_url());
//                    menus.add(menu);
//                }
//                if (null != menus && !menus.isEmpty()) {
//                    BaseApplication.single.writeMenus(menus);
//                    //绑定极光推送别名
//                    //UIUtils.bindPush();
//
//                    EventBus.getDefault().post(new RefreshMenuEvent());
//                    //跳转到首页
//                    //ActivityUtils.getInstance ().skipActivity ( ref.get(), HomeActivity.class );
//                } else {
//                    ToastUtils.showShortToast( "获取菜单信息失败。");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UpdateLeftInfoModel> call, Throwable t) {
//                if(progressPopupWindow!=null) progressPopupWindow.dismissView();
//            }
//        });
//
//        if( progressPopupWindow ==null){
//            progressPopupWindow=new ProgressPopupWindow( PhoneLoginActivity.this);
//        }
//        progressPopupWindow.showProgress("正在获取菜单信息,请稍等...");
//        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(),Gravity.CENTER,0,0);
    }

    protected void getLeftMenu(UpdateLeftInfoModel model ){
        if (progressPopupWindow != null) {
            progressPopupWindow.dismissView();
        }
        if (model == null) {
            ToastUtils.showShortToast( "获取菜单信息失败。");
            return;
        }
        if (model.getCode() != 200) {
            String msg = "获取菜单信息失败";
            if (!TextUtils.isEmpty(model.getMsg())) {
                msg = model.getMsg();
            }
            ToastUtils.showShortToast( msg);
            return;
        }
        if (model.getData() == null || model.getData().getHome_menus() == null) {
            ToastUtils.showShortToast( "获取菜单信息失败");
            return;
        }

        //设置侧滑栏菜单
        List<MenuBean> menus = new ArrayList<>();
        MenuBean menu;
        List<UpdateLeftInfoModel.MenuModel> home_menus = model.getData().getHome_menus();
        for (UpdateLeftInfoModel.MenuModel home_menu : home_menus) {
            menu = new MenuBean();
            menu.setMenuGroup(String.valueOf(home_menu.getMenu_group()));
            menu.setMenuIcon(home_menu.getMenu_icon());
            menu.setMenuName(home_menu.getMenu_name());
            menu.setMenuUrl(home_menu.getMenu_url());
            menus.add(menu);
        }
        if (null != menus && !menus.isEmpty()) {
            BaseApplication.single.writeMenus(menus);
            //绑定极光推送别名
            //UIUtils.bindPush();

            EventBus.getDefault().post(new RefreshMenuEvent());
            //跳转到首页
            //ActivityUtils.getInstance ().skipActivity ( ref.get(), HomeActivity.class );
        } else {
            ToastUtils.showShortToast( "获取菜单信息失败。");
        }
    }

    static class MyLoginListener implements Response.Listener<PhoneLoginModel> {
        WeakReference<PhoneLoginActivity> ref;
        public MyLoginListener(PhoneLoginActivity act ) {
            ref=new WeakReference<>(act);
        }
        @Override
        public void onResponse(PhoneLoginModel phoneLoginModel) {
            if( ref.get()==null)return;

            if( ref.get().progressPopupWindow !=null){
                ref.get().progressPopupWindow.dismissView();
            }

            if( phoneLoginModel ==null ){
                ToastUtils.showShortToast( "登录失败。" );
                return;
            }

            if( phoneLoginModel.getCode() != 200) {
                String msg = "登录失败";
                if( !TextUtils.isEmpty( phoneLoginModel.getMsg()  )){
                    msg = phoneLoginModel.getMsg();
                }
                ToastUtils.showShortToast( msg );
                return;
            }

            PhoneLoginModel.PhoneModel model = phoneLoginModel.getData();

            //写入userID
            //和商城用户系统交互
            ref.get().application.writeMemberInfo(
                    model.getNickName(), String.valueOf(model.getUserid()),
                    model.getHeadImgUrl(), String.valueOf( ref.get().secure )  , model.getAuthorizeCode() , model.getOpenId() );
            ref.get().application.writeMemberLevel(model.getLevelName());

            BaseApplication.single.writeMemberLevelId(model.getLevelId());

            //设置 用户的登级Id
            //Jlibrary.initUserLevelId( model.getLevelId() );

            ref.get().application.writePhoneLogin(model.getLoginName(), model.getRealName(), model.getRelatedType(), model.getAuthorizeCode() , String.valueOf( ref.get().secure ) );
            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType( 2 );

            ref.get().getLeftMenuData();

            ref.get().bindPush();


            Bundle bd = new Bundle();
            //String url = PreferenceHelper.readString(BaseApplication.single, NativeConstants.UI_CONFIG_FILE, NativeConstants.UI_CONFIG_SELF_HREF);
            //bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, url);
            //bd.putBoolean(NativeConstants.KEY_ISMAINUI, true);
            Intent intent = new Intent();
            intent.setClass(ref.get(), HomeActivity.class);
            intent.putExtras(bd);
            //传递推送信息
            if(null !=ref.get().bundlePush){
                intent.putExtra( Constants.PUSH_KEY, ref.get().bundlePush );
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            EventBus.getDefault().post(new RefreshHttpHeaderEvent());

            intent.putExtra("redirecturl", ref.get().redirectUrl );

            ActivityUtils.getInstance().skipActivity(ref.get(), intent );

        }
    }

    static class MyLoginErrorListener implements Response.ErrorListener {
        WeakReference<PhoneLoginActivity> ref;
        public MyLoginErrorListener(PhoneLoginActivity act) {
            ref = new WeakReference<>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null ) return;

            if( ref.get().progressPopupWindow != null ){
                ref.get().progressPopupWindow.dismissView();
            }

            ToastUtils.showShortToast("登录失败");
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(llWechat==null || rlPhoneWeixin==null ) return false;

        if(progressPopupWindow !=null){
            progressPopupWindow.dismissView();
        }
        llWechat.setEnabled(true);
        rlPhoneWeixin.setEnabled(true);

        if( msg.what == ShareSDKLogin.MSG_AUTH_COMPLETE ){
            AccountModel accountModel = (AccountModel) msg.obj;
            if(loginMethod == 3){//
                // gotoBindPhone(accountModel);
                OauthByWeixin(accountModel);
            }else {
                wechatToMall(accountModel);
            }
        }else if( msg.what == ShareSDKLogin.MSG_AUTH_CANCEL){
        }else if( msg.what == ShareSDKLogin.MSG_AUTH_ERROR){
            String error = "授权失败";
            ToastUtils.showLongToast( error );
        }else if(msg.what== Constants.LOGIN_AUTH_SUCCESS){

            AuthMallModel.AuthMall data = (AuthMallModel.AuthMall) msg.obj;
            if( !data.isMobileBind()){
                bindPhone( data.getUserid() , false);
            }else{
                goToHome();
            }
        }else if(msg.what==Constants.LOGIN_AUTH_ERROR){
            String error = msg.obj.toString();
            ToastUtils.showLongToast(error);
        }
        return false;
    }

    protected void getinfo( String accountToken , String unionid , AuthMallModel model ){
        if (model == null || model.getCode() != 200 || model.getData() == null ) {
            Message msg = handler.obtainMessage(Constants.LOGIN_AUTH_ERROR);
            msg.obj = "请求失败,请重试!";
            msg.sendToTarget();
            return;
        }

        AuthMallModel.AuthMall mall = model.getData();
        //和商城用户系统交互
        application.writeMemberInfo(mall.getNickName(), String.valueOf(mall.getUserid()),
                mall.getHeadImgUrl(),  accountToken , unionid , mall.getOpenId() );
        application.writeMemberLevel(mall.getLevelName());
        //
        application.writeMemberLevelId(mall.getLevelId());
        //设置 用户的登级Id
        //Jlibrary.initUserLevelId( mall.getLevelId() );

        //记录登录类型(1:微信登录，2：手机登录)
        application.writeMemberLoginType(1);
        //记录微信关联类型（0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号）
        application.writeMemberRelatedType(mall.getRelatedType());

        //设置侧滑栏菜单
        List<MenuBean> menus = new ArrayList<>();
        MenuBean menu;
        List<AuthMallModel.MenuModel> home_menus = mall.getHome_menus();
        if( home_menus !=null) {
            for (AuthMallModel.MenuModel home_menu : home_menus) {
                menu = new MenuBean();
                menu.setMenuGroup(String.valueOf(home_menu.getMenu_group()));
                menu.setMenuIcon(home_menu.getMenu_icon());
                menu.setMenuName(home_menu.getMenu_name());
                menu.setMenuUrl(home_menu.getMenu_url());
                menus.add(menu);
            }
            application.writeMenus(menus);
        }

        bindPush();

        Message msg = handler.obtainMessage(Constants.LOGIN_AUTH_SUCCESS);
        msg.obj = mall;
        msg.sendToTarget();
    }

    /**
     *
     * @param account
     */
    protected void wechatToMall(final AccountModel account ) {
        if (progressPopupWindow == null)  progressPopupWindow = new ProgressPopupWindow(PhoneLoginActivity.this);
        progressPopupWindow.showProgress("正在登录，请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

       String url = Constants.getINTERFACE_PREFIX();
        if( !url.endsWith("/") ) url +="/";
        url +="weixin/LoginAuthorize";

        AuthParamUtils paramUtils = new AuthParamUtils( "");
        Map param = paramUtils.obtainParams(account);

        GsonRequest<AuthMallModel> gsonRequest = new GsonRequest<AuthMallModel>(
                Request.Method.POST, url, AuthMallModel.class, null, param,
                new Response.Listener<AuthMallModel>() {
                    @Override
                    public void onResponse(AuthMallModel authMallModel) {
                        getinfo( account.getAccountToken() , account.getAccountUnionId() , authMallModel);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Message msg = new Message();
                        msg.what = Constants.LOGIN_AUTH_ERROR;
                        msg.obj = "调用授权接口失败，请确认！";
                        handler.sendMessage(msg);
                    }
                }
        );

        VolleyUtil.getRequestQueue().add( gsonRequest );
    }

    /***
     * 绑定推送信息
     */
    protected void bindPush(){
        String userId = BaseApplication.single.readUserId();
        String userKey = Constants.getSMART_KEY();
        String userRandom = String.valueOf(System.currentTimeMillis());
        String userSecure = Constants.getSMART_SECURITY();
        String userSign = SignUtil.getSecure( userKey , userSecure , userRandom);
        String alias = BaseApplication.getPhoneIMEI();

        PushHelper.bindingUserId( userId ,alias, userKey,userRandom,userSign );

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetAliase(SetAliasEvent event){
        int code = event.getCode();
    }

    /***
     * 微信登录成功以后，根据返回的参数IsMobileBind,显示绑定手机界面
     */
    protected void bindPhone( int userid , boolean forceBindPhone ){
        EventBus.getDefault().post(new RefreshHttpHeaderEvent());

        Intent intent = new Intent(PhoneLoginActivity.this, BindPhoneActivity.class);

        intent.putExtra("redirecturl", redirectUrl);
        intent.putExtra("ForceBindPhone", forceBindPhone );
        intent.putExtra("callBind",true);
        intent.putExtra("userid", String.valueOf( userid));

        startActivityForResult ( intent , BINDPHONE_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == BINDPHONE_REQUESTCODE && resultCode == RESULT_OK){
            if(loginMethod==3){

                String phone = data.getStringExtra("phone");
                String code = data.getStringExtra("code");
                AccountModel accountModel = (AccountModel) data.getSerializableExtra("wechatData");

                loginWithWechatPhone(  phone , code , accountModel );
            }else {
                goToHome();
            }
        }else if( requestCode == BINDPHONE_REQUESTCODE && resultCode != RESULT_OK){
            goToHome();
        }else if( requestCode == WEIXIN_BINDPHONE_REQUESTCODE && resultCode== RESULT_OK){
            AccountModel accountModel = (AccountModel) data.getSerializableExtra("wechatData");
            AuthMallModel authMallModel = (AuthMallModel) data.getSerializableExtra("userInfo");
            authMallModel.getData().setMobileBind(true);
            getinfo( accountModel.getAccountToken(), accountModel.getAccountUnionId() ,  authMallModel);
        }else if( requestCode==WEIXIN_BINDPHONE_REQUESTCODE && resultCode != RESULT_OK){
            goToHome();
        }
    }

    protected void goToHome(){
        //跳转到首页
        Bundle bd = new Bundle();
        Intent intent = new Intent();
        intent.setClass( this , HomeActivity.class);
        intent.putExtras(bd);
        if(null != bundlePush){
            intent.putExtra( Constants.PUSH_KEY, bundlePush );
        }

        //设置调转页面
        intent.putExtra("redirecturl", redirectUrl );

        ActivityUtils.getInstance().skipActivity( this , intent );

        EventBus.getDefault().post(new RefreshHttpHeaderEvent());

    }

    /**
     *
     * 微信授权成功以后，跳转到绑定手机界面
     * @param accountModel
     */
    protected void gotoBindPhone(AccountModel accountModel){
        Intent intent = new Intent(PhoneLoginActivity.this, BindPhoneActivity.class);
        intent.putExtra("redirecturl", redirectUrl);
        intent.putExtra("ForceBindPhone",true);
        intent.putExtra("wechatData",accountModel);
        intent.putExtra("callBind",false);
        startActivityForResult ( intent , BINDPHONE_REQUESTCODE);
    }

    /***
     *
     */
    void loginWithWechatPhone( String phone , String code , AccountModel accountModel ) {

        String url = Constants.getINTERFACE_PREFIX();
        if (!url.endsWith("/")) url += "/";
        url += "account/loginorregisterbymobilewithoauth";

        AuthParamUtils authParamUtils = new AuthParamUtils("");
        Map param = new HashMap();
        param.put("customerid", BaseApplication.single.readMerchantId());
        param.put("mobile", phone);
        param.put("code", code);
        param.put("secure", String.valueOf(System.currentTimeMillis()));
        param.put("sex", accountModel ==null ? "" : String.valueOf(accountModel.getSex()));
        param.put("nickname", accountModel==null ? "" :  accountModel.getNickname());
        param.put("openid", accountModel==null ? "" :  accountModel.getOpenid());
        param.put("city", accountModel==null ? "" : accountModel.getCity());
        param.put("country", accountModel==null ? "" :  accountModel.getCountry());
        param.put("province", accountModel==null ? "" :  accountModel.getProvince());
        param.put("headimgurl",accountModel==null ? "" :   accountModel.getAccountIcon());
        param.put("unionid", accountModel==null ? "" : accountModel.getAccountUnionId());
        param = authParamUtils.obtainParams(param);

        GsonRequest<PhoneLoginModel> gsonRequest =
                new GsonRequest<>(Request.Method.POST, url, PhoneLoginModel.class, null, param,
                        new MyLoginListener(this),
                        new MyLoginErrorListener(this));

        VolleyUtil.getRequestQueue().add(gsonRequest);

        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow(PhoneLoginActivity.this);
        }
        progressPopupWindow.showProgress("正在登录，请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /***
     * 手机为主授权为辅模式下，微信授权完成后，检查是否存在用户
     */
    void OauthByWeixin( AccountModel accountModel ){
        String url = Constants.getINTERFACE_PREFIX();
        if (!url.endsWith("/")) url += "/";
        url += "account/oauthbyweixin";

        AuthParamUtils authParamUtils = new AuthParamUtils("");
        Map param = new HashMap();
        param.put("customerid", BaseApplication.single.readMerchantId());
        param.put("unionid", accountModel.getAccountUnionId());
        param = authParamUtils.obtainParams(param);

        GsonRequest<AuthMallModel> gsonRequest =
                new GsonRequest<>(Request.Method.POST, url,
                        AuthMallModel.class, null, param,
                        new MyOauthLoginListener (this , accountModel ),
                        new MyLoginErrorListener(this));

        VolleyUtil.getRequestQueue().add(gsonRequest);

        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow(PhoneLoginActivity.this);
        }
        progressPopupWindow.showProgress("正在登录，请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    void dealLoginUserInfo( AccountModel accountModel ,  AuthMallModel authMall){
        if(  !authMall.getData().isMobileBind()){

            Intent intent = new Intent(PhoneLoginActivity.this, BindPhoneActivity.class);
            intent.putExtra("redirecturl", redirectUrl);
            intent.putExtra("ForceBindPhone", true );
            intent.putExtra("callBind",true);
            intent.putExtra("userInfo", authMall);
            intent.putExtra("wechatData", accountModel);
            intent.putExtra("userid", String.valueOf( authMall.getData().getUserid()));
            startActivityForResult ( intent , WEIXIN_BINDPHONE_REQUESTCODE);

        }else{
            getinfo( accountModel.getAccountToken() , accountModel.getAccountUnionId() , authMall );
        }
    }

    static class MyOauthLoginListener implements Response.Listener<AuthMallModel>{
        WeakReference<PhoneLoginActivity> ref;
        AccountModel account;
        public MyOauthLoginListener(PhoneLoginActivity act , AccountModel account ){
            ref = new WeakReference<>(act);
            this.account = account;
        }

        @Override
        public void onResponse(AuthMallModel authMallModel) {
            if( ref.get()==null)return;

            if( ref.get().progressPopupWindow !=null){
                ref.get().progressPopupWindow.dismissView();
            }

            if( authMallModel ==null ){
                ToastUtils.showShortToast( "登录失败。" );
                return;
            }

            if( authMallModel.getCode() == 605){
                //ToastUtils.showLongToast(authMallModel.getMsg());
                ref.get().gotoBindPhone( account );
                return;
            }


            if (authMallModel == null || authMallModel.getCode() != 200 || authMallModel.getData() == null ) {
                Message msg = ref.get().handler.obtainMessage(Constants.LOGIN_AUTH_ERROR);
                msg.obj = "请求失败,请重试!";
                msg.sendToTarget();
                return;
            }

            //ref.get().getinfo(  account.getAccountToken() , account.getAccountUnionId() ,  authMallModel );
            ref.get().dealLoginUserInfo( account , authMallModel );

        }
    }
}
