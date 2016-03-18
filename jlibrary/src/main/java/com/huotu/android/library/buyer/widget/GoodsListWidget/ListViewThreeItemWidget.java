package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeConfig;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewThreeItemConfig;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.widget.SpanningUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ListViewThreeItemWidget extends BaseLinearLayoutWidget implements View.OnClickListener{
    private ListViewThreeConfig listViewThreeConfig;
    private LayoutInflater layoutInflater;
    SimpleDraweeView iv1, iv2, iv3;
    TextView tvName1,tvName2,tvName3;
    TextView tvPrice1,tvPrice2,tvPrice3;
    TextView tvJiFen1,tvJiFen2, tvJiFen3;
    TextView tvLine1,tvLine2, tvLine3;
    SimpleDraweeView ivJifenPic1, ivJifenPic2, ivJifenPic3;
    RelativeLayout rl1,rl2,rl3;
    ListHandler mHandler;
    int imageWidth1,imageWidth2,imageWidth3;

    public ListViewThreeItemWidget(Context context, ListViewThreeConfig listViewThreeConfig) {
        super(context);

        this.layoutInflater = LayoutInflater.from(context);
        this.listViewThreeConfig = listViewThreeConfig;
        this.mHandler = new ListHandler(this);

        create_Layout();
    }

    @Override
    public void onClick(View v) {
        if( v.getTag()==null ) return;
        if( v.getTag() instanceof GoodsBean) {
            GoodsBean good =( GoodsBean) v.getTag();
            String detailurl = good.getDetailUrl();
            String linkname = good.getGoodName();
            EventBus.getDefault().post(new LinkEvent( linkname , detailurl));
        }
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
        if( this.listViewThreeConfig.getGoods_layout().equals(Constant.WIDGETLAYOUT_SIZE_2) ){
            create_YIDALIANGXIAO();
        }else if( this.listViewThreeConfig.getGoods_layout().equals(Constant.WIDGETLAYOUT_SIZE_3) ){
            create_XIANGXILIEBIAO();
        }else{
            Logger.e("未知布局样式");
        }
    }

    private void create_YIDALIANGXIAO(){
        this.layoutInflater.inflate(R.layout.listview_three_item1, this, true);

        if( this.listViewThreeConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)){
            setStyle_YIDALIANGXIAO_KAPINGBUJU();
        }else if(this.listViewThreeConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL) ){
            setStyle_YIDALIANGXIAO_JIJIANBUJU();
        }else{
            Logger.e("未知布局样式");
        }
    }

    private void findViewById(){
        rl1 = (RelativeLayout)findViewById(R.id.listview_three_item_rl1);
        rl1.setOnClickListener(this);
        rl2 = (RelativeLayout)findViewById(R.id.listview_three_item_rl2);
        rl2.setOnClickListener(this);
        rl3 = (RelativeLayout)findViewById(R.id.listview_three_item_rl3);
        rl3.setOnClickListener(this);

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

        ivJifenPic1 = (SimpleDraweeView) findViewById(R.id.listview_three_item_jifenPic1);
        ivJifenPic2 = (SimpleDraweeView) findViewById(R.id.listview_three_item_jifenPic2);
        ivJifenPic3 = (SimpleDraweeView) findViewById(R.id.listview_three_item_jifenPic3);

        rl1.setBackgroundResource(R.drawable.gray_border_style);
        rl2.setBackgroundResource(R.drawable.gray_border_style);
        rl3.setBackgroundResource(R.drawable.gray_border_style);

        if( !this.listViewThreeConfig.getProduct_showname().equals(Constant.GOODS_SHOW) ){
            tvName1.setVisibility(View.GONE);
            tvName2.setVisibility(View.GONE);
            tvName3.setVisibility(View.GONE);
        }else{
            tvName1.setVisibility(View.VISIBLE);
            tvName2.setVisibility(View.VISIBLE);
            tvName3.setVisibility(View.VISIBLE);
        }

        if( !this.listViewThreeConfig.getProduct_showprices().endsWith(Constant.GOODS_SHOW) ){
            tvPrice1.setVisibility(View.GONE);
            tvPrice2.setVisibility(View.GONE);
            tvPrice3.setVisibility(View.GONE);
        }else{
            tvPrice1.setVisibility(View.VISIBLE);
            tvPrice2.setVisibility(View.VISIBLE);
            tvPrice3.setVisibility(View.VISIBLE);
        }

        if( !this.listViewThreeConfig.getProduct_userInteger().equals(Constant.GOODS_SHOW) ){
            tvJiFen1.setVisibility(View.GONE);
            tvJiFen2.setVisibility(View.GONE);
            tvJiFen3.setVisibility(View.GONE);
        }else{
            tvJiFen1.setVisibility(View.VISIBLE);
            tvJiFen2.setVisibility(View.VISIBLE);
            tvJiFen3.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty( this.listViewThreeConfig.getBackground() )){
            ivJifenPic1.setVisibility(GONE);
            ivJifenPic2.setVisibility(GONE);
            ivJifenPic3.setVisibility(GONE);
        }else{
            ivJifenPic1.setVisibility(VISIBLE);
            ivJifenPic2.setVisibility(VISIBLE);
            ivJifenPic3.setVisibility(VISIBLE);
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
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.listview_three_item_jifenPic1);
        tvPrice1.setLayoutParams(layoutParams);
        tvPrice1.setTextColor(Color.WHITE);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price1);
        tvJiFen1.setLayoutParams(layoutParams);
        tvJiFen1.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)ivJifenPic1.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.listview_three_item_jifen1);
        //layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.listview_three_item_price1);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price1);
        ivJifenPic1.setLayoutParams(layoutParams);

        layoutParams =(RelativeLayout.LayoutParams) tvName2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_pic2);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic2);
        tvName2.setLayoutParams(layoutParams);
        tvName2.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_name2);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_name2);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.listview_three_item_jifenPic2);
        tvPrice2.setLayoutParams(layoutParams);
        tvPrice2.setTextColor(Color.WHITE);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price2);
        tvJiFen2.setLayoutParams(layoutParams);
        tvJiFen2.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)ivJifenPic2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.listview_three_item_jifen2);
        //layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.listview_three_item_price2);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price2);
        ivJifenPic2.setLayoutParams(layoutParams);

        layoutParams =(RelativeLayout.LayoutParams) tvName3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_pic3);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_pic3);
        tvName3.setLayoutParams(layoutParams);
        tvName3.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)tvPrice3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_three_item_name3);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_three_item_name3);
        layoutParams.addRule(RelativeLayout.LEFT_OF,R.id.listview_three_item_jifenPic3);
        tvPrice3.setLayoutParams(layoutParams);
        tvPrice3.setTextColor(Color.BLACK);

        layoutParams =(RelativeLayout.LayoutParams) tvJiFen3.getLayoutParams();
        layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price3);
        tvJiFen3.setLayoutParams(layoutParams);
        tvJiFen3.setTextColor(Color.BLACK);

        layoutParams = (RelativeLayout.LayoutParams)ivJifenPic3.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.listview_three_item_jifen3);
        //layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.listview_three_item_price3);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_three_item_price3);
        ivJifenPic3.setLayoutParams(layoutParams);

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

        set_jiefenPic();
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
        layoutParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
        tvPrice1.setLayoutParams(layoutParams);
        tvPrice1.setTextColor(Color.WHITE);
        tvPrice1.setBackgroundResource(R.drawable.transparent_circle_bg);

        set_JiFen_Style(tvJiFen1, R.id.listview_three_item_pic1, leftMargin, topMargin, rightMargin, bottomMargin);

        tvName2.setVisibility(GONE);
        layoutParams = (RelativeLayout.LayoutParams)tvPrice2.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_three_item_pic2);
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.listview_three_item_pic2);
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        tvPrice2.setLayoutParams(layoutParams);
        tvPrice2.setTextColor(Color.WHITE);
        tvPrice2.setBackgroundResource(R.drawable.transparent_circle_bg);

        set_JiFen_Style(tvJiFen2, R.id.listview_three_item_pic2, leftMargin, topMargin, rightMargin, bottomMargin);

        tvName3.setVisibility(GONE);
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

        set_jiefenPic();
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

        if( this.listViewThreeConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD) ){
            setStyle_XIANGXILIEBIAO_KAPINGBUJU();
        }else if( this.listViewThreeConfig.getGoods_layer().equals(Constant.LAYER_STYLE_NORMAL)){
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

        set_jiefenPic();
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

        set_jiefenPic();
    }

    private void create_PUBULIU(){
        //throw new Exception("no Implementation method!");
        Logger.e("no Implementation method!");
    }

    /**
     * 设置 返利 图标
     */
    private void set_jiefenPic(){
        if(TextUtils.isEmpty(listViewThreeConfig.getBackground())) return;

        int jwidth = DensityUtils.dip2px(getContext(), Constant.REBATEICON_WIDTH);
        String url = Variable.resourceUrl + listViewThreeConfig.getBackground();
        FrescoDraweeController.loadImage(ivJifenPic1, jwidth ,url );
        FrescoDraweeController.loadImage(ivJifenPic2, jwidth ,url );
        FrescoDraweeController.loadImage(ivJifenPic3, jwidth ,url );
    }

    public void addItem(List<GoodsBean> dataItems){
        if( dataItems.size()>0 ){
            set_Image_Height(iv1, imageWidth1, dataItems.get(0));
            tvName1.setText(dataItems.get(0).getGoodName());
            set_Price_Style(tvPrice1, dataItems.get(0));
            String jifen1 = CommonUtil.formatJiFen( dataItems.get(0).getScore() );
            tvJiFen1.setText( jifen1 +"积分");
            rl1.setTag( dataItems.get(0) );
        }
        if( dataItems.size() >1 ){
            //int width = iv2.getMeasuredWidth();
            //FrescoDraweeController.loadImage(iv2,width, dataItems.get(1).getImageurl());
            set_Image_Height(iv2, imageWidth2, dataItems.get(1));
            tvName2.setText(dataItems.get(1).getGoodName());
            set_Price_Style(tvPrice2, dataItems.get(1));
            //tvJiFen2.setText( CommonUtil.FormatDouble( dataItems.get(1).getRebate()));
            String jifen2 = CommonUtil.formatJiFen( dataItems.get(1).getScore() );
            tvJiFen2.setText( jifen2 +"积分");
            rl2.setTag(dataItems.get(1));
        }
        if( dataItems.size() >2 ){
            set_Image_Height(iv3, imageWidth3, dataItems.get(2));
            tvName3.setText(dataItems.get(2).getGoodName());
            set_Price_Style(tvPrice3, dataItems.get(2));
            String jifen3 = CommonUtil.formatJiFen( dataItems.get(2).getScore() );
            tvJiFen3.setText( jifen3 +"积分");
            rl3.setTag(dataItems.get(2));
        }
    }

    private void set_Image_Height( SimpleDraweeView iv , int width , GoodsBean item){
        if( listViewThreeConfig.getGoods_layout().equals( Constant.WIDGETLAYOUT_SIZE_2) ){
            //int width = iv.getMeasuredWidth();
            FrescoDraweeController.loadImage(iv, width, item.getThumbnailPic());
        }
        if( listViewThreeConfig.getGoods_layout().equals( Constant.WIDGETLAYOUT_SIZE_3) ) {
            //int width = MyApplication.ScreenWidth * 2 / 5;
            FrescoDraweeController.loadImage(iv, width, item.getThumbnailPic());
        }
    }

    private void set_Price_Style( TextView tv , GoodsBean item ){
        if( listViewThreeConfig.getGoods_layout().equals( Constant.WIDGETLAYOUT_SIZE_2) ) {
            if (listViewThreeConfig.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)) {
                String priceStr = CommonUtil.formatDouble(item.getPrice());
                String zPriceStr = CommonUtil.formatPrice( item.getPriceLevel());
                SpanningUtil.set_Price_Format2(tv, priceStr, zPriceStr, Color.RED, Color.GRAY);
            } else if (listViewThreeConfig.getGoods_layout().equals( Constant.LAYER_STYLE_NORMAL )) {
                tv.setTextColor(Color.WHITE);
            }
        }else if( listViewThreeConfig.getGoods_layout().equals(Constant.WIDGETLAYOUT_SIZE_3)) {
            String priceStr = CommonUtil.formatDouble(item.getPrice());
            String zPriceStr = CommonUtil.formatPrice(item.getPriceLevel());
            SpanningUtil.set_Price_Format1(tv, priceStr, zPriceStr, Color.RED , Color.GRAY);
        }
    }
}
