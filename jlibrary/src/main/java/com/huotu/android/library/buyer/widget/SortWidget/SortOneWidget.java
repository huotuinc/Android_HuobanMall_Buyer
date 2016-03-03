package com.huotu.android.library.buyer.widget.SortWidget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.huotu.android.library.buyer.ApiService;
import com.huotu.android.library.buyer.adapter.BrandAdapter;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.BrandBean;
import com.huotu.android.library.buyer.bean.BizBean.ClassBean;
import com.huotu.android.library.buyer.bean.SortBean.SortOneConfig;
import com.huotu.android.library.buyer.utils.DensityUtils;
import com.huotu.android.library.buyer.R;
import com.huotu.android.library.buyer.adapter.ClassAdapter;
import com.huotu.android.library.buyer.utils.RetrofitUtil;
import com.huotu.android.library.buyer.utils.TypeFaceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jinxiangdong on 2016/1/21.
 */
public class SortOneWidget extends LinearLayout implements View.OnClickListener{
    private TextView tvone;
    private TextView tvoneup;
    private TextView tvonedown;
    private TextView tvoneline;

    private TextView tvtwo;
    private TextView tvtwodown;
    private TextView tvtwoline;

    private TextView tvthree;
    private TextView tvthreeup;
    private TextView tvthreedown;
    private TextView tvthreeline;

    private TextView tvfour;
    private TextView tvfourline;

    private TextView tvfive;
    private TextView tvfiveline;

    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private LinearLayout llFilter;
    private GridView gridView;
    private TextView tvBrand;
    private TextView tvClass;
    private TextView tvHot;

    private SortOneConfig config;
    private final String ASC="asc";
    private final String DESC="desc";
    private String sortCol="新品";
    private String sorttype=DESC;
    private View filterDialog =null;
    private ClassAdapter classAdapter;
    private List<ClassBean> classList;
    private BrandAdapter brandAdapter;
    private List<BrandBean> brandList;

    public SortOneWidget(Context context , SortOneConfig config) {
        super(context);

        this.config = config;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sort_one, this, true);

        tvone = (TextView)findViewById(R.id.sortone_1);
        tvoneup = (TextView)findViewById(R.id.sortone_1_up);
        tvoneup.setTextColor(Color.RED );
        tvonedown = (TextView)findViewById(R.id.sortone_1_down);
        tvoneline = (TextView)findViewById(R.id.sortone_1_line);

        tvtwo = (TextView)findViewById(R.id.sortone_2);
        tvtwodown = (TextView)findViewById(R.id.sortone_2_down);
        tvtwoline = (TextView)findViewById(R.id.sortone_2_line);

        tvthree = (TextView)findViewById(R.id.sortone_3);
        tvthreeup = (TextView)findViewById(R.id.sortone_3_up);
        tvthreedown = (TextView)findViewById(R.id.sortone_3_down);
        tvthreeline = (TextView)findViewById(R.id.sortone_3_line);

        tvfour = (TextView)findViewById(R.id.sortone_4);
        tvfourline = (TextView)findViewById(R.id.sortone_4_line);

        tvfive = (TextView)findViewById(R.id.sortone_5);
        tvfiveline = (TextView)findViewById(R.id.sortone_5_line);

        rl1=(RelativeLayout)findViewById(R.id.sortone_1_rl);
        rl1.setOnClickListener(this);

        rl2 = (RelativeLayout)findViewById(R.id.sortone_2_rl);
        rl2.setOnClickListener(this);

        rl3 = (RelativeLayout)findViewById(R.id.sortone_3_rl);
        rl3.setOnClickListener(this);

        rl4 = (RelativeLayout)findViewById(R.id.sortone_4_rl);
        rl4.setOnClickListener(this);

        rl5 = (RelativeLayout)findViewById(R.id.sortone_5_rl);
        rl5.setOnClickListener(this);

        llFilter = (LinearLayout)findViewById(R.id.sortone_filter);


        tvoneup.setTypeface( TypeFaceUtil.FONTAWEOME(getContext()) );
        tvonedown.setTypeface( TypeFaceUtil.FONTAWEOME(getContext()) );

        tvtwodown.setTypeface(  TypeFaceUtil.FONTAWEOME(getContext()) );

        tvthreeup.setTypeface( TypeFaceUtil.FONTAWEOME(getContext()) );
        tvthreedown.setTypeface( TypeFaceUtil.FONTAWEOME(getContext()) );

        tvoneline.setBackgroundColor(Color.RED);
        int hPx = DensityUtils.dip2px(context, 2);
        int h2Px = DensityUtils.dip2px(context,1);
        tvoneline.setHeight(hPx );

        tvtwoline.setBackgroundColor(Color.GRAY);
        tvtwoline.setHeight(h2Px);
        tvthreeline.setBackgroundColor(Color.GRAY);
        tvthreeline.setHeight(h2Px);
        tvfourline.setBackgroundColor(Color.GRAY);
        tvfourline.setHeight(h2Px);
        tvfiveline.setBackgroundColor(Color.GRAY);
        tvfiveline.setHeight(h2Px);

        if( config.isFilterRule()){
            rl5.setVisibility(VISIBLE);
        }else{
            rl5.setVisibility(GONE);
        }
    }

    protected void clearStyle(){
        tvone.setTextColor(Color.BLACK);
        tvoneline.setBackgroundColor(Color.GRAY);
        tvoneup.setTextColor(Color.GRAY);
        tvonedown.setTextColor(Color.GRAY);
        tvtwo.setTextColor(Color.BLACK);
        tvtwodown.setTextColor(Color.GRAY);
        tvtwoline.setBackgroundColor(Color.GRAY);
        tvthree.setTextColor(Color.BLACK);
        tvthreeup.setTextColor(Color.GRAY);
        tvthreedown.setTextColor(Color.GRAY);
        tvthreeline.setBackgroundColor(Color.GRAY);
        tvfour.setTextColor(Color.BLACK);
        tvfourline.setBackgroundColor(Color.GRAY);

        tvfive.setTextColor(Color.BLACK);
        tvfiveline.setBackgroundColor(Color.GRAY);

        llFilter.setVisibility(GONE);

    }

    protected void setStyle( TextView tvCol , TextView tvUp,TextView tvDown , TextView tvLine ,  String colName ){
        clearStyle();

        tvCol.setTextColor(Color.RED);
        if( sorttype.equals(ASC) && sortCol.equals(colName) ){
            tvUp.setTextColor(Color.GRAY);
            tvDown.setTextColor(Color.RED);
            sorttype = DESC;
        }else if( sorttype.equals(DESC) && sortCol.equals(colName) ){
            tvUp.setTextColor(Color.RED);
            tvDown.setTextColor(Color.GRAY);
            sorttype = ASC;
        }else{
            tvUp.setTextColor(Color.RED);
            tvDown.setTextColor(Color.GRAY);
            sorttype=ASC;
            sortCol=colName;
        }

        tvLine.setBackgroundColor(Color.RED);
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.sortone_1_rl) {
            clearStyle();
           setStyle(tvone, tvoneup, tvonedown, tvoneline, "新品");
        }else if(v.getId()==R.id.sortone_2_rl){
            clearStyle();
            tvtwoline.setBackgroundColor(Color.RED);
            tvtwo.setTextColor( Color.RED);
            tvtwodown.setTextColor(Color.RED);
            sortCol="销量";
            sorttype=DESC;
        }else if(v.getId()==R.id.sortone_3_rl){
            setStyle(tvthree, tvthreeup, tvthreedown, tvthreeline, "价格");
        }else if(v.getId()==R.id.sortone_4_rl){
            clearStyle();
            tvfourline.setBackgroundColor(Color.RED);
            tvfour.setTextColor(Color.RED);
            sortCol="综合";
            sorttype="";
        }else if( v.getId() == R.id.sortone_5_rl){
            clearStyle();
            tvfiveline.setBackgroundColor(Color.RED);
            tvfive.setTextColor(Color.RED);
            sortCol="筛选";
            sorttype="";
            showFilterDialog();
        }else if( v.getId()==R.id.layout_filter_dialog_brand ){
            tvBrand.setBackgroundResource(R.drawable.layout_filter_dialog_tab_selected_style);
            int topPad= DensityUtils.dip2px(getContext(),10);
            int bottonPad= DensityUtils.dip2px(getContext(),12);
            int topPad2 = DensityUtils.dip2px(getContext(),8);
            tvBrand.setPadding(0, topPad, 0, bottonPad);
            tvClass.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            tvClass.setPadding(0, topPad2, 0, topPad2);
            tvHot.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            tvHot.setPadding(0, topPad2, 0, topPad2);
            int margion = DensityUtils.dip2px(getContext(),4);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvClass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //layoutParams.setMargins(margion, margion, margion, margion);
            tvBrand.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvClass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.layout_filter_dialog_line1);
            layoutParams.setMargins(margion, margion, margion, margion);
            tvClass.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvClass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.layout_filter_dialog_line2);
            layoutParams.setMargins(margion, margion, margion, margion);
            tvHot.setLayoutParams(layoutParams);

            tvBrand.setTextColor(ContextCompat.getColor( getContext() , R.color.orangered));
            tvClass.setTextColor( ContextCompat.getColor(getContext() , R.color.gray));
            tvHot.setTextColor( ContextCompat.getColor( getContext() , R.color.gray));

        }else if( v.getId()==R.id.layout_filter_dialog_class){
            tvClass.setBackgroundResource(R.drawable.layout_filter_dialog_tab_selected_style);
            tvBrand.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            tvHot.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            int topPad= DensityUtils.dip2px(getContext(),10);
            int bottonPad= DensityUtils.dip2px(getContext(),12);
            int topPad2 = DensityUtils.dip2px(getContext(),8);
            tvClass.setPadding(0, topPad, 0, bottonPad);
            tvBrand.setPadding(0, topPad2, 0, topPad2);
            tvHot.setPadding(0, topPad2, 0, topPad2);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvClass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tvClass.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvBrand.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.layout_filter_dialog_line0);
            int margion = DensityUtils.dip2px(getContext(),4);
            layoutParams.setMargins(margion,margion,margion,margion);
            tvBrand.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvHot.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.layout_filter_dialog_line2);
            layoutParams.setMargins(margion, margion, margion, margion);
            tvHot.setLayoutParams(layoutParams);

            tvBrand.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            tvClass.setTextColor( ContextCompat.getColor(getContext(),R.color.orangered) );
            tvHot.setTextColor( ContextCompat.getColor(getContext(),R.color.gray) );
        }else if( v.getId()==R.id.layout_filter_dialog_hot){
            tvHot.setBackgroundResource(R.drawable.layout_filter_dialog_tab_selected_style);
            tvBrand.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            tvClass.setBackgroundResource(R.drawable.layout_filter_dialog_tab_normal_style);
            int topPad= DensityUtils.dip2px(getContext(),10);
            int bottonPad= DensityUtils.dip2px(getContext(),12);
            int topPad2 = DensityUtils.dip2px(getContext(),8);
            tvHot.setPadding(0, topPad, 0, bottonPad);
            tvBrand.setPadding(0, topPad2, 0, topPad2);
            tvClass.setPadding(0, topPad2, 0, topPad2);

            int margion = DensityUtils.dip2px(getContext(),4);
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvHot.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //layoutParams.setMargins(margion, margion, margion, margion);
            tvHot.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvBrand.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.layout_filter_dialog_line0);
            layoutParams.setMargins(margion, margion, margion, margion);
            tvBrand.setLayoutParams(layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //(RelativeLayout.LayoutParams)tvClass.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.layout_filter_dialog_line1);
            layoutParams.setMargins(margion, margion, margion, margion);
            tvClass.setLayoutParams(layoutParams);

            tvBrand.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            tvClass.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
            tvHot.setTextColor(ContextCompat.getColor(getContext(),R.color.orangered));
        }
    }

    /**
     * 弹出筛选对话框
     */
    protected void showFilterDialog(){
        llFilter.setVisibility(VISIBLE);
        if( filterDialog ==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            filterDialog = layoutInflater.inflate(R.layout.layout_filter_dialog, null);
            llFilter.addView(filterDialog);
            gridView = (GridView)llFilter.findViewById(R.id.layout_filter_dialog_data);

            //LinearLayout.LayoutParams layoutParams = (LayoutParams)gridView.getLayoutParams();

            tvBrand = (TextView)llFilter.findViewById(R.id.layout_filter_dialog_brand);
            tvBrand.setOnClickListener(this);
            tvClass = (TextView)llFilter.findViewById(R.id.layout_filter_dialog_class);
            tvClass.setOnClickListener(this);
            tvHot = (TextView)llFilter.findViewById(R.id.layout_filter_dialog_hot);
            tvHot.setOnClickListener(this);
            asyncGetClassData();
        }
    }

    protected void asyncGetBrandData(){
        ApiService apiService = RetrofitUtil.getInstance().create(ApiService.class);
        int customerid=0;
        Call<BizBaseBean<List<BrandBean>>> call = apiService.getBrandList(customerid);
        call.enqueue(new Callback<BizBaseBean<List<BrandBean>>>() {
            @Override
            public void onResponse(Response<BizBaseBean<List<BrandBean>>> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    protected void asyncGetClassData(){
        if(classList ==null){
            classList =new ArrayList<>();
            classAdapter = new ClassAdapter( gridView , classList,getContext());
            gridView.setAdapter(classAdapter);
        }
        for(int i=0;i<50;i++){
            ClassBean bean = new ClassBean();
            bean.setCatId(i);
            bean.setCatName("分享石帆芬" + String.valueOf(i));
            bean.setCatPath("");
            bean.setChildCount(20);
            bean.setGoodCount(100);
            bean.setParentId(i);
            classList.add(bean);
        }
        classAdapter.notifyDataSetChanged();

    }
}
