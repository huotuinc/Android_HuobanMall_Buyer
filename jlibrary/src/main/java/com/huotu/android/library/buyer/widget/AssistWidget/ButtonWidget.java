package com.huotu.android.library.buyer.widget.AssistWidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.AsistBean.ButtonConfig;
import com.huotu.android.library.buyer.bean.AsistBean.LinkBean;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 按钮布局
 * Created by jinxiangdong on 2016/2/18.
 */
public class ButtonWidget extends BaseLinearLayout{
    private ButtonConfig config;
    public ButtonWidget(Context context , ButtonConfig config ) {
        super(context);
        this.config = config;

        this.setOrientation(HORIZONTAL);

        //int heightPx = DensityUtils.dip2px(getContext(), config.getHeight());
        int heightPx = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(CommonUtil.parseColor(config.getBackColor()));

//        RoundRectShape roundRectShape2 = new RoundRectShape( outR,null, null);
//        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
//        shapeDrawable2.setPadding(8, 8, 8, 8);
//        shapeDrawable2.getPaint().setColor(CommonUtil.parseColor(config.getWidgetBorderColor()));
//        shapeDrawable2.getPaint().setStyle(Paint.Style.STROKE);

//        int[] normalState = new int[] {};
//        StateListDrawable stateListDrawable =new StateListDrawable();
//        stateListDrawable.addState( normalState , );

        createButton();
    }

    protected void createButton(){
        if( this.config.getLinks()==null || this.config.getLinks().size()<1) return;

        int radius = DensityUtils.dip2px(getContext(), config.getRadius());

        for(LinkBean bean : this.config.getLinks()){
            Button btn = new Button(getContext());
            btn.setOnClickListener(this);
            btn.setTag(bean);
            btn.setText(bean.getName());
            btn.setTextColor(CommonUtil.parseColor(bean.getFontColor()));
            //btn.setBackgroundColor(CommonUtil.parseColor(bean.getBackColor()));
            int heightPx = DensityUtils.dip2px(getContext(), config.getHeight());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, heightPx , 1.0f);
            int space = DensityUtils.dip2px(getContext(), 4);
            layoutParams.setMargins( space , space, space ,space );
            btn.setPadding( space, space ,space,space );
            btn.setLayoutParams(layoutParams);

            float[] outR = { radius , radius , radius , radius ,radius , radius, radius, radius };
            RectF inRect = new RectF(1f,1f,1f,1f);
            float[] inR = {radius ,radius ,radius ,radius ,radius ,radius ,radius ,radius };

            RoundRectShape roundRectShape1 = new RoundRectShape( outR, null , null );
            ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape1);
            //shapeDrawable1.setPadding(6, 6, 6, 6);
            shapeDrawable1.getPaint().setColor(CommonUtil.parseColor(bean.getBackColor()));
            shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);

            RoundRectShape roundRectShape2 = new RoundRectShape( outR, inRect , inR);
            ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
            //shapeDrawable2.setPadding(6, 6, 6, 6);
            shapeDrawable2.getPaint().setColor(CommonUtil.parseColor(config.getBorderColor()));
            shapeDrawable2.getPaint().setStyle(Paint.Style.STROKE);

            Drawable[] drawables = new Drawable[]{shapeDrawable1,shapeDrawable2};

            LayerDrawable layer = new LayerDrawable(drawables);

            btn.setBackgroundDrawable(layer);
            //btn.setBackgroundColor(CommonUtil.parseColor( bean.getBackColor() ));

            this.addView(btn);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if( v.getTag()!=null && v.getTag() instanceof LinkBean){
            LinkBean bean = (LinkBean)v.getTag();
            golink( bean.getName() , bean.getLinkUrl() );
        }else{
            Logger.e("url error");
        }
    }
}