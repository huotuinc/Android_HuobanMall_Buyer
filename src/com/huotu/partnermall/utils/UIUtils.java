package com.huotu.partnermall.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.huotu.partnermall.model.MenuBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 构建页面的工具类
 */
public
class UIUtils {

    private
    BaseApplication application;
    private
    Context         context;
    private
    Resources       resources;
    private LinearLayout  mainMenuLayout;
    private WindowManager wManager;
    private  Handler       mHandler;

    public
    UIUtils (
            BaseApplication application, Context context, Resources resources, LinearLayout
            mainMenuLayout, WindowManager wManager, Handler       mHandler
            ) {
        this.application = application;
        this.context = context;
        this.resources = resources;
        this.mainMenuLayout = mainMenuLayout;
        this.mHandler = mHandler;
        this.wManager = wManager;
    }

    /**
     * 动态加载菜单栏
     */
    public
    void loadMenus ( ) {

        String menuStr = application.readMenus ( );
        //json格式转成list
        Gson gson = new Gson ( );
        List< MenuBean > menuList = gson.fromJson (
                menuStr, new TypeToken< List< MenuBean > > (
                ) { }.getType ( )
                                                  );
        //按分组排序
        menuSort ( menuList );
        //
        if ( null == menuList || menuList.isEmpty ( ) ) {

            KJLoger.e ( "未加载商家定义的菜单" );
        }
        else {
            int size = menuList.size ( );
            for ( int i = 0 ; i < size ; i++ ) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup
                                                                                      .LayoutParams.MATCH_PARENT, (wManager.getDefaultDisplay ().getHeight ()/15));
                //取出分组
                String menuGroup = menuList.get ( i ).getMenuGroup ();
                RelativeLayout menuLayout = ( RelativeLayout ) LayoutInflater.from ( context ).inflate ( R.layout.main_menu, null );
                menuLayout.setBackgroundColor ( resources.getColor ( R.color.theme_color ) );
                if(i<(size-1))
                {
                    if(0 == i)
                    {
                        lp.setMargins ( 0, 20, 0, 0 );
                    }
                    String menuGroupNext = menuList.get ( i+1 ).getMenuGroup ();
                    if(!menuGroupNext.equals ( menuGroup ))
                    {
                        menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_no_bottom ) );
                        lp.setMargins ( 0, 0, 0, 20 );
                    }
                    else
                    {
                        menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_bottom ) );
                    }
                }
                else
                {
                    //最后一个
                    menuLayout.setBackgroundDrawable ( resources.getDrawable ( R.drawable.main_menu_no_bottom ) );
                    lp.setMargins ( 0, 0, 0, 20 );
                }

                final MenuBean menu = menuList.get ( i );
                //设置ID
                menuLayout.setId ( i );
                //设置图标
                ImageView menuIcon = ( ImageView ) menuLayout.findViewById ( R.id.menuIcon );
                int iconId = resources.getIdentifier ( menu.getMenuIcon (), "drawable", "com.huotu.partnermall.inner" );
                Drawable menuIconDraw = resources.getDrawable ( iconId );
                SystemTools.loadBackground ( menuIcon,  menuIconDraw);
                //设置文本
                TextView menuText = ( TextView ) menuLayout.findViewById ( R.id.menuText );
                menuText.setText ( menu.getMenuName ( ) );
                menuLayout.setOnClickListener (
                        new View.OnClickListener ( ) {
                            @Override
                            public
                            void onClick ( View v ) {
                                //加载具体的页面
                                Message msg = mHandler.obtainMessage ( Constants.LOAD_PAGE_MESSAGE_TAG, menu.getMenuUrl () );
                                mHandler.sendMessage ( msg );

                                //隐藏侧滑菜单
                                application.layDrag.closeDrawer ( Gravity.LEFT );

                            }
                        }
                                              );

                menuLayout.setLayoutParams ( lp );

                mainMenuLayout.addView ( menuLayout );
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


}
