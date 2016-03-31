package com.huotu.android.library.buyer.widget.TextWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huotu.android.library.buyer.bean.TextBean.NavigationConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;
import com.huotu.android.library.buyer.bean.TextBean.LinkConfig;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;
import com.huotu.android.library.buyer.R;

/**
 * Created by Administrator on 2016/1/7.
 */
public class NavigationWidget extends BaseLinearLayout{
    NavigationConfig navigationConfig;

    public NavigationWidget(Context context, NavigationConfig navigationConfig) {
        super(context);

        this.navigationConfig = navigationConfig;

        this.setOrientation(VERTICAL);
        if (navigationConfig.getLinks() == null || navigationConfig.getLinks().size() < 1) return;
        int count = navigationConfig.getLinks().size();
        for (int i = 0; i < count; i++) {
            LinkConfig item = navigationConfig.getLinks().get(i);
            createLayout(item, (i+1) == count  ? false : true);
        }
    }

    protected void createLayout(LinkConfig item, boolean showLine) {
        int rlContent_id = item.hashCode();
        RelativeLayout rlContent = new RelativeLayout(getContext());
        rlContent.setId(rlContent_id);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlContent.setLayoutParams(layoutParams);
        rlContent.setTag(item);
        rlContent.setOnClickListener(this);

        int topPx = DensityUtils.dip2px(getContext(), 10);
        int bottomPx = topPx;
        int leftPx = DensityUtils.dip2px(getContext(), 5);
        int rightPx = leftPx;
        rlContent.setPadding(leftPx, topPx, rightPx, bottomPx);

        this.addView(rlContent);

        int tvArrow_id = item.getLinkName().hashCode();
        TextView tvArrow = new TextView(getContext());
        tvArrow.setId(tvArrow_id);
        tvArrow.setGravity(Gravity.CENTER_VERTICAL);
        tvArrow.setTextColor(Color.GRAY);
        tvArrow.setTextSize(20);
        tvArrow.setTypeface(TypeFaceUtil.FONTAWEOME(getContext()));
        tvArrow.setText(R.string.fa_chevron_right);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvArrow.setLayoutParams(layoutParams1);
        rlContent.addView(tvArrow);

        TextView tvTitle = new TextView(getContext());
        tvTitle.setSingleLine();
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setTextSize( 18 );
        tvTitle.setText(item.getText_Name());
        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams1.addRule(RelativeLayout.LEFT_OF, tvArrow_id);
        tvTitle.setLayoutParams(layoutParams1);
        rlContent.addView(tvTitle);

        if (!showLine) return;

        TextView tvLine = new TextView(getContext());
        this.addView(tvLine);
        int heightPx = DensityUtils.dip2px(getContext(), 1);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        layoutParams.setMargins(leftPx,0,rightPx,0);
        tvLine.setLayoutParams(layoutParams);
        tvLine.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag()!=null && v.getTag() instanceof LinkConfig){
            LinkConfig bean = (LinkConfig)v.getTag();
            String name = bean.getLinkName();
            String url = bean.getLinkUrl();
            CommonUtil.link(name,url);
        }
    }
}
