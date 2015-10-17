package com.huotu.partnermall.ui.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.huotu.partnermall.async.LoadLogoImageAyscTask;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.AccountModel;
import com.huotu.partnermall.model.MerchantPayInfo;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.VolleyHttpUtil;
import com.huotu.partnermall.widgets.NoticePopWindow;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面
 */
public
class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private
    Button    loginBtn;
    private
    AutnLogin login;
    //handler对象
    public Handler mHandler;
    public
    ProgressPopupWindow progress;
    //windows类
    WindowManager wManager;

    public
    NoticePopWindow noticePop;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        mHandler = new Handler ( this );
        setContentView ( R.layout.login_ui );
        findViewById ( );
        initView ( );
        wManager = this.getWindowManager ( );
        progress = new ProgressPopupWindow ( LoginActivity.this, LoginActivity.this, wManager );
    }

    @Override
    protected
    void findViewById ( ) {
        loginBtn = ( Button ) this.findViewById ( R.id.loginId );
        loginBtn.setOnClickListener ( this );
    }

    @Override
    protected
    void initView ( ) {

    }

    @Override
    protected
    void onDestroy ( ) {
        super.onDestroy ( );
        progress.dismissView ( );
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
        progress.dismissView ( );
        loginBtn.setClickable ( true );
    }

    @Override
    public
    void onClick ( View v ) {
        switch ( v.getId ( ) ) {
            case R.id.loginId: {
                //
                progress.showProgress ( );
                progress.showAtLocation (
                        findViewById ( R.id.loginId ),
                        Gravity.CENTER, 0, 0
                                        );
                //微信授权登录
                Platform wechat = ShareSDK.getPlatform ( LoginActivity.this, Wechat.NAME );
                login = new AutnLogin ( LoginActivity.this, mHandler, loginBtn );
                login.authorize ( new Wechat ( LoginActivity.this ) );
                loginBtn.setClickable ( false );
                //ActivityUtils.getInstance ().skipActivity ( LoginActivity.this, HomeActivity.class );
            }
            break;
            default:
                break;
        }
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {

        switch ( msg.what )
        {
            //授权登录
            case Constants.MSG_AUTH_COMPLETE:
            {
                progress.dismissView ( );
                //提示授权成功
                Platform plat = ( Platform ) msg.obj;
                ToastUtils.showShortToast ( LoginActivity.this, "微信授权成功，登陆中" );
                login.authorize ( plat );
            }
            break;
            //授权登录
            case Constants.LOGIN_AUTH_ERROR:
            {
                progress.dismissView ( );
                //提示授权失败
                String notice = ( String ) msg.obj;
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, notice);
                noticePop.showNotice ( );
                noticePop.showAtLocation (
                        findViewById ( R.id.loginId ),
                        Gravity.CENTER, 0, 0
                                         );
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {
                progress.dismissView ( );
                //提示授权失败
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "微信授权失败");
                noticePop.showNotice ();
                noticePop.showAtLocation (
                        findViewById ( R.id.loginId ),
                        Gravity.CENTER, 0, 0
                                         );

            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                //提示取消授权
                progress.dismissView ();
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "微信授权被取消");
                noticePop.showNotice ();
                noticePop.showAtLocation (
                        findViewById ( R.id.loginId ),
                        Gravity.CENTER, 0, 0
                                         );

            }
            break;
            case Constants.MSG_USERID_FOUND:
            {
                //提示授权成功
                ToastUtils.showShortToast ( LoginActivity.this, "已经获取用户信息" );
            }
            break;
            case Constants.MSG_LOGIN:
            {
                //登录后更新界面
                AccountModel account = ( AccountModel ) msg.obj;
                //获取商家域名
                //获取商户站点
                String rootUrl = "http://mallapi.huobanj.cn/mall/getmsiteurl";
                rootUrl += "?customerId="+application.readMerchantId ();
                AuthParamUtils paramUtil = new AuthParamUtils ( application, System.currentTimeMillis (), rootUrl );
                final String rootUrls = paramUtil.obtainUrls ( );
                HttpUtil.getInstance ().doVolleySite(LoginActivity.this, application, rootUrls );
                //获取商户支付信息
                String targetUrl = "http://mallapi.huobanj.cn/PayConfig?customerid=";
                targetUrl += "3447";//动态获取商户编号，现在暂时使用3447
                AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), targetUrl );
                final String url = paramUtils.obtainUrls ( );
                HttpUtil.getInstance ().doVolley(LoginActivity.this, application, url);
                //和商家授权
                final Map param = paramUtils.obtainParams ( account );
                String authUrl = "http://mallapi.huobanj.cn/weixin/loginorregister";
                //String authUrl = "http://192.168.1.56:8032/weixin/loginorregister";
                HttpUtil.getInstance ().doVolley ( LoginActivity.this, LoginActivity.this,
                                                   mHandler, application, authUrl, param, account );
            }
            break;
            case Constants.MSG_USERID_NO_FOUND:
            {
                //提示授权成功
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "获取用户信息失败");
                noticePop.showNotice ();
                noticePop.showAtLocation (
                        findViewById ( R.id.loginId ),
                        Gravity.CENTER, 0, 0
                                         );
            }
            break;
        }
        return false;
    }
}
