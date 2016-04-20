package com.huotu.android.library.buyer.widget.GoodsListWidget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.DataItem;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.GoodsListWidget.BaseLinearLayoutWidget;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.GoodsListBean.ListViewOneItemConfig;
import com.huotu.android.library.buyer.widget.SpanningUtil;


/**
 * Created by jinxiangdong on 2016/1/11.
 */
public class ListViewOneItemWidget extends BaseLinearLayoutWidget implements View.OnClickListener{
    private ListViewOneItemConfig config;
    private RelativeLayout rl1;
    private SimpleDraweeView pic1;
    private TextView tvName1;
    private TextView tvPrice1;
    private TextView tvJiFen1;
    private int itemWidth;
    private SimpleDraweeView jifenPic1;

    public ListViewOneItemWidget(Context context , ListViewOneItemConfig config , int itemWidth ) {
        super(context);

        this.config = config;
        this.itemWidth = itemWidth;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.listview_one_item1 , this , true );
        rl1=(RelativeLayout)findViewById(R.id.listview_one_item1_rl1);
        rl1.setOnClickListener(this);

//        LinearLayout.LayoutParams llayoutParams = (LinearLayout.LayoutParams)rl1.getLayoutParams();
//        int topMarginPx = DensityUtils.dip2px(getContext(), 4);
//        llayoutParams.setMargins(0, topMarginPx, 0, 0);
//        rl1.setLayoutParams(llayoutParams);

        pic1 = (SimpleDraweeView)findViewById(R.id.listview_one_item1_pic1);
        CommonUtil.setSimpleDraweeViewWidthHeight(pic1, this.itemWidth);
        tvName1 =  (TextView)findViewById(R.id.listview_one_item1_name1);
        tvName1.setMaxLines(2);
        tvPrice1 =  (TextView)findViewById(R.id.listview_one_item1_price1);
        tvJiFen1 =  (TextView)findViewById(R.id.listview_one_item1_jifen1);
        jifenPic1 = (SimpleDraweeView) findViewById(R.id.listview_one_item1_jifenpic1);

        if( config.getProduct_showname().equals( Constant.GOODS_SHOW ) ){
            tvName1.setVisibility(View.VISIBLE);
        }else {
            tvName1.setVisibility(View.GONE);
        }
        if( config.getProduct_showprices().equals( Constant.GOODS_SHOW ) ){
            tvPrice1.setVisibility(View.VISIBLE);
        }else{
            tvPrice1.setVisibility(View.GONE);
        }
        if( config.getProduct_userInteger().equals(Constant.GOODS_SHOW) ){
            tvJiFen1.setVisibility(View.VISIBLE);
        }else{
            tvJiFen1.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty( config.getBackground())){
            jifenPic1.setVisibility(GONE);
        }else{
            jifenPic1.setVisibility(VISIBLE);
        }

//        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(itemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
//        int leftMargionPx = DensityUtils.dip2px( getContext() , 4 );
//        int topMargionPx = leftMargionPx;
//        llLayoutParams.setMargins( leftMargionPx , topMargionPx ,0,0);
//        this.setLayoutParams(llLayoutParams);

        if( config.getGoods_layer().equals(Constant.LAYER_STYLE_CARD)){
            rl1.setBackgroundResource(R.drawable.gray_border_style);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_one_item1_pic1);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_one_item1_pic1);
            tvName1.setLayoutParams(layoutParams);
            tvName1.setTextColor(Color.BLACK);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_one_item1_name1);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_one_item1_name1);
            tvPrice1.setLayoutParams(layoutParams);

            if( TextUtils.isEmpty(config.getBackground()) ) {
                layoutParams = (RelativeLayout.LayoutParams) tvJiFen1.getLayoutParams(); //new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.listview_one_item1_price1);

                tvJiFen1.setLayoutParams(layoutParams);
                tvJiFen1.setBackgroundColor(Color.TRANSPARENT);
                tvJiFen1.setTextColor(Color.BLACK);
            }else{
                int releId;
                if(tvPrice1.getVisibility() == VISIBLE){
                    releId = R.id.listview_one_item1_price1;
                }else if( tvName1.getVisibility()==VISIBLE){
                    releId = R.id.listview_one_item1_name1;
                }else {
                    releId = R.id.listview_one_item1_pic1;
                }

                layoutParams = (RelativeLayout.LayoutParams)jifenPic1.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW , releId );
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                jifenPic1.setLayoutParams(layoutParams);

                int jWidth = DensityUtils.dip2px(getContext(), Constant.REBATEICON_WIDTH);
                String url = Variable.resourceUrl + config.getBackground();
                FrescoDraweeController.loadImage(jifenPic1 , jWidth , url );

                layoutParams = (RelativeLayout.LayoutParams) tvJiFen1.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_TOP , R.id.listview_one_item1_jifenpic1);
                layoutParams.addRule(RelativeLayout.RIGHT_OF , R.id.listview_one_item1_jifenpic1);

                tvJiFen1.setLayoutParams(layoutParams);
                tvJiFen1.setBackgroundColor(Color.TRANSPARENT);
                tvJiFen1.setTextColor(Color.BLACK);
            }
        }else if( config.getGoods_layer().equals( Constant.LAYER_STYLE_NORMAL) ){
            rl1.setBackgroundResource(0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            tvJiFen1.setLayoutParams(layoutParams);
            tvJiFen1.setBackgroundColor(Color.RED);
            tvJiFen1.setTextColor(Color.WHITE);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
            tvPrice1.setLayoutParams(layoutParams);
            tvPrice1.setTextColor(Color.WHITE);
            tvPrice1.setBackgroundResource(R.drawable.transparent_circle_bg);

        }else if(  config.getGoods_layer().equals(Constant.LAYER_STYLE_PROMOTION) ){
            rl1.setBackgroundResource(R.drawable.gray_border_style);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_one_item1_pic1);
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.listview_one_item1_pic1);
            layoutParams.setMargins(5, 5, 5, 5);
            tvJiFen1.setBackgroundColor(Color.RED);
            tvJiFen1.setTextColor(Color.WHITE);
            tvJiFen1.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_one_item1_pic1);
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.listview_one_item1_pic1);
            tvPrice1.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.listview_one_item1_pic1);
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.listview_one_item1_pic1);
            tvName1.setLayoutParams(layoutParams);
            tvName1.setBackgroundColor(Color.RED);
            int leftPadding = 6;
            int leftPaddingPx = DensityUtils.dip2px( getContext() , leftPadding );
            int rightPaddingPx = leftPaddingPx;
            int topPadding = 3;
            int topPaddingPx = DensityUtils.dip2px( getContext() , topPadding );
            int bottomPaddingPx= topPaddingPx;
            tvName1.setPadding( leftPaddingPx , topPaddingPx , rightPaddingPx , bottomPaddingPx );
            tvName1.setText("我要\r\n抢购");

        }
    }

    public void addData( GoodsBean item ){
        int width = this.itemWidth;
        //FrescoDraweeController.loadImage(pic1, width, item.getThumbnailPic());
        FrescoDraweeController.loadImage(pic1,width,width,item.getThumbnailPic());

        rl1.setTag(item);

        if( config.getGoods_layer().equals( Constant.LAYER_STYLE_CARD ) ) {
            tvName1.setText(item.getGoodName());
            String price = CommonUtil.formatDouble(item.getPrice());
            String zPrice =CommonUtil.formatPrice(item.getPriceLevel());

           SpanningUtil.set_Price_Format1(tvPrice1, price, zPrice, Color.RED, Color.GRAY);

            String jf = CommonUtil.formatJiFen( item.getScore()); //item.getScore().toString(); //CommonUtil.FormatDouble( item.getRebate() );
            tvJiFen1.setText( jf  +"积分");

        }else if( config.getGoods_layer().equals( Constant.LAYER_STYLE_NORMAL) ){
            tvName1.setText("");

            String price = CommonUtil.formatDouble( item.getPrice() );
            String zPrice = CommonUtil.formatPrice(item.getPriceLevel());
            SpanningUtil.set_Price_Format2(tvPrice1, price, zPrice, Color.WHITE, Color.WHITE);

            String jf = CommonUtil.formatJiFen(item.getScore()); //CommonUtil.FormatDouble( item.getRebate() );
            tvJiFen1.setText( jf +"积分" );

        }else if( config.getGoods_layer().equals( Constant.LAYER_STYLE_PROMOTION )){
            tvName1.setText("我要\r\n抢购");
            String jf = CommonUtil.formatJiFen(item.getScore()); //item.getScore().toString();//CommonUtil.FormatDouble( item.getRebate() );
            tvJiFen1.setText( jf  + "积分");
            String price = CommonUtil.formatDouble( item.getPrice() );
            String zPrice = CommonUtil.formatPrice( item.getPriceLevel() );

            SpanningUtil.set_Price_Format2(tvPrice1, price, zPrice, Color.RED, Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getTag()!=null && v.getTag() instanceof GoodsBean){
            GoodsBean bean = (GoodsBean)v.getTag();
            String url = bean.getDetailUrl();
            String name = bean.getGoodName();
            CommonUtil.link(name , url );
        }
    }
}