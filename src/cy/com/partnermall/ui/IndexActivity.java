package cy.com.partnermall.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cy.com.partnermall.BaseApplication;
import cy.com.partnermall.inner.R;

import cy.com.partnermall.ui.base.BaseActivity;
import cy.com.partnermall.widgets.HomeSearchBarPopupWindow.onSearchBarItemClickListener;

public class IndexActivity extends BaseActivity implements OnClickListener,
		onSearchBarItemClickListener, RadioGroup.OnCheckedChangeListener {

	public  BaseApplication application;
	private ImageView       scanImage;
	private ImageView       loginBtn;
	private TextView        title;
	private
	Resources resources;

	//商品类别切换功能
	private RadioGroup  mRadioGroup;
	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	private RadioButton mRadioButton3;
	private RadioButton mRadioButton4;
	private RadioButton mRadioButton5;
	private RadioButton mRadioButton6;
	private RadioButton mRadioButton7;
	private RadioButton mRadioButton8;
	private RadioButton mRadioButton9;
	private RadioButton mRadioButton10;

	private float                mCurrentCheckedRadioLeft;
	private HorizontalScrollView mHorizontalScrollView;
	private ViewPager            mViewPager;
	private ArrayList< View >    mViews;
	private WebView loadPage;

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

		mRadioGroup = (RadioGroup)findViewById ( R.id.radioGroup );
		mRadioButton1 = (RadioButton)findViewById ( R.id.btn1 );
		mRadioButton2 = (RadioButton)findViewById(R.id.btn2);
		mRadioButton3 = (RadioButton)findViewById ( R.id.btn3 );
		mRadioButton4 = (RadioButton)findViewById(R.id.btn4);
		mRadioButton5 = (RadioButton)findViewById ( R.id.btn5 );
		mRadioButton6 = (RadioButton)findViewById(R.id.btn6);
		mRadioButton7 = (RadioButton)findViewById ( R.id.btn7 );
		mRadioButton8 = (RadioButton)findViewById(R.id.btn8);
		mRadioButton9 = (RadioButton)findViewById(R.id.btn9);
		mRadioButton10 = (RadioButton)findViewById(R.id.btn10);
		mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalMenu);
		mViewPager = (ViewPager)findViewById(R.id.catagoryItemPager);

		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());

		mViews = new ArrayList<View>();
		mViews.add ( getLayoutInflater ( ).inflate ( R.layout.home_web_view, null ) );
		mViews.add ( getLayoutInflater ( ).inflate ( R.layout.home_web_view, null ) );
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add ( getLayoutInflater ( ).inflate ( R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));
		mViews.add(getLayoutInflater().inflate(R.layout.home_web_view, null));

		mViewPager.setAdapter ( new MyPagerAdapter ( ) );

		mRadioButton1.setChecked( true );
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
	public void onBarCodeButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onColorButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public
	void onCheckedChanged ( RadioGroup group, int checkedId ) {

		if (checkedId == R.id.btn1) {
			mViewPager.setCurrentItem(1);
		}else if (checkedId == R.id.btn2) {
			mViewPager.setCurrentItem(2);
		}else if (checkedId == R.id.btn3) {
			mViewPager.setCurrentItem(3);
		}else if (checkedId == R.id.btn4) {
			mViewPager.setCurrentItem(4);
		}else if (checkedId == R.id.btn5) {
			mViewPager.setCurrentItem(5);
		}
		else if (checkedId == R.id.btn6) {
			mViewPager.setCurrentItem(6);
		}
		else if (checkedId == R.id.btn7) {
			mViewPager.setCurrentItem(7);
		}
		else if (checkedId == R.id.btn8) {
			mViewPager.setCurrentItem(8);
		}
		else if (checkedId == R.id.btn9) {
			mViewPager.setCurrentItem(9);
		}
		else if (checkedId == R.id.btn10) {
			mViewPager.setCurrentItem(10);
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
			//Log.i("zj", "position="+position);

			/*if (position == 1) {
				mRadioButton1.performClick ( );
			}else if (position == 2) {
				mRadioButton2.performClick();
			}else if (position == 3) {
				mRadioButton3.performClick();
			}else if (position == 4) {
				mRadioButton4.performClick();
			}else if (position == 5) {
				mRadioButton5.performClick();
			}else if (position == 6) {
				mRadioButton6.performClick();
			}else if (position == 7) {
				mRadioButton7.performClick ( );
			}
			else if (position == 8) {
				mRadioButton8.performClick ( );
			}
			else if (position == 9) {
				mRadioButton9.performClick ( );
			}
			else if (position == 10) {
				mRadioButton10.performClick();
			}*/
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
}
