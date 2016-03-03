package com.huotu.android.library.buyer.widget.AssistWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.bean.AsistBean.Guides1Config;

/**
 * 辅助线组件
 * Created by jinxiangdong on 2016/1/14.
 */
public class Guides1Widget extends LinearLayout {
    private Guides1Config config;
    private Paint paint;
    PathEffect effects;

    public Guides1Widget(Context context , Guides1Config config ) {
        super(context);

        this.config = config;

        this.setBackgroundColor(Color.parseColor( config.getWidgetBackColor() ) );
        int topPadding = DensityUtils.dip2px(getContext(), 15);
        int leftPadding = DensityUtils.dip2px(getContext(),15);
        this.setPadding( leftPadding , topPadding,leftPadding,topPadding);

//        TextView tvLine = new TextView(context);
//        int heightPx = DensityUtils.dip2px( getContext(), 1 );
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
//        layoutParams.gravity = Gravity.CENTER_VERTICAL;
//        tvLine.setLayoutParams(layoutParams);
//        tvLine.setText("sdfadfasdfsdfa");
//        tvLine.setTextColor(Color.GREEN);
//        tvLine.setBackgroundResource(R.drawable.dotted_line_style);
//        this.addView(tvLine);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        effects = new DashPathEffect(new float[] { 8 , 6 }, 1);
        paint.setPathEffect(effects);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;
        int startY = this.getHeight()/2;
        int endX = getWidth();
        int endY = startY;
        Path path = new Path();
        path.moveTo(startX,startY);
        path.lineTo(endX,endY);
        canvas.drawPath( path , paint);
    }
}
