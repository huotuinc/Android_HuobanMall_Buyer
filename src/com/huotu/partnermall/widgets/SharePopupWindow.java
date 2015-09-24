package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huotu.partnermall.adapter.ShareAdapter;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.utils.WindowUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享弹出框
 */
public
class SharePopupWindow extends PopupWindow {

    private Context                context;
    private PlatformActionListener platformActionListener;
    private Platform.ShareParams   shareParams;
    private Activity               aty;

    public
    SharePopupWindow ( Context cx, Activity aty ) {
        this.context = cx;
        this.aty = aty;
    }

    public
    PlatformActionListener getPlatformActionListener ( ) {
        return platformActionListener;
    }

    public
    void setPlatformActionListener (
            PlatformActionListener platformActionListener
                                   ) {
        this.platformActionListener = platformActionListener;
    }

    public
    void showShareWindow ( ) {
        View view = LayoutInflater.from ( context ).inflate (
                R.layout.share_layout,
                null
                                                            );
        GridView     gridView = ( GridView ) view.findViewById ( R.id.share_gridview );
        ShareAdapter adapter  = new ShareAdapter ( context );
        gridView.setAdapter ( adapter );

        Button btn_cancel = ( Button ) view.findViewById ( R.id.btn_cancel );
        // 取消按钮
        btn_cancel.setOnClickListener (
                new View.OnClickListener () {

                                          public void onClick(View v) {
                                              // 销毁弹出框
                                              dismiss();
                                          }
                                      });

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth( LinearLayout.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight( LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPop);
        WindowUtils.backgroundAlpha ( aty, 0.4f );
        // 实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        //this.setBackgroundDrawable(dw);
        this.setBackgroundDrawable ( context.getResources ( ).getDrawable ( R.drawable
                                                                                    .share_window_bg ) );

        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    private class ShareItemClickListener implements AdapterView.OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share(position);
            pop.dismiss();

        }
    }

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {

        if (position == 0) {
            //sina分享
            sinaWeibo ( );
        } else if (position == 1) {
            //微信朋友圈分享
            wechatMoments ( );
        } else if(position==2){
            //qq控件分享
            qzone();
        }else{
            Platform plat = null;
            plat = ShareSDK.getPlatform ( context, getPlatform ( position ) );
            if (platformActionListener != null) {
                plat.setPlatformActionListener(platformActionListener);
            }

            plat.share(shareParams);
        }
    }



    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            Platform.ShareParams sp = new Platform.ShareParams ();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setShareType ( Platform.SHARE_IMAGE );
            sp.setShareType ( Platform.SHARE_MUSIC );
            sp.setTitle ( shareModel.getText ( ) );
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            shareParams = sp;
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = SinaWeibo.NAME;
                break;
            case 1:
                platform = WechatMoments.NAME;
                break;
            case 2:
                platform = QZone.NAME;
                break;
            default:
                break;
        }
        return platform;
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        Platform.ShareParams sp = new Platform.ShareParams ();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle ( ) );
        sp.setSiteUrl(shareParams.getUrl ( ) );

        Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);

        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    /**
     * 分享到微信朋友圈
     */
    private void wechatMoments()
    {
        Platform.ShareParams sp = new Platform.ShareParams (  );
        sp.setTitle ( shareParams.getTitle ( ) );
        sp.setTitleUrl ( shareParams.getUrl ( ) ); // 标题的超链接
        sp.setText ( shareParams.getText ( ) );
        sp.setImageUrl ( shareParams.getImageUrl ( ) );
        sp.setSite ( shareParams.getTitle ( ) );
        sp.setSiteUrl ( shareParams.getUrl ( ) );
        Platform moments = ShareSDK.getPlatform ( context, WechatMoments.NAME);
        moments.setPlatformActionListener ( platformActionListener );
        //执行分享
        moments.share ( sp );

    }

    /**
     * 分享到sina微博
     */
    private void sinaWeibo()
    {
        Platform.ShareParams sp = new Platform.ShareParams (  );
        sp.setTitle ( shareParams.getTitle ( ) );
        sp.setTitleUrl ( shareParams.getUrl ( ) ); // 标题的超链接
        sp.setText ( shareParams.getText ( ) );
        sp.setImageUrl ( shareParams.getImageUrl ( ) );
        sp.setSite ( shareParams.getTitle ( ) );
        sp.setSiteUrl ( shareParams.getUrl ( ) );
        Platform sinaWeibo = ShareSDK.getPlatform ( context, SinaWeibo.NAME );

        sinaWeibo.setPlatformActionListener ( platformActionListener );
        //执行分享
        sinaWeibo.share ( sp );
    }
}
