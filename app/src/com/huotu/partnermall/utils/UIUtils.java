package com.huotu.partnermall.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.MainMenuModel;
import com.huotu.partnermall.model.MenuBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 构建页面的工具类
 */
public class UIUtils {
    private BaseApplication application;
    private Context context;
    private Resources resources;
    private LinearLayout mainMenuLayout;
    private Handler mHandler;

    public UIUtils (  BaseApplication application, Context context, Resources resources, LinearLayout  mainMenuLayout,  Handler mHandler ) {
        this.application = application;
        this.context = context;
        this.resources = resources;
        this.mainMenuLayout = mainMenuLayout;
        this.mHandler = mHandler;
    }

    /**
     * 动态加载菜单栏
     */
    public
    void loadMenus ( ) {
        String menuStr = application.readMenus();
        //json格式转成list
        Gson gson = new Gson();
        List<MenuBean> menuList = gson.fromJson(menuStr, new TypeToken<List<MenuBean>>() {
        }.getType());
        //
        if (null == menuList || menuList.isEmpty()) {
            //Loger.e("未加载商家定义的菜单");
        } else {
            //根据登录类型，添加相关的绑定菜单
            addRelateTypeMenu(menuList);
            //按分组排序
            menuSort(menuList);

            int size = menuList.size();
            for (int i = 0; i < size; i++) {
                int leftWidth = Constants.SCREEN_WIDTH * 80 / 100;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(leftWidth, (Constants.SCREEN_HEIGHT / 15));

                //取出分组
                String menuGroup = menuList.get(i).getMenuGroup();

                RelativeLayout menuLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.main_menu, null);
                menuLayout.setBackgroundColor(resources.getColor(R.color.theme_color));
                mainMenuLayout.addView(menuLayout);

                if (i < (size - 1)) {
                    if (0 == i) {
                        lp.setMargins(0, 20, 0, 0);
                    }
                    String menuGroupNext = menuList.get(i + 1).getMenuGroup();
                    if (!menuGroupNext.equals(menuGroup)) {
                        //Drawable d = resources.getDrawable(R.drawable.main_menu_no_bottom, null);
                        menuLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.main_menu_no_bottom));
                        //SystemTools.loadBackground( menuLayout , d );
                        lp.setMargins(0, 0, 0, 20);
                    } else {
                        menuLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.main_menu_bottom));
                    }
                } else {
                    //最后一个
                    menuLayout.setBackgroundDrawable(resources.getDrawable(R.drawable.main_menu_no_bottom));
                    lp.setMargins(0, 0, 0, 20);
                }

                final MenuBean menu = menuList.get(i);
                //设置ID
                menuLayout.setId(i);
                //设置图标
                ImageView menuIcon = (ImageView) menuLayout.findViewById(R.id.menuIcon);
                //从sd卡上获取图标
                /*int iconId = resources.getIdentifier ( menu.getMenuIcon (), "drawable", application.readSysInfo () );
                Drawable menuIconDraw = resources.getDrawable ( iconId );*/
                Bitmap map = null; //SystemTools.readBitmapFromSD ( menu.getMenuIcon () );
                if (null != map) {
                    Drawable menuIconDraw = new BitmapDrawable(context.getResources(), map);
                    SystemTools.loadBackground(menuIcon, menuIconDraw);
                } else {
                    //int iconId = resources.getIdentifier ( "menu_default", "drawable", application.readSysInfo () );
                    //int iconId = resources.getIdentifier( menu.getMenuIcon() , "drawable" ,application.readSysInfo() );
                    int iconId = resources.getIdentifier(menu.getMenuIcon(), "drawable", application.getPackageName());
                    Drawable menuIconDraw = null;
                    if (iconId > 0) {
                        menuIconDraw = resources.getDrawable(iconId);
                    }
                    if (menuIconDraw == null) {
                        //iconId = resources.getIdentifier( "menu_default" ,"drawable" ,application.readSysInfo ()  );
                        iconId = resources.getIdentifier("menu_default", "drawable", application.getPackageName());
                        menuIconDraw = resources.getDrawable(iconId);
                    }
                    SystemTools.loadBackground(menuIcon, menuIconDraw);
                }

                //设置文本
                TextView menuText = (TextView) menuLayout.findViewById(R.id.menuText);
                //mParam =new RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
                //menuText.setLayoutParams( mParam );
                //SystemTools.setFontStyle ( menuText, application );
                menuText.setTextColor(Color.GRAY);
                menuText.setText(menu.getMenuName());

                menuLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = menu.getMenuUrl();
                                if (url.contains(Constants.CUSTOMER_ID)) {
                                    url = url.replace(Constants.CUSTOMER_ID, "customerid=" + application.readMerchantId());
                                }
                                if (url.contains(Constants.USER_ID)) {
                                    url = url.replace(Constants.USER_ID, "userid=" + application.readUserId());
                                }
                                if ("/".equals(url)) {
                                    url = application.obtainMerchantUrl();
                                    AuthParamUtils paramUtils = new AuthParamUtils(application, System.currentTimeMillis(), url, context);
                                    url = paramUtils.obtainUrl();
                                    //加载具体的页面
                                    Message msg = mHandler.obtainMessage(Constants.LOAD_PAGE_MESSAGE_TAG, url);
                                    mHandler.sendMessage(msg);
                                } else {
                                    AuthParamUtils paramUtils = new AuthParamUtils(application, System.currentTimeMillis(), url, context);
                                    url = paramUtils.obtainUrl();
                                    //加载具体的页面
                                    Message msg = mHandler.obtainMessage(Constants.LOAD_PAGE_MESSAGE_TAG, application.obtainMerchantUrl() + url);
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                );

                menuLayout.setLayoutParams(lp);

            }
        }
    }

    /**
     * 按分组排序
     * @param menus
     * @return
     */
    private void menuSort(List<MenuBean> menus)
    {
        ComparatorMenu comparatorMenu = new ComparatorMenu ();
        Collections.sort ( menus, comparatorMenu );
    }

    public class ComparatorMenu implements Comparator {

        @Override
        public
        int compare ( Object lhs, Object rhs ) {

            MenuBean menu01 = ( MenuBean ) lhs;
            MenuBean menu02 = ( MenuBean ) rhs;
            //比较分组序号
            return menu01.getMenuGroup ( ).compareTo ( menu02.getMenuGroup () );
        }
    }

    public void addRelateTypeMenu(List<MenuBean> menus){
        //int loginType = application.readMemberLoginType();
        //if( loginType== 1){
            int relateType = application.readMemberRelatedType();
            if( relateType == 1 ){//0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号

            MenuBean item = new MenuBean();
            item.setMenuGroup("888");
            item.setMenuIcon("home_menu_bindphone");
            item.setMenuName("绑定手机");
            item.setMenuTag("");
            item.setMenuUrl("http://www.bindphone.com");
            menus.add(item);
            return;
        }//微信登录。
        else if( relateType == 0) {
//            int relateType = application.readMemberRelatedType();
//            if (relateType != 0) {//0-手机帐号还未关联微信,1-微信帐号还未绑定手机,2-已经有关联帐号
//                return;
//            }
            MenuBean item = new MenuBean();
            item.setMenuGroup("888");
            item.setMenuIcon("home_menu_bindweixin");
            item.setMenuName("绑定微信");
            item.setMenuTag("");
            item.setMenuUrl("http://www.bindweixin.com");
            menus.add(item);
        }
    }
}