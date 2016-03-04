package com.huotu.android.library.buyer.widget.AssistWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.AsistBean.Guides2Config;


/**
 * Created by Administrator on 2016/1/14.
 */
public class Guides2Widget extends FrameLayout {
    private Guides2Config config;

    public Guides2Widget(Context context , Guides2Config config ) {
        super(context);

        this.config = config;

        TextView tvBg = new TextView(getContext());
        TextView tvTitle = new TextView(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);

        tvBg.setBackgroundResource(R.color.gray);
        int top = config.getPaddingTop();
        int bottom = config.getPaddingTop();
        int left = config.getPaddingLeft();
        int right = config.getPaddingLeft();
        int leftpx = DensityUtils.dip2px(getContext(), left);
        int toppx = DensityUtils.dip2px( getContext() , top );
        layoutParams.setMargins(leftpx, toppx, leftpx, toppx);
        layoutParams.gravity = Gravity.CENTER;
        tvBg.setPadding(leftpx, toppx, leftpx, toppx);
        tvBg.setLayoutParams(layoutParams);
        tvBg.setBackgroundResource(R.color.gray);


        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //toppx = DensityUtils.dip2px(getContext() , 5 );
        layoutParams.setMargins( leftpx , toppx , leftpx , toppx );

        layoutParams.gravity = Gravity.CENTER;
        tvTitle.setLayoutParams(layoutParams);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setBackgroundColor(Color.parseColor( config.getBackColor()));
        int px = DensityUtils.dip2px(getContext() , 8);
        tvTitle.setPadding( px ,0, px ,0);
        tvTitle.setTextColor(Color.parseColor(  config.getFontColor()));
        tvTitle.setText(config.getName());
        tvTitle.setTextSize(18);

        this.setBackgroundColor(Color.parseColor(  config.getBackColor()));

        this.addView(tvBg);
        this.addView(tvTitle);

        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(llLayoutParams);

    }
}
