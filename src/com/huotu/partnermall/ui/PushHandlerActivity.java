package com.huotu.partnermall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.login.PhoneLoginActivity;
import com.huotu.partnermall.utils.Util;

public class PushHandlerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushProcess();
    }

    protected void pushProcess(){
        if(null == getIntent() || !getIntent().hasExtra( Constants.HUOTU_PUSH_KEY)) {
            finish();
            return;
        }

        Bundle bundle = getIntent().getBundleExtra(Constants.HUOTU_PUSH_KEY);
        if(null==bundle) {
            finish();
            return;
        }


        //通过配置文件获取登录界面的类名，进行相应的登录操作
        String loginActivityClassName = this.getString(R.string.login_activity_classname);
        if( TextUtils.isEmpty(loginActivityClassName)) {
            loginActivityClassName = PhoneLoginActivity.class.getName();
        }

        boolean loginActivityIsLoaded = Util.isAppLoaded(this , loginActivityClassName);
        //boolean loginActivityIsLoaded = Util.isAppLoaded(this , PhoneLoginActivity.class.getName());
        if(loginActivityIsLoaded ){
            //Intent intent = new Intent(this, PhoneLoginActivity.class);
            Intent intent = new Intent();
            intent.setClassName( this.getPackageName() , loginActivityClassName );
            intent.putExtra( Constants.HUOTU_PUSH_KEY , bundle);
            this.startActivity(intent);
            this.finish();
            return;
        }

        boolean fragMainActivityIsLoaded = Util.isAppLoaded(this , HomeActivity.class.getName());
        if( fragMainActivityIsLoaded){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra( Constants.HUOTU_PUSH_KEY , bundle);
            this.startActivity(intent);
            this.finish();
            return;
        }

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra( Constants.HUOTU_PUSH_KEY , bundle);
        this.startActivity(intent);
        this.finish();
        return;
    }
}
