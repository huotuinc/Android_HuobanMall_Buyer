package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.AdBean.AdWindowConfig;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * 橱窗组件
 * Created by jinxiangdong on 2016/1/12.
 */
public class AdWindowWidget extends BaseLinearLayout{
    private AdWindowConfig adWindowConfig;

    public AdWindowWidget(Context context, AdWindowConfig adWindowConfig) {
        super(context);

        this.adWindowConfig = adWindowConfig;

        createLayout();
    }

    @Override
    public void onClick(View v) {
        if( v.getTag() ==null)return;
        AdImageBean bean  = (AdImageBean)v.getTag();
        if( bean ==null )return;

        CommonUtil.link( bean.getTitle() , bean.getLinkUrl() );
        //EventBus.getDefault().post( new LinkEvent( bean.getLinkName() , bean.getLinkUrl()));
    }

    private void createLayout(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate( R.layout.ad_window , this, true );

        TextView tvTitle = (TextView)findViewById(R.id.ad_window_title);
        SimpleDraweeView image1 = (SimpleDraweeView)findViewById(R.id.ad_window_image1);
        SimpleDraweeView image2 = (SimpleDraweeView)findViewById(R.id.ad_window_image2);
        SimpleDraweeView image3 = (SimpleDraweeView)findViewById(R.id.ad_window_image3);
        TextView tvSubTitle = (TextView)findViewById(R.id.ad_window_subtitle);
        TextView tvDescription = (TextView)findViewById(R.id.ad_window_description);

        tvTitle.setText(adWindowConfig.getText_name());
        tvSubTitle.setText(adWindowConfig.getText_contentName());
        tvDescription.setText(adWindowConfig.getText_description());

        if( adWindowConfig.getImages()==null || adWindowConfig.getImages().size()<1 )return;

        int itemCount = 3;//adWindowConfig.getImages().size();
        int itemWidth = getResources().getDisplayMetrics().widthPixels / itemCount;
        if( adWindowConfig.getImages().size()>0 ){
            String url = Variable.resourceUrl + adWindowConfig.getImages().get(0).getImageUrl();
            FrescoDraweeController.loadImage( image1, itemWidth, url );
            image1.setOnClickListener(this);
            image1.setTag( adWindowConfig.getImages().get(0) );
        }
        if( adWindowConfig.getImages().size()>1) {
            String url = Variable.resourceUrl + adWindowConfig.getImages().get(1).getImageUrl();
            FrescoDraweeController.loadImage(image2, itemWidth, url);
            image2.setOnClickListener(this);
            image2.setTag(adWindowConfig.getImages().get(1));
        }
        if( adWindowConfig.getImages().size()>2) {
            String url = Variable.resourceUrl + adWindowConfig.getImages().get(2).getImageUrl();
            FrescoDraweeController.loadImage(image3, itemWidth, url);
            image3.setOnClickListener(this);
            image3.setTag(adWindowConfig.getImages().get(2));
        }
    }
}
