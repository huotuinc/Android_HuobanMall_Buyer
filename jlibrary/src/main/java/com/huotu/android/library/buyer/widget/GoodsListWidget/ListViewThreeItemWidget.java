package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeItemConfig;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.SpanningUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ListViewThreeItemWidget extends BaseLinearLayoutWidget {
    private ListViewThreeItemConfig listViewThreeConfig;
    private LayoutInflater layoutInflater;
    SimpleDraweeView iv1, iv2, iv3;
    TextView tvName1,tvName2,tvName3;
    TextView tvPrice1,tvPrice2,tvPrice3;
    TextView tvJiFen1,tvJiFen2, tvJiFen3;
    TextView tvLine1,tvLine2, tvLine3;
    RelativeLayout rl1,rl2,rl3;
    ListHandler mHandler;
    int imageWidth1,imageWidth2,imageWidth3;

    public ListViewThreeItemWidget(Context context, ListViewThreeItemConfig listViewThreeConfig) {
        super(context);

        this.layoutInflater = LayoutInflater.from(context);
        this.listViewThreeConfig = listViewThreeConfig;
        this.mHandler = new ListHandler(this);

        create_Layout();

        //asyncLoadData();
    }

    @Override
    protected void RefreshUI(Message msg) {
        super.RefreshUI(msg);

        List<GoodsBean> list = (List<GoodsBean>)msg.obj;

        addItem(list);
    }

    /*
    根据配置，设置布局样式
     */
    private void create_Layout(){
        if( this.listViewThreeConfig.getWidgetLayout().equals(Constant.WIDGETLAYOUT_SIZE_2) ){
            create_YIDALIANGXIAO();
        }else if( this.listViewThreeConfig.getWidgetLayout().equals(Constant.WIDGETLAYOUT_SIZE_3) ){
            create_XIANGXILIEBIAO();
        }else{
            Logger.e("未知布局样式");
        }
    }

    private void create_YIDALIANGXIAO(){
        this.layoutInflater.inflate(R.layout.listview_three_item1, this, true);

        if( this.listViewThreeConfig.getGridStyle().equals(Constant.GRIDSTYLE_CARD)){
            setStyle_YIDALIANGXIAO_KAPINGBUJU();
        }else if(this.listViewThreeConfig.getGridStyle().equals(Constant.GRIDSTYLE_NORMAL) ){
            setStyle_YIDALIANGXIAO_JIJIANBUJU();
        }else{
            Logger.e("未知布局样式");
        }
    }

    private void findViewById(){
        rl1 = (RelativeLayout)findViewById(R.id.listview_three_item_rl1);
        rl2 = (RelativeLayout)findViewById(R.id.listview_three_item_rl2);
        rl3 = (RelativeLayout)findViewById(R.id.listview_three_item_rl3);

        iv1 = (SimpleDraweeView)findViewById(R.id.listview_three_item_pic1);
        iv2 = (SimpleDraweeView)findViewById(R.id.listview_three_item_pic2);
        iv3 = (SimpleDraweeView)findViewById(R.id.listview_three_item_pic3);

        tvName1 =  (TextView)findViewById(R.id.listview_three_item_name1);
        tvName2 =  (TextView)findViewById(R.id.listview_three_item_name2);
        tvName3 =  (TextView)findViewById(R.id.listview_three_item_name3);

        tvPrice1 =  (TextView)findViewById(R.id.listview_three_item_price1);
        tvPrice2 =  (TextView)findViewById(R.id.listview_three_item_price2);
        tvPrice3 =  (TextView)findViewById(R.id.listview_three_item_price3);

        tvJiFen1 =  (TextView)findViewById(R.id.listview_three_item_jifen1);
        tvJiFen2 =  (TextView)findViewById(R.id.listview_three_item_jifen2);
        tvJiFen3 =  (TextView)findViewById(R.id.listview_three_item_jifen3);

        rl1.setBackgroundResource(R.drawable.gray_border_style);
        rl2.setBackgroundResource(R.drawable.gray_border_style);
        rl3.setBackgroundResource(R.drawable.gray_border_style);

        if( !this.listViewThreeConfig.isShowName() ){
            tvName1.setVisibility(View.GONE);
            tvName2.setVisibility(View.GONE);
            tvName3.setVisibility(View.GONE);
        }else{
            tvName1.setVisibility(View.VISIBLE);
            tvName2.setVisibility(View.VISIBLE);
            tvName3.setVisibility(View.VISIBLE);
        }

        if( !this.listViewThreeConfig.isShowPrices() ){
            tvPrice1.setVisibility(View.GONE);
            tvPrice2.setVisibility(View.GONE);
            tvPrice3.setVisibility(View.GONE);
        }else{
            tvPrice1.setVisibility(View.VISIBLE);
            tvPrice2.setVisibility(View.VISIBLE);
            tvPrice3.setVisibility(View.VISIBLE);
        }

        if( !this.listViewThreeConfig.isShowUserInteger() ){
            tvJiFen1.setVisibility(View.GONE);
            tvJiFen2.setVisibility(View.GONE);
            tvJiFen3.setVisibility(View.GONE);
        }else{
            tvJiFen1.setVisibility(View.VISIBLE);
            tvJiFen2.setVisibility(View.VISIBLE);
            tvJiFen3.setVisibility(View.VISIBLE);
        }
    }
    /*
    一大两小 卡片布局
     */
    private void setStyle_YIDALIANGXIAO_KAPINGBUJU(){
        findViewById();

        RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams) tvName1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_pic1);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic1);
        tvName1.setLayoutParams(layoutParams);
        tvName1.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_name1);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_name1);
        tvPrice1.setLayoutParams(layoutParams);
        tvPrice1.setTextColor(Color.WHITE);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price1);
        tvJiFen1.setLayoutParams(layoutParams);
        tvJiFen1.setTextColor(Color.BLACK);

        layoutParams =(RelativeLayout.LayoutParams) tvName2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_pic2);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic2);
        tvName2.setLayoutParams(layoutParams);
        tvName2.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_name2);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_name2);
        tvPrice2.setLayoutParams(layoutParams);
        tvPrice2.setTextColor(Color.WHITE);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price2);
        tvJiFen2.setLayoutParams(layoutParams);
        tvJiFen2.setTextColor(Color.BLACK);

        layoutParams =(RelativeLayout.LayoutParams) tvName3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_pic3);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic3);
        tvName3.setLayoutParams(layoutParams);
        tvName3.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW , R.id.listview_three_item_name3);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_name3);
        tvPrice3.setLayoutParams(layoutParams);
        tvPrice3.setTextColor(Color.BLACK);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen3.getLayoutParams();
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price3);
        tvJiFen3.setLayoutParams(layoutParams);
        tvJiFen3.setTextColor(Color.BLACK);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int ivw = getResources().getDisplayMetrics().widthPixels;
        int ivh =  h* ivw / w;
        layoutParams = new RelativeLayout.LayoutParams( ivw , ivh );
        iv1.setLayoutParams( layoutParams );

        imageWidth1 = ivw;


        imageWidth3 = imageWidth2 = getResources().getDisplayMetrics().widthPixels/2;
        //int ivh2 = h * ivw2/w;
        //layoutParams = new RelativeLayout.LayoutParams(ivw2,ivh2);
        //iv2.setLayoutParams( layoutParams );

        //layoutParams.height = 300;
        //iv3.setLayoutParams(layoutParams);
    }
    /*
    一大二小 ，极简布局
     */
    private void setStyle_YIDALIANGXIAO_JIJIANBUJU(){
        findViewById();

        int topMargin = DensityUtils.dip2px(getContext(), 4);
        int leftMargin = topMargin;
        int rightMargin = topMargin;
        int bottomMargin = topMargin;

        RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams) tvName1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_three_item_pic1);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic1);
        layoutParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        tvName1.setLayoutParams(layoutParams);
        tvName1.setTextColor(Color.WHITE);
        tvName1.setBackgroundResource(R.drawable.transparent_circle_bg);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_three_item_pic1);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.listview_three_item_pic1);
        tvPrice1.setLayoutParams(layoutParams);
        tvPrice1.setTextColor(Color.WHITE);
        tvPrice1.setBackgroundResource(R.drawable.transparent_circle_bg);

        set_JiFen_Style(tvJiFen1, R.id.listview_three_item_pic1, leftMargin, topMargin, rightMargin, bottomMargin);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_three_item_pic2);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.listview_three_item_pic2);
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        tvPrice2.setLayoutParams(layoutParams);
        tvPrice2.setTextColor(Color.WHITE);
        tvPrice2.setBackgroundResource(R.drawable.transparent_circle_bg);

        set_JiFen_Style(tvJiFen2, R.id.listview_three_item_pic2, leftMargin, topMargin, rightMargin, bottomMargin);


        layoutParams = (RelativeLayout.LayoutParams)tvPrice3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_three_item_pic3);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.listview_three_item_pic3);
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        tvPrice3.setLayoutParams(layoutParams);
        tvPrice3.setTextColor(Color.WHITE);
        tvPrice3.setBackgroundResource(R.drawable.transparent_circle_bg);

        set_JiFen_Style(tvJiFen3, R.id.listview_three_item_pic3, leftMargin, topMargin, rightMargin, bottomMargin);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int ivw = getResources().getDisplayMetrics().widthPixels;
        int ivh =  h* ivw / w;
        layoutParams = new RelativeLayout.LayoutParams( ivw , ivh );
        iv1.setLayoutParams(layoutParams);

        imageWidth1 = ivw;
        imageWidth2 = imageWidth3 = ivw /2;
    }

    private void set_JiFen_Style(TextView tv , int resId , int leftMargin, int topMargin, int rightMargin, int bottomMargin){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_TOP , resId );
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, resId);
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        tv.setBackgroundColor(Color.RED);
        tv.setTextColor(Color.WHITE);
        tv.setLayoutParams(layoutParams);
        tv.setPadding(4, 4, 4, 4);
    }

    private void create_XIANGXILIEBIAO(){
        this.layoutInflater.inflate(R.layout.listview_three_item2, this, true);

        if( this.listViewThreeConfig.getGridStyle().equals(Constant.GRIDSTYLE_CARD) ){
            setStyle_XIANGXILIEBIAO_KAPINGBUJU();
        }else if( this.listViewThreeConfig.getGridStyle().equals(Constant.GRIDSTYLE_NORMAL)){
            setStyle_XIANGXILIEBIAO_JIJIANBUJU();
        }
    }

    /*
    详细列表，卡片布局
     */
    private void setStyle_XIANGXILIEBIAO_KAPINGBUJU(){
         findViewById();

        tvName1.setTextColor(Color.BLACK);
        tvName2.setTextColor(Color.BLACK);
        tvName3.setTextColor(Color.BLACK);

//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.defaultpic);
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
        int ivw =getResources().getDisplayMetrics().widthPixels *2/5;
//        int ivh =  h* ivw / w;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( ivw , ViewGroup.LayoutParams.WRAP_CONTENT );
        iv1.setLayoutParams(layoutParams);
        //iv2.setLayoutParams(layoutParams);
        //iv3.setLayoutParams(layoutParams);

        imageWidth1 = imageWidth2 = imageWidth3 = ivw;
    }
    /*
    详细列表，极简布局
     */
    private void setStyle_XIANGXILIEBIAO_JIJIANBUJU(){
        findViewById();
        tvLine1 = (TextView)findViewById(R.id.listview_three_item_line1);
        tvLine2 =(TextView) findViewById(R.id.listview_three_item_line2);
        tvLine3 =(TextView)findViewById(R.id.listview_three_item_line3);
        tvLine1.setVisibility(View.VISIBLE);
        tvLine2.setVisibility(View.VISIBLE);
        tvLine3.setVisibility(View.VISIBLE);

        rl1.setBackgroundResource(0);
        rl2.setBackgroundResource(0);
        rl3.setBackgroundResource(0);

        imageWidth1 = imageWidth2 = imageWidth3 =getResources().getDisplayMetrics().widthPixels*2/5;
        //int ivw = MyApplication.ScreenWidth*2/5;
    }

    private void create_PUBULIU(){
        //throw new Exception("no Implementation method!");
        Logger.e("no Implementation method!");
    }


    public void addItem(List<GoodsBean> dataItems){
        if( dataItems.size()>0 ){
            //int width = iv1.getMeasuredWidth();
            //FrescoDraweeController.loadImage(iv1, width, dataItems.get(0).getImageurl());
            set_Image_Height( iv1 , imageWidth1 , dataItems.get(0) );
            tvName1.setText( dataItems.get(0).getGoodName() );
            set_Price_Style( tvPrice1 , dataItems.get(0) );
            tvJiFen1.setText(dataItems.get(0).getScore());
        }
        if( dataItems.size() >1 ){
            //int width = iv2.getMeasuredWidth();
            //FrescoDraweeController.loadImage(iv2,width, dataItems.get(1).getImageurl());
            set_Image_Height(iv2, imageWidth2, dataItems.get(1));
            tvName2.setText(dataItems.get(1).getGoodName());
            set_Price_Style(tvPrice2, dataItems.get(1));
            //tvJiFen2.setText( CommonUtil.FormatDouble( dataItems.get(1).getRebate()));
            tvJiFen2.setText( dataItems.get(1).getScore());
        }
        if( dataItems.size() >2 ){
            //int width = iv3.getMeasuredWidth();
            //FrescoDraweeController.loadImage(iv3,width, dataItems.get(2).getImageurl());
            set_Image_Height( iv3 , imageWidth3 , dataItems.get(2) );
            tvName3.setText( dataItems.get(2).getGoodName() );
            set_Price_Style(tvPrice3 , dataItems.get(2));
            tvJiFen3.setText( dataItems.get(2).getScore());
        }
    }

    private void set_Image_Height( SimpleDraweeView iv , int width , GoodsBean item){
        if( listViewThreeConfig.getWidgetLayout().equals( Constant.WIDGETLAYOUT_SIZE_2) ){
            //int width = iv.getMeasuredWidth();
            FrescoDraweeController.loadImage(iv, width, item.getThumbnailPic());
        }
        if( listViewThreeConfig.getWidgetLayout().equals( Constant.WIDGETLAYOUT_SIZE_3) ) {
            //int width = MyApplication.ScreenWidth * 2 / 5;
            FrescoDraweeController.loadImage(iv, width, item.getThumbnailPic());
        }
    }

    private void set_Price_Style( TextView tv , GoodsBean item ){
        if( listViewThreeConfig.getWidgetLayout().equals( Constant.WIDGETLAYOUT_SIZE_2) ) {
            if (listViewThreeConfig.getGridStyle().equals(Constant.GRIDSTYLE_CARD)) {
                String priceStr = CommonUtil.FormatDouble(item.getMarketPrice());
                String zPriceStr = CommonUtil.FormatDouble( item.getPrice());
                SpanningUtil.set_Price_Format2(tv, priceStr, zPriceStr, Color.RED, Color.GRAY);
            } else if (listViewThreeConfig.getGridStyle().equals( Constant.GRIDSTYLE_NORMAL )) {
                tv.setTextColor(Color.WHITE);
            }
        }else if( listViewThreeConfig.getWidgetLayout().equals(Constant.WIDGETLAYOUT_SIZE_3)) {
            String priceStr = CommonUtil.FormatDouble(item.getMarketPrice());
            String zPriceStr = CommonUtil.FormatDouble(item.getPrice());
            SpanningUtil.set_Price_Format1(tv, priceStr, zPriceStr, Color.RED , Color.GRAY);
        }
    }

    public void asyncLoadData(){
        //TODO
        new Thread(){
            @Override
            public void run() {
                super.run();
                //try {
                //    Thread.sleep(1000);
                //}catch (Exception ex){}
                Message msg= mHandler.obtainMessage(1);
                List<DataItem> list= new ArrayList<DataItem>();
                DataItem item= new DataItem();
                item.setImageurl("http://images0.cnblogs.com/news_topic/letv.gif");
                item.setPrice(230.33);
                item.setzPrice(200.22);
                item.setTitle("乐视");
                item.setRebate(230);
                list.add(item);

                item= new DataItem();
                item.setImageurl("http://pic1.win4000.com/wallpaper/5/5306bc5e18592.jpg");
                item.setPrice(1330.33);
                item.setzPrice(1200.00);
                item.setTitle("美景");
                item.setRebate(20);
                list.add(item);

                item= new DataItem();
                item.setImageurl("http://news.youth.cn/yl/201410/W020141002290662988927.jpg");
                item.setPrice(1330.33);
                item.setzPrice(232.22);
                item.setTitle("胡歌");
                item.setRebate(20);
                list.add(item);


                msg.obj = list;

                mHandler.sendMessage(msg);
            }
        }.start();
    }

}
