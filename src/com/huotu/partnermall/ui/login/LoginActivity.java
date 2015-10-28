package com.huotu.partnermall.ui.login;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    RelativeLayout loginL;
    private
    AutnLogin      login;
    //handler对象
    public Handler mHandler;
    public
    ProgressPopupWindow progress;
    public
    ProgressPopupWindow successProgress;
    //windows类
    WindowManager wManager;

    public
    NoticePopWindow noticePop;
    private
    TextView        loginText;
    public
    AssetManager    am;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        mHandler = new Handler ( this );
        am = this.getAssets ();
        setContentView ( R.layout.login_ui );
        findViewById ( );
        initView ( );
        wManager = this.getWindowManager ( );
        progress = new ProgressPopupWindow ( LoginActivity.this, LoginActivity.this, wManager );
        successProgress = new ProgressPopupWindow ( LoginActivity.this, LoginActivity.this, wManager );
    }

    @Override
    protected
    void findViewById ( ) {
        loginL = ( RelativeLayout ) this.findViewById ( R.id.loginL );
        loginL.setOnClickListener ( this );
        loginText = ( TextView ) this.findViewById ( R.id.loginText );
    }

    @Override
    protected
    void initView ( ) {
        SystemTools.setFontStyle ( loginText, application );
        loginL.setBackgroundColor (
                SystemTools.obtainColor (
                        application.obtainMainColor (
                                                    )
                                        )
                                  );
    }

    @Override
    protected
    void onDestroy ( ) {
        super.onDestroy ( );
        progress.dismissView ( );
        successProgress.dismissView ();
    }

    @Override
    protected
    void onResume ( ) {
        super.onResume ( );
        if(null != progress)
        {
            progress.dismissView ( );
        }

        loginL.setClickable ( true );
    }

    @Override
    public
    void onClick ( View v ) {
        switch ( v.getId ( ) ) {
            case R.id.loginL: {
                //
                progress.showProgress ( null );
                progress.showAtLocation (
                        findViewById ( R.id.loginL ),
                        Gravity.CENTER, 0, 0
                                        );
                //微信授权登录
                Platform wechat = ShareSDK.getPlatform ( LoginActivity.this, Wechat.NAME );
                login = new AutnLogin ( LoginActivity.this, mHandler, loginL );
                login.authorize ( new Wechat ( LoginActivity.this ) );
                loginL.setClickable ( false );
                //ActivityUtils.getInstance ().skipActivity ( LoginActivity.this, HomeActivity
                // .class );
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
                //提示授权成功
                Platform plat = ( Platform ) msg.obj;
                login.authorize ( plat );
            }
            break;
            //授权登录
            case Constants.LOGIN_AUTH_ERROR:
            {
                loginL.setClickable ( true );
                progress.dismissView ( );
                //提示授权失败
                String notice = ( String ) msg.obj;
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, notice);
                noticePop.showNotice ( );
                noticePop.showAtLocation (
                        findViewById ( R.id.loginL ),
                        Gravity.CENTER, 0, 0
                                         );
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {
                loginL.setClickable ( true );
                progress.dismissView ( );
                Throwable throwable = ( Throwable ) msg.obj;
                if("cn.sharesdk.wechat.utils.WechatClientNotExistException".equals ( throwable.toString () ))
                {
                    //手机没有安装微信客户端
                    noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "手机没有安装微信客户端");
                    noticePop.showNotice ();
                    noticePop.showAtLocation (
                            findViewById ( R.id.loginL ),
                            Gravity.CENTER, 0, 0
                                             );
                }
                else
                {
                    loginL.setClickable ( true );
                    progress.dismissView ();
                    //提示授权失败
                    noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "微信授权失败");
                    noticePop.showNotice ();
                    noticePop.showAtLocation (
                            findViewById ( R.id.loginL ),
                            Gravity.CENTER, 0, 0
                                             );
                }

            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                loginL.setClickable ( true );
                //提示取消授权
                progress.dismissView ();
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "微信授权被取消");
                noticePop.showNotice ();
                noticePop.showAtLocation (
                        findViewById ( R.id.loginL ),
                        Gravity.CENTER, 0, 0
                                         );

            }
            break;
            case Constants.MSG_USERID_FOUND:
            {
                successProgress.showProgress ( "已经获取用户信息" );
                successProgress.showAtLocation (
                        findViewById ( R.id.loginL ),
                        Gravity.CENTER, 0, 0
                                        );

            }
            break;
            case Constants.MSG_LOGIN:
            {
                progress.dismissView ( );
                //登录后更新界面
                AccountModel account = ( AccountModel ) msg.obj;
                //和商家授权
                AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), "", LoginActivity.this );
                final Map param = paramUtils.obtainParams ( account );
                String authUrl = Constants.INTERFACE_PREFIX + "weixin/LoginAuthorize";
                //String authUrl = "http://192.168.1.56:8032/weixin/LoginAuthorize";
                HttpUtil.getInstance ().doVolley ( LoginActivity.this, LoginActivity.this,
                                                   mHandler, application, authUrl, param, account );
            }
            break;
            case Constants.MSG_USERID_NO_FOUND:
            {
                progress.dismissView ();
                //提示授权成功
                noticePop = new NoticePopWindow ( LoginActivity.this, LoginActivity.this, wManager, "获取用户信息失败");
                noticePop.showNotice ();
                noticePop.showAtLocation (
                        findViewById ( R.id.loginL ),
                        Gravity.CENTER, 0, 0
                                         );
            }
            break;
        }
        return false;
    }
}
