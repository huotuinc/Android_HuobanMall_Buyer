package com.huotu.android.library.buyer.widget.GoodsWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
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
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.R;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 商品布局 单方格
 * Created by jinxiangdong on 2016/1/25.
 */
public class GoodsOneWidget extends BaseLinearLayout {
    private SimpleDraweeView ivPic;
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvPrice;
    private TextView tvJifen;
    private SimpleDraweeView ivJifen;
    private GoodsOneConfig goodsOneConfig;
    private List<GoodsBean> goods;

    public GoodsOneWidget(Context context , GoodsOneConfig goodsOneConfig) {
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
        ivPic.setId(ivPic.hashCode());
        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);

        tvName = new TextView(getContext());
        tvName.setId(tvName.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, ivPic.getId());
        tvName.setLayoutParams(layoutParams);
        tvName.setMaxLines(2);
        tvName.setTextColor(Color.BLACK);
        rlGoods.addView(tvName);

        int relaId;
        if( goodsOneConfig.getProduct_showname().equals(Constant.GOODS_SHOW) ){
            relaId = tvName.getId();
        }else{
            relaId =ivPic.getId();
        }

        tvDesc = new TextView( getContext());
        tvDesc.setId( tvDesc.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, relaId);
        tvDesc.setLayoutParams(layoutParams);
        rlGoods.addView(tvDesc);

        if(goodsOneConfig.getProduct_showsyno().equals(Constant.GOODS_SHOW)){
            relaId = tvDesc.getId();
        }else if(goodsOneConfig.getProduct_showname().equals(Constant.GOODS_SHOW)){
            relaId = tvName.getId();
        }else{
            relaId = ivPic.getId();
        }

        tvPrice = new TextView( getContext() );
        tvPrice.setId( tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, relaId);
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        if(goodsOneConfig.getProduct_showprices().equals(Constant.GOODS_SHOW)){
            relaId = tvPrice.getId();
        }else if(goodsOneConfig.getProduct_showsyno().equals(Constant.GOODS_SHOW)){
            relaId = tvDesc.getId();
        }else if(goodsOneConfig.getProduct_showname().equals(Constant.GOODS_SHOW)){
            relaId=tvName.getId();
        }else {
            relaId = ivPic.getId();
        }

        tvJifen = new TextView( getContext() );
        tvJifen.setId( tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, relaId);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvJifen.setLayoutParams(layoutParams);
        tvJifen.setTextColor(Color.BLACK);
        rlGoods.addView(tvJifen);

        if(  !TextUtils.isEmpty( goodsOneConfig.getBackground())) {
            ivJifen = new SimpleDraweeView(getContext());
            ivJifen.setId( ivJifen.hashCode());
            int widthPx= DensityUtils.dip2px(getContext() , goodsOneConfig.getIconWidth());
            layoutParams = new RelativeLayout.LayoutParams( widthPx , ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.LEFT_OF, tvJifen.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, tvJifen.getId());
            ivJifen.setLayoutParams(layoutParams);
            rlGoods.addView(ivJifen);
        }

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
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);

        tvPrice = new TextView( getContext() );
        tvPrice.setId(tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW , ivPic.getId());
        //layoutParams.addRule( RelativeLayout.RIGHT_OF , tvName.getId() );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        tvName = new TextView(getContext());
        tvName.setId(tvName.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW , ivPic.getId());
        layoutParams.addRule(RelativeLayout.LEFT_OF , tvPrice.getId());
        tvName.setLayoutParams(layoutParams);
        tvName.setMaxLines(1);
        tvName.setTextColor(Color.BLACK);
        rlGoods.addView(tvName);

        tvDesc = new TextView( getContext());
        tvDesc.setId(tvDesc.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
        tvDesc.setLayoutParams(layoutParams);
        rlGoods.addView(tvDesc);


        tvJifen = new TextView( getContext() );
        tvJifen.setId(tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, ivPic.getId());
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
        if( this.goodsOneConfig.getProduct_showsyno().equals( Constant.GOODS_SHOW ) ){
            tvDesc.setVisibility(VISIBLE);
        }else {
            tvDesc.setVisibility(GONE);
        }
        if( this.goodsOneConfig.getProduct_showprices().equals( Constant.GOODS_SHOW ) ){
            tvPrice.setVisibility(VISIBLE);
        }else {
            tvPrice.setVisibility(GONE);
        }
        if( this.goodsOneConfig.getProduct_userInteger().contains(Constant.GOODS_SHOW) ){
            tvJifen.setVisibility(VISIBLE);
            if(ivJifen!=null) ivJifen.setVisibility(VISIBLE);
        }else {
            tvJifen.setVisibility(GONE);
            if(ivJifen!=null) ivJifen.setVisibility(GONE);
        }

        if( ivJifen !=null && !TextUtils.isEmpty( goodsOneConfig.getBackground() ) ){
            int widthPx=DensityUtils.dip2px(getContext(), Constant.REBATEICON_WIDTH );

            String bgUrl = Variable.resourceUrl + goodsOneConfig.getBackground();

            FrescoDraweeController.loadImage(ivJifen, widthPx, bgUrl);
        }

        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        FrescoDraweeController.loadImage(ivPic, picWidth, good.getThumbnailPic());
        tvName.setText( good.getGoodName() );
        tvDesc.setText(good.getBrief());

        String priceStr = CommonUtil.formatDouble( good.getPrice() );
        String zpriceStr = CommonUtil.formatPrice( good.getPriceLevel() );
        SpanningUtil.set_Price_Format2(tvPrice, priceStr, zpriceStr, Color.RED, Color.GRAY);
        String jifenStr = CommonUtil.formatJiFen(good.getScore());
        tvJifen.setText( jifenStr +"积分" );
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

    /**
     * 获得商品信息
     * TODO
     */
    private void asyncGetGoodsData(){
        BizApiService apiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl ).create( BizApiService.class );
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid= Variable.CustomerId;
        String goodsids = goodsOneConfig.getBindDataID();
        int levelid = Variable.userLevelId;

        Call<BizBaseBean<List<GoodsBean>>> call = apiService.getGoodsDetail( key,
                random, secure , customerid , goodsids , levelid);
        call.enqueue(new Callback<BizBaseBean<List<GoodsBean>>>() {
            @Override
            public void onResponse(Response<BizBaseBean<List<GoodsBean>>> response) {
                if( response ==null || response.code() != Constant.REQUEST_SUCCESS || response.body()==null||
                        response.body().getData()==null ){
                    Logger.e( response.message());
                    EventBus.getDefault().post(new LoadCompleteEvent());
                    return;
                }

                goods = response.body().getData();
                for( GoodsBean item : goods) {
                    if ( !TextUtils.isEmpty( goodsOneConfig.getGoods_layer()) && goodsOneConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)) {
                        create_card(item);
                    } else if ( !TextUtils.isEmpty(goodsOneConfig.getGoods_layer()) && goodsOneConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL)) {
                        create_jijian(item);
                    }else{//当没有 这个 goods_layer 属性时，默认 使用卡片布局
                        create_card(item);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e( "error" , t );
            }
        });
    }
}
