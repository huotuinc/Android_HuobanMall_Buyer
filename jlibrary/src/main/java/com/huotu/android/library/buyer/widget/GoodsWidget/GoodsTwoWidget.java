package com.huotu.android.library.buyer.widget.GoodsWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.huotu.android.library.buyer.bean.GoodsBean.GoodsTwoConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.SpanningUtil;
import com.huotu.android.library.buyer.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 二方格(默认样式)
 * Created by jinxiangdong on 2016/1/25.
 */
public class GoodsTwoWidget extends LinearLayout {
    private GoodsTwoConfig goodsTwoConfig;
    private List<LinearLayout> lls=null;
    private int count;
    private List<GoodsBean> goods;
    private SimpleDraweeView ivPic;
    private TextView tvName;
    //private TextView tvDesc;
    private TextView tvPrice;
    private SimpleDraweeView ivJifen;
    private TextView tvJifen;

    public GoodsTwoWidget(Context context , GoodsTwoConfig goodsTwoConfig) {
        super(context);

        this.goodsTwoConfig = goodsTwoConfig;
        this.count = 0;
      if( this.goodsTwoConfig.isStyleLayout()){
          create_PUBULayout();
      }else{
          create_NormalLayout();
      }

        asyncGetGoodsData();
    }
    protected  void create_NormalLayout(){
        setOrientation(VERTICAL);
    }

    protected void create_PUBULayout(){
        this.setOrientation( HORIZONTAL);
        lls = new ArrayList<>();
        for( int i=0; i < 2;i++) {
            LinearLayout llItem = new LinearLayout(getContext());
            this.addView(llItem);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            int leftPaddingPx = DensityUtils.dip2px(getContext(), i == 0 ? 4 : 2);
            int rightPaddingPx = DensityUtils.dip2px( getContext() , i == 1 ? 4 : 2 );
            llItem.setPadding(leftPaddingPx,0,rightPaddingPx , 0 );
            llItem.setOrientation(VERTICAL);
            llItem.setLayoutParams(layoutParams);
            lls.add(llItem);
        }
    }

    private RelativeLayout create_jijian( GoodsBean good ){
        RelativeLayout rlGoods = new RelativeLayout(getContext());
        LinearLayout.LayoutParams llLayoutOParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutOParams.setMargins(2,2,2,2);
        rlGoods.setLayoutParams(llLayoutOParams);
        int leftPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getLeftMargion());
        int topPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getTopMargion());
        int rightPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getRightMargion());
        int bottomPx = DensityUtils.dip2px(getContext(),this.goodsTwoConfig.getBottomMargion());
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
        tvPrice.setBackgroundResource(R.drawable.transparent_circle_bg);
        rlGoods.addView(tvPrice);

//        tvName = new TextView(getContext());
//        tvName.setId(tvName.hashCode());
//        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.ALIGN_TOP , tvPrice.getId());
//        layoutParams.addRule(RelativeLayout.LEFT_OF , tvPrice.getId());
//        tvName.setLayoutParams(layoutParams);
//        tvName.setMaxLines(1);
//        tvName.setTextColor(Color.BLACK);
//        rlGoods.addView(tvName);

//        tvDesc = new TextView( getContext());
//        tvDesc.setId(tvDesc.hashCode());
//        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
//        tvDesc.setLayoutParams(layoutParams);
//        rlGoods.addView(tvDesc);

        tvJifen = new TextView( getContext() );
        tvJifen.setId(tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, ivPic.getId());
        topPx = DensityUtils.dip2px(getContext(), 5);
        leftPx = topPx;
        layoutParams.setMargins(leftPx, topPx, 0, 0);
        tvJifen.setLayoutParams(layoutParams);
        tvJifen.setBackgroundColor(Color.RED);
        tvJifen.setPadding(leftPx, topPx, leftPx, topPx);
        tvJifen.setTextColor(Color.WHITE);
        rlGoods.addView(tvJifen);

        //this.addView(rlGoods);

        setStyle(good);

        return rlGoods;
    }

    private RelativeLayout create_chuxiao(GoodsBean good ){
        RelativeLayout rlGoods = new RelativeLayout(getContext());
        rlGoods.setBackgroundResource(R.drawable.gray_border_style);
        LinearLayout.LayoutParams llLayoutOParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutOParams.setMargins(2,2,2,2);
        rlGoods.setLayoutParams(llLayoutOParams);
        int leftPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getLeftMargion());
        int topPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getTopMargion());
        int rightPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getRightMargion());
        int bottomPx = DensityUtils.dip2px(getContext(),this.goodsTwoConfig.getBottomMargion());
        rlGoods.setPadding(leftPx, topPx, rightPx, bottomPx);

        ivPic = new SimpleDraweeView(getContext());
        ivPic.setId(ivPic.hashCode());
        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);


        TextView tvCX = new TextView(getContext());
        tvCX.setId(tvCX.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT , ivPic.getId() );
        tvCX.setLayoutParams(layoutParams);
        tvCX.setText("我要\r\n促销");
        tvCX.setBackgroundColor(Color.RED);
        tvCX.setTextColor(Color.WHITE);
        tvCX.setGravity(Gravity.CENTER);
        rlGoods.addView(tvCX);

        tvPrice = new TextView( getContext() );
        tvPrice.setId(tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW , ivPic.getId());
        layoutParams.addRule( RelativeLayout.LEFT_OF , tvCX.getId() );
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT , ivPic.getId());
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        tvJifen = new TextView( getContext() );
        tvJifen.setId(tvJifen.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, ivPic.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, ivPic.getId());
        topPx = DensityUtils.dip2px(getContext(), 5);
        leftPx = topPx;
        layoutParams.setMargins(leftPx, topPx, 0, 0);
        tvJifen.setLayoutParams(layoutParams);
        tvJifen.setBackgroundColor(Color.RED);
        tvJifen.setPadding(leftPx, topPx, leftPx, topPx);
        tvJifen.setTextColor(Color.WHITE);
        rlGoods.addView(tvJifen);



        setStyle(good);

        return rlGoods;

    }

    /**
     *
     */
    protected void asyncGetGoodsData() {
        BizApiService apiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create( BizApiService.class );
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid= Variable.CustomerId;
        String goodsids = goodsTwoConfig.getBindDataID();
        int levelid = Variable.userLevelId;
        Call<BizBaseBean<List<GoodsBean>>> call = apiService.getGoodsDetail( key,
                random, secure , customerid , goodsids , levelid);
        call.enqueue(new Callback<BizBaseBean<List<GoodsBean>>>() {
            @Override
            public void onResponse(Response<BizBaseBean<List<GoodsBean>>> response) {
                if( response ==null || response.code() != Constant.REQUEST_SUCCESS || response.body()==null|| response.body().getData()==null ){
                    Logger.e(response.message());
                    //EventBus.getDefault().post(new LoadCompleteEvent());
                    return;
                }

                goods = response.body().getData();
                if (goodsTwoConfig.isStyleLayout()) {
                    infliate_1();
                } else {
                    infliate_2();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e( "error" , t );
            }
        });
    }

    protected void infliate_1(){
        for(GoodsBean item : goods) {
            int position = count % 2;
            LinearLayout llItem = lls.get(position);
            count++;
            RelativeLayout oneWidget=null;
            if( goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)) {
                oneWidget = create_card(item);
            }else if(goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL)){
                oneWidget = create_jijian(item);
            }else if(goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_PROMOTION)){
                oneWidget=create_chuxiao(item);
            }
            llItem.addView(oneWidget);
        }
    }

    protected void infliate_2(){
        int size= goods.size();

        for(int i=0; i<size ; i+=2 ) {
            GoodsBean item1 = goods.get(i);
            GoodsBean item2 = i+1 >=size ? null : goods.get(i+1);

            LinearLayout ll= create_two(item1,item2);
            this.addView(ll);

        }
    }

    protected LinearLayout create_two(GoodsBean item1 , GoodsBean item2){
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(HORIZONTAL);
        RelativeLayout rlOne=null;
        RelativeLayout rlTwo=null;
        if( goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD) ) {
            rlOne = create_card(item1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlOne.getLayoutParams();//new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            layoutParams.weight = 1.0f;
            rlOne.setLayoutParams(layoutParams);

            if( item2 !=null){
                rlTwo = create_card( item2);
                layoutParams = (LinearLayout.LayoutParams) rlTwo.getLayoutParams();//new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                layoutParams.weight=1.0f;
                rlTwo.setLayoutParams(layoutParams);
            }else{
                rlTwo = new RelativeLayout(getContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);

                rlTwo.setLayoutParams(layoutParams);
            }
        }else if( goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL)){
            rlOne = create_jijian(item1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            rlOne.setLayoutParams(layoutParams);

            if( item2 !=null){
                rlTwo = create_jijian(item2);
                rlTwo.setLayoutParams(layoutParams);
            }else{
                rlTwo = new RelativeLayout(getContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                layoutParams.setMargins(2,2,2,2);
                rlTwo.setLayoutParams(layoutParams);
            }
        }else if( goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_PROMOTION)){
            rlOne = create_chuxiao(item1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            rlOne.setLayoutParams(layoutParams);

            if( item2 !=null){
                rlTwo = create_chuxiao(item2);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                rlTwo.setLayoutParams(layoutParams);
            }else{
                rlTwo = new RelativeLayout(getContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                layoutParams.setMargins(2,2,2,2);
                rlTwo.setLayoutParams(layoutParams);
            }
        }

        ll.addView(rlOne);
        ll.addView(rlTwo);

        return ll;
    }

    private RelativeLayout create_card( GoodsBean good  ) {
        RelativeLayout rlGoods = new RelativeLayout(getContext());
        LinearLayout.LayoutParams llLayoutOParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutOParams.setMargins(2,2,2,2);
        rlGoods.setLayoutParams(llLayoutOParams);
        rlGoods.setBackgroundResource(R.drawable.gray_border_style);
        int leftPx = DensityUtils.dip2px(getContext(), this.goodsTwoConfig.getLeftMargion());
        int topPx = DensityUtils.dip2px( getContext() , this.goodsTwoConfig.getTopMargion());
        int rightPx = DensityUtils.dip2px( getContext(),this.goodsTwoConfig.getRightMargion());
        int bottomPx = DensityUtils.dip2px(getContext(),this.goodsTwoConfig.getBottomMargion());

        rlGoods.setPadding(leftPx, topPx, rightPx, bottomPx);

        ivPic = new SimpleDraweeView(getContext());
        ivPic.setId( ivPic.hashCode() );
        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels/2;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivPic.setLayoutParams(layoutParams);
        rlGoods.addView(ivPic);

        tvName = new TextView(getContext());
        tvName.setId(tvName.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, ivPic.getId());
        tvName.setLayoutParams(layoutParams);
        tvName.setMaxLines(2);
        //tvName.setTextColor(Color.BLACK);
        rlGoods.addView(tvName);

//        tvDesc = new TextView( getContext());
//        tvDesc.setId( tvDesc.hashCode());
//        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
//        tvDesc.setLayoutParams(layoutParams);
//        rlGoods.addView(tvDesc);

        tvPrice = new TextView( getContext() );
        tvPrice.setId( tvPrice.hashCode());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
        tvPrice.setLayoutParams(layoutParams);
        rlGoods.addView(tvPrice);

        tvJifen = new TextView( getContext() );
        tvJifen.setId( tvJifen.hashCode());

        if(  TextUtils.isEmpty( goodsTwoConfig.getBackground()) ) {
            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, tvPrice.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tvJifen.setLayoutParams(layoutParams);
            //tvJifen.setTextColor(Color.BLACK);
            rlGoods.addView(tvJifen);
        }else {
            ivJifen = new SimpleDraweeView(getContext());
            ivJifen.setId( ivJifen.hashCode());
            int widthPx= DensityUtils.dip2px(getContext() , goodsTwoConfig.getIconWidth());
            layoutParams = new RelativeLayout.LayoutParams( widthPx , ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, ivPic.getId());
            layoutParams.addRule(RelativeLayout.BELOW, tvPrice.getId());
            ivJifen.setLayoutParams(layoutParams);
            rlGoods.addView(ivJifen);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, ivJifen.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, ivJifen.getId());
            tvJifen.setLayoutParams(layoutParams);
            tvJifen.setTextColor(Color.BLACK);
            rlGoods.addView(tvJifen);
        }

        setStyle(good);

        return rlGoods;
    }


    protected void setStyle( GoodsBean good ){
        if( this.goodsTwoConfig.getProduct_showname().equals( Constant.GOODS_SHOW )){
            if(tvName!=null) tvName.setVisibility(VISIBLE);
        }else {
            if(tvName!=null) tvName.setVisibility(GONE);
        }
//        if( this.goodsTwoConfig.getIsShowSyno().equals( Constant.GOODS_SHOW ) ){
//            tvDesc.setVisibility(VISIBLE);
//        }else {
//            tvDesc.setVisibility(GONE);
//        }
        if( this.goodsTwoConfig.getProduct_showprices().equals(Constant.GOODS_SHOW) ){
            tvPrice.setVisibility(VISIBLE);
        }else {
            tvPrice.setVisibility(GONE);
        }
        if( this.goodsTwoConfig.getProduct_userInteger().contains(Constant.GOODS_SHOW) ){
            tvJifen.setVisibility(VISIBLE);
        }else {
            tvJifen.setVisibility(GONE);
        }


        int picWidth = getContext().getResources().getDisplayMetrics().widthPixels/2;
        FrescoDraweeController.loadImage(ivPic, picWidth, good.getThumbnailPic());
        if( tvName!=null) tvName.setText( good.getGoodName() );
        //tvDesc.setText(good.getSpec());

        String priceStr = CommonUtil.formatDouble( good.getPrice());// String.valueOf( good.getMarketPrice() );
        String zpriceStr = CommonUtil.formatPrice( good.getPriceLevel() ); //String.valueOf(good.getPrice());

        if( goodsTwoConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL) ){
            SpanningUtil.set_Price_Format2(tvPrice, priceStr, zpriceStr , Color.WHITE ,Color.WHITE );
        }else{
            SpanningUtil.set_Price_Format1(tvPrice, priceStr, zpriceStr, Color.RED, Color.GRAY);
        }
        String jifenStr = CommonUtil.formatJiFen(good.getScore()); //String.valueOf( good.getRebate() );
        tvJifen.setText( jifenStr +"积分" );

        if( ivJifen!=null && !TextUtils.isEmpty(goodsTwoConfig.getBackground()) ){
            int iconWidth = DensityUtils.dip2px(getContext(), Constant.REBATEICON_WIDTH );
            String bgUrl = Variable.resourceUrl + goodsTwoConfig.getBackground();
            FrescoDraweeController.loadImage(ivJifen, iconWidth, bgUrl);
        }
    }

}
