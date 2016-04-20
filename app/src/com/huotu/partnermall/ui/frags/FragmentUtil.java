package com.huotu.partnermall.ui.frags;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.huotu.partnermall.ui.base.BaseFragment;

import java.util.Stack;

/**
 * Created by Administrator on 2016/4/13.
 */
public class FragmentUtil {

    private static final String EXTRA_CURRENT_POSITION = "extra_current_position";

    private FragmentManager mFragmentManager;

    private FragmentNavigatorAdapter mAdapter;

    @IdRes
    private int mContainerViewId;

    private int mCurrentPosition = -1;

    private int mDefaultPosition;

    private Stack<Integer> stack;

    public FragmentUtil(FragmentManager fragmentManager, FragmentNavigatorAdapter adapter, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mAdapter = adapter;
        this.mContainerViewId = containerViewId;

        this.stack = new Stack<>();
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION, mDefaultPosition);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    public void showFragment(int position, boolean notify) {

        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (notify) {
            removeAll(transaction);
            show(position, transaction);
            transaction.commitAllowingStateLoss();
        } else {
            String backStackName="";
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                if (position == i) {
                    backStackName = mAdapter.getTagName(i);
                    show(i, transaction);
                    stack.push(i);
                } else {
                    hide(i, transaction);
                }
            }

            transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void popFragment(){
        if(stack.empty()) return;

        int position = stack.pop();
        position = stack.peek();
        if(position == -1) return;

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        this.mCurrentPosition = position;
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                show(i, transaction);
            } else {
                hide(i, transaction);
            }
        }

        transaction.commit();
    }


    public void showFragment2( int position ) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        String backStackName = "";
        int count = mAdapter.getCount();
//        for (int i = 0; i < count; i++) {
//            if (position == i) {
//                backStackName = mAdapter.getTagName(i);
//                show(i, transaction);
//            } else {
//                hide(i, transaction);
//            }
//        }

        String tag = mAdapter.getTagName(position);
        Fragment fragment = mAdapter.onCreateFragment(position);
        transaction.replace(mContainerViewId , fragment , tag);
        //Fragment fragment = mFragmentManager.findFragmentByTag(tag);

//        if( fragment==null ){
//            add(position , transaction );
//        }else{
//            transaction.replace( mContainerViewId , fragment , tag );
//        }

        transaction.addToBackStack(tag);

        transaction.commit();
    }

    public void showFragment(int position) {

        //showFragment2(position);

        showFragment(position, false);
    }

    public void removeAllFragment(boolean allowingStateLoss) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public Fragment getCurrentFragment() {
        String tag = mAdapter.getTagName(mCurrentPosition);
        return mFragmentManager.findFragmentByTag(tag);
    }

    private void show(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTagName(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            add(position, transaction);
        } else {
            transaction.show(fragment);
        }
    }

    private void hide(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTagName(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    /**
     *
     * @param tag
     */
    public void showWithNoBackStack( String tag ){
//        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
//        if( fragment ==null) return;
//        if( !fragment.isVisible()){
//            return;
//        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.hide(fragment);
//        transaction.commit();

        for( int i = 0;i < mAdapter.getCount();i++){
            String tempTag = mAdapter.getTagName(i);
            Fragment fragment = mFragmentManager.findFragmentByTag(tag);
            if( fragment ==null ) continue;
            if( tempTag.equals( tag ) ){
                if( !fragment.isVisible()) {
                    transaction.show(fragment);
                }
            }else if(fragment.isVisible()){
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    private void add(int position, FragmentTransaction transaction) {
        Fragment fragment = mAdapter.onCreateFragment(position);
        if( fragment.isAdded()) return;
        String tag = mAdapter.getTagName(position);
        transaction.add(mContainerViewId, fragment, tag);
    }

    private void removeAll(FragmentTransaction transaction) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            remove(i, transaction);
        }
    }

    private void remove(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTagName(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    public void setDefaultPosition(int defaultPosition) {
        this.mDefaultPosition = defaultPosition;
        if (mCurrentPosition == -1) {
            this.mCurrentPosition = defaultPosition;
        }
    }

    public boolean ExistFragment(int position){
        String tag = mAdapter.getTagName(position);
        if( mFragmentManager.findFragmentByTag(tag)==null) return false;
        return true;
    }
}
