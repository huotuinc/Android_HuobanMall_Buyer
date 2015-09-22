package com.huotu.partnermall.onekeyshare;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatHelper;

public
class WxShareUtils {

    private Handler mHandler;
    private Context mContext;
    private static class Holder
    {
        private static final WxShareUtils instance = new WxShareUtils();
    }

    private WxShareUtils()
    {

    }

    public static final WxShareUtils getInstance()
    {
        return Holder.instance;
    }

    public void share(Handler handler, Context mContext, int type, String[] params)
    {
        switch ( type )
        {
            case Platform.SHARE_TEXT :
            {
                this.mHandler = handler;
                //文本分享
                shareText(mContext, params);
            }
            break;
            case Platform.SHARE_IMAGE:
            {
                this.mHandler = handler;
                //图片分享
                shareImage(mContext, params);
            }
            break;
            case Platform.SHARE_MUSIC:
            {
                this.mHandler = handler;
                shareMuisc(mContext, params);
            }
            break;
            case Platform.SHARE_WEBPAGE:
            {
                this.mHandler = handler;
                shareWebPage(mContext, params);
            }
            break;
            default:
                break;
        }

    }

    private void shareText(Context mContext, String[] params)
    {

    }

    private void shareImage(Context mContext, String[] params)
    {

    }

    private void shareMuisc(Context mContext, String[] params)
    {

    }
    private void shareWebPage(Context mContext, String[] params)
    {

        if(null == mContext || params.length < 3)
        {
            ToastUtils.showShortToast ( mContext, "微信朋友圈分享失败" );
            return;
        }
        else
        {
            this.mContext = mContext;
            for(int i=0; i<params.length; i++)
            {
                String str = params[i];
                if( TextUtils.isEmpty ( str ) && i!=2)
                {
                    ToastUtils.showShortToast ( mContext, "微信朋友圈分享失败" );
                    return;
                }
            }

            String title = params[0];
            String text = params[1];
            String image = params[2];
            String url = params[3];

            WechatHelper.ShareParams sp = new WechatMoments.ShareParams ( );
            sp.title = title;
            if (TextUtils.isEmpty(image))
            {
                sp.imageData = ((BitmapDrawable )mContext.getResources().getDrawable( R.drawable.ic_launcher)).getBitmap();
            }
            else if (image.contains("http://") || image.contains("https://"))
            {
                sp.imageUrl = image;
            }
            sp.text = text;
            if (url.contains("http://") || url.contains("https://"))
            {
                sp.url = url;
            }
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform plat = null;
            plat = ShareSDK.getPlatform ( WechatMoments.NAME );
            plat.setPlatformActionListener (  new PlatformActionListener ( ) {
                                                  @Override
                                                  public
                                                  void onComplete ( Platform platform, int action, HashMap< String, Object
                                                          > hashMap ) {
                                                      Message msg = new Message();
                                                      msg.what = Constants.SHARE_SUCCESS;
                                                      msg.arg1 = action;
                                                      msg.obj = platform;
                                                      mHandler.sendMessage ( msg );
                                                  }

                                                  @Override
                                                  public
                                                  void onError ( Platform platform, int action, Throwable throwable ) {

                                                      Message msg = new Message();
                                                      msg.what = Constants.SHARE_ERROR;
                                                      msg.arg1 = action;
                                                      msg.obj = platform;
                                                      mHandler.sendMessage(msg);

                                                      // 分享失败的统计
                                                      ShareSDK.logDemoEvent ( 4, platform );
                                                  }

                                                  @Override
                                                  public
                                                  void onCancel ( Platform platform, int action ) {

                                                      Message msg = new Message();
                                                      msg.what = Constants.SHARE_CANCEL;
                                                      msg.arg1 = action;
                                                      msg.obj = platform;
                                                      mHandler.sendMessage(msg);

                                                      // 分享失败的统计
                                                      ShareSDK.logDemoEvent(5, platform);
                                                  }
                                              }  );
            plat.share(sp);
        }

    }


}
