package com.huotu.partnermall.ui;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.CatagoryBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.KJLoger;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends BaseActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener, Handler.Callback  {

    public  BaseApplication application;
    private ImageView       searchImage;
    private ImageView       refreshBtn;
    private TextView        indexTitle;
    private
    WebView   viewPage;
    private
    Resources resources;
    private RadioGroup group;
    private Handler mHandler = null;

    private ScrollView        mScrollView;
    private ViewPager         mViewPager;
    private ArrayList< View > mViews;
    private WebView           loadPage;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        application = ( BaseApplication ) CategoryActivity.this.getApplication ( );
        resources = CategoryActivity.this.getResources ( );
        setContentView ( R.layout.category_ui );
        initView ( );
    }

    @Override
    protected
    void findViewById ( ) {
        // TODO Auto-generated method stub

    }

    private
    class MyPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public
        void onPageScrollStateChanged ( int arg0 ) {
            // TODO Auto-generated method stub

        }

        @Override
        public
        void onPageScrolled ( int arg0, float arg1, int arg2 ) {
            // TODO Auto-generated method stub

        }

        /**
         *
         */
        @Override
        public
        void onPageSelected ( int position ) {
            // TODO Auto-generated method stub

        }

    }

    private
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View v, int position, Object obj) {
            // TODO Auto-generated method stub
            ((ViewPager)v).removeView(mViews.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager)v).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    protected
    void initView ( ) {
        // TODO Auto-generated method stub
        searchImage = ( ImageView ) this.findViewById ( R.id.searchImage );
        searchImage.setOnClickListener ( this );
        refreshBtn = ( ImageView ) this.findViewById (R.id.refreshBtn);
        refreshBtn.setOnClickListener ( this );
        indexTitle = ( TextView ) this.findViewById (R.id.indexTitle);
        indexTitle.setText ( resources.getString ( R.string.category_title ) );

        group = ( RadioGroup ) this.findViewById ( R.id.index_radioGroup );
        mHandler = new Handler(this);
        mScrollView = (ScrollView)findViewById ( R.id.verticalMenu );
        mViewPager = (ViewPager)findViewById(R.id.catagoryItemPager);
        mViews = new ArrayList< View > (  );
        //动态获取产品类别
        new ObtainVerticalCatagoryAsyncTask(mHandler).execute ( "" );
        group.setOnCheckedChangeListener ( this );
        mViewPager.setOnPageChangeListener ( new MyPagerOnPageChangeListener ( ) );
        mViewPager.setCurrentItem( 1 );
    }

    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId () )
        {
            case R.id.searchImage:
            {
                //搜索事件

            }
            break;
            case R.id.refreshBtn:
            {
                //刷新界面事件
            }
            break;
            default:
                break;
        }
    }

    @Override
    public
    boolean handleMessage ( Message msg ) {
        List<CatagoryBean> catagorys = null;
        switch ( msg.what )
        {

            case Constants.SUCCESS_CODE:
            {
                catagorys = ( List< CatagoryBean > ) msg.obj;
                int size = catagorys.size ();
                for(int i=0; i<size; i++)
                {
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.vertical_radio_button, null );
                    radioButton.setText ( catagorys.get ( i ).getCatagoryName ( ) );
                    radioButton.setId ( i );
                    if(0 == i)
                    {
                        radioButton.setChecked ( true );
                    }
                    group.addView ( radioButton);

                    //设置切换界面
                    mViews.add ( getLayoutInflater ( ).inflate ( R.layout.home_web_view, null ) );

                }
            }
            break;
            case Constants.ERROR_CODE:
            {
                catagorys = ( List< CatagoryBean > ) msg.obj;
                int size = catagorys.size ();
                for(int i=0; i<size; i++)
                {
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.vertical_radio_button, null );
                    radioButton.setText ( catagorys.get ( i ).getCatagoryName ( ) );
                    radioButton.setId ( i );
                    if(0 == i)
                    {
                        radioButton.setChecked ( true );
                    }
                    group.addView ( radioButton );
                }
            }
            break;
            case Constants.NULL_CODE:
            {
                catagorys = ( List< CatagoryBean > ) msg.obj;
                int size = catagorys.size ();
                for(int i=0; i<size; i++)
                {
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.vertical_radio_button, null );
                    radioButton.setText ( catagorys.get ( i ).getCatagoryName ( ) );
                    group.addView ( radioButton );
                }
            }
            break;
        }
        return false;
    }

    @Override
    public
    void onCheckedChanged ( RadioGroup group, int checkedId ) {

        KJLoger.i ( "checkId" + checkedId );
        int max = mViews.size ();
        for(int i=0; i<max; i++)
        {
            if(i == checkedId)
            {
                mViewPager.setCurrentItem ( i+1 );
            }
        }
    }

    class ObtainVerticalCatagoryAsyncTask extends AsyncTask<String, Void, List<CatagoryBean>>
    {
        Handler mHandler = null;

        public ObtainVerticalCatagoryAsyncTask(Handler mHandler)
        {
            this.mHandler = mHandler;
        }

        @Override
        protected
        void onPreExecute ( ) {
            super.onPreExecute ( );
        }

        @Override
        protected
        void onPostExecute ( List< CatagoryBean > catagorys ) {
            super.onPostExecute ( catagorys );
            if(null != catagorys && 0 != catagorys.size ())
            {
                Message msg = mHandler.obtainMessage ( Constants.SUCCESS_CODE );
                msg.obj = catagorys;
                mHandler.sendMessage ( msg );
            }
            else if(null == catagorys)
            {
                Message msg = mHandler.obtainMessage ( Constants.ERROR_CODE );
                //赋默认值
                msg.obj = catagorys;
                mHandler.sendMessage ( msg );
            }
            else if(null != catagorys && 0 == catagorys.size ())
            {
                Message msg = mHandler.obtainMessage ( Constants.NULL_CODE );
                //赋默认值
                msg.obj = catagorys;
                mHandler.sendMessage ( msg );
            }
        }

        @Override
        protected
        List< CatagoryBean > doInBackground ( String... params ) {
            if( Constants.IS_TEST)
            {
                List<CatagoryBean> catagorys = new ArrayList< CatagoryBean > (  );
                CatagoryBean catagory1 = new CatagoryBean ();
                catagory1.setCatagoryName ( "全部" );
                CatagoryBean catagory2 = new CatagoryBean ();
                catagory2.setCatagoryName ( "男装" );
                CatagoryBean catagory3 = new CatagoryBean ();
                catagory3.setCatagoryName ( "女装" );

                catagorys.add ( catagory1 );
                catagorys.add ( catagory2 );
                catagorys.add ( catagory3 );

                return catagorys;

            }
            else
            {

            }

            return null;
        }
    }
}

