package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.TextBean.ArticleTitleConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;


/**
 * 微信标题组件
 * Created by jinxiangdong on 2016/1/7.
 */
public class ArticleTitleWidget extends LinearLayout{
    private ArticleTitleConfig articleTitleConfig;
    private TextView tvTitle;
    private TextView tvDateAuthorLink;

    public ArticleTitleWidget(Context context , ArticleTitleConfig articleTitleConfig ) {
        super(context);

        this.articleTitleConfig = articleTitleConfig;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.text_articletitle, this, true);

        //rl.setPadding( articleTitleConfig.getLeftMargion() , articleTitleConfig.getTopMargion() , articleTitleConfig.getRightMargion() , articleTitleConfig.getBottomMargion() );
        tvTitle = (TextView)findViewById(R.id.text_articletitle_title);
        tvDateAuthorLink = (TextView) findViewById(R.id.text_articletitle_date_author_link);

        tvTitle.setText( articleTitleConfig.getTitle_name() );

        String content = articleTitleConfig.getTitle_time() + " " + articleTitleConfig.getTitle_author() +" " + articleTitleConfig.getTitle_linkname();
        SpannableString spannableString = new SpannableString( content );
        int startIndex = articleTitleConfig.getTitle_time().length() + 1 + articleTitleConfig.getTitle_author().length() +1;
        int endIndex = content.length();
        spannableString.setSpan(new LinkClickableSpan(articleTitleConfig.getLinkUrl()), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tvDateAuthorLink.setText(spannableString);
        tvDateAuthorLink.setMovementMethod(LinkMovementMethod.getInstance());

        if(articleTitleConfig.getTitle_position().equals(Constant.TEXT_LEFT)){
            tvTitle.setGravity(Gravity.LEFT);
            tvDateAuthorLink.setGravity(Gravity.LEFT);
        }else if(articleTitleConfig.getTitle_position().equals(Constant.TEXT_CENTER)){
            tvTitle.setGravity(Gravity.CENTER);
            tvDateAuthorLink.setGravity(Gravity.CENTER);
        }else if(articleTitleConfig.getTitle_position().equals(Constant.TEXT_RIGHT)){
            tvTitle.setGravity(Gravity.RIGHT);
            tvDateAuthorLink.setGravity(Gravity.RIGHT);
        }
    }

    class LinkClickableSpan extends ClickableSpan{
        private String link;
        public LinkClickableSpan(String link ) {
            super();
            this.link=link;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor( getResources().getColor( R.color.steelblue ) );
        }

        @Override
        public void onClick(View widget) {
            String url = articleTitleConfig.getLinkUrl();
            String name = articleTitleConfig.getTitle_name();
            CommonUtil.link( name , url);
        }
    }
}
