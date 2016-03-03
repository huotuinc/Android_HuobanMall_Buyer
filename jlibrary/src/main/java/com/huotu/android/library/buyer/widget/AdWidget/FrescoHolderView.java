package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;


/**
 * Created by Administrator on 2016/1/13.
 */
public class FrescoHolderView implements Holder<AdImageBean> {
    private SimpleDraweeView iv;
    private int width;
    public FrescoHolderView(int w){
        this.width = w;
    }
    @Override
    public View createView(Context context ) {
        iv = new SimpleDraweeView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        return iv;
    }
    @Override
    public void UpdateUI(Context context,int position, AdImageBean data) {
        FrescoDraweeController.loadImage(iv, width, data.getImageUrl());
    }
}
