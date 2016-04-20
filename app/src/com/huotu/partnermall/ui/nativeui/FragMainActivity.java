package com.huotu.partnermall.ui.nativeui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.ParallelExecutorCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.tscenter.biz.rpc.deviceFp.BugTrackMessageService;
import com.huotu.android.library.buyer.Jlibrary;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.BottomNavEvent;
import com.huotu.android.library.buyer.bean.Data.ClassEvent;
import com.huotu.android.library.buyer.bean.Data.FooterEvent;
import com.huotu.android.library.buyer.bean.Data.HeaderEvent;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Data.QuitEvent;
import com.huotu.android.library.buyer.bean.Data.SearchEvent;
import com.huotu.android.library.buyer.bean.Data.SmartUiEvent;
import com.huotu.android.library.buyer.bean.Data.StartLoadEvent;
import com.huotu.android.library.buyer.widget.WidgetBuilder;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.BackEvent;
import com.huotu.partnermall.ui.base.BaseFragment;
import com.huotu.partnermall.ui.base.NativeBaseActivity;
import com.huotu.partnermall.ui.frags.FragmentIndex;
import com.huotu.partnermall.ui.frags.FragmentInteractionListener;
import com.huotu.partnermall.ui.frags.FragmentSmartUI;
import com.huotu.partnermall.ui.frags.FragmentUtil;
import com.huotu.partnermall.ui.frags.FragmentWebView;
import com.huotu.partnermall.ui.frags.FragmentsAdapter;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.PATCH;

public class FragMainActivity extends NativeBaseActivity
        implements View.OnClickListener {
    @Bind(R.id.activity_fragmain_footer)
    RelativeLayout rlFooter;
    @Bind(R.id.activity_fragmain_header)
    RelativeLayout rlHeader;
    @Bind(R.id.activity_fragmain_content)
    RelativeLayout rlContent;
    @Bind(R.id.titleText)
    TextView tvTitle;
    @Bind(R.id.titleLeftImage)
    ImageView ivLeft;
    @Bind(R.id.titleRightImage)
    ImageView ivRight;

    //List<BaseFragment> fragments;
    //FragmentWebView fragmentWebView;
    //FragmentIndex fragmentIndex;
    FragmentManager fragmentManager;
    //FragmentSmartUI fragmentSmartUI;

    String smartuiConfigUrl;

    FragmentsAdapter fragmentsAdapter;
    FragmentUtil fragmentUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_main);
        ButterKnife.bind(this);
        setImmerseLayout(rlHeader);
        rlHeader.setBackgroundColor(SystemTools.obtainColor(BaseApplication.single.obtainMainColor()));
        //初始化用户等级
        Jlibrary.initUserLevelId(BaseApplication.single.readMemberLevelId());

        if (getIntent().hasExtra(NativeConstants.KEY_SMARTUICONFIGURL)) {
            smartuiConfigUrl = getIntent().getStringExtra(NativeConstants.KEY_SMARTUICONFIGURL);
        }
        if (getIntent().hasExtra(NativeConstants.KEY_ISMAINUI)) {
            //如果是主界面，则设置相关参数
            isMainUI = getIntent().getBooleanExtra(NativeConstants.KEY_ISMAINUI, false);
            Jlibrary.initMainUIConfigUrl(smartuiConfigUrl);
        }

        initView();

        initFragments();
    }
    protected void initView(){
        ivLeft.setBackgroundResource( R.drawable.main_title_left_back );
        ivLeft.setOnClickListener(this);

        ivRight.setBackgroundResource( R.drawable.home_title_right_share );
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( v.getId()==R.id.titleLeftImage){
//            backFragmentStack();
            backFrag();
        }else if( v.getId()==R.id.titleRightImage){
            share();
        }
    }

    protected void share(){
        Fragment fragment = fragmentUtil.getCurrentFragment();
        if( fragment==null) return;
        BaseFragment baseFragment =(BaseFragment) fragment;
        if( baseFragment==null ) return;
        baseFragment.share();
    }

   protected boolean backFrag(){
       Fragment fragment = fragmentUtil.getCurrentFragment();
       if( fragment==null ) return false;
       if( fragment.getClass().getName().equals( FragmentIndex.class.getName() )) return false;

       fragmentUtil.popFragment();

       fragment = fragmentUtil.getCurrentFragment();
       if( fragment !=null ){
           ((BaseFragment)fragment).refreshTitle();
       }

       if( fragment!=null && fragment.getClass().getName().equals(FragmentIndex.class.getName())){
           ivLeft.setVisibility(View.GONE);
           ivRight.setVisibility(View.GONE);
           rlFooter.setVisibility(View.VISIBLE);
       }else if( fragment!=null && fragment.getClass().getName().equals(FragmentSmartUI.class.getName())){
           ivLeft.setVisibility(View.VISIBLE);
           ivRight.setVisibility(View.GONE);
           rlFooter.setVisibility(View.VISIBLE);
       }else if( fragment!=null && fragment.getClass().getName().equals(FragmentWebView.class.getName()) ){
           ivLeft.setVisibility(View.VISIBLE);
           ivRight.setVisibility(View.VISIBLE);
           rlFooter.setVisibility(View.VISIBLE);
       }

       return true;
   }

    protected boolean backFragmentStack(){

//        Fragment curFrag = fragmentUtil.getCurrentFragment();
//        if( curFrag==null) return false;
//        if( curFrag instanceof FragmentIndex) return false;




        boolean isDeal = false;
        int count= fragmentManager.getBackStackEntryCount();
        if( count >1 ) {
            boolean isPop = fragmentManager.popBackStackImmediate();
            if( isPop) {

                isDeal = true;
            }
            refreshTitle();
        }
        return isDeal;
    }

    protected void refreshTitle(){
        //fragmentManager.getBackStackEntryAt()
        int count = fragmentManager.getBackStackEntryCount();
        if( count == 0 ) return;
        int top= count-1;
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt( top );
        String backStackName = backStackEntry.getName();
        if( TextUtils.isEmpty( backStackName )){
            return;
        }

        //fragmentUtil.showWithNoBackStack( backStackName );

//        List<Fragment> fragments = fragmentManager.getFragments();
//        if( fragments==null|| fragments.size()<1)return;
//        for(int i=0;i<fragments.size();i++){
//            if( fragments.get(i) !=null && fragments.get(i).isVisible()) {
//                BaseFragment fragment = (BaseFragment) fragments.get(i);
//                fragment.refreshTitle();
//            }else if(fragments.get(i) !=null ){
//                BaseFragment fragment = (BaseFragment) fragments.get(i);
//                fragmentUtil.hide( fragment.getClass().getName() );
//            }
//        }
    }

    protected void initFragments(){
        fragmentManager = this.getSupportFragmentManager();
        //fragmentManager.addOnBackStackChangedListener(this);
        fragmentsAdapter = new FragmentsAdapter();
        fragmentUtil = new FragmentUtil( fragmentManager , fragmentsAdapter , R.id.activity_fragmain_content);
        fragmentUtil.setDefaultPosition(0);

        Bundle bd = new Bundle();
        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL,smartuiConfigUrl);
        fragmentsAdapter.onCreateFragment(0).setArguments(bd);

        fragmentUtil.showFragment(fragmentUtil.getCurrentPosition());


//        fragmentIndex = FragmentIndex.newInstance();
//        Bundle bd = new Bundle();
//        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL , smartuiConfigUrl);
//        fragmentIndex.setArguments( bd );
//        fragmentIndex.setTagName( FragmentIndex.class.getName() );

//        fragments = new ArrayList<>();
//        fragments.add(fragmentIndex);

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.activity_fragmain_content, fragmentIndex , fragmentIndex.getTagName());
//        fragmentTransaction.commit();

//        fragmentWebView = new FragmentWebView();
//        fragmentWebView.setTagName( fragmentWebView.getClass().getName() );
//        fragments.add(fragmentWebView);

//        fragmentSmartUI = new FragmentSmartUI();
//        fragmentSmartUI.setTagName( fragmentSmartUI.getClass().getName());
//        fragments.add(fragmentSmartUI);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Register();

        dismissProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();

        UnRegister();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void QuitApp() {
        //if( backFragmentStack() ) return;
        if( backFrag()) return;

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showLongToast( "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            try {
                this.finish();
                Runtime.getRuntime().exit(0);
            } catch (Exception e) {
                Runtime.getRuntime().exit(-1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuitEvent(QuitEvent event){
        this.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeaderEvent(HeaderEvent event){
        tvTitle.setText( TextUtils.isEmpty( event.getTitle()) ? "" :  event.getTitle() );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBottonEvent(BottomNavEvent event){
        View widget = WidgetBuilder.build(event.getWidgetConfig(),this);
        if( widget ==null)return;
        rlFooter.setVisibility(View.VISIBLE);
        rlFooter.removeAllViews();
        rlFooter.addView(widget);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLinkEvnent(LinkEvent event) {
//        String tag = fragmentWebView.getTagName();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        boolean isfind = false;
//        for (int i = 0; i < fragments.size(); i++) {
//            String temp = ((BaseFragment)fragments.get(i)).getTagName();
//            Fragment fragment = fragmentManager.findFragmentByTag( temp );
//            if (fragment == null) continue;
//
//            if (((BaseFragment) fragment).getTagName().equals(tag)) {
//                fragmentTransaction.show(fragment);
//                ((FragmentWebView) fragment).setUrl(event.getLinkUrl());
//                isfind = true;
//            } else {
//                fragmentTransaction.hide(fragment);
//            }
//        }
//        if (!isfind) {
//            fragmentTransaction.add(R.id.activity_fragmain_content, fragmentWebView, tag);
//            Bundle bd = new Bundle();
//            bd.putString(Constants.INTENT_URL, event.getLinkUrl());
//            fragmentWebView.setArguments(bd);
//        }
//        fragmentTransaction.commit();


        ivRight.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        //rlFooter.setVisibility(View.GONE);

        String url = event.getLinkUrl();
        url = signUrl(url);


        boolean isExist = fragmentUtil.ExistFragment(2);
        if(isExist){
            fragmentUtil.showFragment(2);
            ((BaseFragment)fragmentUtil.getCurrentFragment()).setUrl( url );
        }else {
            Bundle bd = new Bundle();
            bd.putString(Constants.INTENT_URL, url );
            fragmentsAdapter.onCreateFragment(2).setArguments(bd);
            fragmentUtil.showFragment(2);
        }
    }

    protected String signUrl(String url){
        Uri uri = Uri.parse( url );
        String path = uri.getPath().toLowerCase().trim();
        //if( path.endsWith( Constant.URL_PERSON_INDEX) || path.endsWith(Constant.URL_SHOP_CART)){
        if( !path.endsWith( Constant.URL_DETAIL_ASPX ) ){
            AuthParamUtils paramUtils = new AuthParamUtils(BaseApplication.single, System.currentTimeMillis(), url, this);
            url = paramUtils.obtainUrl();
        }
        return url;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmartUiEvent(SmartUiEvent event) {
//        String smartUrl = event.getConfigUrl();
//        Bundle bd = new Bundle();
//        bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
//        if( event.ismainUi() ){
//            bd.putBoolean(NativeConstants.KEY_NEEDREFRESHUI,true);
//            //ActivityUtils.getInstance().showActivity(NativeActivity.this, NativeActivity.class, bd);
//        }else {
//            ActivityUtils.getInstance().showActivity(NativeActivity.this, SmartUIActivity.class, bd);
//        }

        rlFooter.setVisibility(View.VISIBLE);

        String smartUrl = event.getConfigUrl();
        if( event.ismainUi() ){
            boolean isExist = fragmentUtil.ExistFragment(0);
            if( isExist){
                fragmentUtil.showFragment(0);
                ((BaseFragment)fragmentUtil.getCurrentFragment()).setUrl( smartUrl );
            }else {
                Bundle bd = new Bundle();
                bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
                fragmentsAdapter.onCreateFragment(0).setArguments(bd);
                fragmentUtil.showFragment(0);
            }
        }else{
            boolean isExist = fragmentUtil.ExistFragment(1);
            if(isExist){
                fragmentUtil.showFragment(1);
                ((BaseFragment)fragmentUtil.getCurrentFragment()).setUrl(smartUrl);
            }else {

                Bundle bd = new Bundle();
                bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
                fragmentsAdapter.onCreateFragment(1).setArguments(bd);
                fragmentUtil.showFragment(1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackEvent(BackEvent event){
        ivLeft.setVisibility( event.isShowBack()? View.VISIBLE: View.GONE);
        ivRight.setVisibility(event.isShowShare()?View.VISIBLE:View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSmartUiClassEvent(ClassEvent event) {
        //if(!isMainUI)return;

        rlFooter.setVisibility(View.VISIBLE);

        String smartUrl = event.getConfigUrl();
        //Bundle bd = new Bundle();
        //bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
        //bd.putInt(NativeConstants.KEY_CLASSID, event.getClassId());

        boolean isExist = fragmentUtil.ExistFragment(1);
        if( isExist){
            fragmentUtil.showFragment(1);
            ((BaseFragment)fragmentUtil.getCurrentFragment()).setParameter( smartUrl , event.getClassId() , "" );
        }else {
            Bundle bd = new Bundle();
            bd.putInt(NativeConstants.KEY_CLASSID, event.getClassId());
            bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
            fragmentsAdapter.onCreateFragment(1).setArguments(bd);
            fragmentUtil.showFragment(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchUIEvent(SearchEvent event) {
        rlFooter.setVisibility(View.VISIBLE);
        String smartUrl = event.getConfigUrl();
        String keyword = event.getKeyword();
        boolean isExist = fragmentUtil.ExistFragment(1);
        if( isExist){
            fragmentUtil.showFragment(1);
            ((BaseFragment)fragmentUtil.getCurrentFragment()).setParameter( smartUrl , 0 , keyword );
        }else {
            Bundle bd = new Bundle();
            bd.putString(NativeConstants.KEY_SMARTUICONFIGURL, smartUrl);
            bd.putString(NativeConstants.KEY_SEARCH,  keyword );

            fragmentsAdapter.onCreateFragment(1).setArguments(bd);
            fragmentUtil.showFragment(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFooterEvent(FooterEvent event){
        rlFooter.setVisibility( event.isShow()? View.VISIBLE:View.GONE );
    }
}
