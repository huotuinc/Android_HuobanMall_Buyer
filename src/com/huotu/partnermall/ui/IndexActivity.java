package com.huotu.partnermall.ui;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.CatagoryBean;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.widgets.OneKeyShareUtils;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends BaseActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener, Handler.Callback {

    public  BaseApplication application;
    private ImageView       scanImage;
    private ImageView       loginBtn;
    private TextView        title;
    private
    Resources resources;
    private RadioGroup group;
    private Handler mHandler = null;

    private HorizontalScrollView mHorizontalScrollView;
    private ViewPager            mViewPager;
    private ArrayList< View >    mViews;
    private WebView              loadPage;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        // TODO Auto-generated method stub
        super.onCreate ( savedInstanceState );
        resources = IndexActivity.this.getResources ( );
        application = ( BaseApplication ) IndexActivity.this.getApplication ( );
        setContentView ( R.layout.activity_index );

        initView ( );
    }

    @Override
    protected
    void findViewById ( ) {
        // TODO Auto-generated method stub

    }

    @Override
    protected
    void initView ( ) {
        scanImage = ( ImageView ) this.findViewById ( R.id.scanImage );
        scanImage.setOnClickListener ( this );
        loginBtn = ( ImageView ) this.findViewById ( R.id.loginBtn );
        loginBtn.setOnClickListener ( this );
        title = ( TextView ) this.findViewById ( R.id.title );
        title.setText ( resources.getString ( R.string.home_title ) );
        group = ( RadioGroup ) this.findViewById ( R.id.index_radioGroup );
        mHandler = new Handler(this);
        mHorizontalScrollView = (HorizontalScrollView)findViewById ( R.id.horizontalMenu );
        mViewPager = (ViewPager)findViewById(R.id.catagoryItemPager);
        mViews = new ArrayList< View > (  );
        //动态获取产品类别
        new ObtainhorizontalCatagoryAsyncTask(mHandler).execute ( "" );
        group.setOnCheckedChangeListener ( this );
        mViewPager.setOnPageChangeListener ( new MyPagerOnPageChangeListener ( ) );
        mViewPager.setCurrentItem( 1 );
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch ( v.getId () )
        {
            case R.id.scanImage:
            {
                //扫描功能键
                //模拟扫描类
                /*String shareTitle = "分享01";
                String shareText = "分享的一个测试";
                String shareUrl = "http://www.baidu.com";

                OneKeyShareUtils oks = new OneKeyShareUtils ( shareTitle, null, shareText, null, shareUrl, null, null, null, IndexActivity.this );
                oks.shareShow (null, false);*/
            }
            break;
            case R.id.loginBtn:
            {
                //切换出侧滑界面
                application.layDrag.openDrawer ( Gravity.RIGHT );
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
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.catagory_radio, null );
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
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.catagory_radio, null );
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
                    RadioButton radioButton = ( RadioButton ) LayoutInflater.from ( this ).inflate ( R.layout.catagory_radio, null );
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

        KJLoger.i ( "checkId" + checkedId);
        int max = mViews.size ();
        for(int i=0; i<max; i++)
        {
            if(i == checkedId)
            {
                mViewPager.setCurrentItem ( i+1 );
            }
        }

    }

    private class MyPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }
        /**
         *
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub

        }

    }

    private class MyPagerAdapter extends PagerAdapter {

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

    class ObtainhorizontalCatagoryAsyncTask extends AsyncTask<String, Void, List<CatagoryBean>>
    {
        Handler mHandler = null;

        public ObtainhorizontalCatagoryAsyncTask(Handler mHandler)
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

