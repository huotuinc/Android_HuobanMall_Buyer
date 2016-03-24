package com.huotu.android.library.buyer.widget.AssistWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.bean.AsistBean.Guides1Config;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

/**
 * 辅助线组件
 * Created by jinxiangdong on 2016/1/14.
 */
public class Guides1Widget extends BaseLinearLayout {
    private Guides1Config config;
    private Paint paint;
    PathEffect effects;

    public Guides1Widget(Context context , Guides1Config config ) {
        super(context);

        this.config = config;

        this.setBackgroundColor(CommonUtil.parseColor(config.getBackColor()) );
        int topPadding = DensityUtils.dip2px(getContext(), 15);
        int leftPadding = DensityUtils.dip2px(getContext(),15);
        this.setPadding( leftPadding , topPadding,leftPadding,topPadding);

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
