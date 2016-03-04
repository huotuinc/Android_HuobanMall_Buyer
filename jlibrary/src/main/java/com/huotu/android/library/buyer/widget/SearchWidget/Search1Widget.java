package com.huotu.android.library.buyer.widget.SearchWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.SearchBean.Search1Config;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;


/**
 * 搜索组件一
 * Created by jinxiangdong on 2016/1/14.
 */
public class Search1Widget  extends LinearLayout {
    private Search1Config config;
    private TextView tvName;
    private TextView tvSearch;

    public Search1Widget(Context context , Search1Config config) {
        super(context);

        this.config = config;

        if( this.config.getSearch_style().equals(Constant.CUSTOM_SEARCH_STYLE_A )){
            setStyle1();
        }else if(this.config.getSearch_style().equals(Constant.CUSTOM_SEARCH_STYLE_B)){
            setStyle2();
        }
    }

    protected void setStyle1(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.search_one_2,this,true);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int leftpx = DensityUtils.dip2px(getContext(), config.getPaddingLeft());
        int toppx = DensityUtils.dip2px(getContext(), config.getPaddingTop());
        //layoutParams.setMargins(leftpx, toppx, leftpx, toppx);
        this.setPadding( leftpx , toppx , leftpx , toppx );
        this.setBackgroundColor( Color.parseColor( config.getSearch_background() ) );
        this.setLayoutParams(layoutParams);

        tvSearch = (TextView)findViewById(R.id.search_one_search);

        tvName=(TextView)findViewById(R.id.search_one_text);

        tvSearch.setTypeface(TypeFaceUtil.FONTAWEOME( getContext() ));
    }

    protected void setStyle2(){

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        layoutInflater.inflate(R.layout.search_one,this,true);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int leftpx = DensityUtils.dip2px( getContext() , config.getPaddingLeft());
        int toppx = DensityUtils.dip2px( getContext() , config.getPaddingTop());
        //layoutParams.setMargins( leftpx , toppx , leftpx , toppx );
        this.setPadding( leftpx , toppx , leftpx , toppx );
        this.setBackgroundColor(Color.parseColor(config.getSearch_background()));

        this.setLayoutParams(layoutParams);

        tvSearch = (TextView)findViewById(R.id.search_one_search);
        tvName=(TextView)findViewById(R.id.search_one_text);
    }
}
