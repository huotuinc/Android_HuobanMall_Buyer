package com.huotu.android.library.buyer.widget.SearchWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.android.library.buyer.ConfigApiService;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.SearchBean.Search2Config;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 搜索组件二
 * Created by jinxiangdong on 2016/1/14.
 */
public class Search2Widget extends RelativeLayout implements View.OnClickListener , TextWatcher , Callback<Object>{
    private Search2Config config;
    private TextView tvRightPic;
    private EditText etText;
    private TextView tvSearch;

    public Search2Widget(Context context , Search2Config config ) {
        super(context);
        this.config = config;

        int topPx= DensityUtils.dip2px(getContext(), 10);
        int leftPx = DensityUtils.dip2px( getContext() , 10);
        this.setPadding( leftPx ,topPx ,leftPx,topPx );
        this.setBackgroundColor( Color.parseColor( config.getSearch_background()) );

        tvSearch = new TextView(getContext());
        int tvSearch_id = tvSearch.hashCode();
        tvSearch.setId(tvSearch_id);
        tvSearch.setOnClickListener(this);
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

        etText = new EditText(getContext());
        int etText_id = etText.hashCode();
        etText.setId(etText_id);
        etText.addTextChangedListener(this);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, tvSearch_id);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftPx = DensityUtils.dip2px(getContext(),10);
        layoutParams.setMargins(0 , 0, leftPx, 0);
        leftPx = DensityUtils.dip2px(getContext(),30);
        topPx = DensityUtils.dip2px(getContext(), 10);
        etText.setPadding(leftPx, topPx, leftPx, topPx);
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

        tvRightPic = new TextView(getContext());
        tvRightPic.setId( tvRightPic.hashCode() );
        tvRightPic.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));
        tvRightPic.setText(R.string.fa_times_circle);
        tvRightPic.setOnClickListener(this);
        layoutParams =new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, etText_id);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftPx = DensityUtils.dip2px(getContext(),15);
        layoutParams.setMargins(leftPx, 0, leftPx/2, 0);
        tvRightPic.setLayoutParams(layoutParams);
        tvRightPic.setTextSize(18);
        this.addView(tvRightPic);

    }

    @Override
    public void onClick(View v) {
        if( v.getId()== tvRightPic.getId()){
            etText.setText("");
        }else if( v.getId()==tvSearch.getId()){
            //String url = config.get
            getSearchPage();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(TextUtils.isEmpty( etText.getText())){
            if( tvRightPic!=null) tvRightPic.setVisibility(GONE);
        }else{
            if( tvRightPic!=null) tvRightPic.setVisibility(VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected void getSearchPage(){
        ConfigApiService configApiService = RetrofitUtil.getConfigInstance().create(ConfigApiService.class);

        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid = Variable.CustomerId;

        Call<Object> call = configApiService.findByMerchantId( key , random , secure , customerid );
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<Object> response) {
        String str = response.message();
    }

    @Override
    public void onFailure(Throwable t) {
        Logger.e(t.getMessage());
    }
}
