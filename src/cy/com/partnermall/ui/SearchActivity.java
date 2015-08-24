package cy.com.partnermall.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cy.com.partnermall.BaseApplication;
import cy.com.partnermall.inner.R;

import cy.com.partnermall.ui.base.BaseActivity;

public class SearchActivity extends BaseActivity implements OnClickListener {

	public  BaseApplication application;
	private ImageView       searchImage;
	private ImageView       refreshBtn;
	private TextView        indexTitle;
	private
	WebView   viewPage;
	private
	Resources resources;

	@Override
	protected
	void onCreate ( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub
		super.onCreate ( savedInstanceState );
		application = ( BaseApplication ) SearchActivity.this.getApplication ();
		resources = SearchActivity.this.getResources ();
		setContentView ( R.layout.activity_search );

		initView ();
	}

	@Override
	protected
	void findViewById ( ) {
		// TODO Auto-generated method stub
	}

	@Override
	protected
	void initView ( ) {
		// TODO Auto-generated method stub
		searchImage = ( ImageView ) this.findViewById (R.id.searchImage);
		searchImage.setOnClickListener ( this );
		refreshBtn = ( ImageView ) this.findViewById (R.id.refreshBtn);
		refreshBtn.setOnClickListener ( this );
		indexTitle = ( TextView ) this.findViewById (R.id.indexTitle);
		indexTitle.setText ( resources.getString ( R.string.search_title ) );

		setContentView ( R.layout.category_ui );
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
}
