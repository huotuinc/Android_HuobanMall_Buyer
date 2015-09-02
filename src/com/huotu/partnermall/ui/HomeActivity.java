package com.huotu.partnermall.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;


import com.huotu.partnermall.AppManager;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.ToastUtils;

public class HomeActivity extends TabActivity {

    public static final String TAG = HomeActivity.class.getSimpleName ( );

    private long exitTime = 0l;
    private RadioGroup      mTabButtonGroup;
    private TabHost         mTabHost;
    //application引用
    public  BaseApplication application;
    //底部菜单数组
    public String[] menus;
    //侧滑菜单引用

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) HomeActivity.this.getApplication ( );
        AppManager.getInstance ( ).addActivity ( this );
        setContentView ( R.layout.activity_home );
        //获取底部的菜单选项
        loadMenus ( );
        findViewById ( );
        initView ( );
    }

    /**
     * 获取底部的菜单选项
     */
    private void loadMenus()
    {
        String menuStr = application.readMenus ( );
        menus = menuStr.split ( "," );
    }

    private
    void findViewById ( ) {
        mTabButtonGroup = ( RadioGroup ) findViewById ( R.id.home_radio_button_group );
        mTabButtonGroup.setVisibility ( View.VISIBLE );
        int menuCount = menus.length;
        if(3 == menuCount)
        {
            RadioButton rb01 = ( RadioButton ) findViewById ( R.id.radioButton_01 );
            rb01.setVisibility ( View.VISIBLE );
            rb01.setText ( menus[0] );
            RadioButton rb02 = ( RadioButton ) findViewById ( R.id.radioButton_02 );
            rb02.setVisibility ( View.VISIBLE );
            rb02.setText ( menus[1] );
            RadioButton rb03 = ( RadioButton ) findViewById ( R.id.radioButton_03 );
            rb03.setVisibility ( View.VISIBLE );
            rb03.setText ( menus[2] );
        }
        if(4 == menuCount)
        {
            RadioButton rb01 = ( RadioButton ) findViewById ( R.id.radioButton_01 );
            rb01.setVisibility ( View.VISIBLE );
            rb01.setText ( menus[0] );
            RadioButton rb02 = ( RadioButton ) findViewById ( R.id.radioButton_02 );
            rb02.setVisibility ( View.VISIBLE );
            rb02.setText ( menus[1] );
            RadioButton rb03 = ( RadioButton ) findViewById ( R.id.radioButton_03 );
            rb03.setVisibility ( View.VISIBLE );
            rb03.setText ( menus[2] );
            RadioButton rb04 = ( RadioButton ) findViewById ( R.id.radioButton_04 );
            rb04.setVisibility ( View.VISIBLE );
            rb04.setText ( menus[3] );
        }
        if(5 == menuCount)
        {
            RadioButton rb01 = ( RadioButton ) findViewById ( R.id.radioButton_01 );
            rb01.setVisibility ( View.VISIBLE );
            rb01.setText ( menus[0] );
            RadioButton rb02 = ( RadioButton ) findViewById ( R.id.radioButton_02 );
            rb02.setVisibility ( View.VISIBLE );
            rb02.setText ( menus[1] );
            RadioButton rb03 = ( RadioButton ) findViewById ( R.id.radioButton_03 );
            rb03.setVisibility ( View.VISIBLE );
            rb03.setText ( menus[2] );
            RadioButton rb04 = ( RadioButton ) findViewById ( R.id.radioButton_04 );
            rb04.setVisibility ( View.VISIBLE );
            rb04.setText ( menus[3] );
            RadioButton rb05 = ( RadioButton ) findViewById ( R.id.radioButton_05 );
            rb05.setVisibility ( View.VISIBLE );
            rb05.setText ( menus[4] );
        }
        if(6 == menuCount)
        {
            RadioButton rb01 = ( RadioButton ) findViewById ( R.id.radioButton_01 );
            rb01.setVisibility ( View.VISIBLE );
            rb01.setText ( menus[0] );
            RadioButton rb02 = ( RadioButton ) findViewById ( R.id.radioButton_02 );
            rb02.setVisibility ( View.VISIBLE );
            rb02.setText ( menus[1] );
            RadioButton rb03 = ( RadioButton ) findViewById ( R.id.radioButton_03 );
            rb03.setVisibility ( View.VISIBLE );
            rb03.setText ( menus[2] );
            RadioButton rb04 = ( RadioButton ) findViewById ( R.id.radioButton_04 );
            rb04.setVisibility ( View.VISIBLE );
            rb04.setText ( menus[3] );
            RadioButton rb05 = ( RadioButton ) findViewById ( R.id.radioButton_05 );
            rb05.setVisibility ( View.VISIBLE );
            rb05.setText ( menus[4] );
            RadioButton rb06 = ( RadioButton ) findViewById ( R.id.radioButton_06 );
            rb06.setVisibility ( View.VISIBLE );
            rb06.setText ( menus[5] );
        }

    }

    private
    void initView ( ) {

        mTabHost = getTabHost ( );

        Intent intent_1     = new Intent ( this, IndexActivity.class );
        Intent intent_2   = new Intent ( this, SearchActivity.class );
        Intent intent_3 = new Intent ( this, CategoryActivity.class );
        Intent intent_4     = new Intent ( this, CartActivity.class);
        Intent intent_5 = new Intent(this, PersonalActivity.class);
        Intent intent_6 = new Intent(this, SettingActivity.class);

        //初始化侧滑菜单面板
        application.layDrag = ( DrawerLayout ) this.findViewById (R.id.layDrag);

        mTabHost.addTab(mTabHost.newTabSpec( Constants.TAB_1).setIndicator(Constants.TAB_1)
                                .setContent(intent_1));
        mTabHost.addTab(mTabHost.newTabSpec(Constants.TAB_2)
                                .setIndicator ( Constants.TAB_2 ).setContent ( intent_2 ));
        mTabHost.addTab(mTabHost.newTabSpec(Constants.TAB_3)
                                .setIndicator(Constants.TAB_3).setContent(intent_3));
        mTabHost.addTab(mTabHost.newTabSpec(Constants.TAB_4).setIndicator ( Constants.TAB_4 )
                                .setContent ( intent_4 ));
        mTabHost.addTab(mTabHost.newTabSpec(Constants.TAB_5)
                                .setIndicator ( Constants.TAB_5 ).setContent ( intent_5 ));
        mTabHost.addTab(mTabHost.newTabSpec(Constants.TAB_6)
                                .setIndicator(Constants.TAB_6).setContent(intent_6));

        mTabHost.setCurrentTabByTag(Constants.TAB_1);

        mTabButtonGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                    switch (checkedId) {
                                                        case R.id.radioButton_01:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_1);
                                                            break;

                                                        case R.id.radioButton_02:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_2);
                                                            break;

                                                        case R.id.radioButton_03:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_3);
                                                            break;

                                                        case R.id.radioButton_04:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_4);
                                                            break;

                                                        case R.id.radioButton_05:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_5);
                                                            break;
                                                        case R.id.radioButton_06:
                                                            mTabHost.setCurrentTabByTag(Constants.TAB_6);
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        // 2秒以内按两次推出程序
        if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                ToastUtils.showLongToast ( getApplicationContext ( ), "再按一次退出程序" );
                exitTime = System.currentTimeMillis();
            } else
            {
                try
                {

                    HomeActivity.this.finish();
                    Runtime.getRuntime().exit(0);
                } catch (Exception e)
                {
                    Runtime.getRuntime().exit(-1);
                }
            }

            return true;
        }
        // TODO Auto-generated method stub
        return super.dispatchKeyEvent ( event );
    }
}

