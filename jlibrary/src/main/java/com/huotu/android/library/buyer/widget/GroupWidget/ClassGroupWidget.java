package com.huotu.android.library.buyer.widget.GroupWidget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.ClassBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsBean;
import com.huotu.android.library.buyer.bean.BizBean.GoodsListBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.GroupBean.ClassGroupConfig;
import com.huotu.android.library.buyer.bean.GroupBean.GroupBean;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.utils.FrescoDraweeController;
import com.huotu.android.library.buyer.utils.Logger;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.SignUtil;
import com.huotu.android.library.buyer.widget.BaseLinearLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 分类分组组件
 * Created by Administrator on 2016/3/25.
 */
public class ClassGroupWidget extends BaseLinearLayout {
    private ClassGroupConfig config;
    private LayoutInflater layoutInflater;
    private LinearLayout leftMenu;
    private LinearLayoutCompat rightMenu;
    private TextView currentView;
    private List<ClassBean> classes;

    public ClassGroupWidget(Context context , ClassGroupConfig config ) {
        super(context);
        this.config = config;
        this.setBackgroundColor(Color.WHITE);
        this.setOrientation(HORIZONTAL);
        this.layoutInflater = LayoutInflater.from(getContext());
        createLeftMenu();
        createRightMenu();

        asyncRequestData();
    }

    protected void createLeftMenu(){
        if( config.getClass() ==null||config.getGroups().size()<1)return;

        int leftpadding = DensityUtils.dip2px(getContext(), 5);
        int toppadding = DensityUtils.dip2px(getContext(),8);
        leftMenu = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftMenu.setLayoutParams(layoutParams);
        leftMenu.setOrientation(VERTICAL);
        leftMenu.setPadding(leftpadding, toppadding, leftpadding, toppadding);
        this.addView(leftMenu);

        for(GroupBean bean:config.getGroups()){
            TextView menu = new TextView(getContext());
            menu.setId(menu.hashCode());
            leftMenu.addView(menu);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, toppadding, 0, toppadding);
            menu.setLayoutParams(layoutParams);
            menu.setText(bean.getName());
            menu.setTextColor(Color.BLACK);
            menu.setTag(bean);
            menu.setGravity(Gravity.CENTER);

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

    protected void createRightMenu(){
        rightMenu = new LinearLayoutCompat(getContext());
        LinearLayout.LayoutParams flayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rightMenu.setLayoutParams(flayoutParams);
        rightMenu.setBackgroundResource(R.color.lightgraywhite);
        rightMenu.setOrientation(LinearLayoutCompat.VERTICAL);
        this.addView(rightMenu);

    }

    protected void asyncRequestData(){
        if( leftMenu.getChildCount()<0)return;
        View view = leftMenu.getChildAt(0);
        if(view instanceof TextView) {
            //currentView = (TextView)view;
            view.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        if( v instanceof TextView ) {
            if (currentView != null) {
                if (currentView.getId() == v.getId()) return;
                currentView.setTextColor(Color.BLACK);
            }

            currentView = (TextView) v;
            currentView.setTextColor(getResources().getColor(R.color.orangered));
            if (currentView.getTag() == null) return;
            if (currentView.getTag() instanceof GroupBean) {
                GroupBean group = (GroupBean) currentView.getTag();

                asyncRequestData(group);
            }
        }else if( v instanceof LinearLayout){
            if(v.getTag()!=null && v.getTag() instanceof ClassBean){
                ClassBean bean = (ClassBean)v.getTag();
                String url = String.format("/%s?customerid=%d&cateid=%d", Constant.URL_CLASS_ASPX, Variable.CustomerId, bean.getCatId());///Mall/List.aspx?customerid=4471&amp;cateid=1178
                CommonUtil.link(bean.getCatName(), url );
            }
        }
    }

    protected void asyncRequestData( final GroupBean group ) {
        if( group==null)return;

        BizApiService apiService = RetrofitUtil.getBizRetroftInstance(Variable.BizRootUrl).create(BizApiService.class);
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        int customerid= Variable.CustomerId;
        int catid = group.getId();

        Call<BizBaseBean<List<ClassBean>>> call = apiService.getGoodsCatList(
                key, random, secure, customerid, catid);

        call.enqueue(new Callback<BizBaseBean<List<ClassBean>>>() {
            @Override
            public void onResponse(Response<BizBaseBean<List<ClassBean>>> response) {
                if (response == null || response.code() != Constant.REQUEST_SUCCESS
                        || response.body() == null || response.body().getData() == null || response.body().getData() == null) {
                    Logger.e(response.message());
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                if (classes == null) {
                    classes = new ArrayList<>();
                }else{
                    classes.clear();
                }

                classes.addAll(response.body().getData());

                createClassLayout(group, classes);
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
            }
        });
    }

    protected void createClassLayout( GroupBean group , List<ClassBean> classes ) {
        rightMenu.removeAllViews();
        if (classes == null || classes.size() < 1) return;
        int rightMargionPx = DensityUtils.dip2px(getContext(), 4);
        int topMargionPx = DensityUtils.dip2px(getContext(), 4);
        RelativeLayout rlGroup = new RelativeLayout(getContext());
        //TextView tvMore = new TextView(getContext());
        //int tvmoreid = tvMore.hashCode();
        //tvMore.setId(tvmoreid);
        //tvMore.setText("更多");
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //layoutParams.setMargins(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);
        //tvMore.setLayoutParams(layoutParams);
        //tvMore.setPadding(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);

        TextView tvGroup = new TextView(getContext());
        tvGroup.setText(group.getName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);
        //layoutParams.addRule(RelativeLayout.LEFT_OF, tvmoreid);
        tvGroup.setLayoutParams(layoutParams);
        tvGroup.setPadding(rightMargionPx, topMargionPx, rightMargionPx, topMargionPx);

        rlGroup.addView(tvGroup);
        //rlGroup.addView(tvMore);
        rlGroup.setTag(group);

        rightMenu.addView(rlGroup);

        create_normal();
    }

    private void create_normal(){
        int imageWidth = DensityUtils.dip2px( getContext() ,80);

        int count = classes.size();
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

                ClassBean bean = classes.get(idx);

                View view = layoutInflater.inflate(R.layout.group_goods_normal_item, rightMenu, false);
                layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                layoutParams.setMargins(margion,margion,margion,margion);
                view.setLayoutParams(layoutParams);

                LinearLayout ll = (LinearLayout) view.findViewById(R.id.group_goods_normal_item_ll);
                ll.setOnClickListener(this);
                ll.setTag(bean);

                TextView tvName = (TextView) view.findViewById(R.id.group_goods_normal_item_name);
                SimpleDraweeView ivPic = (SimpleDraweeView) view.findViewById(R.id.group_goods_normal_item_pic);
                tvName.setText(bean.getCatName());
                FrescoDraweeController.loadImage(ivPic, imageWidth, bean.getPicPath());
                lineView.addView(view);
            }
        }
    }

}
