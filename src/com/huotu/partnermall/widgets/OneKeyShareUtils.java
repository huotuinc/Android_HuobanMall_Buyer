package com.huotu.partnermall.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.ShareMsgModel;
import com.huotu.partnermall.onekeyshare.OnekeyShare;
import com.huotu.partnermall.onekeyshare.WxShareUtils;
import com.mob.tools.utils.UIHandler;
import cn.sharesdk.framework.Platform;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public
class OneKeyShareUtils {

    private
    ShareMsgModel msgModel;
    private
    Context context;
    private
    Handler mHandler;

    public OneKeyShareUtils(ShareMsgModel msgModel, Context context, Handler mHandler)
    {
        this.msgModel = msgModel;
        this.context = context;
        this.mHandler = mHandler;
    }

    public void shareShow(String platform,  boolean silent )
    {
        OnekeyShare oks = new OnekeyShare ();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        //隐藏编辑框
        oks.setSilent ( silent );
        oks.setDialogMode ();
        if(null != msgModel && null != msgModel.getText ( ))
        {
            oks.setText ( msgModel.getText ( ) );
        }
        if(null != msgModel && null != msgModel.getImagePath ( ))
        {
            oks.setImagePath ( msgModel.getImagePath ( ) );
        }

        if(null != msgModel && null != msgModel.getTitle ( ))
        {
            oks.setTitle ( msgModel.getTitle ( ) );
        }
        if(null != msgModel && null != msgModel.getTitleUrl ( ))
        {
            oks.setTitleUrl ( msgModel.getTitleUrl ( ) );
        }
        if(null != msgModel && null != msgModel.getSite ( ))
        {
            oks.setSite ( msgModel.getSite ( ) );
        }
        if(null != msgModel && null != msgModel.getSiteUrl ( ))
        {
            oks.setSiteUrl ( msgModel.getSiteUrl ( ) );
        }
        //添加微信自定义分享
        /*Bitmap wxLogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.logo_wechatmoments );
        String wxLabel = context.getResources ().getString ( R.string.wechatmoments );
        oks.setCustomerLogo ( wxLogo, wxLogo, wxLabel, new WxClick(mHandler, context, Platform.SHARE_WEBPAGE, getParam()) );
        Bitmap sinaLogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.logo_sinaweibo );
        String sinaLabel = context.getResources ().getString ( R.string.sinaweibo );
        oks.setCustomerLogo ( sinaLogo, sinaLogo, sinaLabel, null );
        Bitmap qzoneLogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.logo_qzone );
        String qzoneLabel = context.getResources ().getString ( R.string.qzone );
        oks.setCustomerLogo ( qzoneLogo, qzoneLogo, qzoneLabel, null );
*/

        //设置自定义的外部回调
       //oks.setCallback ();
        //启动分享GUI
        oks.show ( context );
    }
    /*private String[] getParam()
    {
        String shareContentTxt = "我是来自<a href=\"http://www.baidu.com\">百度</>客户端分享实例Demo的数据,如果您看到此条数据,可以选择忽略它";
        String sharklink = "<a href=\"http://www.baidu.com\">百度</>";
        String title = "伙伴商城买家版客户端分享";
        return new String[]{shareContentTxt, title, sharklink};
    }*/
}


 /*class WxClick implements View.OnClickListener
{

    private Handler mHandler;
    private Context mContext;
    private int type;
    private
    String[] params;

    public WxClick(Handler mHandler, Context mContext, int type, String[] params)
    {
        this.mHandler = mHandler;
        this.mContext = mContext;
        this.type = type;
        this.params = params;
    }

    @Override
    public
    void onClick ( View v ) {

        //微信分享
        WxShareUtils.getInstance ().share ( mHandler, mContext, type, params );

    }
}*/