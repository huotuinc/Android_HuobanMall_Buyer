package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.TextBean.TitleConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

/**
 * 标题组件
 * Created by jinxiangdong on 2016/1/7.
 *
 */
public class TitleWidget extends BaseLinearLayout {
    TitleConfig config;
    TextView tvTitle;
    TextView tvSubTitle;
    LinearLayout ll;

    public TitleWidget(Context context, TitleConfig config) {
        super(context);

        this.config = config;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.text_title, this, true);

        ll = (LinearLayout)findViewById(R.id.text_title_ll);
        tvTitle= (TextView)findViewById(R.id.text_title_title);
        tvSubTitle = (TextView)findViewById(R.id.text_title_subTitle);

        if( this.config.getTitle_position().equals(Constant.TEXT_LEFT) ){
            tvTitle.setGravity(Gravity.LEFT);
            tvSubTitle.setGravity(Gravity.LEFT);
        }else if( this.config.getTitle_position().equals( Constant.TEXT_CENTER)){
            tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            tvSubTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        }else if( this.config.getTitle_position().equals(Constant.TEXT_RIGHT )){
            tvTitle.setGravity(Gravity.RIGHT);
            tvSubTitle.setGravity(Gravity.RIGHT);
        }

        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.getFontSize() );
        int bgColor = CommonUtil.parseColor(this.config.getTitle_background());
        ll.setBackgroundColor(bgColor);

        String title = this.config.getTitle_name();
        if(!TextUtils.isEmpty(this.config.getTitle_linkname())){
            title += " - " + this.config.getTitle_linkname();
        }

        tvTitle.setText( title );
        this.tvTitle.setOnClickListener(this);
        tvSubTitle.setText( this.config.getTitle_subname() );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        if( v.getId()== R.id.text_title_title){
            CommonUtil.link( config.getTitle_name() , config.getLinkUrl() );
        }
    }
}
