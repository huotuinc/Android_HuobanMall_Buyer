package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.TextBean.RichTextConfig;

/**
 * Created by Administrator on 2016/1/7.
 */
public class RichTextWidget extends LinearLayout {
    WebView wbHtmlText;
    RichTextConfig richTextConfig;

    public RichTextWidget(Context context , RichTextConfig richTextConfig) {
        super(context);

        this.richTextConfig = richTextConfig;
        wbHtmlText = new WebView(context);
        this.addView(wbHtmlText);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wbHtmlText.setLayoutParams(layoutParams);

        wbHtmlText.loadData( this.richTextConfig.getContent() , "text/html;charset=UTF-8", null );
    }
}
