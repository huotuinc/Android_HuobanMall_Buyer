package com.huotu.partnermall.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.onekeyshare.OnekeyShare;

public
class OneKeyShareUtils {

    //title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    private String shareTitle;
    //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    private String shareTitleUrl;
    //text是分享文本，所有平台都需要这个字段
    private String shareText;
    //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    private String shareImagePath;
    //url仅在微信（包括好友和朋友圈）中使用
    private String shareUrl;
    //comment是我对这条分享的评论，仅在人人网和QQ空间使用
    private String shareComment;
    //site是分享此内容的网站名称，仅在QQ空间使用
    private String shareSite;
    //siteUrl是分享此内容的网站地址，仅在QQ空间使用
    private String shareSiteUrl;

    private
    Context context;

    public OneKeyShareUtils(String shareTitle, String shareTitleUrl, String shareText, String shareImagePath, String shareUrl, String shareComment, String shareSite, String shareSiteUrl, Context context)
    {
        this.shareTitle = shareTitle;
        this.shareTitleUrl = shareTitleUrl;
        this.shareText = shareText;
        this.shareImagePath = shareImagePath;
        this.shareUrl = shareUrl;
        this.shareComment = shareComment;
        this.shareSite = shareSite;
        this.shareSiteUrl = shareSiteUrl;
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

        /*Bitmap sinalogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.share_sina );
        String sinalabel = context.getResources ().getString ( R.string.share_sina );*/
        //分享sina


        //自定义sina ICON
        //oks.setCustomerLogo ( sinalogo, null, sinalabel, null );
        //自定义weixin ICON
        /*Bitmap weixinlogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.share_moments );
        String weixinlabel = context.getResources ().getString ( R.string.share_weChat );
        oks.setCustomerLogo (weixinlogo, null, weixinlabel, null );*/
        //自定义QQ空间 ICON
        /*Bitmap qzonelogo = BitmapFactory.decodeResource ( context.getResources (), R.drawable.share_qzone );
        String qzonelabel = context.getResources ().getString ( R.string.share_qzone );
        oks.setCustomerLogo (qzonelogo, null, qzonelabel, null );*/

        if(null != shareTitle)
        {
            oks.setTitle ( shareTitle );
        }
        if(null != shareTitleUrl)
        {
            oks.setTitleUrl ( shareTitleUrl );
        }
        if(null != shareText)
        {
            oks.setText ( shareText );
        }
        if(null != shareImagePath)
        {
            oks.setImagePath ( shareImagePath );
        }
        if(null != shareUrl)
        {
            oks.setUrl ( shareUrl );
        }
        if(null != shareComment)
        {
            oks.setComment ( shareComment );
        }
        if(null != shareSite)
        {
            oks.setSite ( shareSite );
        }
        if(null != shareSiteUrl)
        {
            oks.setSiteUrl ( shareSiteUrl );
        }

        //设置自定义的外部回调
        oks.setCallback ( new OnekeyShare () );
        //启动分享GUI
        oks.show ( context );
    }
}
