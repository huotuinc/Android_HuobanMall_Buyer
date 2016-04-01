package com.huotu.android.library.buyer.utils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineFactory;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FrescoDraweeController {
    public static void loadImage( SimpleDraweeView simpleDraweeView , int width , String url){
        DraweeController draweeController = Fresco
                .newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .setUri( url )
                .setOldController(simpleDraweeView.getController())
                .setControllerListener( new FrescoControllerListener(simpleDraweeView , width))
                .build();
        simpleDraweeView.setController( draweeController);
    }

    public static void clearCache(){
        ImagePipelineFactory.getInstance().getImagePipeline().clearCaches();
    }
}
