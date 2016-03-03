package com.huotu.android.library.buyer.widget.GoodsWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.GoodsBean.GoodsOneConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.SpanningUtil;
import com.huotu.android.library.buyer.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品布局 单方格 卡片样式
 * Created by jinxiangdong on 2016/1/31.
 */
public class GoodsOneCardWidget extends LinearLayout {
    private GoodsOneConfig goodsOneConfig;
    private SimpleDraweeView ivPic;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvJifen;
    private List<GoodsBean> goods;

    public GoodsOneCardWidget(Context context , GoodsOneConfig goodsOneConfig) {
        super(context);

        this.setOrientation(VERTICAL);
        this.goodsOneConfig = goodsOneConfig;


        asyncGetGoodsData();

    }

    private void create_card( GoodsBean good  ) {
        RelativeLayout rlGoods = new RelativeLayout(getContext());
        LinearLayout.LayoutParams llLayoutOParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutOParams.setMargins(2,2,2,2);
        rlGoods.setLayoutParams(llLayoutOParams);
        rlGoods.setBackgroundResource(R.drawable.gray_border_style);
        int leftPx = DensityUtils.dip2px(getContext(), this.goodsOneConfig.getLeftMargion());
        int topPx = DensityUtils.dip2px( getContext() , this.goodsOneConfig.getTopMargion());
        int rightPx = DensityUtils.dip2px( getContext() , this.goodsOneConfig.getRightMargion());
        int bottomPx = DensityUtils.dip2px(getContext(),this.goodsOneConfig.getBottomMargion());

        rlGoods.setPadding(leftPx, topPx, rightPx, bottomPx);

        ivPic = new SimpleDraweeView(getContext());
        ivPic.setId( ivPic.hashCode() );
        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels * 2/5;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);

        tvName = new TextView(getContext());
        tvName.setId(tvName.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP , ivPic.getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ivPic.getId());
        //layoutParams.setMargins();
        tvName.setLayoutParams(layoutParams);
        tvName.setSingleLine();
        tvName.setTextSize(18);
        tvName.setTextColor(Color.BLACK);
        rlGoods.addView(tvName);

        tvPrice = new TextView( getContext() );
        tvPrice.setId( tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, tvName.getId());
        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        tvJifen = new TextView( getContext() );
        tvJifen.setId( tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM , ivPic.getId() );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvJifen.setLayoutParams(layoutParams);
        tvJifen.setTextColor(Color.BLACK);
        rlGoods.addView(tvJifen);

        this.addView(rlGoods);

        setStyle(good);
    }

    private void create_jijian( GoodsBean good ){
        RelativeLayout rlGoods = new RelativeLayout(getContext());
        LinearLayout.LayoutParams llLayoutOParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutOParams.setMargins(2,2,2,2);
        rlGoods.setLayoutParams(llLayoutOParams);
        int leftPx = DensityUtils.dip2px( getContext() , this.goodsOneConfig.getLeftMargion());
        int topPx = DensityUtils.dip2px( getContext() , this.goodsOneConfig.getTopMargion());
        int rightPx = DensityUtils.dip2px( getContext() , this.goodsOneConfig.getRightMargion());
        int bottomPx = DensityUtils.dip2px(getContext(),this.goodsOneConfig.getBottomMargion());
        rlGoods.setPadding(leftPx, topPx, rightPx, bottomPx);

        ivPic = new SimpleDraweeView(getContext());
        ivPic.setId( ivPic.hashCode() );
        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);

        tvName = new TextView(getContext());
        tvName.setId(tvName.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP , ivPic.getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ivPic.getId());
        tvName.setLayoutParams(layoutParams);
        tvName.setSingleLine();
        tvName.setTextSize(18);
        tvName.setTextColor(Color.BLACK);
        rlGoods.addView(tvName);

        tvPrice = new TextView( getContext() );
        tvPrice.setId(tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
        layoutParams.addRule( RelativeLayout.ALIGN_LEFT , tvName.getId() );
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        tvJifen = new TextView( getContext() );
        tvJifen.setId(tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM , ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        topPx = DensityUtils.dip2px(getContext(), 5);
        leftPx = topPx;
        layoutParams.setMargins( leftPx , topPx ,0,0 );
        tvJifen.setLayoutParams(layoutParams);
        tvJifen.setBackgroundColor(Color.RED);
        tvJifen.setPadding(leftPx, topPx, leftPx, topPx);
        tvJifen.setTextColor(Color.WHITE);
        rlGoods.addView(tvJifen);

        this.addView(rlGoods );

        setStyle(good);
    }

    protected void setStyle( GoodsBean good ){
        if( this.goodsOneConfig.getIsShowName().equals( Constant.GOODS_SHOW )){
            tvName.setVisibility(VISIBLE);
        }else {
            tvName.setVisibility(GONE);
        }

        if( this.goodsOneConfig.getIsShowPrices().equals(Constant.GOODS_SHOW) ){
            tvPrice.setVisibility(VISIBLE);
        }else {
            tvPrice.setVisibility(GONE);
        }
        if( this.goodsOneConfig.getIsShowUserInteger().contains(Constant.GOODS_SHOW) ){
            tvJifen.setVisibility(VISIBLE);
        }else {
            tvJifen.setVisibility(GONE);
        }

        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels * 2/ 5;
        FrescoDraweeController.loadImage(ivPic, picWidth, good.getThumbnailPic());
        tvName.setText( good.getGoodName() );

        String priceStr = String.valueOf( good.getMarketPrice() );
        String zpriceStr = String.valueOf( good.getPrice());
        SpanningUtil.set_Price_Format1(tvPrice, priceStr, zpriceStr, Color.RED, Color.GRAY);
        String jifenStr = String.valueOf( good.getRebate() );
        tvJifen.setText( jifenStr +"积分" );
    }


    /**
     * 获得商品信息
     * TODO
     */
    private void asyncGetGoodsData(){
        //模拟数据---------------------------------------------
        goods = new ArrayList<>();
        for(int i=0;i<5;i++){
            GoodsBean item = new GoodsBean();
            item.setBn(String.valueOf(i));
            item.setBrand("brand");
            item.setBrandId(i);
            item.setCatId(i);
            item.setCreateTime(new Date());
            item.setGoodName("把iPhone部门看成一个国家它到底有多富？");
            item.setGoodType("goodtype");
            item.setIntro("intro");
            item.setMarketPrice(299d);
            item.setPdtSpec("");
            item.setPrice(250d);
            item.setThumbnailPic("http://res.olquan.cn/resource/images/wechat/4471/mall/pic20160123132920.jpg");

            //item.setDetailurl("http://res.olquan.cn/resource/images/wechat/4471/mall/pic20160123132920.jpg");
            //item.setImageurl("http://res.olquan.cn/resource/images/wechat/4471/mall/pic20160123132920.jpg");
            //item.setPrice(299d);
            //item.setRebate(8);
            item.setBrief("iPhone 一直都是炙手可热的摇钱树，“富可敌国”不是说着玩的。苹果又再一次在收入上破纪录了，虽然 iPhone 的出货量增长幅度为史上最低");
            //item.setTitle("把iPhone部门看成一个国家它到底有多富？");
            //item.setzPrice(250d);
            goods.add(item);
        }
        //-----------------------------------------------------

        for( GoodsBean item : goods) {
            if (goodsOneConfig.getGridStyle().equals(Constant.GRIDSTYLE_CARD)) {
                create_card(item);
            } else if (goodsOneConfig.getGridStyle().equals(Constant.GRIDSTYLE_NORMAL)) {
                create_jijian(item);
            }
        }
    }

}
