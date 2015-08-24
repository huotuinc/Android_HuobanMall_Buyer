package cy.com.partnermall.ui;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.nostra13.universalimageloader.core.ImageLoader;

import cy.com.partnermall.BaseApplication;
import cy.com.partnermall.inner.R;

import cy.com.partnermall.AppManager;
import cy.com.partnermall.utils.ToastUtils;

public class HomeActivity extends TabActivity {

	public static final String TAG = HomeActivity.class.getSimpleName ( );

	private long exitTime = 0l;
	private RadioGroup   mTabButtonGroup;
	private TabHost      mTabHost;
	//application引用
	public BaseApplication application;
	//侧滑菜单引用
	/**
	 * 底部Tab菜单
	 */
	public static final String TAB_MAIN     = "MAIN_ACTIVITY";
	public static final String TAB_SEARCH   = "SEARCH_ACTIVITY";
	public static final String TAB_CATEGORY = "CATEGORY_ACTIVITY";
	public static final String TAB_CART     = "CART_ACTIVITY";
	public static final String TAB_PERSONAL = "PERSONAL_ACTIVITY";

	@Override
	protected
	void onCreate ( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub
		super.onCreate ( savedInstanceState );
		application = ( BaseApplication ) HomeActivity.this.getApplication ();
		AppManager.getInstance ( ).addActivity ( this );
		setContentView ( R.layout.activity_home );
		findViewById ( );
		initView ( );
	}

	private
	void findViewById ( ) {
		mTabButtonGroup = ( RadioGroup ) findViewById ( R.id.home_radio_button_group );
	}

	private
	void initView ( ) {

		mTabHost = getTabHost ( );

		Intent i_main     = new Intent ( this, IndexActivity.class );
		Intent i_search   = new Intent ( this, SearchActivity.class );
		Intent i_category = new Intent ( this, CategoryActivity.class );
		Intent i_cart     = new Intent ( this, CartActivity.class);
		Intent i_personal = new Intent(this, PersonalActivity.class);

		//初始化侧滑菜单面板
		application.layDrag = ( DrawerLayout ) this.findViewById (R.id.layDrag);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_main));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_SEARCH)
				.setIndicator(TAB_SEARCH).setContent(i_search));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CATEGORY)
				.setIndicator(TAB_CATEGORY).setContent(i_category));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_CART).setIndicator(TAB_CART)
				.setContent(i_cart));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_PERSONAL)
				.setIndicator(TAB_PERSONAL).setContent(i_personal));

		mTabHost.setCurrentTabByTag(TAB_MAIN);

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_main:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							break;

						case R.id.home_tab_search:
							mTabHost.setCurrentTabByTag(TAB_SEARCH);
							break;

						case R.id.home_tab_category:
							mTabHost.setCurrentTabByTag(TAB_CATEGORY);
							break;

						case R.id.home_tab_cart:
							mTabHost.setCurrentTabByTag(TAB_CART);
							break;

						case R.id.home_tab_personal:
							mTabHost.setCurrentTabByTag(TAB_PERSONAL);
							break;

						default:
							break;
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		// 2秒以内按两次推出程序
		if (event.getKeyCode () == KeyEvent.KEYCODE_BACK
			&& event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - exitTime) > 2000)
			{
				ToastUtils.showLongToast ( getApplicationContext ( ), "再按一次退出程序" );
				exitTime = System.currentTimeMillis();
			} else
			{
				try
				{

					HomeActivity.this.finish();
					Runtime.getRuntime().exit(0);
				} catch (Exception e)
				{
					Runtime.getRuntime().exit(-1);
				}
			}

			return true;
		}
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent ( event );
	}
}
