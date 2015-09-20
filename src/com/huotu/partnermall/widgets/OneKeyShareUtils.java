package com.huotu.partnermall.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.ShareMsgModel;
import com.huotu.partnermall.onekeyshare.OnekeyShare;

public
class OneKeyShareUtils {

    private
    ShareMsgModel msgModel;

    private
    Context context;

    public OneKeyShareUtils(ShareMsgModel msgModel, Context context)
    {
        this.msgModel = msgModel;
        this.context = context;
    }

    public void shareShow(String platform,  boolean silent )
    {
        OnekeyShare oks = new OnekeyShare ();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        //隐藏编辑框
        oks.setSilent ( silent );
        oks.setDialogMode ();

        if(null != msgModel && null != msgModel.getShareTitle ())
        {
            oks.setTitle ( msgModel.getShareTitle () );
        }
        if(null != msgModel && null != msgModel.getShareTitleUrl ())
        {
            oks.setTitleUrl ( msgModel.getShareTitleUrl () );
        }
        if(null != msgModel && null != msgModel.getShareText ())
        {
            oks.setText ( msgModel.getShareText () );
        }
        if(null != msgModel && null != msgModel.getShareImagePath ())
        {
            oks.setImagePath ( msgModel.getShareImagePath () );
        }
        if(null != msgModel && null != msgModel.getShareSiteUrl ())
        {
            oks.setUrl ( msgModel.getShareSiteUrl () );
        }
        if(null != msgModel && null != msgModel.getShareComment ())
        {
            oks.setComment ( msgModel.getShareComment () );
        }
        if(null != msgModel && null != msgModel.getShareSite ())
        {
            oks.setSite ( msgModel.getShareSite () );
        }
        if(null != msgModel && null != msgModel.getShareSiteUrl ())
        {
            oks.setSiteUrl ( msgModel.getShareSiteUrl () );
        }

        //设置自定义的外部回调
        oks.setCallback ( new OnekeyShare () );
        //启动分享GUI
        oks.show ( context );
    }
}
