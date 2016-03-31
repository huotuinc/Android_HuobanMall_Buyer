package com.huotu.android.library.buyer.widget.AssistWidget;

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
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.Data.SmartUiEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.AsistBean.GuidesShopConfig;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import okhttp3.internal.framed.Variant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jinxiangdong on 2016/1/14.
 * 进入店铺组件
 */
public class GuidesShopWidget extends BaseLinearLayout {
    private GuidesShopConfig config;
    private SimpleDraweeView ivLogo;
    private TextView tvShopName;
    private TextView tvTip;

    public GuidesShopWidget(Context context , GuidesShopConfig config ) {
        super(context);
        this.config = config;

        int leftPx = DensityUtils.dip2px(context, 10);
        int topPx = DensityUtils.dip2px(context, 10);
        int rightPx = leftPx;
        int bottomPx = topPx;
        this.setPadding(0, topPx, 0, bottomPx );


        RelativeLayout rl = new RelativeLayout(getContext());
        LinearLayout.LayoutParams llayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(llayoutParams);
        //rl.setOnClickListener(this);
        this.addView(rl);

        ivLogo = new SimpleDraweeView(context);
        int ivLogo_id = ivLogo.hashCode();
        ivLogo.setId(ivLogo_id);
        int logoWidth = 40;
        int logoWidthPx = DensityUtils.dip2px( getContext() , logoWidth);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( logoWidthPx, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivLogo.setLayoutParams(layoutParams);
        rl.addView(ivLogo);

        TextView tvArrow = new TextView(getContext());
        Integer tvArrow_id = tvArrow.hashCode();
        tvArrow.setId( tvArrow_id );
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMargins(leftPx,0,leftPx,0);
        tvArrow.setLayoutParams(layoutParams);
        tvArrow.setTypeface(TypeFaceUtil.FONTAWEOME(context));
        tvArrow.setTextSize(14);
        tvArrow.setText(R.string.fa_chevron_right);
        rl.addView(tvArrow);

        tvTip = new TextView(getContext());
        Integer tvTip_id = tvTip.hashCode();
        tvTip.setId( tvTip_id );
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF , tvArrow_id );
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvTip.setLayoutParams(layoutParams);
        tvTip.setTypeface(TypeFaceUtil.FONTAWEOME(context));
        tvTip.setText("进入店铺 ");
        rl.addView(tvTip);

        tvShopName = new TextView(getContext());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule( RelativeLayout.LEFT_OF , tvTip_id );
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ivLogo_id);
        layoutParams.addRule( RelativeLayout.CENTER_VERTICAL );
        tvShopName.setText("我的店铺");
        tvShopName.setTextColor(Color.BLACK);
        tvShopName.setTextSize(18);
        tvShopName.setLayoutParams(layoutParams);
        rl.addView(tvShopName);

        TextView tvLine = new TextView(context);
        int heightPx = DensityUtils.dip2px(context,1);
        tvLine.setBackgroundResource( R.color.lightgraywhite );
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        layoutParams.setMargins(0,topPx+heightPx,0,0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM );
        tvLine.setLayoutParams(layoutParams);

        rl.addView(tvLine);

        asyncLoadData();
    }

    public void asyncLoadData(){
        BizApiService bizApiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        int customerId= Variable.CustomerId;

        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);

        Call<BizBaseBean<MallInfoBean>> call = bizApiService.getMallInfo(key, random, secure, customerId);
        call.enqueue(new LogoCallBack(this));
    }

    public class LogoCallBack implements Callback<BizBaseBean<MallInfoBean>> {
        WeakReference<GuidesShopWidget> ref;
        public LogoCallBack(GuidesShopWidget widget){
            this.ref =  new WeakReference<>(widget);
        }

        @Override
        public void onResponse(Response<BizBaseBean<MallInfoBean>> response) {
            if( ref.get()==null)return;
            if( response==null||response.code() !=200 || response.body() ==null || response.body().getData()==null){
                Logger.e(response.message());
                return;
            }
            String logoUrl = response.body().getData().getLogo();
            //String indexUrl = response.body().getData().getIndexUrl();
            int imageWidthPx = DensityUtils.dip2px(ref.get().getContext(), 30);
            FrescoDraweeController.loadImage(ref.get().ivLogo, imageWidthPx, logoUrl);
            String shopName = response.body().getData().getMallName();
            ref.get().tvShopName.setText( shopName );
            ref.get().setTag(response.body().getData());
            ref.get().setOnClickListener(ref.get());
        }

        @Override
        public void onFailure(Throwable t) {
            Logger.e(t.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()!=null && v.getTag() instanceof MallInfoBean ) {
            String url = Variable.mainUiConfigUrl;
            EventBus.getDefault().post(new SmartUiEvent(url,false));
        }else{
            Logger.e("url error");
        }
    }
}
