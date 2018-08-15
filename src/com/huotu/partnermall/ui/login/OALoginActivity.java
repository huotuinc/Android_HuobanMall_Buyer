package com.huotu.partnermall.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.TextureView;
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
import com.huotu.android.library.libpush.PushHelper;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.DataBase;
import com.huotu.partnermall.model.GoIndexEvent;
import com.huotu.partnermall.model.MenuBean;
import com.huotu.partnermall.model.OALoginModel;
import com.huotu.partnermall.model.PhoneLoginModel;
import com.huotu.partnermall.model.RefreshHttpHeaderEvent;
import com.huotu.partnermall.model.RefreshMenuEvent;
import com.huotu.partnermall.model.UpdateLeftInfoModel;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.WebViewActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.EncryptUtil;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 云品星球app的
 *  oa登录界面
 *  http://mallapi.championstar.cn/User/OAlogin
 */
public class OALoginActivity extends BaseActivity implements Handler.Callback {
    @BindView(R.id.edtPhoneOrUserName)
    EditText etPhoneOrUser;
    @BindView(R.id.editPassword)
    com.huotu.android.library.libedittext.EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnOALogin)
    Button btnOALogin;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvForgetPassword)
    TextView tvForget;
    @BindView(R.id.titleLeftImage)
    ImageView ivLeft;
    @BindView(R.id.activity_oalogin_header)
    RelativeLayout rlHeader;
    @BindView(R.id.titleText)
    TextView tvTitle;
    @BindView(R.id.layRoot)
    RelativeLayout layRoot;

    Bundle bundlePush;
    String redirectUrl;
    Handler handler;
    ProgressPopupWindow progressPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oalogin);
        unbinder=ButterKnife.bind(this);

        initPush();
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initPush();
        redirectUrl = intent.getExtras() == null ? "" : intent.getExtras().getString("redirecturl");
    }

    @Override
    protected void initView() {
        handler = new Handler(this);

        layRoot.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        ivLeft.setBackgroundResource(R.drawable.main_title_left_back);

        tvTitle.setText("用户登录");

        String isGradient = getString(R.string.header_gradient);
        if (!Boolean.parseBoolean(isGradient)) {
            rlHeader.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        }

        SystemTools.setWindowsStyle(btnLogin, 15f, 8, SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));

        setImmerseLayout(rlHeader);

        redirectUrl = getIntent().getExtras() == null ? "" : getIntent().getExtras().getString("redirecturl");
    }


    protected void initPush() {
        //获得推送信息
        if (null != getIntent() && getIntent().hasExtra(Constants.PUSH_KEY)) {
            bundlePush = getIntent().getBundleExtra(Constants.PUSH_KEY);
        }
    }

    @OnClick(R.id.titleLeftImage)
    protected void onBack() {
        EventBus.getDefault().post(new GoIndexEvent());
        this.finish();
    }

    /***
     * 手机号或者用户名登录
     */
    @OnClick(R.id.btnLogin)
    protected void onBtnLoginClick() {
        String user = etPhoneOrUser.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            etPhoneOrUser.setError("请输入手机号或登录名");
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etPhoneOrUser, 0);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            etPassword.setError("请输入密码");
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etPassword, 0);
            return;
        }
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        loginOA(user, pwd);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (progressPopupWindow != null) {
            progressPopupWindow.dismissView();
        }

        if (TextUtils.isEmpty(etPhoneOrUser.getText().toString())) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etPhoneOrUser, 0);
        } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etPassword, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);

        if (null != progressPopupWindow) {
            progressPopupWindow.dismissView();
            progressPopupWindow = null;
        }

        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            EventBus.getDefault().post(new GoIndexEvent());
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.tvRegister)
    public void register(View v) {
        String customerId = application.readMerchantId();
        String urlStr = "UserCenter/OAzc.aspx?customerid=" + customerId + "&gduid=";
        gotoWeb(urlStr);

    }

    @OnClick(R.id.tvForgetPassword)
    public void forgetPassword(View v) {
        String customerId = application.readMerchantId();
        String urlStr = "UserCenter/OAlogin-w.aspx?customerid=" + customerId;
        gotoWeb(urlStr);
    }

    @OnClick(R.id.btnOALogin)
    public void oaLogin(View v) {
        String url = "UserCenter/OAlogin-s.aspx?customerid=" + application.readMerchantId();
        gotoWeb(url);
    }

    protected void gotoWeb(String url_suffer) {
        String domain = application.obtainMerchantUrl();
        domain = domain.endsWith("/") ? domain : domain + "/";
        String urlStr = domain + url_suffer;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_URL, urlStr);
        ActivityUtils.getInstance().showActivity(this, WebViewActivity.class, bundle);
    }

    protected void loginOA(String loginName, String password) {
        String url = Constants.getINTERFACE_PREFIX();

        String md5Pwd = EncryptUtil.getInstance().encryptMd532(password);

        //url ="http://mallapi.championstar.cn";

        url += url.endsWith("/") ? "Account/OAlogin" : "/Account/OAlogin";
        Map<String, String> map = new HashMap<>();
        map.put("customerid", application.readMerchantId());
        map.put("loginName", loginName);
        map.put("Password", md5Pwd);
        map.put("secure" , String.valueOf( System.currentTimeMillis()));

        AuthParamUtils authParamUtils = new AuthParamUtils(url);
        Map<String, String> params = authParamUtils.obtainParams(map);

        GsonRequest<OALoginModel> request = new GsonRequest<>(
                Request.Method.POST,
                url,
                OALoginModel.class,
                null,
                params,
                new OALoginListener(this),
                new OALoginErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);
        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow(this);
        }
        progressPopupWindow.showProgress("正在登录，请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    static class OALoginListener implements Response.Listener<OALoginModel> {
        WeakReference<OALoginActivity> ref;

        public OALoginListener(OALoginActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(OALoginModel oaLoginModel) {
            if (ref.get() == null) return;

            if (ref.get().progressPopupWindow != null) {
                ref.get().progressPopupWindow.dismissView();
            }

            if (oaLoginModel == null) {
                ToastUtils.showShortToast("登录失败。");
                return;
            }

            if (oaLoginModel.getCode() != 200) {
                String msg = "登录失败";
                if (!TextUtils.isEmpty(oaLoginModel.getMsg())) {
                    msg = oaLoginModel.getMsg();
                }
                ToastUtils.showShortToast(msg);
                return;
            }

            OALoginModel.OALogin model = oaLoginModel.getData();

            //写入userID
            //和商城用户系统交互
            ref.get().application.writeMemberInfo(
                    model.getUsername(), String.valueOf(model.getUserid()),
                    model.getWxHeadImg(),  model.getAuthorizeCode() , model.getWxUnionId(), model.getWxOpenId());
            ref.get().application.writeMemberLevel(model.getLevelName());

            BaseApplication.single.writeMemberLevelId(model.getLevelID());

            ref.get().application.writePhoneLogin(model.getUsername(), model.getUsername(), model.getRelatedType(), model.getAuthorizeCode() );
            //记录登录类型（1:微信登录，2：手机登录）
            ref.get().application.writeMemberLoginType(2);

            ref.get().getLeftMenuData();

            ref.get().bindPush();

            Bundle bd = new Bundle();
            Intent intent = new Intent();
            intent.setClass(ref.get(), HomeActivity.class);
            intent.putExtras(bd);
            //传递推送信息
            if (null != ref.get().bundlePush) {
                intent.putExtra(Constants.PUSH_KEY, ref.get().bundlePush);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            EventBus.getDefault().post(new RefreshHttpHeaderEvent());

            intent.putExtra("redirecturl", ref.get().redirectUrl);

            ActivityUtils.getInstance().skipActivity(ref.get(), intent);

        }
    }

    static class OALoginErrorListener implements Response.ErrorListener {
        WeakReference<OALoginActivity> ref;

        public OALoginErrorListener(OALoginActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (ref.get() == null) return;

            if (ref.get().progressPopupWindow != null) {
                ref.get().progressPopupWindow.dismissView();
            }

            ToastUtils.showShortToast("登录失败");
        }
    }


    /***
     * 绑定推送信息
     */
    protected void bindPush() {
        String userId = BaseApplication.single.readUserId();
        String userKey = Constants.getSMART_KEY();
        String userRandom = String.valueOf(System.currentTimeMillis());
        String userSecure = Constants.getSMART_SECURITY();
        String userSign = SignUtil.getSecure(userKey, userSecure, userRandom);
        String alias = BaseApplication.getPhoneIMEI();
        PushHelper.bindingUserId(userId, alias, userKey, userRandom, userSign);
    }

    /***
     * 获得左侧菜单数据
     */
    protected void getLeftMenuData() {
        String url = Constants.getINTERFACE_PREFIX() ;//+ "/weixin/UpdateLeftInfo";
        url += url.endsWith("/")? "weixin/UpdateLeftInfo": "/weixin/UpdateLeftInfo";
        String customerId = BaseApplication.single.readMerchantId();
        String userId = BaseApplication.single.readMemberId();
        String userType = String.valueOf(BaseApplication.single.readMemberType());
        url += "?customerId=" + customerId + "&userId=" + userId + "&clientUserType=" + userType;

        AuthParamUtils authParamUtils = new AuthParamUtils(BaseApplication.single, System.currentTimeMillis(), url);
        //url = authParamUtils.obtainUrl();
        url = authParamUtils.obtainUrlName();
        GsonRequest<UpdateLeftInfoModel> request = new GsonRequest<UpdateLeftInfoModel>(Request.Method.GET
                , url, UpdateLeftInfoModel.class, null, new Response.Listener<UpdateLeftInfoModel>() {
            @Override
            public void onResponse(UpdateLeftInfoModel updateLeftInfoModel) {
                getLeftMenu(updateLeftInfoModel);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressPopupWindow != null) progressPopupWindow.dismissView();
            }
        });

        VolleyUtil.getRequestQueue().add(request);

        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow(OALoginActivity.this);
        }
        progressPopupWindow.showProgress("正在获取菜单信息,请稍等...");
        progressPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    protected void getLeftMenu(UpdateLeftInfoModel model) {
        if (progressPopupWindow != null) {
            progressPopupWindow.dismissView();
        }
        if (model == null) {
            ToastUtils.showShortToast("获取菜单信息失败。");
            return;
        }
        if (model.getCode() != 200) {
            String msg = "获取菜单信息失败";
            if (!TextUtils.isEmpty(model.getMsg())) {
                msg = model.getMsg();
            }
            ToastUtils.showShortToast(msg);
            return;
        }
        if (model.getData() == null || model.getData().getHome_menus() == null) {
            ToastUtils.showShortToast("获取菜单信息失败");
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
            ToastUtils.showShortToast("获取菜单信息失败。");
        }
    }
}
