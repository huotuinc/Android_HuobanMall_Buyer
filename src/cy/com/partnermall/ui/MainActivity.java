package cy.com.partnermall.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import cy.com.partnermall.inner.R;

import cy.com.partnermall.ui.base.BaseActivity;
import cy.com.partnermall.widgets.jazzviewpager.JazzyViewPager;
import cy.com.partnermall.widgets.jazzviewpager.OutlineContainer;
import cy.com.partnermall.widgets.jazzviewpager.JazzyViewPager.TransitionEffect;
import cy.com.partnermall.widgets.viewpager.CirclePageIndicator;

public class MainActivity extends BaseActivity {

	public static final String TAG = MainActivity.class.getSimpleName();

	private JazzyViewPager mViewPager = null;
	private List<View> mPageViews = new ArrayList<View>();
	private CirclePageIndicator mIndicator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		mViewPager = (JazzyViewPager) findViewById(R.id.main_container);
		mIndicator = (CirclePageIndicator) findViewById(R.id.main_indicator);
	}

	@Override
	protected void initView() {
		View view1 = new View(MainActivity.this);
		view1.setBackgroundColor(getResources().getColor(R.color.blue));
		mPageViews.add(view1);

		View view2 = new View(MainActivity.this);
		view2.setBackgroundColor(getResources().getColor(R.color.red));
		mPageViews.add(view2);

		View view3 = new View(MainActivity.this);
		view3.setBackgroundColor(getResources().getColor(R.color.green));
		mPageViews.add(view3);

		View view4 = new View(MainActivity.this);
		view4.setBackgroundColor(getResources().getColor(R.color.yellow));
		mPageViews.add(view4);

		mViewPager.setTransitionEffect(TransitionEffect.FlipHorizontal);
		mViewPager.setAdapter(new MainAdapter());

		mIndicator.setCentered(true);
		mIndicator.setRadius(8);
		mIndicator.setViewPager(mViewPager);
	}

	private class MainAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			container.addView(mPageViews.get(position),
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mViewPager.setObjectForPosition(mPageViews.get(position), position);
			return mPageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView(mViewPager.findViewFromObject(position));
		}

		@Override
		public int getCount() {
			return mPageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}
	}

}
