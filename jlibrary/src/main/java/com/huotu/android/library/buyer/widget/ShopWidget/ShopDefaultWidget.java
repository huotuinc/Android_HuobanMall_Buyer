package com.huotu.android.library.buyer.widget.ShopWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.ShopBean.ShopDefaultConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺头默认组件
 * Created by jinxiangdong on 2016/1/25.
 */
public class ShopDefaultWidget extends RelativeLayout implements View.OnClickListener{
    private SimpleDraweeView ivBg;
    private SimpleDraweeView ivAvator;
    private TextView tvShopName;
    private TextView tvGoodsCount;
    private TextView tvNewGoodsCount;
    private TextView tvOrder;
    private ShopDefaultConfig config;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private MallInfoBean mallInfoBean;

    public ShopDefaultWidget(Context context , ShopDefaultConfig config ) {
        super(context);

        this.config =config;

        createLayout();

        asyncGetData();
    }

    protected void createLayout(){
        LayoutInflater.from(getContext()).inflate( R.layout.shop_default,this,true);

        ll1 = (LinearLayout)this.findViewById(R.id.shop_default_ll1);
        ll1.setOnClickListener(this);
        ll2=(LinearLayout)this.findViewById(R.id.shop_default_ll2);
        ll2.setOnClickListener(this);
        ll3 = (LinearLayout)this.findViewById(R.id.shop_default_ll3);
        ll3.setOnClickListener(this);
        ivBg = (SimpleDraweeView)this.findViewById(R.id.shop_default_bg);
        ivAvator=(SimpleDraweeView)this.findViewById(R.id.shop_default_avorator);
        ivAvator.setBackgroundResource(R.drawable.gray_round_border_style);
        tvGoodsCount = (TextView)this.findViewById(R.id.shop_default_goodscount);
        tvNewGoodsCount = (TextView)this.findViewById(R.id.shop_default_newgoodscount);
        tvShopName = (TextView)this.findViewById(R.id.shop_default_shopname);
        tvNewGoodsCount = (TextView)this.findViewById(R.id.shop_default_newgoodscount);
        tvOrder = (TextView)this.findViewById(R.id.shop_default_order);
        tvOrder.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));

        if(  config.getShow_type() == Constant.WIDGETBACKTYPE_COLOR ){
            ivBg.setBackgroundColor(CommonUtil.parseColor( config.getColor()) );
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.shop_default_ll1){//跳转到全部商品页面
            String url = mallInfoBean.getGoodsListUrl();
            String name = "";
            CommonUtil.link( name , url );
            //EventBus.getDefault().post(new LinkEvent( name , url ));
        }else if( v.getId()==R.id.shop_default_ll3){//跳转到订单页面
            String url = mallInfoBean.getOrderListUrl();
            String name = "";
            EventBus.getDefault().post(new LinkEvent( name , url ));
        }
    }

    protected void asyncGetData() {
        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        int customerid= Variable.CustomerId;

        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure( Variable.BizKey , Variable.BizAppSecure , random);

        Call<BizBaseBean<MallInfoBean>> call =
                bizApiService.getMallInfo(
                        key,
                        random,
                        secure,
                        customerid );

        call.enqueue(new Callback<BizBaseBean<MallInfoBean>>() {
            @Override
            public void onResponse(Response<BizBaseBean<MallInfoBean>> response) {
                if( response ==null || response.code() != 200 ) {
                    Toast.makeText(getContext(),response.message(),Toast.LENGTH_LONG).show();
                    return;
                }
                mallInfoBean = response.body().getData();
                tvShopName.setText(  response.body().getData().getMallName() );
                tvGoodsCount.setText( String.valueOf( response.body().getData().getGoodNum()  ));
                tvNewGoodsCount.setText(String.valueOf( response.body().getData().getNewGoodNum() ));
                //tvOrder.setText( String.valueOf( response.body().getData().getOrderNum() ) );
                int widthPx = DensityUtils.dip2px(getContext(), 90);
                int width = ivAvator.getLayoutParams() == null ? widthPx : ivAvator.getLayoutParams().width;
                FrescoDraweeController.loadImage(ivAvator, width, response.body().getData().getLogo());
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
            }
        });

    }
}
