package com.huotu.partnermall.ui.sis;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.SystemTools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleActivity extends FragmentActivity implements View.OnClickListener {
    TextView back;
    SalesPageAdapter _salesAdapter;
    RelativeLayout _sales_statis1;
    ViewPager _sale_viewPager;
    MJSaleStatisticModel _data;
    TextView tvTotal;
    TextView tvToday;
    ImageView space1;
    ImageView space2;
    ImageView space3;
    RelativeLayout ll1;
    RelativeLayout ll2;
    LinearLayout ll3;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_sale);

        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void initData(){
        header = (RelativeLayout)findViewById(R.id.sis_sale_header);
        header.setBackgroundColor( SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()) );
        back =(TextView)findViewById(R.id.sis_sale_back);
        back.setOnClickListener(this);
        ll1=(RelativeLayout)findViewById(R.id.sis_sale_today_ll);
        ll1.setOnClickListener(this);
        ll2=(RelativeLayout)findViewById(R.id.sis_sale_week_ll);
        ll2.setOnClickListener(this);
        ll3 = (LinearLayout)findViewById(R.id.sis_sale_month_ll);
        ll3.setOnClickListener(this);
        space1=(ImageView)findViewById(R.id.sis_sale_today_space2);
        space2=(ImageView)findViewById(R.id.sis_sale_week_space2);
        space3=(ImageView)findViewById(R.id.sis_sale_month_space1);

        tvToday =(TextView)findViewById(R.id.sis_sale_info_count);
        tvTotal = (TextView)findViewById(R.id.sis_sale_total);

        _sales_statis1= (RelativeLayout)findViewById(R.id.sis_sale_statis1);
        _sales_statis1.setOnClickListener(this);

        _sale_viewPager = (ViewPager)findViewById(R.id.sis_sale_viewPager);

        _sale_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //_currentIndx = position;

                if (_data == null || _data.getResultData() == null) return;
                DecimalFormat format = new DecimalFormat("0.00");
                if (position == 0) {
                    Double amount = _data.getResultData().getTotalAmount();
                    Double todayAmount = _data.getResultData().getTodayAmount();
                    String temp = format.format(amount);
                    tvTotal.setText(temp);
                    temp = format.format(todayAmount);
                    tvToday.setText(temp);
                    space1.setVisibility(View.VISIBLE);
                    space2.setVisibility(View.GONE);
                    space3.setVisibility(View.GONE);
                } else if (position == 1) {
                    Double amount = _data.getResultData().getTotalAmount();
                    Double weekAmount = _data.getResultData().getWeekAmount();
                    String temp = format.format(amount);
                    tvTotal.setText(temp);
                    temp = format.format(weekAmount);
                    tvToday.setText(temp);
                    space1.setVisibility(View.GONE);
                    space2.setVisibility(View.VISIBLE);
                    space3.setVisibility(View.GONE);
                } else if (position == 2) {
                    Double amount = _data.getResultData().getTotalAmount();
                    Double monthAmount = _data.getResultData().getMonthAmount();
                    String temp = format.format(amount);
                    tvTotal.setText(temp);
                    temp = format.format(monthAmount);
                    tvToday.setText(temp);
                    space1.setVisibility(View.GONE);
                    space2.setVisibility(View.GONE);
                    space3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initFragments();
    }

    @Override
    public void onClick(View v) {
        if( v.getId()==R.id.sis_sale_statis1){
            //ToastUtils.showLongToast(this,"222222222222");
            SaleActivity.this.startActivity(new Intent(SaleActivity.this,SalesDetailActivity.class));
        }else if( v.getId()==R.id.sis_sale_today_ll){
            _sale_viewPager.setCurrentItem(0,true);
        }else if(v.getId()==R.id.sis_sale_week_ll){
            _sale_viewPager.setCurrentItem(1,true);
        }else if(v.getId()==R.id.sis_sale_month_ll){
            _sale_viewPager.setCurrentItem(2,true);
        }else if( v.getId()==R.id.sis_sale_back){
            finish();
        }
    }

    protected void initFragments(){
        _data = new MJSaleStatisticModel();
        SaleStatisticModel m = new SaleStatisticModel();
        m.setMonthAmount(1000.00);
        List<Double> am=new ArrayList<>();
        am.add(100.11);
        am.add(200.22);
        am.add(300.33);
        am.add(400.44);
        am.add(500.55);
        m.setMonthAmounts(am);
        List<Date> d = new ArrayList<>();
        Date dt =new Date(2015,11,1);
        d.add(dt  );
        dt =new Date(2015,11,2);
        d.add(dt  );
        dt =new Date(2015,11,3);
        d.add(dt  );
        dt =new Date(2015,11,4);
        d.add(dt  );
        dt =new Date(2015,11,5);
        d.add(dt);
        m.setMonthTimes(d);

        m.setTodayAmount(500.34);

        am=new ArrayList<>();
        am.add(10.11);
        am.add(20.22);
        am.add(30.33);
        am.add(40.44);
        am.add(50.55);
        m.setTodayAmounts(am);

        List<Integer>td = new ArrayList<>();
        td.add(1);
        td.add(2);
        td.add(3);
        td.add(4);
        td.add(5);
        m.setTodayTimes(td);

        m.setWeekAmount(3423.45);
        am=new ArrayList<>();
        am.add(810.11);
        am.add(820.22);
        am.add(830.33);
        am.add(840.44);
        am.add(850.55);
        m.setWeekAmounts(am);

        d = new ArrayList<>();
        dt= new Date(2015,11,1);
        d.add(dt);
        dt =new Date(2015,11,2);
        d.add(dt  );
        dt =new Date(2015,11,3);
        d.add(dt  );
        dt =new Date(2015,11,4);
        d.add(dt  );
        dt =new Date(2015,11,5);
        d.add(dt);
        m.setWeekTimes(d);

        m.setTotalAmount(999999.99);

        _data.setResultData(m);

        tvToday.setText( _data.getResultData().getTodayAmount().toString() );
        tvTotal.setText( _data.getResultData().getTotalAmount().toString() );

        _salesAdapter = new SalesPageAdapter( _data , this );
        _sale_viewPager.setAdapter(_salesAdapter);
    }

    public class MJSaleStatisticModel extends BaseModel {
        public SaleStatisticModel getResultData() {
            return resultData;
        }

        public void setResultData(SaleStatisticModel resultData) {
            this.resultData = resultData;
        }

        private SaleStatisticModel resultData;

    }

    public class SaleStatisticModel {
        private Double totalAmount;
        private Double todayAmount;
        private Double weekAmount;
        private Double monthAmount;
        private List<Integer> todayTimes;
        private List<Double> todayAmounts;
        private List<Date> weekTimes;
        private List<Double> weekAmounts;
        private List<Date> monthTimes;
        private List<Double> monthAmounts;

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Double getTodayAmount() {
            return todayAmount;
        }

        public void setTodayAmount(Double todayAmount) {
            this.todayAmount = todayAmount;
        }

        public Double getWeekAmount() {
            return weekAmount;
        }

        public void setWeekAmount(Double weekAmount) {
            this.weekAmount = weekAmount;
        }

        public Double getMonthAmount() {
            return monthAmount;
        }

        public void setMonthAmount(Double monthAmount) {
            this.monthAmount = monthAmount;
        }

        public List<Integer> getTodayTimes() {
            return todayTimes;
        }

        public void setTodayTimes(List<Integer> todayTimes) {
            this.todayTimes = todayTimes;
        }

        public List<Double> getTodayAmounts() {
            return todayAmounts;
        }

        public void setTodayAmounts(List<Double> todayAmounts) {
            this.todayAmounts = todayAmounts;
        }

        public List<Date> getWeekTimes() {
            return weekTimes;
        }

        public void setWeekTimes(List<Date> weekTimes) {
            this.weekTimes = weekTimes;
        }

        public List<Double> getWeekAmounts() {
            return weekAmounts;
        }

        public void setWeekAmounts(List<Double> weekAmounts) {
            this.weekAmounts = weekAmounts;
        }

        public List<Date> getMonthTimes() {
            return monthTimes;
        }

        public void setMonthTimes(List<Date> monthTimes) {
            this.monthTimes = monthTimes;
        }

        public List<Double> getMonthAmounts() {
            return monthAmounts;
        }

        public void setMonthAmounts(List<Double> monthAmounts) {
            this.monthAmounts = monthAmounts;
        }

    }

    public class SalesPageAdapter extends PagerAdapter {
        MJSaleStatisticModel _data;
        List<LineChart> charts;
        Context context;

        public SalesPageAdapter() {
            super();
        }

        public SalesPageAdapter( MJSaleStatisticModel data , Context context ) {
            super();
            this.context = context;
            this._data = data;
            charts =new ArrayList<>(3);
            LineChart lineChart =new LineChart( context );
            int bgColor=0xFFFFFFFF;
            lineChart.setBackgroundColor(bgColor);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription("");
            lineChart.setNoDataText("暂无数据");
            //MJMarkerView mv = new MJMarkerView( getActivity() , R.layout.custom_marker_view);
            //_saleslineChart.setMarkerView(mv);
            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            charts.add(lineChart);
            lineChart =new LineChart( context );
            lineChart.setBackgroundColor(bgColor);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription("");
            lineChart.setNoDataText("暂无数据");
            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            charts.add(lineChart);
            lineChart =new LineChart( context );
            lineChart.setBackgroundColor(bgColor);
            lineChart.setDrawGridBackground(false);
            lineChart.setDescription("");
            lineChart.setNoDataText("暂无数据");
            lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            charts.add(lineChart);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LineChart chart = charts.get(position);
            if( position == 0) {
                setLineChartData( chart, (ArrayList) _data.getResultData().getTodayTimes() , _data.getResultData().getTodayAmounts());
            }else if( position == 1){
                setLineChartData(chart ,(ArrayList) _data.getResultData().getWeekTimes() , _data.getResultData().getWeekAmounts() );
            }else if( position==2){
                setLineChartData(chart, (ArrayList)_data.getResultData().getMonthTimes(),_data.getResultData().getMonthAmounts());
            }

            container.addView(charts.get(position),0);
            return charts.get(position);
            //return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(charts.get(position) );
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public int getCount() {
            return charts.size();
        }

        protected void setLineChartData( LineChart lineChart , List<Object> xData1 , List<Double> yData1 ){
            if( xData1==null || yData1==null )return;

            //int bgColor=0xFFFFFFFF;
            int gridColor=0xFFD3D3D3;
            int lineColor =0xFFFF3C00;
            //int circleColor=0xFFFFFFFF;
            int textColor = 0xFF000000;

            //lineChart.setGridBackgroundColor(gridColor);
            //lineChart.setBackgroundColor(bgColor);
            //lineChart.setDescription("");
            //lineChart.setNoDataText("暂无数据");
            //lineChart.getAxisRight().setEnabled(false);

            List<String> xValues1= new ArrayList<String>();
            List<Entry> yValues1=new ArrayList<>();
            int count = xData1.size();
            for(int i=0;i< count ;i++){
                Object xObj = xData1.get(i);
                String x ="";
                if( xObj instanceof Date){
                    int day = ((Date)xObj ).getDate();
                    x = day+"日";
                }else {
                    x = xObj.toString()+"时";
                }

                xValues1.add( x );
                Double yDou = yData1.get(i);
                Float y = yDou.floatValue(); //(Float) yData1.get(i);
                Entry item=new Entry( y , i );
                yValues1.add(item);
            }

            LineDataSet dataSet =new LineDataSet( yValues1 ,"");
            dataSet.setCircleColor(lineColor);
            //dataSet.setCircleColors(new int[]{Color.rgb(255, 60, 00)});
            dataSet.setCircleSize(5);
            //dataSet.setDrawCircleHole(true);
            dataSet.setLineWidth(3);
            dataSet.setColor(lineColor);
            dataSet.setValueTextSize(14);
            dataSet.setValueTextColor(textColor);
            //dataSet.setDrawCubic(true);
            dataSet.setDrawValues(false);
            dataSet.setCircleColorHole( Color.WHITE );
            dataSet.setDrawCircleHole(true);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(textColor);

            YAxis yAxis1 = lineChart.getAxisRight();
            yAxis1.setTextColor(0xFFFFFFFF);
            yAxis1.setEnabled(true);
            yAxis1.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setTextColor(0xFF000000);

            lineChart.setBorderColor( gridColor );
            lineChart.setDrawBorders(true);
            //lineChart.getAxisLeft().setEnabled(false);

            lineChart.getLegend().setEnabled(false);

            LineData data =new LineData(xValues1 , dataSet );
            lineChart.setData(data);
            lineChart.animateX(2000, Easing.EasingOption.EaseInOutQuart);
        }
    }
}
