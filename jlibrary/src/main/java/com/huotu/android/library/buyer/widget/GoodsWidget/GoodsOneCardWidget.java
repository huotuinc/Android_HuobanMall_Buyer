package com.huotu.android.library.buyer.widget.GoodsWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.GoodsBean.GoodsOneConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;
import com.huotu.android.library.buyer.widget.SpanningUtil;
import com.huotu.android.library.buyer.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品布局 单方格 卡片样式
 * Created by jinxiangdong on 2016/1/31.
 */
public class GoodsOneCardWidget extends BaseLinearLayout {
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
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, ivPic.getId());
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ivPic.getId());
        //layoutParams.setMargins();
        tvName.setLayoutParams(layoutParams);
        tvName.setLines(2);
        tvName.setTextSize(18);
        //tvName.setTextColor(Color.BLACK);
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
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvJifen.setLayoutParams(layoutParams);
        //tvJifen.setTextColor(Color.BLACK);
        rlGoods.addView(tvJifen);

        this.addView(rlGoods);

        rlGoods.setOnClickListener(this);
        rlGoods.setTag(good);

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

        this.addView(rlGoods);

        rlGoods.setOnClickListener(this);
        rlGoods.setTag(good);

        setStyle(good);
    }

    protected void setStyle( GoodsBean good ){
        if( this.goodsOneConfig.getProduct_showname().equals( Constant.GOODS_SHOW )){
            tvName.setVisibility(VISIBLE);
        }else {
            tvName.setVisibility(GONE);
        }

        if( this.goodsOneConfig.getProduct_showprices().equals(Constant.GOODS_SHOW) ){
            tvPrice.setVisibility(VISIBLE);
        }else {
            tvPrice.setVisibility(GONE);
        }
        if( this.goodsOneConfig.getProduct_userInteger().contains(Constant.GOODS_SHOW) ){
            tvJifen.setVisibility(VISIBLE);
        }else {
            tvJifen.setVisibility(GONE);
        }

        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels * 2/ 5;
        FrescoDraweeController.loadImage(ivPic, picWidth, good.getThumbnailPic());
        tvName.setText( good.getGoodName() );

        String priceStr = CommonUtil.formatDouble(good.getPrice() );
        String zpriceStr = CommonUtil.formatPrice( good.getPriceLevel() ); //String.valueOf( good.getPrice());
        SpanningUtil.set_Price_Format1(tvPrice, priceStr, zpriceStr, Color.RED, Color.GRAY);
        String jifenStr = CommonUtil.formatJiFen(good.getScore()); //String.valueOf( good.getRebate() );
        tvJifen.setText( jifenStr +"积分" );
    }

    /**
     * 获得商品信息
     *
     */
    private void asyncGetGoodsData(){
        BizApiService apiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create( BizApiService.class );
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid= Variable.CustomerId;
        String goodsids = goodsOneConfig.getBindDataID();
        int levelid = Variable.userLevelId;
        Call<BizBaseBean<List<GoodsBean>>> call = apiService.getGoodsDetail( key, random, secure , customerid , goodsids , levelid);

        call.enqueue(new Callback<BizBaseBean<List<GoodsBean>>>() {
            @Override
            public void onResponse( Call<BizBaseBean<List<GoodsBean>>> call, Response<BizBaseBean<List<GoodsBean>>> response) {
                if( response ==null || response.code() != Constant.REQUEST_SUCCESS || response.body()==null||
                        response.body().getData()==null ){
                    Logger.e(response.message());
                    return;
                }

                goods = response.body().getData();
                for( GoodsBean item : goods) {
                    if (goodsOneConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)) {
                        create_card(item);
                    } else if (goodsOneConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL)) {
                        create_jijian(item);
                    }
                }
            }

            @Override
            public void onFailure( Call<BizBaseBean<List<GoodsBean>>> call, Throwable t) {
                Logger.e( "error" , t );
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if( v.getTag()!=null && v.getTag() instanceof GoodsBean){
            GoodsBean bean = (GoodsBean)v.getTag();
            String url = bean.getDetailUrl();
            String name = bean.getGoodName();
            CommonUtil.link(name , url );
        }
    }
}
