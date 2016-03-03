package com.huotu.android.library.buyer.widget.AdWidget;

import android.content.Context;
import android.widget.LinearLayout;
import com.huotu.android.library.buyer.R;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.huotu.android.library.buyer.bean.AdBean.AdBannerConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdImageBean;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Banner组件
 * Created by jinxiangdong on 2016/1/13.
 */
public class AdBannerWidget extends com.bigkoo.convenientbanner.ConvenientBanner implements OnItemClickListener {
    private AdBannerConfig config;

    public AdBannerWidget(Context context , AdBannerConfig  config ) {
        super(context , config.isAutoPlay() );

        this.config = config;
        final int iwidth = this.config.getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(config.getWidth(), config.getHeight());
        this.setLayoutParams(layoutParams);

        this.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoHolderView( iwidth );
            }
        }, config.getImages() )
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
        .setOnItemClickListener(this);

        this.startTurning(4000);
    }

    @Override
    public void onItemClick(int position) {
        AdImageBean bean = config.getImages().get( position );
        EventBus.getDefault().post( new LinkEvent(bean.getLinkName(), bean.getLinkUrl() ));
    }
}
