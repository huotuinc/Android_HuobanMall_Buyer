package com.huotu.partnermall.widgets.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.widgets.custom.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

/**
 * 底部导航组件
 * Created by jinxiangdong on 2016/1/22.
 */
public class FooterOneWidget extends BaseLinearLayout //implements Callback<BizBaseBean<MallInfoBean>>
{
    private FooterOneConfig footerOneConfig;
    private MallInfoBean mallInfoBean;
    /**
     * 底部导航栏图标的宽度
     */
    public final static int FOOTER_ICON_WIDTH = 25;
    /**
     * url地址中的参数 占位符
     */
    public final static String URL_PARAMETER_CUSTOMERID="{CustomerID}";
    /**
     * URL地址中的参数 占位符
     */
    public final static String URL_PARAMETER_QQ ="{QQ}";
    /**
     * 资源根地址
     */
    public String resourceUrl;

    public FooterOneWidget(Context context , FooterOneConfig footerOneConfig) {
        super(context);

        this.footerOneConfig= footerOneConfig;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        this.setOrientation(VERTICAL);
        TextView tvLine = new TextView(getContext());
        int heightPx = DensityUtils.dip2px(getContext(),1);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        tvLine.setBackgroundColor(Color.LTGRAY);
        tvLine.setLayoutParams(layoutParams);
        this.addView(tvLine);

        LinearLayout llContainer = new LinearLayout(getContext());
        llContainer.setOrientation(HORIZONTAL);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(layoutParams);
        llContainer.setBackgroundColor(SystemTools.parseColor(footerOneConfig.getBackgroundColor()));
        int topMargion = DensityUtils.dip2px(getContext(), footerOneConfig.getTopMargion());
        int bottomMargion = DensityUtils.dip2px(getContext(),footerOneConfig.getBottomMargion());
        llContainer.setPadding(0, topMargion, 0, bottomMargion);
        this.addView(llContainer);

        if( footerOneConfig.getRows()==null || footerOneConfig.getRows().size()<1 ) return;
        for(FooterImageBean item : footerOneConfig.getRows()){
            LinearLayout ll = new LinearLayout(context);
            ll.setId(item.hashCode());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            ll.setOrientation(VERTICAL);
            ll.setLayoutParams(layoutParams);
            int padpx= DensityUtils.dip2px(getContext(),2);
            ll.setPadding(padpx,padpx,padpx,padpx);
            ll.setOnClickListener(this);

            SimpleDraweeView iv = new SimpleDraweeView(context);
            int iconWidth = DensityUtils.dip2px(getContext(), FOOTER_ICON_WIDTH);
            layoutParams = new LayoutParams( iconWidth , iconWidth );
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(layoutParams);
            ll.addView(iv);
            String imageUrl = resourceUrl + item.getImageUrl();
            FrescoDraweeController.loadImage(iv, iconWidth , imageUrl);

            TextView tv = new TextView(context);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tv.setLayoutParams(layoutParams);
            tv.setText(item.getName());
            tv.setTextColor(SystemTools.parseColor( footerOneConfig.getFontColor() ) );
            ll.addView(tv);

            llContainer.addView(ll);
        }

        //asyncGetMallInfo();
    }

    @Override
    public void onClick(View v) {
        for( FooterImageBean item : footerOneConfig.getRows()) {
            boolean isOpenInNewWindow = false;
            if (v.getId() == item.hashCode()) {
                String url = item.getLinkUrl();
                //url = url.replace( URL_PARAMETER_CUSTOMERID , String.valueOf( BaseApplication.single.readMerchantId () ));

                //当检测到 {QQ} 特殊字符串时，则标记为在新窗口打开。
                if( url.contains( URL_PARAMETER_QQ ) ){
                    isOpenInNewWindow=true;
                }
                //当检测到 客服地址 存在时，则替换为新的客服地址，否则使用老的QQ联系方式
                String kefuUrl = BaseApplication.single.readMerchantWebChannel();
                if(!TextUtils.isEmpty( kefuUrl)){
                    url = url.replace( URL_PARAMETER_QQ , kefuUrl );
                }else {
                    String qq = mallInfoBean != null ? mallInfoBean.getClientQQ() : "";
                    url = url.replace(URL_PARAMETER_QQ, "http://wpa.qq.com/msgrd?v=3&uin=" + qq + "&site=qq&menu=yes");
                }

                String name = item.getName();
                link( name , url , isOpenInNewWindow );
                break;
            }
        }
    }

    protected void link(String linkName , String relativeUrl , boolean isOpenInNewWindow ){
        if( TextUtils.isEmpty( relativeUrl )) return;

        String url=relativeUrl;
        String customerId = BaseApplication.single.readMerchantId();
        url = url.replace( URL_PARAMETER_CUSTOMERID , customerId );

        if( !url.startsWith("http://")){
            url = BaseApplication.single.obtainMerchantUrl() + relativeUrl;
        }

        url = url.replace( URL_PARAMETER_CUSTOMERID, customerId );
        

        // TODO: 2016/9/14
    }

    /**
     * 通过API获得 店铺信息
     */
//    protected void asyncGetMallInfo(){
//        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create( BizApiService.class);
//        int customerId= Variable.CustomerId;
//        String key = Variable.BizKey;
//        String random = String.valueOf(System.currentTimeMillis());
//        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
//
//        Call<BizBaseBean<MallInfoBean>> call = bizApiService.getMallInfo( key , random , secure , customerId );
//        call.enqueue( this );
//    }

//    @Override
//    public void onResponse(Call<BizBaseBean<MallInfoBean>> call, Response<BizBaseBean<MallInfoBean>> response) {
//        if (response == null || response.code() != 200 || response.body() == null || response.body().getData() == null) {
//            Logger.e(response.message());
//            return;
//        }
//        mallInfoBean = response.body().getData();
//    }

//    @Override
//    public void onFailure(Call<BizBaseBean<MallInfoBean>> call, Throwable t) {
//        Log.e("error",t.getMessage());
//    }
}
