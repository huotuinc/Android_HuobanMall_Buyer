package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.FrescoControllerListener;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;


/**
 * Created by Administrator on 2016/1/13.
 */
public class FrescoHolderView implements Holder<AdImageBean> {
    private SimpleDraweeView iv;
    private int width;
    private FrescoControllerListener.ImageCallback imageCallback;

    public FrescoHolderView(int w , FrescoControllerListener.ImageCallback imageCallback){
        this.width = w;
        this.imageCallback = imageCallback;
    }
    @Override
    public View createView(Context context ) {
        iv = new SimpleDraweeView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        return iv;
    }
    @Override
    public void UpdateUI(Context context,int position, AdImageBean data) {

        String imageUrl = Variable.resourceUrl + data.getImageUrl();
        FrescoDraweeController.loadImage(iv, width,  imageUrl , imageCallback );
    }
}
