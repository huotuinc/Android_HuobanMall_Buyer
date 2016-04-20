package com.huotu.partnermall.ui.frags;

import android.support.v4.app.Fragment;

import com.huotu.partnermall.ui.base.BaseFragment;

/**
 * Created by aspsine on 16/3/30.
 */
public interface FragmentNavigatorAdapter {

    BaseFragment onCreateFragment(int position);

    String getTagName(int position);

    int getCount();
}
