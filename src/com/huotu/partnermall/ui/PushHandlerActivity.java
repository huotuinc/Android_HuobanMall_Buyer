package com.huotu.partnermall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huotu.partnermall.config.Constants;
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

        boolean loginActivityIsLoaded = Util.isAppLoaded(this , PhoneLoginActivity.class.getName());
        if(loginActivityIsLoaded ){
            Intent intent = new Intent(this, PhoneLoginActivity.class);
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
