package com.huotu.partnermall.ui.frags;

import com.huotu.partnermall.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class FragmentsAdapter implements FragmentNavigatorAdapter {
    private List<BaseFragment> fragments;
    //private List<String> names;

    public FragmentsAdapter(){
        fragments=new ArrayList<>();
        FragmentIndex fragmentIndex = FragmentIndex.newInstance();
        fragmentIndex.setMainUI(true);
        fragments.add(fragmentIndex);
        FragmentSmartUI fragmentSmartUI = FragmentSmartUI.newInstance();
        fragments.add(fragmentSmartUI);
        FragmentWebView fragmentWebView = new FragmentWebView();
        fragments.add(fragmentWebView);

//        names=new ArrayList<>();
//        names.add("FragmentIndex");
//        names.add("FragmentSmartUI");
//        names.add("FragmentWebView");
    }

    @Override
    public BaseFragment onCreateFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public String getTagName(int position) {
        //return names.get(position);
        return fragments.get(position).getClass().getName();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
