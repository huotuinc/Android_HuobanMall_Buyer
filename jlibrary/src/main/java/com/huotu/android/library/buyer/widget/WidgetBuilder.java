package com.huotu.android.library.buyer.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
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
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewTwoConfig;
import com.huotu.android.library.buyer.bean.GroupBean.ClassGroupConfig;
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
import com.huotu.android.library.buyer.utils.CommonUtil;
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
import com.huotu.android.library.buyer.widget.GoodsListWidget.ListViewTwoWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsOneCardWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsOneWidget;
import com.huotu.android.library.buyer.widget.GoodsWidget.GoodsTwoWidget;
import com.huotu.android.library.buyer.widget.GroupWidget.ClassGroupWidget;
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
        if( widgetConfig.getType() == WidgetTypeEnum.TEXT_TITLE.getIndex() ){
            GsonUtil<TitleConfig> gsonUtil =new GsonUtil<>();
            TitleConfig titleConfig = new TitleConfig();
            titleConfig = CommonUtil.convertMap(titleConfig, widgetConfig.getProperties());
            //titleConfig = gsonUtil.toBean( widgetConfig.getProperties() , titleConfig );
            TitleWidget titleWidget = new TitleWidget(activity , titleConfig);
            //titleWidget.setLinkClickListener( activity );
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleWidget.setLayoutParams(layoutParams);
            return titleWidget;
        }else if(widgetConfig.getType()==WidgetTypeEnum.TEXT_RICHTEXT.getIndex()){
            GsonUtil<RichTextConfig> gsonUtil =new GsonUtil<>();
            RichTextConfig richTextConfig = new RichTextConfig();
            richTextConfig = CommonUtil.convertMap( richTextConfig , widgetConfig.getProperties() );
            //richTextConfig = gsonUtil.toBean( widgetConfig.getProperties() , richTextConfig );
            RichTextWidget richTextWidget = new RichTextWidget(activity , richTextConfig);
            return richTextWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.TEXT_ARTICLETITLE.getIndex()){
            GsonUtil<ArticleTitleConfig> gsonUtil =new GsonUtil<>();
            ArticleTitleConfig articleTitleConfig = new ArticleTitleConfig();
            articleTitleConfig = CommonUtil.convertMap( articleTitleConfig , widgetConfig.getProperties() );
            //articleTitleConfig = gsonUtil.toBean( widgetConfig.getProperties() , articleTitleConfig );
            ArticleTitleWidget articleTitleWidget = new ArticleTitleWidget(activity , articleTitleConfig);
            return articleTitleWidget;
        }else if( widgetConfig.getType()==WidgetTypeEnum.TEXT_NAVIGATION.getIndex()){
            GsonUtil<NavigationConfig> gsonUtil =new GsonUtil<>();
            NavigationConfig navigationConfig = new NavigationConfig();
            navigationConfig = CommonUtil.convertMap( navigationConfig , widgetConfig.getProperties() );
            //navigationConfig = gsonUtil.toBean( widgetConfig.getProperties() , navigationConfig );
            NavigationWidget navigationWidget = new NavigationWidget(activity , navigationConfig);
            //navigationWidget.setLinkClickListener(activity);
            return navigationWidget;

        }else if(widgetConfig.getType()==WidgetTypeEnum.TEXT_BILLBOARD.getIndex()){
            GsonUtil<BillBoardConfig> gsonUtil =new GsonUtil<>();
            BillBoardConfig billBoardConfig = new BillBoardConfig();
            billBoardConfig = CommonUtil.convertMap(billBoardConfig,widgetConfig.getProperties());
            //billBoardConfig = gsonUtil.toBean( widgetConfig.getProperties() , billBoardConfig );
            BillBoardWidget billBoardWidget = new BillBoardWidget(activity , billBoardConfig);
            return billBoardWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.LISTVIEW_THREE.getIndex()){
            GsonUtil<ListViewThreeConfig> gsonUtil =new GsonUtil<>();
            ListViewThreeConfig listViewThreeConfig = new ListViewThreeConfig();
            listViewThreeConfig =  CommonUtil.convertMap( listViewThreeConfig , widgetConfig.getProperties() );
            //listViewThreeConfig = gsonUtil.toBean( widgetConfig.getProperties() , listViewThreeConfig);
            ListViewThreeWidget listViewThreeWidget = new ListViewThreeWidget(activity, listViewThreeConfig);
            return listViewThreeWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.LISTVIEW_TWO.getIndex() ){
            GsonUtil<ListViewTwoConfig> gsonUtil = new GsonUtil<>();
            ListViewTwoConfig listViewTwoConfig = new ListViewTwoConfig();
            listViewTwoConfig = CommonUtil.convertMap( listViewTwoConfig, widgetConfig.getProperties() );
            //listViewTwoConfig = gsonUtil.toBean( widgetConfig.getProperties() , listViewTwoConfig );
            ListViewTwoWidget listViewTwoWidget =new ListViewTwoWidget(activity, listViewTwoConfig );
            return listViewTwoWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_WINDOW.getIndex() ){
            GsonUtil<AdWindowConfig> gsonUtil = new GsonUtil<>();
            AdWindowConfig config = new AdWindowConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() , config );
            AdWindowWidget adWindowWidget =new AdWindowWidget(activity,  config );
            return adWindowWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_ADONE.getIndex() ){
            GsonUtil<AdOneConfig> gsonUtil = new GsonUtil<>();
            AdOneConfig config = new AdOneConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() , config );
            AdOneWidget adOneWidget =new AdOneWidget(activity,  config );
            return adOneWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_ADBANNER.getIndex() ){
            GsonUtil<AdBannerConfig> gsonUtil = new GsonUtil<>();
            AdBannerConfig config = new AdBannerConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() , config );
            AdBannerWidget adBannerWidget =new AdBannerWidget(activity,  config );
            //adBannerWidget.setOnItemClickListener(activity);
            return adBannerWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_ADPAGEBANNER.getIndex() ){
            GsonUtil<AdPageBannerConfig> gsonUtil = new GsonUtil<>();
            AdPageBannerConfig config = new AdPageBannerConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() , config );
            AdPageBannerWidget adPageBannerWidget =new AdPageBannerWidget(activity,  config );
            //adBannerWidget.setOnItemClickListener(activity);
            return adPageBannerWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.ASSIST_GUIDES2.getIndex() ){
            GsonUtil<Guides2Config> gsonUtil = new GsonUtil<>();
            Guides2Config config = new Guides2Config();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() , config );
            Guides2Widget guides2Widget =new Guides2Widget(activity,  config );
            return guides2Widget;
        }else if( widgetConfig.getType() ==WidgetTypeEnum.SEARCH_ONE.getIndex() ){
            GsonUtil<Search1Config> gsonUtil=new GsonUtil<>();
            Search1Config config = new Search1Config();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties(), config);
            Search1Widget search1Widget = new Search1Widget(activity, config);
            return  search1Widget;
        }else if(widgetConfig.getType() == WidgetTypeEnum.SHOP_TWO.getIndex() ){
            GsonUtil<ShopTwoConfig> gsonUtil =new GsonUtil<>();
            ShopTwoConfig config = new ShopTwoConfig();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() ,config );
            ShopTwoWidget shopTwoWidget = new ShopTwoWidget(activity , config);
            return shopTwoWidget;
        }else if(widgetConfig.getType() == WidgetTypeEnum.SHOP_ONE.getIndex() ){
            GsonUtil<ShopOneConfig> gsonUtil =new GsonUtil<>();
            ShopOneConfig config = new ShopOneConfig();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() ,config );
            ShopOneWidget shopOneWidget = new ShopOneWidget(activity , config);
            return shopOneWidget;
        }else if(widgetConfig.getType() == WidgetTypeEnum.AD_ADAVERAGEBANNER.getIndex() ){
            GsonUtil<AdAverageConfig> gsonUtil =new GsonUtil<>();
            AdAverageConfig config = new AdAverageConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean( widgetConfig.getProperties() ,config );
            AdAverageWidget adAverageWidget = new AdAverageWidget(activity , config);
            return adAverageWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_ADTHREE.getIndex() ){
            GsonUtil<AdThreeConfig> gsonUtil = new GsonUtil<>();
            AdThreeConfig config = new AdThreeConfig();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            AdThreeWidget adThreeWidget = new AdThreeWidget(activity, config);
            return  adThreeWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.AD_ADFOUR.getIndex() ){
            GsonUtil<AdFourConfig> gsonUtil = new GsonUtil<>();
            AdFourConfig config = new AdFourConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            AdFourWidget adFOURWidget = new AdFourWidget(activity, config);
            return  adFOURWidget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.SORT_ONE.getIndex() ){
            GsonUtil<SortOneConfig> gsonUtil = new GsonUtil<>();
            SortOneConfig config = new SortOneConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            SortOneWidget widget = new SortOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.FOOTER_ONE.getIndex() ){
            GsonUtil<FooterOneConfig> gsonUtil = new GsonUtil<>();
            FooterOneConfig config = new FooterOneConfig();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            FooterOneWidget widget = new FooterOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.GOODS_ONE.getIndex() ){
            GsonUtil<GoodsOneConfig> gsonUtil = new GsonUtil<>();
            GoodsOneConfig config = new GoodsOneConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            GoodsOneWidget widget = new GoodsOneWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.SHOP_DEFAULT.getIndex() ){
            GsonUtil<ShopDefaultConfig> gsonUtil = new GsonUtil<>();
            ShopDefaultConfig config = new ShopDefaultConfig();

            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            ShopDefaultWidget widget = new ShopDefaultWidget(activity, config);
            return  widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.ASSIST_GUIDES1.getIndex() ){
            GsonUtil<Guides1Config> gsonUtil=new GsonUtil<>();
            Guides1Config config=  new Guides1Config();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            Guides1Widget widget = new Guides1Widget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.ASSIST_GUIDESSHOP.getIndex()){
            GsonUtil<GuidesShopConfig> gsonUtil=new GsonUtil<>();
            GuidesShopConfig config=  new GuidesShopConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            GuidesShopWidget widget = new GuidesShopWidget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.PROMOTION_ONE.getIndex()){
            GsonUtil<Promotion1Config> gsonUtil=new GsonUtil<>();
            Promotion1Config config=  new Promotion1Config();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            Promotion1Widget widget = new Promotion1Widget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.SEARCH_TWO.getIndex() ){
            GsonUtil<Search2Config> gsonUtil=new GsonUtil<>();
            Search2Config config=  new Search2Config();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            Search2Widget widget = new Search2Widget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.GOODS_ONE_CARD.getIndex() ){
            GsonUtil<GoodsOneConfig> gsonUtil=new GsonUtil<>();
            GoodsOneConfig config=  new GoodsOneConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            GoodsOneCardWidget widget = new GoodsOneCardWidget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.GOODS_TWO.getIndex()){
            GsonUtil<GoodsTwoConfig> gsonUtil=new GsonUtil<>();
            GoodsTwoConfig config=  new GoodsTwoConfig();
            config =  CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            GoodsTwoWidget widget = new GoodsTwoWidget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.ASSIST_BUTTON.getIndex() ){
            GsonUtil<ButtonConfig> gsonUtil=new GsonUtil<>();
            ButtonConfig config=  new ButtonConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties() , config);
            ButtonWidget widget = new ButtonWidget(activity,config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.GROUP_GOODS.getIndex() ) {
            GsonUtil<GoodsGroupConfig> gsonUtil = new GsonUtil<>();
            GoodsGroupConfig config = new GoodsGroupConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties(), config);
            GoodsGroupWidget widget = new GoodsGroupWidget(activity, config);
            return widget;
        }else if( widgetConfig.getType() == WidgetTypeEnum.GROUP_Class.getIndex()){
            GsonUtil<ClassGroupConfig> gsonUtil = new GsonUtil<>();
            ClassGroupConfig config = new ClassGroupConfig();
            config = CommonUtil.convertMap( config , widgetConfig.getProperties() );
            //config = gsonUtil.toBean(widgetConfig.getProperties(), config);
            ClassGroupWidget widget = new ClassGroupWidget(activity, config);
            return widget;
        }
        else {
            return null;
        }
    }

}
