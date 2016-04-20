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
import android.provider.Telephony;
import android.support.v4.view.PagerTabStrip;
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
import android.widget.Toast;

import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.GroupGoodsBean;
import com.huotu.android.library.buyer.bean.Data.LoadCompleteEvent;
import com.huotu.android.library.buyer.bean.GroupBean.GoodsGroupConfig;
import com.huotu.android.library.buyer.bean.GroupBean.GroupBean;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.SpanningUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 商品分组组件
 * Created by Administrator on 2016/2/19.
 */
public class GoodsGroupWidget extends LinearLayout implements View.OnClickListener {
    private GoodsGroupConfig config;
    private ScrollView leftScrollView;
    private LinearLayout leftMenu;
    private ScrollView rightScrollView;
    private LinearLayoutCompat rightMenu;
    private List<GoodsBean> goods;
    private LayoutInflater layoutInflater;
    private int MsgCode = 100;
    private TextView currentView;
    private int scrollCount = 0;
    private ProgressDialog progressDialog;
    private LinearLayout llSpace;
    private int leftWidth;
    private int rightWidth;
    private int spaceWidth;

    public GoodsGroupWidget(Context context, GoodsGroupConfig config) {
        super(context);

        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(Color.WHITE);
        this.layoutInflater = LayoutInflater.from(getContext());

        spaceWidth = DensityUtils.px2sp(getContext(), 1);
        int screenWidth = DensityUtils.getScreenW(getContext());
        leftWidth = (screenWidth - spaceWidth)/3;
        rightWidth = screenWidth - spaceWidth - leftWidth;

        this.config = config;

        createLeftMenu();

        createSpace();

        createRightMenu();

        asyncRequestData();
    }

    protected void createSpace(){
        llSpace = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( spaceWidth , LayoutParams.MATCH_PARENT);
        llSpace.setLayoutParams(layoutParams);
        llSpace.setBackgroundResource(R.color.lightgraywhite);
        this.addView(llSpace);
    }

    protected void createRightMenu() {
        rightScrollView = new ScrollView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( rightWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
        rightScrollView.setLayoutParams(layoutParams);
        this.addView(rightScrollView);

        rightMenu = new LinearLayoutCompat(getContext());
        FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    protected void scrollPrevious() {
        if (currentView == null) return;
        if (config.getGroups() == null || config.getGroups().size() < 1) return;
        GroupBean group = (GroupBean) currentView.getTag();
        if (group == null) return;

        int idx = config.getGroups().indexOf(group);
        if (idx < 0) return;
        if (idx == 0) return;

        idx = idx - 1;
        group = config.getGroups().get(idx);
        View view = leftMenu.findViewWithTag(group);
        if (view == null) return;
        view.performClick();

        scrollToView(view);
    }

    protected void scrollNext() {
        if (currentView == null) return;
        if (config.getGroups() == null || config.getGroups().size() < 1) return;
        GroupBean group = (GroupBean) currentView.getTag();
        if (group == null) return;

        int idx = config.getGroups().indexOf(group);
        if (idx < 0) return;
        idx += 1;
        if (idx >= config.getGroups().size()) return;

        group = config.getGroups().get(idx);
        View view = leftMenu.findViewWithTag(group);
        if (view == null) return;
        view.performClick();

        scrollToView(view);
    }

    protected void scrollToView(final View view) {
        Rect scrollRect = new Rect();
        leftScrollView.getHitRect(scrollRect);
        //判断view是否在scrollview可见范围内
        if (!view.getLocalVisibleRect(scrollRect)) {

            leftScrollView.post(new Runnable() {
                @Override
                public void run() {
                    int[] loca1 = new int[2];
                    view.getLocationOnScreen(loca1);
                    int[] loca2 = new int[2];
                    view.getLocationInWindow(loca2);
                    Log.i("tt", loca1[0] + " " + loca1[1]);
                    Log.i("tt", loca2[0] + " " + loca2[1]);
                    Log.i("tt", "" + view.getTop());
                    int offset = loca1[1] - leftScrollView.getMeasuredHeight();
                    int offset2 = (int) view.getTop() - leftScrollView.getMeasuredHeight();
                    if (offset2 < 0) {
                        offset2 = 0;
                    } else {
                        offset2 += view.getPaddingTop();
                        offset2 += view.getMeasuredHeight();
                    }
                    int theight = leftScrollView.getChildAt(0).getMeasuredHeight();
                    if (offset2 > theight) {
                        offset2 = theight;
                    }
                    leftScrollView.smoothScrollTo(0, offset2);
                }
            });
        }
    }

    protected void createLeftMenu() {
        if (config.getGroups() == null || config.getGroups().size() < 1) return;

        leftScrollView = new ScrollView(getContext());
        leftScrollView.setVerticalScrollBarEnabled(false);
        leftScrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( leftWidth , LayoutParams.MATCH_PARENT);
        leftScrollView.setLayoutParams(layoutParams);
        this.addView(leftScrollView);
        int leftpadding = DensityUtils.dip2px(getContext(), 8);
        int toppadding = DensityUtils.dip2px(getContext(), 8);
        int rightMargion = DensityUtils.dip2px(getContext(), 1);
        leftMenu = new LinearLayout(getContext());
        FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //flayoutParams.setMargins(0,0,rightMargion,0);
        leftMenu.setLayoutParams(flayoutParams);
        leftMenu.setOrientation(VERTICAL);

        leftScrollView.addView(leftMenu);

        for (GroupBean bean : config.getGroups()) {
            TextView menu = new TextView(getContext());
            menu.setId(menu.hashCode());
            leftMenu.addView(menu);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //layoutParams.setMargins(0, toppadding, 0, toppadding);
            //menu.setPadding(0,toppadding,0,toppadding);
            menu.setLayoutParams(layoutParams);
            menu.setText(bean.getName());
            menu.setTextColor(Color.BLACK);
            menu.setTag(bean);

            menu.setGravity(Gravity.CENTER);

            //menu.setBackgroundDrawable(style);
            menu.setOnClickListener(this);
            menu.setPadding(leftpadding, toppadding, leftpadding, toppadding);

            TextView space = new TextView(getContext());
            int heightPx = DensityUtils.dip2px(getContext(), 1);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
            space.setBackgroundResource(R.color.lightgraywhite);
            space.setLayoutParams(layoutParams);
            leftMenu.addView(space);
        }
    }

    protected StateListDrawable getMenuStyle() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        int[] selectedState = new int[]{android.R.attr.state_selected, android.R.attr.state_pressed};
        int[] normaledState = new int[]{-android.R.attr.state_selected};

        RoundRectShape roundRectShape1 = new RoundRectShape(null, null, null);
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape1);
        shapeDrawable1.getPaint().setColor(Color.WHITE);

        RectF inset = new RectF(10f, 0f, 0f, 0f);
        float[] outR = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        RoundRectShape roundRectShape2 = new RoundRectShape(outR, inset, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
        shapeDrawable2.getPaint().setColor(getResources().getColor(R.color.green));
        shapeDrawable2.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable2.getPaint().setStrokeWidth(2);

        RectF inset2 = new RectF(5f, 0f, 0f, 0f);
        RoundRectShape roundRectShape3 = new RoundRectShape(outR, inset2, outR);
        ShapeDrawable shapeDrawable3 = new ShapeDrawable(roundRectShape3);
        shapeDrawable3.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable3.getPaint().setColor(Color.WHITE);
        Drawable[] drawables = new Drawable[]{shapeDrawable2, shapeDrawable3};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);


        stateListDrawable.addState(normaledState, shapeDrawable1);
        stateListDrawable.addState(selectedState, shapeDrawable2);

        return stateListDrawable;
    }

    protected void createGoodsLayout(GroupBean group, List<GoodsBean> goods) {
        rightMenu.removeAllViews();
        if (goods == null || goods.size() < 1) return;
        int rightMargionPx = DensityUtils.dip2px(getContext(), 4);
        int topMargionPx = DensityUtils.dip2px(getContext(), 4);
        RelativeLayout rlGroup = new RelativeLayout(getContext());
        TextView tvMore = new TextView(getContext());
        int tvmoreid = tvMore.hashCode();
        tvMore.setId(tvmoreid);
        tvMore.setText(" 更多 ");
        tvMore.setTag(group.getId());
        tvMore.setOnClickListener(this);
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

        if (config.getGoods_layout().equals(Constant.LAYER_STYLE_CARD)) {
            create_card(rightMenu);
        } else if (config.getGoods_layout().equals(Constant.LAYER_STYLE_NORMAL)) {
            create_normal();
        }
    }

    private void create_normal() {
        int imageWidth = DensityUtils.dip2px(getContext(), 80);

        int count = goods.size();
        int lineCount = count / 3;
        int margion = DensityUtils.dip2px(getContext(), 4);

        //int imageWidth = ( rightWidth - 3 * margion*2)/2;

        lineCount += count % 3 > 0 ? 1 : 0;
        for (int i = 0; i < lineCount; i++) {
            LinearLayout lineView = new LinearLayout(getContext());
            lineView.setOrientation(HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lineView.setLayoutParams(layoutParams);
            rightMenu.addView(lineView);

            for (int k = 0; k < 3; k++) {
                int idx = i * 3 + k;
                if (idx >= count) {
                    View emptyView = layoutInflater.inflate(R.layout.group_goods_normal_item, rightMenu, false);
                    layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
                    layoutParams.setMargins(margion, margion, margion, margion);
                    emptyView.setLayoutParams(layoutParams);
                    emptyView.setBackgroundResource(R.color.transparent);
                    lineView.addView(emptyView);
                    continue;
                }

                GoodsBean bean = goods.get(idx);

                View view = layoutInflater.inflate(R.layout.group_goods_normal_item, rightMenu, false);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                layoutParams.setMargins(margion, margion, margion, margion);
                view.setLayoutParams(layoutParams);

                LinearLayout ll = (LinearLayout) view.findViewById(R.id.group_goods_normal_item_ll);
                ll.setOnClickListener(this);
                ll.setTag(bean);
                TextView tvName = (TextView) view.findViewById(R.id.group_goods_normal_item_name);
                SimpleDraweeView ivPic = (SimpleDraweeView) view.findViewById(R.id.group_goods_normal_item_pic);
                tvName.setText(bean.getGoodName());
                FrescoDraweeController.loadImage(ivPic, imageWidth, bean.getThumbnailPic());
                lineView.addView(view);
            }
        }
    }

    private void create_card(LinearLayoutCompat rightMenu) {
        int imageWidth = DensityUtils.dip2px(getContext(), 80);

        for (GoodsBean bean : goods) {
            View view = layoutInflater.inflate(R.layout.group_goods_card_item, rightMenu, false);
            TextView tvName = (TextView) view.findViewById(R.id.group_goods_card_goodname);
            SimpleDraweeView ivPic = (SimpleDraweeView) view.findViewById(R.id.group_goods_card_pic);
            TextView tvPrice = (TextView) view.findViewById(R.id.group_goods_card_price);
            TextView tvJifen = (TextView) view.findViewById(R.id.group_goods_card_jifen);
            RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.group_goods_card_rl);
            rl.setOnClickListener(this);
            rl.setTag(bean);

            tvName.setText(bean.getGoodName());
            if (config.getProduct_userInteger().equals(Constant.GOODS_SHOW)) {
                tvJifen.setText(CommonUtil.formatJiFen(bean.getScore()) + "积分");
            }

            String price = CommonUtil.formatDouble(bean.getPrice());
            String zPrice = CommonUtil.formatPrice(bean.getPriceLevel());
            SpanningUtil.set_Price_Format1(tvPrice, price, zPrice, Color.RED, Color.GRAY);

            FrescoDraweeController.loadImage(ivPic, imageWidth, bean.getThumbnailPic());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            rightMenu.addView(view);
            view.setTag(bean);
        }
    }

    @Override
    public void onClick(View v) {
        if( v instanceof  TextView && v.getTag() !=null && v.getTag() instanceof  GroupBean ) {
            if (currentView != null) {
                if (currentView.getId() == v.getId()) return;
                currentView.setTextColor(Color.BLACK);
                currentView.setBackgroundResource(R.color.white);
            }

            currentView = (TextView) v;
            currentView.setTextColor(getResources().getColor(R.color.orangered));
            currentView.setBackgroundResource(R.color.lightgraywhite);
            if (currentView.getTag() == null) return;
            if (currentView.getTag() instanceof GroupBean) {
                GroupBean group = (GroupBean) currentView.getTag();
                asyncRequestData(group);
            }
        }else if( v instanceof LinearLayout || v instanceof RelativeLayout){
            if(v.getTag()!=null && v.getTag() instanceof GoodsBean){
                GoodsBean bean = (GoodsBean)v.getTag();
                CommonUtil.link( bean.getGoodName(), bean.getDetailUrl() );
            }
        }else if( v instanceof TextView && v.getTag()!=null && v.getTag() instanceof Integer ){
            int catid = (int)v.getTag();
            String url = String.format("/%s?customerid=%d&cateid=%d", Constant.URL_CLASS_ASPX , Variable.CustomerId, catid);
            CommonUtil.link("",url);
        }
    }

    protected void asyncRequestData() {
        if (leftMenu == null || leftMenu.getChildCount() < 0) return;
        View view = leftMenu.getChildAt(0);
        if (view instanceof TextView) {
            //currentView = (TextView)view;
            view.performClick();
        }
    }

    protected void asyncRequestData(final GroupBean group) {
        if (group == null) return;

        BizApiService apiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid = Variable.CustomerId;
        int userlevelid = Variable.userLevelId;
        int catid = group.getId();
        String sortRule = "0:desc";
        String filter = "";
        String searchKey = "";
        int pIndex = 1;
        int pageSize = group.getCount();

        Call<BizBaseBean<GoodsListBean>> call = apiService.getGoodsList(
                key, random, secure, customerid, catid, userlevelid, sortRule, searchKey, filter, pIndex, pageSize
        );

        call.enqueue(new Callback<BizBaseBean<GoodsListBean>>() {
            @Override
            public void onResponse( Call<BizBaseBean<GoodsListBean>> call, Response<BizBaseBean<GoodsListBean>> response) {
                if (response == null || response.code() != Constant.REQUEST_SUCCESS
                        || response.body() == null || response.body().getData() == null || response.body().getData().getGoods() == null) {
                    Logger.e(response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (goods == null) {
                    goods = new ArrayList<>();
                } else {
                    goods.clear();
                }

                goods.addAll(response.body().getData().getGoods());

                createGoodsLayout(group, goods);
            }

            @Override
            public void onFailure(Call<BizBaseBean<GoodsListBean>> call,Throwable t) {
                Logger.e(t.getMessage());
            }
        });
    }
}
