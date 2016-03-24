package com.huotu.android.library.buyer.widget.PromotionsWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huotu.android.library.buyer.bean.PromotionsBean.Promotion1Config;
import com.huotu.android.library.buyer.bean.PromotionsBean.PromotionBean;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

/**
 * Created by Administrator on 2016/1/29.
 */
public class Promotion1Widget extends BaseLinearLayout {
    private Promotion1Config config;

    public Promotion1Widget(Context context , Promotion1Config config) {
        super(context);

        this.config = config;

        this.setOrientation(HORIZONTAL);
        int leftPx = DensityUtils.dip2px(context, 10);
        int topPx = leftPx;
        int rightPx=leftPx;
        int bottomPx = leftPx;
        this.setPadding( leftPx,  topPx , rightPx,bottomPx );

        if( config==null || config.getCoupns()==null||config.getCoupns().size()<1) return;

        if( config.getCoupns().size()>0){
            createLayout( config.getCoupns().get(0) , R.drawable.promotion_border_one_style , "#fa5262" , 0  );
        }
        if( config.getCoupns().size()>1){
            createLayout( config.getCoupns().get(1) , R.drawable.promotion_border_two_style , "#7acf8d" , 1 );
        }
        if( config.getCoupns().size()>2){
            createLayout( config.getCoupns().get(2) , R.drawable.promotion_border_three_style , "#ff9664" ,1);
        }
    }

    private void createLayout( PromotionBean bean , int borderStyle , String fontColor , int leftMargion){
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        int px = DensityUtils.dip2px(getContext(), leftMargion);
        layoutParams.setMargins(px, 0, 0, 0);
        ll.setBackgroundResource(borderStyle);
        ll.setLayoutParams(layoutParams);
        ll.setPadding(2, 0, 2, 0);
        this.addView(ll);
        ll.setOnClickListener(this);

        TextView tvPrice = new TextView(getContext());
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        int topPx = DensityUtils.dip2px(getContext(), 15);
        layoutParams.setMargins(0, topPx , 0, topPx);
        layoutParams.gravity = Gravity.CENTER;
        tvPrice.setLayoutParams(layoutParams);
        tvPrice.setText(bean.getPrices());
        tvPrice.setTextColor(CommonUtil.parseColor(fontColor));
        tvPrice.setGravity(Gravity.CENTER_HORIZONTAL);
        tvPrice.setSingleLine();
        tvPrice.setEllipsize(TextUtils.TruncateAt.END);
        tvPrice.setTextSize(22);
        ll.addView(tvPrice);

        TextView tvName =new TextView(getContext());
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        topPx = DensityUtils.dip2px(getContext(), 15);
        layoutParams.setMargins(0, 0 , 0, topPx);
        layoutParams.gravity = Gravity.CENTER;
        tvName.setLayoutParams(layoutParams);
        tvName.setGravity(Gravity.CENTER_HORIZONTAL);
        tvName.setText(bean.getName());
        tvName.setTextColor(CommonUtil.parseColor(fontColor));
        tvName.setSingleLine();
        tvName.setTextSize(14);
        ll.addView(tvName);
        ll.setTag(bean);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(v.getTag()!=null && v.getTag() instanceof PromotionBean ){
            PromotionBean bean = (PromotionBean)v.getTag();
            String url = bean.getPageUrl();
            String name = bean.getName();
            CommonUtil.link(name , url);
        }
    }
}
