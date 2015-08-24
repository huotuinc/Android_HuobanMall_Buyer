package cy.com.partnermall.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import cy.com.partnermall.inner.R;

import cy.com.partnermall.adapter.IndexGalleryAdapter;
import cy.com.partnermall.entity.IndexGalleryItemData;
import cy.com.partnermall.ui.base.BaseActivity;
import cy.com.partnermall.utils.CommonTools;
import cy.com.partnermall.widgets.HomeSearchBarPopupWindow;
import cy.com.partnermall.widgets.HomeSearchBarPopupWindow.onSearchBarItemClickListener;
import cy.com.partnermall.widgets.jazzviewpager.JazzyViewPager;
import cy.com.partnermall.widgets.jazzviewpager.OutlineContainer;
import cy.com.partnermall.widgets.jazzviewpager.JazzyViewPager.TransitionEffect;

public class IndexActivity extends BaseActivity implements OnClickListener,
		onSearchBarItemClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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
}
