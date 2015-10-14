package com.huotu.partnermall.ui.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huotu.partnermall.async.LoadLogoImageAyscTask;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
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

import org.json.JSONObject;

import java.lang.reflect.Method;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面
 */
public
class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private
    Button loginBtn;
    private
    AutnLogin login;
    //handler对象
    public Handler mHandler;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        mHandler = new Handler ( this );
        setContentView ( R.layout.login_ui );
        findViewById ( );
        initView();
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
    public
    void onClick ( View v ) {
        switch ( v.getId () )
        {
            case R.id.loginId:
            {
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
                //提示授权成功
                Platform plat = ( Platform ) msg.obj;
                ToastUtils.showShortToast ( LoginActivity.this, "微信授权成功，登陆中" );
                login.authorize ( plat );
            }
            break;
            case Constants.MSG_AUTH_ERROR:
            {
                //提示授权失败
                ToastUtils.showShortToast ( LoginActivity.this, "微信授权失败" );
            }
            break;
            case Constants.MSG_AUTH_CANCEL:
            {
                //提示取消授权
                ToastUtils.showShortToast ( LoginActivity.this, "微信授权被取消" );
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
                //提示授权成功
                ToastUtils.showShortToast ( LoginActivity.this, "登录成功" );
                //登录后更新界面
                AccountModel account = ( AccountModel ) msg.obj;
                //和商城用户系统交互
                application.writeMemberInfo (
                        account.getAccountName ( ), account.getAccountId (),
                        account.getAccountIcon ( ), account.getAccountToken ( ),
                        account.getAccountUnionId ()
                                            );
                //获取商户支付信息
                AuthParamUtils paramUtils = new AuthParamUtils ( application, System.currentTimeMillis (), "http://mallapi.huobanj.cn/PayConfig?customerid=3447" );
                final String url = paramUtils.obtainUrls ( );
                HttpUtil.getInstance ().doVolley(LoginActivity.this, application, url);
                //赋值
                //跳转到首页
                ActivityUtils.getInstance ().skipActivity ( LoginActivity.this, HomeActivity.class );
            }
            break;
            case Constants.MSG_USERID_NO_FOUND:
            {
                //提示授权成功
                ToastUtils.showShortToast ( LoginActivity.this, "获取用户信息失败" );
            }
            break;
        }
        return false;
    }
}
