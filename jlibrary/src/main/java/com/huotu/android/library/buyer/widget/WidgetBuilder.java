package com.huotu.android.library.buyer.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.AdBean.AdBannerConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdFourConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdOneConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdPageBannerConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdThreeConfig;
import com.huotu.android.library.buyer.bean.AdBean.AdWindowConfig;
import com.huotu.android.library.buyer.bean.AsistBean.ButtonConfig;
import com.huotu.android.library.buyer.bean.AsistBean.Guides1Config;
import com.huotu.android.library.buyer.bean.AsistBean.Guides2Config;
import com.huotu.android.library.buyer.bean.AsistBean.GuidesShopConfig;
import com.huotu.android.library.buyer.bean.FooterBean.FooterOneConfig;
import com.huotu.android.library.buyer.bean.GoodsBean.GoodsOneConfig;
import com.huotu.android.library.buyer.bean.GoodsBean.GoodsTwoConfig;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeConfig;
import com.huotu.android.library.buyer.bean.GoodsListBean.PubuConfig;
import com.huotu.android.library.buyer.bean.GroupBean.GoodsGroupConfig;
import com.huotu.android.library.buyer.bean.PromotionsBean.Promotion1Config;
import com.huotu.android.library.buyer.bean.SearchBean.Search1Config;
import com.huotu.android.library.buyer.bean.SearchBean.Search2Config;
import com.huotu.android.library.buyer.bean.ShopBean.ShopDefaultConfig;
import com.huotu.android.library.buyer.bean.ShopBean.ShopOneConfig;
import com.huotu.android.library.buyer.bean.ShopBean.ShopTwoConfig;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.bean.TextBean.ArticleTitleConfig;
import com.huotu.android.library.buyer.bean.TextBean.BillBoardConfig;
import com.huotu.android.library.buyer.bean.TextBean.NavigationConfig;
import com.huotu.android.library.buyer.bean.TextBean.RichTextConfig;
import com.huotu.android.library.buyer.bean.TextBean.TitleConfig;
import com.huotu.android.library.buyer.bean.WidgetConfig;
import com.huotu.android.library.buyer.bean.WidgetTypeEnum;
import com.huotu.android.library.buyer.utils.GsonUtil;
import com.huotu.android.library.buyer.widget.AdWidget.AdAverageWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdBannerWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdOneWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdPageBannerWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdWindowWidget;
import com.huotu.android.library.buyer.widget.AssistWidget.Guides1Widget;
import com.huotu.android.library.buyer.widget.AssistWidget.Guides2Widget;
import com.huotu.android.library.buyer.widget.AssistWidget.GuidesShopWidget;
import com.huotu.android.library.buyer.widget.FooterWidget.FooterOneWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewThreeWidget;
import com.huotu.android.library.buyer.widget.GoodsListWidget.PubuWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsOneCardWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsOneWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsTwoWidget;
import com.huotu.android.library.buyer.widget.PromotionsWidget.Promotion1Widget;
import com.huotu.android.library.buyer.widget.SearchWidget.Search1Widget;
import com.huotu.android.library.buyer.widget.SearchWidget.Search2Widget;
import com.huotu.android.library.buyer.widget.ShopWidget.ShopDefaultWidget;
import com.huotu.android.library.buyer.widget.ShopWidget.ShopOneWidget;
import com.huotu.android.library.buyer.widget.ShopWidget.ShopTwoWidget;
import com.huotu.android.library.buyer.widget.SortWidget.SortOneWidget;
import com.huotu.android.library.buyer.widget.TextWidget.ArticleTitleWidget;
import com.huotu.android.library.buyer.widget.TextWidget.BillBoardWidget;
import com.huotu.android.library.buyer.widget.TextWidget.NavigationWidget;
import com.huotu.android.library.buyer.widget.TextWidget.RichTextWidget;
import com.huotu.android.library.buyer.bean.AdBean.AdAverageConfig;
import com.huotu.android.library.buyer.widget.AdWidget.AdFourWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdThreeWidget;
import com.huotu.android.library.buyer.widget.AssistWidget.ButtonWidget;
import com.huotu.android.library.buyer.widget.GroupWidget.GoodsGroupWidget;
import com.huotu.android.library.buyer.widget.TextWidget.TitleWidget;
import com.huotu.android.library.buyer.widget.AdWidget.AdAverageWidget;
import com.huotu.android.library.buyer.widget.TextWidget.TitleWidget;


/**
 * 组件创建器
 * Created by Administrator on 2016/1/7.
 */
public class WidgetBuilder {
    public static View build( WidgetConfig widgetConfig , Activity activity){
        if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.TEXT_TITLE ){
            GsonUtil<TitleConfig> gsonUtil =new GsonUtil<>();
            TitleConfig titleConfig = new TitleConfig();
            titleConfig = gsonUtil.toBean( widgetConfig.getJsonString() , titleConfig );
            TitleWidget titleWidget = new TitleWidget(activity , titleConfig);
            //titleWidget.setLinkClickListener( activity );
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleWidget.setLayoutParams(layoutParams);
            return titleWidget;
        }else if(widgetConfig.getWidgetTypeEnum()==WidgetTypeEnum.TEXT_RICHTEXT){
            GsonUtil<RichTextConfig> gsonUtil =new GsonUtil<>();
            RichTextConfig richTextConfig = new RichTextConfig();
            richTextConfig = gsonUtil.toBean( widgetConfig.getJsonString() , richTextConfig );
            RichTextWidget richTextWidget = new RichTextWidget(activity , richTextConfig);
            return richTextWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.TEXT_ARTICLETITLE){
            GsonUtil<ArticleTitleConfig> gsonUtil =new GsonUtil<>();
            ArticleTitleConfig articleTitleConfig = new ArticleTitleConfig();
            articleTitleConfig = gsonUtil.toBean( widgetConfig.getJsonString() , articleTitleConfig );
            ArticleTitleWidget articleTitleWidget = new ArticleTitleWidget(activity , articleTitleConfig);
            return articleTitleWidget;
        }else if( widgetConfig.getWidgetTypeEnum()==WidgetTypeEnum.TEXT_NAVIGATION){
            GsonUtil<NavigationConfig> gsonUtil =new GsonUtil<>();
            NavigationConfig navigationConfig = new NavigationConfig();
            navigationConfig = gsonUtil.toBean( widgetConfig.getJsonString() , navigationConfig );
            NavigationWidget navigationWidget = new NavigationWidget(activity , navigationConfig);
            //navigationWidget.setLinkClickListener(activity);
            return navigationWidget;

        }else if(widgetConfig.getWidgetTypeEnum()==WidgetTypeEnum.TEXT_BILLBOARD){
            GsonUtil<BillBoardConfig> gsonUtil =new GsonUtil<>();
            BillBoardConfig billBoardConfig = new BillBoardConfig();
            billBoardConfig = gsonUtil.toBean( widgetConfig.getJsonString() , billBoardConfig );
            BillBoardWidget billBoardWidget = new BillBoardWidget(activity , billBoardConfig);
            return billBoardWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.LISTVIEW_THREE){
            GsonUtil<ListViewThreeConfig> gsonUtil =new GsonUtil<>();
            ListViewThreeConfig listViewThreeConfig = new ListViewThreeConfig();
            listViewThreeConfig = gsonUtil.toBean( widgetConfig.getJsonString() , listViewThreeConfig);
            ListViewThreeWidget listViewThreeWidget = new ListViewThreeWidget(activity, listViewThreeConfig);
            return listViewThreeWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.LISTVIEW_PUBU ){
            GsonUtil<PubuConfig> gsonUtil = new GsonUtil<>();
            PubuConfig pubuConfig = new PubuConfig();
            pubuConfig = gsonUtil.toBean( widgetConfig.getJsonString() , pubuConfig );
            PubuWidget pubuWidget =new PubuWidget(activity, pubuConfig );
            return pubuWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_WINDOW){
            GsonUtil<AdWindowConfig> gsonUtil = new GsonUtil<>();
            AdWindowConfig config = new AdWindowConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() , config );
            AdWindowWidget adWindowWidget =new AdWindowWidget(activity,  config );
            return adWindowWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADONE){
            GsonUtil<AdOneConfig> gsonUtil = new GsonUtil<>();
            AdOneConfig config = new AdOneConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() , config );
            AdOneWidget adOneWidget =new AdOneWidget(activity,  config );
            return adOneWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADBANNER){
            GsonUtil<AdBannerConfig> gsonUtil = new GsonUtil<>();
            AdBannerConfig config = new AdBannerConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() , config );
            AdBannerWidget adBannerWidget =new AdBannerWidget(activity,  config );
            //adBannerWidget.setOnItemClickListener(activity);
            return adBannerWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADPAGEBANNER ){
            GsonUtil<AdPageBannerConfig> gsonUtil = new GsonUtil<>();
            AdPageBannerConfig config = new AdPageBannerConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() , config );
            AdPageBannerWidget adPageBannerWidget =new AdPageBannerWidget(activity,  config );
            //adBannerWidget.setOnItemClickListener(activity);
            return adPageBannerWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.ASSIST_GUIDES2){
            GsonUtil<Guides2Config> gsonUtil = new GsonUtil<>();
            Guides2Config config = new Guides2Config();
            config = gsonUtil.toBean( widgetConfig.getJsonString() , config );
            Guides2Widget guides2Widget =new Guides2Widget(activity,  config );
            return guides2Widget;
        }else if( widgetConfig.getWidgetTypeEnum()==WidgetTypeEnum.SEARCH_ONE){
            GsonUtil<Search1Config> gsonUtil=new GsonUtil<>();
            Search1Config config = new Search1Config();
            config = gsonUtil.toBean(widgetConfig.getJsonString(), config);
            Search1Widget search1Widget = new Search1Widget(activity, config);
            return  search1Widget;
        }else if(widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.SHOP_TWO){
            GsonUtil<ShopTwoConfig> gsonUtil =new GsonUtil<>();
            ShopTwoConfig config = new ShopTwoConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() ,config );
            ShopTwoWidget shopTwoWidget = new ShopTwoWidget(activity , config);
            return shopTwoWidget;
        }else if(widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.SHOP_ONE){
            GsonUtil<ShopOneConfig> gsonUtil =new GsonUtil<>();
            ShopOneConfig config = new ShopOneConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() ,config );
            ShopOneWidget shopOneWidget = new ShopOneWidget(activity , config);
            return shopOneWidget;
        }else if(widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADAVERAGEBANNER){
            GsonUtil<AdAverageConfig> gsonUtil =new GsonUtil<>();
            AdAverageConfig config = new AdAverageConfig();
            config = gsonUtil.toBean( widgetConfig.getJsonString() ,config );
            AdAverageWidget adAverageWidget = new AdAverageWidget(activity , config);
            return adAverageWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADTHREE){
            GsonUtil<AdThreeConfig> gsonUtil = new GsonUtil<>();
            AdThreeConfig config = new AdThreeConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            AdThreeWidget adThreeWidget = new AdThreeWidget(activity, config);
            return  adThreeWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.AD_ADFOUR){
            GsonUtil<AdFourConfig> gsonUtil = new GsonUtil<>();
            AdFourConfig config = new AdFourConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            AdFourWidget adFOURWidget = new AdFourWidget(activity, config);
            return  adFOURWidget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.SORT_ONE){
            GsonUtil<SortOneConfig> gsonUtil = new GsonUtil<>();
            SortOneConfig config = new SortOneConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            SortOneWidget widget = new SortOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.FOOTER_ONE){
            GsonUtil<FooterOneConfig> gsonUtil = new GsonUtil<>();
            FooterOneConfig config = new FooterOneConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            FooterOneWidget widget = new FooterOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.GOODS_ONE){
            GsonUtil<GoodsOneConfig> gsonUtil = new GsonUtil<>();
            GoodsOneConfig config = new GoodsOneConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            GoodsOneWidget widget = new GoodsOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.SHOP_DEFAULT){
            GsonUtil<ShopDefaultConfig> gsonUtil = new GsonUtil<>();
            ShopDefaultConfig config = new ShopDefaultConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            ShopDefaultWidget widget = new ShopDefaultWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.ASSIST_GUIDES1){
            GsonUtil<Guides1Config> gsonUtil=new GsonUtil<>();
            Guides1Config config=  new Guides1Config();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            Guides1Widget widget = new Guides1Widget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.ASSIST_GUIDESSHOP){
            GsonUtil<GuidesShopConfig> gsonUtil=new GsonUtil<>();
            GuidesShopConfig config=  new GuidesShopConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            GuidesShopWidget widget = new GuidesShopWidget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.PROMOTION_ONE){
            GsonUtil<Promotion1Config> gsonUtil=new GsonUtil<>();
            Promotion1Config config=  new Promotion1Config();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            Promotion1Widget widget = new Promotion1Widget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.SEARCH_TWO){
            GsonUtil<Search2Config> gsonUtil=new GsonUtil<>();
            Search2Config config=  new Search2Config();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            Search2Widget widget = new Search2Widget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.GOODS_ONE_CARD){
            GsonUtil<GoodsOneConfig> gsonUtil=new GsonUtil<>();
            GoodsOneConfig config=  new GoodsOneConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            GoodsOneCardWidget widget = new GoodsOneCardWidget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.GOODS_TWO){
            GsonUtil<GoodsTwoConfig> gsonUtil=new GsonUtil<>();
            GoodsTwoConfig config=  new GoodsTwoConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            GoodsTwoWidget widget = new GoodsTwoWidget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.ASSIST_BUTTON){
            GsonUtil<ButtonConfig> gsonUtil=new GsonUtil<>();
            ButtonConfig config=  new ButtonConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString() , config);
            ButtonWidget widget = new ButtonWidget(activity,config);
            return widget;
        }else if( widgetConfig.getWidgetTypeEnum() == WidgetTypeEnum.GROUP_GOODS) {
            GsonUtil<GoodsGroupConfig> gsonUtil = new GsonUtil<>();
            GoodsGroupConfig config = new GoodsGroupConfig();
            config = gsonUtil.toBean(widgetConfig.getJsonString(), config);
            GoodsGroupWidget widget = new GoodsGroupWidget(activity, config);
            return widget;
        }
        else {
            return null;
        }
    }
}
