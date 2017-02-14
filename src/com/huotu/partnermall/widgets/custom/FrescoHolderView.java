package com.huotu.partnermall.widgets.custom;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.partnermall.model.AdImageBean;

/**
 * Created by Administrator on 2016/1/13.
 */
public class FrescoHolderView implements Holder<AdImageBean> {
    private SimpleDraweeView iv;
    private int width;
    private FrescoControllerListener.ImageCallback imageCallback;
    private int defaultImageId;

    public FrescoHolderView(int w , FrescoControllerListener.ImageCallback imageCallback){
        this.width = w;
        this.imageCallback = imageCallback;
    }

    public FrescoHolderView(int w , FrescoControllerListener.ImageCallback imageCallback , int defaultImageId){
        this.width = w;
        this.imageCallback = imageCallback;
        this.defaultImageId = defaultImageId;
    }

    @Override
    public View createView(Context context ) {
        iv = new SimpleDraweeView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        if( defaultImageId >0 ) {
            iv.getHierarchy().setPlaceholderImage(defaultImageId);
        }

        return iv;
    }
    @Override
    public void UpdateUI(Context context, int position, AdImageBean data) {

        String imageUrl =data.getImageUrl();
        FrescoDraweeController.loadImage(iv, width,  imageUrl , imageCallback );
    }
}
