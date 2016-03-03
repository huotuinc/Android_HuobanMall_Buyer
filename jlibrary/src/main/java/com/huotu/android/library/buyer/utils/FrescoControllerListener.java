package com.huotu.android.library.buyer.utils;

import android.graphics.drawable.Animatable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FrescoControllerListener extends BaseControllerListener {
    WeakReference< SimpleDraweeView> ref;
    int width;

    public FrescoControllerListener(SimpleDraweeView iv, int width){
        this.ref= new WeakReference<SimpleDraweeView>(iv);
        this.width=width;
    }

    @Override
    public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
        super.onFinalImageSet(id, imageInfo, animatable);

        if( ref.get() ==null ) return;

        ImageInfo info = (ImageInfo)imageInfo;
        int h = info.getHeight();
        int w = info.getWidth();

        int ivw = width;
        int ivh = h* ivw / w;
        ViewGroup.LayoutParams layoutParams = ref.get().getLayoutParams();
        //layoutParams.height = ivh;
        layoutParams.width = ivw;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        ref.get().setLayoutParams(layoutParams);

        float ratio = w * 1.0f/h;
        ref.get().setAspectRatio(ratio);
        //ref.get().postInvalidate();
    }
}
