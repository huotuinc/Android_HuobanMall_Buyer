package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.ShopBean.ShopOneConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺头部 一
 * Created by jinxiangdong on 2016/1/15.
 */
public class ShopOneWidget extends RelativeLayout implements View.OnClickListener{
    private ShopOneConfig config;
    private int id1;
    private int id2;
    private SimpleDraweeView leftImage;
    private SimpleDraweeView rightImage;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private int LOGOWIDTH=20;

    public ShopOneWidget(Context context, ShopOneConfig config){
        super(context);
        this.config = config;

        int leftRight = DensityUtils.dip2px(context, config.getPaddingLeft());
        int topBottom = DensityUtils.dip2px(context, config.getPaddingTop());
        this.setPadding(leftRight, topBottom, leftRight, topBottom);
        this.setBackgroundColor(CommonUtil.parseColor(config.getBackColor()));

        leftImage = new SimpleDraweeView(context);
        RoundingParams roundingParams = RoundingParams.asCircle();
        leftImage.getHierarchy().setRoundingParams(roundingParams);

        leftImage.setOnClickListener(this);
        id1 = leftImage.hashCode();
        leftImage.setId(id1);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftImage.setLayoutParams(layoutParams);
        this.addView(leftImage);

        tvLeftTitle = new TextView(context);
        tvLeftTitle.setOnClickListener(this);
        tvLeftTitle.setSingleLine();
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, id1);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        int leftPx = DensityUtils.dip2px(getContext(),2);
        layoutParams.setMargins(leftPx, 0, 0, 0);
        tvLeftTitle.setLayoutParams(layoutParams);
        tvLeftTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvLeftTitle.setTextColor(CommonUtil.parseColor(config.getFontColor()));
        this.addView(tvLeftTitle);

        tvRightTitle = new TextView(context);
        tvRightTitle.setOnClickListener(this);
        int id3 = tvRightTitle.hashCode();
        tvRightTitle.setId(id3);
        tvRightTitle.setSingleLine();
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMargins(leftPx,0,0,0);
        tvRightTitle.setLayoutParams(layoutParams);
        tvRightTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvRightTitle.setTextColor(CommonUtil.parseColor(config.getFontColor()));
        this.addView(tvRightTitle);

        rightImage = new SimpleDraweeView(context);
        rightImage.setOnClickListener(this);
        id2 = rightImage.hashCode();
        rightImage.setId(id2);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, id3);
        rightImage.setLayoutParams(layoutParams);
        this.addView(rightImage);

        int imageWidthPx = DensityUtils.dip2px(context, LOGOWIDTH );
        if( config.getShow_type() == Constant.LOGO_1 ) {
            String imageUrl1  = Variable.resourceUrl + config.getImageUrl1();
            FrescoDraweeController.loadImage(leftImage, imageWidthPx, imageUrl1);
            tvLeftTitle.setText(config.getTitle_linkname1());
        }else{
            //通过API接口获得信息
            asyncGetLogo();
        }

        tvRightTitle.setText(config.getTitle_linkname());
        String imageUrl = Variable.resourceUrl + config.getImageUrl();
        FrescoDraweeController.loadImage(rightImage, imageWidthPx, imageUrl );
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == leftImage.getId() || v.getId()== tvLeftTitle.getId() ){
            String url = config.getLinkUrl1();
            String name = config.getLinkName1();
            CommonUtil.link(name , url);
        }else if( v.getId() == rightImage.getId() || v.getId()== tvRightTitle.getId() ){
            String url = config.getLinkUrl();
            String name = config.getLinkName();
            CommonUtil.link(name , url);

            //FrescoDraweeController.clearCache();
            //Toast.makeText(getContext(),"clearcache",Toast.LENGTH_LONG).show();

        }
    }

    /**
     * 通过API获得 店铺Logo图片
     */
    protected void asyncGetLogo(){
        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance( Variable.BizRootUrl).create( BizApiService.class);
        int customerId= Variable.CustomerId;
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);

        Call<BizBaseBean<MallInfoBean>> call = bizApiService.getMallInfo( key , random , secure , customerId );
        call.enqueue(new LogoCallBack(this));
    }

    public class LogoCallBack implements Callback<BizBaseBean<MallInfoBean>>{
        WeakReference<ShopOneWidget> ref;
        public LogoCallBack(ShopOneWidget widget){
            this.ref =  new WeakReference<>(widget);
        }

        @Override
        public void onResponse( Call<BizBaseBean<MallInfoBean>> call , Response<BizBaseBean<MallInfoBean>> response) {
            if( ref.get()==null)return;
            if( response==null||response.code() !=200 || response.body() ==null || response.body().getData()==null){
                Logger.e(response.message());
                return;
            }
            String logoUrl = response.body().getData().getLogo();
            int imageWidthPx = DensityUtils.dip2px(ref.get().getContext() , LOGOWIDTH );
            FrescoDraweeController.loadImage(ref.get().leftImage , imageWidthPx, logoUrl );
            ref.get().tvLeftTitle.setText(response.body().getData().getMallName());
        }

        @Override
        public void onFailure(Call<BizBaseBean<MallInfoBean>> call , Throwable t) {
            Logger.e(t.getMessage());
        }
    }
}
