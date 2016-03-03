package com.huotu.android.library.buyer.widget.AssistWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.AsistBean.GuidesShopConfig;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

/**
 * Created by jinxiangdong on 2016/1/14.
 * 进入店铺组件
 */
public class GuidesShopWidget extends RelativeLayout {
    private GuidesShopConfig config;
    private SimpleDraweeView ivLogo;
    private TextView tvShopName;
    private TextView tvTip;

    public GuidesShopWidget(Context context , GuidesShopConfig config ) {
        super(context);

        this.config = config;

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int leftPx = DensityUtils.dip2px(context, 10);
        int topPx = DensityUtils.dip2px(context,10);
        int rightPx = leftPx;
        int bottomPx = 0;
        this.setPadding(0,topPx,0,0);

        ivLogo = new SimpleDraweeView(context);
        int ivLogo_id = ivLogo.hashCode();
        ivLogo.setId(ivLogo_id);
        int logoWidth = 45;
        int logoWidthPx = DensityUtils.dip2px( getContext() , logoWidth);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( logoWidthPx, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivLogo.setLayoutParams(layoutParams);
        this.addView(ivLogo);

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
        this.addView(tvArrow);

        tvTip = new TextView(getContext());
        Integer tvTip_id = tvTip.hashCode();
        tvTip.setId( tvTip_id );
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF , tvArrow_id );
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvTip.setLayoutParams(layoutParams);
        tvTip.setTypeface(TypeFaceUtil.FONTAWEOME(context));
        tvTip.setText("进入店铺 ");
        this.addView(tvTip);

        tvShopName = new TextView(getContext());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule( RelativeLayout.LEFT_OF , tvTip_id );
        layoutParams.addRule(RelativeLayout.RIGHT_OF, ivLogo_id);
        layoutParams.addRule( RelativeLayout.CENTER_VERTICAL );
        tvShopName.setText("我的店铺");
        tvShopName.setTextColor(Color.BLACK);
        tvShopName.setTextSize(18);
        tvShopName.setLayoutParams(layoutParams);
        this.addView(tvShopName);

        TextView tvLine = new TextView(context);
        int heightPx = DensityUtils.dip2px(context,1);
        tvLine.setBackgroundResource( R.color.lightgraywhite );
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        layoutParams.setMargins(0,topPx+heightPx,0,0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM );
        tvLine.setLayoutParams(layoutParams);

        this.addView(tvLine);

        //TODO
        //FrescoDraweeController.loadImage( ivLogo , logoWidthPx , config. );
    }

    public void loadData(){

    }

}
