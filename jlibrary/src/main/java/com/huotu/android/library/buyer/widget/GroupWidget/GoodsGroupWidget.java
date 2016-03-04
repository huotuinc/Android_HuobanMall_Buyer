package com.huotu.android.library.buyer.widget.GroupWidget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.huotu.android.library.buyer.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.GroupGoodsBean;
import com.huotu.android.library.buyer.bean.GroupBean.GoodsGroupConfig;
import com.huotu.android.library.buyer.bean.GroupBean.GroupBean;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.widget.SpanningUtil;
import java.util.ArrayList;
import java.util.List;


/**
 * 商品分组组件
 * Created by Administrator on 2016/2/19.
 */
public class GoodsGroupWidget extends LinearLayout implements View.OnClickListener{
    private GoodsGroupConfig config;
    private ScrollView leftScrollView;
    private LinearLayout leftMenu;
    private ScrollView rightScrollView;
    private LinearLayoutCompat rightMenu;
    private List<GroupGoodsBean> goods;
    private LayoutInflater layoutInflater;
    private int MsgCode = 100;
    private TextView currentView;
    private int scrollCount = 0;
    private ProgressDialog progressDialog;

    public GoodsGroupWidget(Context context , GoodsGroupConfig config ){
        super(context);

        this.setOrientation(HORIZONTAL);
        this.layoutInflater = LayoutInflater.from(getContext());

        this.config = config;

        createLeftMenu();
        createRightMenu();

        asyncRequestData();
    }

    protected void createRightMenu(){
        rightScrollView = new ScrollView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightScrollView.setLayoutParams(layoutParams);
        this.addView(rightScrollView);

        rightMenu = new LinearLayoutCompat(getContext());
        FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rightMenu.setLayoutParams(flayoutParams);
        rightMenu.setBackgroundResource(R.color.lightgraywhite);
        rightMenu.setOrientation(LinearLayoutCompat.VERTICAL);
        rightScrollView.addView(rightMenu);

        rightScrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        scrollCount++;
//                        if (v.getScrollY() <= 0) {//判断 滚动到顶部
//                            scrollPrevious();
//                            //return true;
//                        } else {
//                            //childView是scrollview里包含的Linearlayout容器
//                            //View childView = rightScrollView.getChildAt(0);
//                            //if (v.getScrollY() + v.getHeight() >= childView.getMeasuredHeight()) {//判断 滚动到底部
//                                //scrollNext();rightScrollView.
//                                //return true;
//                                index++;
//                            //}
//                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (scrollCount > 0) {
                            scrollCount = 0;
                            View childView = rightScrollView.getChildAt(0);
                            if (v.getScrollY() <= 0) {//判断 滚动到顶部
                                scrollPrevious();
                            } else if (v.getScrollY() + v.getHeight() >= childView.getMeasuredHeight()) {//判断 滚动到底部
                                scrollNext();
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    protected void scrollPrevious(){
        if(currentView==null)return;
        if(config.getGroups()==null||config.getGroups().size()<1) return;
        GroupBean group = (GroupBean) currentView.getTag();
        if(group==null) return;

        int idx = config.getGroups().indexOf(group);
        if(idx<0)return;
        if(idx==0) return;

        idx = idx-1;
        group = config.getGroups().get(idx);
        View view = leftMenu.findViewWithTag(group);
        if(view==null)return;
        view.performClick();

        scrollToView(view);
    }
    protected void scrollNext(){
        if(currentView==null)return;
        if(config.getGroups()==null||config.getGroups().size()<1) return;
        GroupBean group = (GroupBean) currentView.getTag();
        if(group==null) return;

        int idx = config.getGroups().indexOf(group);
        if(idx<0)return;
        idx +=1;
        if( idx >= config.getGroups().size() ) return;

        group = config.getGroups().get(idx);
        View view = leftMenu.findViewWithTag(group);
        if(view==null)return;
        view.performClick();

        scrollToView(view);
    }

    protected void scrollToView(final View view){
        Rect scrollRect=new Rect();
        leftScrollView.getHitRect(scrollRect);
        //判断view是否在scrollview可见范围内
        if(!view.getLocalVisibleRect(scrollRect)){

            leftScrollView.post(new Runnable() {
                @Override
                public void run() {
                    int[] loca1= new int[2];
                    view.getLocationOnScreen(loca1);
                    int[] loca2 = new int[2];
                    view.getLocationInWindow(loca2);
                    Log.i("tt", loca1[0] + " " + loca1[1]);
                    Log.i("tt",loca2[0]+" "+loca2[1]);
                    Log.i("tt", ""+ view.getTop());
                    int offset = loca1[1]-leftScrollView.getMeasuredHeight();
                    int offset2 = (int)view.getTop() -leftScrollView.getMeasuredHeight();
                    if( offset2<0){
                        offset2=0;
                    }else {
                        offset2 += view.getPaddingTop();
                        offset2 += view.getMeasuredHeight();
                    }
                    int theight = leftScrollView.getChildAt(0).getMeasuredHeight();
                    if( offset2> theight ){
                        offset2 = theight;
                    }
                    leftScrollView.smoothScrollTo(0, offset2);
                }
            });
        }
    }

    protected void createLeftMenu(){
        if( config.getGroups()==null||config.getGroups().size()<1)return;

        leftScrollView = new ScrollView(getContext());
        leftScrollView.setVerticalScrollBarEnabled(false);
        leftScrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        leftScrollView.setLayoutParams(layoutParams);
        this.addView(leftScrollView);
        int leftpadding = DensityUtils.dip2px(getContext(), 5);
        int toppadding = DensityUtils.dip2px(getContext(),8);
        leftMenu = new LinearLayout(getContext());
        FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftMenu.setLayoutParams(flayoutParams);
        leftMenu.setOrientation(VERTICAL);
        //leftMenu.setPadding(leftpadding, toppadding, leftpadding, toppadding);
        leftScrollView.addView(leftMenu);

        //StateListDrawable style = getMenuStyle();
        for(GroupBean bean:config.getGroups()){
            TextView menu = new TextView(getContext());
            menu.setId( menu.hashCode() );
            leftMenu.addView(menu);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, toppadding, 0, toppadding);
            menu.setLayoutParams(layoutParams);
            menu.setText(bean.getName());
            menu.setTextColor(Color.BLACK);
            menu.setTag(bean);

            menu.setGravity(Gravity.CENTER);

            //menu.setBackgroundDrawable(style);
            menu.setOnClickListener(this);
            menu.setPadding(leftpadding, toppadding, leftpadding, toppadding);

            TextView space = new TextView(getContext());
            int heightPx = DensityUtils.dip2px(getContext(),1);
            layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightPx);
            space.setBackgroundResource(R.color.lightgraywhite);
            space.setLayoutParams(layoutParams);
            leftMenu.addView(space);
        }
    }

    protected StateListDrawable getMenuStyle(){
        StateListDrawable stateListDrawable =new StateListDrawable();
        int[] selectedState = new int[]{android.R.attr.state_selected , android.R.attr.state_pressed };
        int[] normaledState =new int[]{-android.R.attr.state_selected};

        RoundRectShape roundRectShape1 = new RoundRectShape(null, null,null );
        ShapeDrawable shapeDrawable1 = new ShapeDrawable( roundRectShape1);
        shapeDrawable1.getPaint().setColor(Color.WHITE);

        RectF inset=new RectF(10f,0f,0f,0f);
        float[] outR = new float[]{0f,0f,0f,0f,0f,0f,0f,0f};
        RoundRectShape roundRectShape2 = new RoundRectShape( outR, inset , null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
        shapeDrawable2.getPaint().setColor(getResources().getColor(R.color.green));
        shapeDrawable2.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable2.getPaint().setStrokeWidth(2);

        RectF inset2=new RectF(5f,0f,0f,0f);
        RoundRectShape roundRectShape3 = new RoundRectShape(outR, inset2, outR);
        ShapeDrawable shapeDrawable3 = new ShapeDrawable(roundRectShape3);
        shapeDrawable3.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable3.getPaint().setColor(Color.WHITE);
        Drawable[] drawables=new Drawable[]{shapeDrawable2,shapeDrawable3};
        LayerDrawable layerDrawable = new LayerDrawable( drawables );


        stateListDrawable.addState(normaledState, shapeDrawable1);
        stateListDrawable.addState(selectedState, shapeDrawable2);

        return  stateListDrawable;
    }

    protected void createGoodsLayout( GroupBean group , List<GroupGoodsBean> goods ){
        rightMenu.removeAllViews();
        if( goods ==null|| goods.size()<1) return;
        int rightMargionPx = DensityUtils.dip2px(getContext(), 4);
        int topMargionPx = DensityUtils.dip2px(getContext(),4);
        RelativeLayout rlGroup = new RelativeLayout(getContext());
        TextView tvMore = new TextView(getContext());
        int tvmoreid = tvMore.hashCode();
        tvMore.setId(tvmoreid);
        tvMore.setText("更多");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);
        tvMore.setLayoutParams(layoutParams);
        tvMore.setPadding(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);

        TextView tvGroup = new TextView(getContext());
        tvGroup.setText(group.getName());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);
        layoutParams.addRule(RelativeLayout.LEFT_OF, tvmoreid);
        tvGroup.setLayoutParams(layoutParams);
        tvGroup.setPadding(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);

        rlGroup.addView(tvGroup);
        rlGroup.addView(tvMore);
        rlGroup.setTag(group);

        rightMenu.addView(rlGroup);

        if( config.getGoods_layout().equals(Constant.LAYER_STYLE_CARD)) {
            create_card( rightMenu  );
        }else if(config.getGoods_layout().equals(Constant.LAYER_STYLE_NORMAL)){
            create_normal();
        }
    }

    private void create_normal(){
        int imageWidth = DensityUtils.dip2px( getContext() ,80);

        int count = goods.size();
        int lineCount = count/3;
        int margion = DensityUtils.dip2px(getContext(),4);

        lineCount += count %3 > 0 ? 1:0;
        for(int i=0;i<lineCount;i++) {
            LinearLayout lineView = new LinearLayout(getContext());
            lineView.setOrientation(HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineView.setLayoutParams(layoutParams);
            rightMenu.addView(lineView);

            for(int k = 0;k<3;k++) {
                int idx = i * 3 + k;
                if( idx >= count ) {
                    View emptyView = layoutInflater.inflate(R.layout.group_goods_normal_item, rightMenu, false);
                    layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1.0f);
                    layoutParams.setMargins(margion, margion, margion, margion);
                    emptyView.setLayoutParams(layoutParams);
                    emptyView.setBackgroundResource(R.color.transparent);
                    lineView.addView(emptyView);
                    continue;
                }
                GroupGoodsBean bean = goods.get(idx);

                View view = layoutInflater.inflate(R.layout.group_goods_normal_item, rightMenu, false);
                layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                layoutParams.setMargins(margion,margion,margion,margion);
                view.setLayoutParams(layoutParams);

                TextView tvName = (TextView) view.findViewById(R.id.group_goods_normal_item_name);
                SimpleDraweeView ivPic = (SimpleDraweeView) view.findViewById(R.id.group_goods_normal_item_pic);
                tvName.setText(bean.getGoodname());
                FrescoDraweeController.loadImage(ivPic, imageWidth, bean.getImageUrl());
                lineView.addView(view);
            }
        }
    }

    private void create_card( LinearLayoutCompat rightMenu  ){

        int imageWidth = DensityUtils.dip2px( getContext() ,80);

        for(GroupGoodsBean bean : goods ){
            View view = layoutInflater.inflate(R.layout.group_goods_card_item, rightMenu , false );
            TextView tvName = (TextView)view.findViewById(R.id.group_goods_card_goodname);
            SimpleDraweeView ivPic = (SimpleDraweeView)view.findViewById(R.id.group_goods_card_pic);
            TextView tvPrice = (TextView)view.findViewById(R.id.group_goods_card_price);
            TextView tvJifen = (TextView) view.findViewById(R.id.group_goods_card_jifen);

            tvName.setText(bean.getGoodname());
            if( config.getProduct_userInteger().equals(Constant.GOODS_SHOW) ){
                tvJifen.setText( bean.getJifen() );
            }

            SpanningUtil.set_Price_Format1(tvPrice, bean.getPrice(), bean.getZprice(), Color.RED, Color.GRAY);

            FrescoDraweeController.loadImage(ivPic, imageWidth, bean.getImageUrl());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            rightMenu.addView(view);
            view.setTag(bean);
        }
    }

    @Override
    public void onClick(View v) {
        if(currentView != null){
            if( currentView.getId()== v.getId()) return;
            currentView.setTextColor( Color.BLACK );
        }

        currentView =  (TextView)v;
        currentView.setTextColor( getResources().getColor(R.color.orangered) );
        if( currentView.getTag()==null )return;
        if( currentView.getTag() instanceof GroupBean) {
            GroupBean group = (GroupBean) currentView.getTag();

            //showProgress();

            asyncRequestData(group);
        }
    }

    protected void asyncRequestData(){
        if( leftMenu.getChildCount()<0)return;
        View view = leftMenu.getChildAt(0);
        if(view instanceof TextView) {
            //currentView = (TextView)view;
            view.performClick();
        }
    }

//    protected void showProgress(){
//        if(progressDialog==null){
//            progressDialog=new ProgressDialog(getContext());
//            progressDialog.setMessage("正在加载数据,请稍等...");
//        }
//        progressDialog.show();
//    }
//    protected void closeProgress(){
//        if(progressDialog!=null){
//            progressDialog.dismiss();
//            progressDialog=null;
//        }
//    }

    protected void asyncRequestData( GroupBean group ) {
        if( group==null)return;

        goods = new ArrayList<>();
        for (int i = 0; i <20; i++) {
            GroupGoodsBean good = new GroupGoodsBean();
            good.setGoodid(i);
            good.setGoodname(group.getName() + String.valueOf(i));
            good.setJifen(String.valueOf(i) + "积分");
            if( i%6 ==0 ) {
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130173342.jpg");
            }else if(i%6==1){
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130175304.jpg");
            }else if(i%6==2){
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130181553.jpg");
            }else if(i%6==3){
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130175614.jpg");
            }else if(i%6==4){
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130174749.jpg");
            }else if(i%6==5){
                good.setImageUrl("http://m.olquan.cn/resource/images/wechat/4471/mall/pic20151130175413.jpg");
            }
            good.setPrice("233.00");
            good.setZprice("192.03");
            goods.add(good);
        }

        createGoodsLayout(group, goods);

        //closeProgress();
    }
}
