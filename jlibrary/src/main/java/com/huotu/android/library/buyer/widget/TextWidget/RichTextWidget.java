package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.TextBean.RichTextConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;

/**
 * 富文本 组件
 * Created by jinxiangdong on 2016/1/7.
 */
public class RichTextWidget extends LinearLayout {
    WebView wbHtmlText;
    RichTextConfig richTextConfig;

    public RichTextWidget(Context context , RichTextConfig richTextConfig) {
        super(context);
        this.richTextConfig = richTextConfig;
        this.setBackgroundColor(CommonUtil.parseColor( richTextConfig.getUeditorBackColor() ));

        int padLeft = DensityUtils.dip2px( getContext() , richTextConfig.getPaddingLeft() );
        int padRight= padLeft;
        int padTop = DensityUtils.dip2px(getContext(), richTextConfig.getPaddingTop());
        int padBotton = padTop;
        this.setPadding( padLeft , padTop , padRight , padBotton );

        wbHtmlText = new WebView(context);
        wbHtmlText.setBackgroundColor( CommonUtil.parseColor(richTextConfig.getUeditorBackColor()) );
        this.addView(wbHtmlText);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wbHtmlText.setLayoutParams(layoutParams);
        wbHtmlText.loadData( this.richTextConfig.getUeditorValue() , "text/html;charset=UTF-8", null );
    }
}
