package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.TextBean.ArticleTitleConfig;


/**
 * 微信标题组件
 * Created by jinxiangdong on 2016/1/7.
 */
public class ArticleTitleWidget extends LinearLayout{
    private ArticleTitleConfig articleTitleConfig;
    private TextView tvTitle;
    private RelativeLayout rl;
    private TextView tvDateAuthorLink;

    public ArticleTitleWidget(Context context , ArticleTitleConfig articleTitleConfig ) {
        super(context);

        this.articleTitleConfig = articleTitleConfig;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.text_articletitle, this, true);

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.text_articletitle_rl);
        rl.setPadding( articleTitleConfig.getLeftMargion() , articleTitleConfig.getTopMargion() , articleTitleConfig.getRightMargion() , articleTitleConfig.getBottomMargion() );
        tvTitle = (TextView)findViewById(R.id.text_articletitle_title);
        tvDateAuthorLink = (TextView) findViewById(R.id.text_articletitle_date_author_link);

        tvTitle.setText( articleTitleConfig.getSubjectName() );

        String content = articleTitleConfig.getDate() + " " + articleTitleConfig.getAuthor() +" " + articleTitleConfig.getLinkName();
        SpannableString spannableString = new SpannableString( content );
        int startIndex = articleTitleConfig.getDate().length() + 1 + articleTitleConfig.getAuthor().length() +1;
        int endIndex = content.length();
        //spannableString.setSpan( new ForegroundColorSpan( getResources().getColor( R.color.steelblue )), startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan( new LinkClickableSpan( articleTitleConfig.getLinkName() ) ,startIndex , endIndex , Spanned.SPAN_INCLUSIVE_INCLUSIVE );

        tvDateAuthorLink.setText(spannableString);
        tvDateAuthorLink.setMovementMethod(LinkMovementMethod.getInstance());
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
            Toast.makeText(widget.getContext(), link,Toast.LENGTH_LONG).show();
        }
    }


}
