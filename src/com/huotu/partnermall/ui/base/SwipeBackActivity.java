package com.huotu.partnermall.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.widgets.SwipeBackLayout;

/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 *
 * @author xiaanming
 * Created by Administrator on 2017/3/17.
 */
public class SwipeBackActivity extends BaseActivity {
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate( R.layout.activity_swipebase , null);
        layout.attachToActivity(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right , R.anim.slide_remain);
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.out_from_right );
    }

}
