package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.ShopBean.ShopDefaultConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

/**
 * 店铺头默认组件
 * Created by jinxiangdong on 2016/1/25.
 */
public class ShopDefaultWidget extends RelativeLayout {
    private SimpleDraweeView ivBg;
    private SimpleDraweeView ivAvator;
    private TextView tvShopName;
    private TextView tvGoodsCount;
    private TextView tvNewGoodsCount;
    private TextView tvOrder;
    private ShopDefaultConfig config;


    public ShopDefaultWidget(Context context , ShopDefaultConfig config ) {
        super(context);

        this.config =config;

        createLayout();

        asyncGetData();
    }

    protected void createLayout(){
        LayoutInflater.from(getContext()).inflate( R.layout.shop_default,this,true);

        ivBg = (SimpleDraweeView)this.findViewById(R.id.shop_default_bg);
        ivAvator=(SimpleDraweeView)this.findViewById(R.id.shop_default_avorator);
        ivAvator.setBackgroundColor( Color.TRANSPARENT );
        tvGoodsCount = (TextView)this.findViewById(R.id.shop_default_goodscount);
        tvNewGoodsCount = (TextView)this.findViewById(R.id.shop_default_newgoodscount);
        tvShopName = (TextView)this.findViewById(R.id.shop_default_shopname);
        tvNewGoodsCount = (TextView)this.findViewById(R.id.shop_default_newgoodscount);
        tvOrder = (TextView)this.findViewById(R.id.shop_default_order);
        tvOrder.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));

        //tvNewGoodsCount.setText(String.valueOf(config.getNewGoodsCount()));
        //tvGoodsCount.setText(String.valueOf(config.getGoodsCount()));
        //tvShopName.setText( config.getShopName());

        if(  config.getShow_type() == Constant.WIDGETBACKTYPE_COLOR ){
            ivBg.setBackgroundColor(Color.parseColor( config.getColor()) );
        }else if( config.getShow_type() == Constant.WIDGETBACKTYPE_IMAGE ) {
            int width = getResources().getDisplayMetrics().widthPixels;
            FrescoDraweeController.loadImage(ivBg, width, config.getBackground());
        }

//        if( TextUtils.isEmpty(config.getAvatarUrl())==false){
//            int widthPx = DensityUtils.dip2px( getContext() , 90);
//            int width = ivAvator.getLayoutParams()==null? widthPx : ivAvator.getLayoutParams().width; //ivAvator.getMeasuredWidth();
//            FrescoDraweeController.loadImage(ivAvator, width, config.getAvatarUrl());
//        }
    }

    protected void asyncGetData() {

        //TODO
        String avatarUrl = "http://images0.cnblogs.com/news_topic/20150129123438081.png";
        int widthPx = DensityUtils.dip2px(getContext(), 90);
        int width = ivAvator.getLayoutParams() == null ? widthPx : ivAvator.getLayoutParams().width;
        FrescoDraweeController.loadImage(ivAvator, width, avatarUrl);

    }
}
