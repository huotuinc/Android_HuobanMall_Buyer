package com.huotu.android.library.buyer.widget.SearchWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.SearchBean.Search2Config;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

/**
 * 搜索组件二
 * Created by jinxiangdong on 2016/1/14.
 */
public class Search2Widget extends RelativeLayout {
    private Search2Config config;

    public Search2Widget(Context context , Search2Config config ) {
        super(context);
        this.config = config;

        int topPx= DensityUtils.dip2px(getContext(), 10);
        int leftPx = DensityUtils.dip2px( getContext() , 10);
        this.setPadding( leftPx ,topPx ,leftPx,topPx );
        this.setBackgroundColor( Color.parseColor( config.getWidgetBackColor()) );

        TextView tvSearch = new TextView(getContext());
        int tvSearch_id = tvSearch.hashCode();
        tvSearch.setId(tvSearch_id);
        leftPx = DensityUtils.dip2px(getContext(), 10);
        topPx = DensityUtils.dip2px(getContext(),10);
        tvSearch.setPadding(leftPx, topPx, leftPx, topPx);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvSearch.setBackgroundResource(R.color.orangered);
        tvSearch.setTextColor(Color.WHITE);
        tvSearch.setLayoutParams(layoutParams);
        tvSearch.setText("搜索");
        tvSearch.setTextSize(18);
        this.addView(tvSearch);

        EditText etText = new EditText(getContext());
        int etText_id = etText.hashCode();
        etText.setId(etText_id);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, tvSearch_id);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftPx = DensityUtils.dip2px(getContext(),10);
        layoutParams.setMargins(0 , 0, leftPx, 0);
        leftPx = DensityUtils.dip2px(getContext(),30);
        topPx = DensityUtils.dip2px(getContext(), 10);
        etText.setPadding( leftPx , topPx , leftPx , topPx );
        etText.setLayoutParams(layoutParams);
        etText.setSingleLine();
        etText.setHint("商品搜索:请输入商品关键字");
        etText.setBackgroundResource(R.drawable.gray_round_border_style);
        this.addView(etText);

        TextView tvLeftPic = new TextView(getContext());
        tvLeftPic.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));
        tvLeftPic.setText(R.string.fa_search);
        layoutParams =new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, etText_id);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftPx = DensityUtils.dip2px(getContext(),5);
        layoutParams.setMargins(leftPx, leftPx, leftPx, leftPx);
        tvLeftPic.setLayoutParams(layoutParams);
        tvLeftPic.setTextSize(18);
        this.addView(tvLeftPic);

        TextView tvRightPic = new TextView(getContext());
        tvRightPic.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));
        tvRightPic.setText(R.string.fa_times_circle);
        layoutParams =new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, etText_id);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftPx = DensityUtils.dip2px(getContext(),15);
        layoutParams.setMargins(leftPx, 0, leftPx/2, 0);
        tvRightPic.setLayoutParams(layoutParams);
        tvRightPic.setTextSize(18);
        this.addView(tvRightPic);


    }
}
