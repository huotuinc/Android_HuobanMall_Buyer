package com.huotu.android.library.buyer.widget.FooterWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.FooterBean.FooterImageBean;
import com.huotu.android.library.buyer.bean.FooterBean.FooterOneConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 底部导航组件
 * Created by jinxiangdong on 2016/1/22.
 */
public class FooterOneWidget extends BaseLinearLayout implements Callback<BizBaseBean<MallInfoBean>>{
    private FooterOneConfig footerOneConfig;
    private MallInfoBean mallInfoBean;

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
        llContainer.setBackgroundColor(CommonUtil.parseColor(footerOneConfig.getBackgroundColor()));
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
            int iconWidth = DensityUtils.dip2px(getContext(), Constant.FOOTER_ICON_WIDTH);
            layoutParams = new LayoutParams( iconWidth , iconWidth );
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(layoutParams);
            ll.addView(iv);
            String imageUrl = Variable.resourceUrl + item.getImageUrl();
            FrescoDraweeController.loadImage(iv, iconWidth , imageUrl);

            TextView tv = new TextView(context);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tv.setLayoutParams(layoutParams);
            tv.setText(item.getName());
            tv.setTextColor(CommonUtil.parseColor( footerOneConfig.getFontColor() ) );
            ll.addView(tv);

            llContainer.addView(ll);
        }

        asyncGetMallInfo();
    }

    @Override
    public void onClick(View v) {
        for( FooterImageBean item : footerOneConfig.getRows()) {
            if (v.getId() == item.hashCode()) {
                String url = item.getLinkUrl();
                url = url.replace( Constant.URL_PARAMETER_CUSTOMERID , String.valueOf( Variable.CustomerId ));

                String qq = mallInfoBean!=null? mallInfoBean.getClientQQ():"";
                url = url.replace( Constant.URL_PARAMETER_QQ ,"http://wpa.qq.com/msgrd?v=3&uin="+ qq+"&site=qq&menu=yes");

                String name = item.getName();
                CommonUtil.link( name , url );
                break;
            }
        }
    }

    /**
     * 通过API获得 店铺信息
     */
    protected void asyncGetMallInfo(){
        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create( BizApiService.class);
        int customerId= Variable.CustomerId;
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);

        Call<BizBaseBean<MallInfoBean>> call = bizApiService.getMallInfo( key , random , secure , customerId );
        call.enqueue( this );
    }

    @Override
    public void onResponse(Call<BizBaseBean<MallInfoBean>> call, Response<BizBaseBean<MallInfoBean>> response) {
        if (response == null || response.code() != 200 || response.body() == null || response.body().getData() == null) {
            Logger.e(response.message());
            return;
        }
        mallInfoBean = response.body().getData();
    }

    @Override
    public void onFailure(Call<BizBaseBean<MallInfoBean>> call, Throwable t) {
        Logger.e(t.getMessage());
    }
}
