package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.widgets.KJEditText;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddGoodsActivity extends Activity implements View.OnClickListener{
    LinearLayout llClass1;
    GridView llClass2;
    ClassAdapter adapter;
    GoodsAdapter goodsAdapter;
    PullToRefreshListView goodListView;
    List<SisGoodsModel> goodsList;
    KJEditText etSearchBar;
    TextView tvCancel;
    TextView ivQuery;
    final String PRE_SEARCHKEY_FILE="searchkey_info";
    final String PRE_SEARCHKEY_NAME="searchkey";
    String searchKeys = "";
    List<String> searchKeysList=null;
    ArrayAdapter<String> keysAdapter=null;
    RelativeLayout rlSearchBar;
    RelativeLayout rlHeader;
    RelativeLayout rlcd;
    ImageView ivSelect;
    TextView tvAll;
    TextView tvLine;
    HorizontalScrollView scrollView;
    BaseApplication app;
    List<SisSortModel> categorys;
    String key="";
    int pageno;
    Long classid=0L;
    boolean isRefreshing=true;
    Handler handler;
    ProgressPopupWindow progress;
    ProgressDialog progressDialog;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_add_goods);

        app = (BaseApplication)this.getApplication();
        handler = new Handler(getMainLooper());
        btnBack = (Button)findViewById(R.id.sis_addgoods_back);
        btnBack.setOnClickListener(this);
        rlcd = (RelativeLayout)findViewById(R.id.sis_addgoods_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));
        rlSearchBar =(RelativeLayout)findViewById(R.id.sis_addgoods_searchBar);
        rlSearchBar.setBackgroundColor( SystemTools.obtainColor( app.obtainMainColor()));
        rlHeader = (RelativeLayout)findViewById(R.id.sis_addgoods_header);
        rlHeader.setBackgroundColor(SystemTools.obtainColor( app.obtainMainColor()));
        scrollView =(HorizontalScrollView)findViewById(R.id.sis_addgoods_scrollView);
        ivSelect =(ImageView)findViewById(R.id.sis_addgoods_select);
        ivSelect.setOnClickListener(this);
        tvAll = (TextView)findViewById(R.id.sis_addgoods_all);
        tvAll.setOnClickListener(this);
        tvLine =(TextView)findViewById(R.id.sis_addgoods_footerline);
        tvCancel = (TextView)findViewById(R.id.sis_addgoods_cancel);
        tvCancel.setOnClickListener(this);
        ivQuery = (TextView)findViewById(R.id.sis_addgoods_query);
        ivQuery.setOnClickListener(this);
        etSearchBar = (KJEditText)findViewById(R.id.sis_addgoods_key);
        etSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(etSearchBar.getText())) {
                        etSearchBar.requestFocus();
                        etSearchBar.setError("搜索条件不能为空");
                    } else {
                        key = etSearchBar.getText().toString();
                        key = key.trim();
                        key = key.replace(",", "");
                        key = key.replace("，", "");
                        if (searchKeysList != null && !searchKeysList.contains(key)) {
                            String temp = "";
                            for (String item : searchKeysList) {
                                if (!TextUtils.isEmpty(temp)) temp += ",";
                                temp += item;
                            }
                            if (temp != "") temp = "," + temp;
                            temp = key + temp;
                            AddGoodsActivity.this.searchKeysList.add(0, key);
                            keysAdapter.notifyDataSetChanged();
                            PreferenceHelper.writeString(AddGoodsActivity.this, PRE_SEARCHKEY_FILE, PRE_SEARCHKEY_NAME, temp);
                        }

                        pageno = 0;
                        isRefreshing = true;
                        if( goodListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ){
                            goodListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            goodListView.setMode(PullToRefreshBase.Mode.BOTH);
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                goodListView.setRefreshing(true);
                            }
                        });
                    }
                    return true;
                }

                return false;
            }
        });

        loadSearchKeys();

        goodsList=new ArrayList<>();
        goodsAdapter =new GoodsAdapter(AddGoodsActivity.this, goodsList);
        llClass1=(LinearLayout)findViewById(R.id.sis_addgoods_class1);
        llClass2=(GridView)findViewById(R.id.sis_addgoods_class2);
        goodListView= (PullToRefreshListView)findViewById(R.id.sis_addgoods_listview);
        goodListView.setMode(PullToRefreshBase.Mode.BOTH);
        goodListView.setAdapter(goodsAdapter);
        goodListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageno = 0;
                isRefreshing = true;
                loadGoods(classid, key, pageno);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefreshing = false;
                loadGoods(classid, key, pageno);
            }
        });

        setImmerseLayout();

        getClassData();
    }

    public void setImmerseLayout(){
        if ( ((BaseApplication)this.getApplication()).isKITKAT ()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            int statusBarHeight;
            int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen","android");
            if (resourceId > 0) {
                statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
                rlcd.setPadding(0,statusBarHeight,0,0);
                rlcd.getLayoutParams().height+=statusBarHeight;
                rlcd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()) );
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if( v.getId()==R.id.sis_addgoods_cancel ){
            key = "";
            rlHeader.setVisibility(View.VISIBLE);
            rlSearchBar.setVisibility(View.GONE);
        }else if( v.getId() == R.id.sis_addgoods_query){
            rlHeader.setVisibility(View.GONE);
            rlSearchBar.setVisibility(View.VISIBLE);
            etSearchBar.requestFocus();
        }else if( v.getId()== R.id.sis_addgoods_select){
            if( adapter!=null && adapter.getCount() <=0 ) {
                ToastUtils.showShortToast(AddGoodsActivity.this,"没有二级分类");
                return;
            }

            if(((ImageView)v).getTag() ==null ||  ((ImageView)v).getTag().equals("∨")){
                ((ImageView) v).setTag("∧");
                ((ImageView)v).setImageResource(R.drawable.up);
                llClass2.setVisibility(View.VISIBLE);
            }else{
                ((ImageView) v).setTag("∨");
                ((ImageView)v).setImageResource(R.drawable.down);
                llClass2.setVisibility(View.GONE);
            }
        }else if( v.getId() == R.id.sis_addgoods_all ){
            selectAll();
        }else if( v.getId() == R.id.sis_addgoods_back){
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == SisConstant.REFRESHGOODS_CODE ){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (AddGoodsActivity.this.isFinishing()) return;

                    if (goodListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                        goodListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        goodListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    goodListView.setRefreshing(true);
                }
            }, 500);
        }
    }

    protected void selectAll(){
        for (int i = 0; i < llClass1.getChildCount(); i++) {
            llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_footline).setBackgroundColor(Color.WHITE);
            ((TextView)llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_name)).setTextColor(Color.BLACK);
        }
        tvAll.setTextColor( SystemTools.obtainColor( app.obtainMainColor() ) );
        tvLine.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));
        ivSelect.setTag("∨");

        if( tvAll.getTag() !=null  ) {
            SisSortModel allitem = (SisSortModel)tvAll.getTag();
            loadClass2(allitem);
        }

        llClass2.setVisibility(View.GONE);

        classid = 0L;
        pageno=0;
        if( goodListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ){
            goodListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            goodListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        goodListView.setRefreshing(true);
    }

    protected void loadSearchKeys(){
        searchKeys = PreferenceHelper.readString(this, PRE_SEARCHKEY_FILE, PRE_SEARCHKEY_NAME, "");
        String[] keys = searchKeys.split(",");
        if( keys != null && keys.length > 0 ) {
            if( keys.length > 200 ) {
                String[] temp = new String[200];
                System.arraycopy( keys , 0 ,  temp , 0, 200 );
                searchKeysList = new ArrayList<String>(Arrays.asList(temp));
            }else{
                searchKeysList = new ArrayList<String>(Arrays.asList(keys));
            }
        }else{
            searchKeysList = new ArrayList<String>();
        }
        keysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchKeysList );
        etSearchBar.setAdapter(keysAdapter);
        etSearchBar.setThreshold(1);
        int width = getWindowManager().getDefaultDisplay().getWidth() - tvCancel.getWidth();
        etSearchBar.setDropDownWidth(width);
        etSearchBar.setDropDownVerticalOffset(30);
        etSearchBar.setDropDownWidth(350);
    }

    protected void getClassData(){
        if( SisConstant.CATEGORY !=null && SisConstant.CATEGORY.size()>0 ){

            loadClass1(SisConstant.CATEGORY);
            classid = 0L;
            for(SisSortModel item : SisConstant.CATEGORY){
                if( item.getSisId()==0){
                    tvAll.setTag( item );
                    loadClass2(item);
                    break;
                }
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goodListView.setRefreshing(true);
                }
            },300);
            return;
        }

        if(!Util.isConnect(this)){
            ToastUtils.showLongToast(this,"无网络");
            return;
        }
        if( progressDialog ==null){
            progressDialog=new ProgressDialog(AddGoodsActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage("正在获取数据，请稍等...");
        progressDialog.show();

        String url = SisConstant.INTERFACE_getCategoryList;
        String userid = app.readMemberId();
        url += "?userid="+ userid;
        AuthParamUtils paramUtils =new AuthParamUtils( app , System.currentTimeMillis() , url , this);
        url = paramUtils.obtainUrlName();

        GsonRequest<AppSisSortModel> request =
                new GsonRequest<>(
                        Request.Method.GET ,
                        url,
                        AppSisSortModel.class,
                        null,
                        new MyListener(AddGoodsActivity.this),
                        new MyErrorListener(AddGoodsActivity.this)
                        );
        VolleyUtil.getRequestQueue().add(request);
    }

    protected void loadClass1( final List<SisSortModel> data ){
        llClass1.removeAllViews();

        tvAll.setTextColor(SystemTools.obtainColor( app.obtainMainColor() ));
        tvLine.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));

        if( data ==null )return;

        LayoutInflater inflater = LayoutInflater.from(this);
        for(SisSortModel item : data ){
            View v = inflater.inflate(R.layout.sis_goods_class,null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < llClass1.getChildCount(); i++) {
                        llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_footline).setBackgroundColor(Color.WHITE);
                        ((TextView)llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_name)).setTextColor(Color.BLACK);
                    }

                    tvAll.setTextColor(Color.BLACK);
                    tvLine.setBackgroundColor(Color.WHITE);

                    v.findViewById(R.id.sis_goods_class_footline).setBackgroundColor( SystemTools.obtainColor( app.obtainMainColor() ));
                    ((TextView)v.findViewById(R.id.sis_goods_class_name)).setTextColor( SystemTools.obtainColor(app.obtainMainColor()));
                    loadClass2((SisSortModel) v.getTag());

                    int scrollX = scrollView.getScrollX();
                    System.out.println("scrollX----->"+scrollX);
                    int helfwid = (getWindowManager().getDefaultDisplay().getWidth()- ivSelect.getWidth()) /2;
                    int left = v.getLeft();
                    int leftScreen = left-scrollX;
                    scrollView.smoothScrollBy((leftScreen - helfwid ), 0);

                    llClass2.setVisibility(View.GONE);
                    ivSelect.setTag("∨");
                    ivSelect.setImageResource(R.drawable.down);

                    SisSortModel model = (SisSortModel)v.getTag();
                    classid = model.getSisId();
                    isRefreshing=true;
                    pageno=0;
                    goodsList.clear();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            goodListView.onRefreshComplete();

                            if( goodListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ){
                                goodListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                                goodListView.setMode(PullToRefreshBase.Mode.BOTH);
                            }

                            goodListView.setRefreshing();
                        }
                    });
                }
            });
            TextView tvClass = (TextView)v.findViewById(R.id.sis_goods_class_name);
            tvClass.setText( item.getSisName() );
            v.setTag( item );
            llClass1.addView(v);
        }

    }

    protected void loadClass2(SisSortModel model ){

        List<SisSortModel> listfl= model.getList();
        int m = listfl.size() % 4;
        if( m >0){
            for( int i=0;i<4 - m;i++){
                SisSortModel empty =new SisSortModel();
                empty.setSisId(-1L);
                empty.setSisName("");
                listfl.add(empty);
            }
        }

        adapter=new ClassAdapter( listfl ,this);
        llClass2.setAdapter(adapter);
    }

    protected void loadGoods( long classid ,String key , int pageNo ){
        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            goodListView.onRefreshComplete();
            return;
        }

        String url = SisConstant.INTERFACE_searchGoodsList;

        AuthParamUtils authParamUtils = new AuthParamUtils(app , System.currentTimeMillis() , url , AddGoodsActivity.this );
        Map<String,String> map = new HashMap<>();
        map.put("userid" ,  app.readMemberId());
        map.put("categoryid" , String.valueOf(classid));
        map.put("key", key);
        map.put("pageno", String.valueOf(pageNo));
        //
        //map.put("customerid",app.readMerchantId());

        String urlstr = authParamUtils.getEncodeUrl( map );

        GsonRequest<AppSisGoodsModel> request = new GsonRequest<AppSisGoodsModel>(
                Request.Method.GET,
                urlstr ,
                AppSisGoodsModel.class,
                null,
                new MyGoodsListener(AddGoodsActivity.this),
                new MyGoodsErrorListener(AddGoodsActivity.this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }



    static class MyGoodsListener implements Response.Listener<AppSisGoodsModel>{
        WeakReference<AddGoodsActivity> ref;
        public MyGoodsListener(AddGoodsActivity act){
            ref = new WeakReference<>(act);
        }

        protected void removeRepeatData(List<SisGoodsModel> data){
            Iterator<SisGoodsModel> iterator = data.iterator();
            List<SisGoodsModel> delList= new ArrayList<>();
            while( iterator.hasNext() ){
                SisGoodsModel item = iterator.next();
                for(SisGoodsModel child : ref.get().goodsList){
                    if( item.getGoodsId().equals( child.getGoodsId() ) ) {
                        //iterator.remove();
                        delList.add(item);
                    }
                }
            }
            if( delList.size()>0 ){
                data.removeAll(delList);
            }
        }

        @Override
        public void onResponse(AppSisGoodsModel appSisGoodsModel) {
            if( ref.get() ==null) return;

            ref.get().goodListView.onRefreshComplete();
            if(ref.get().etSearchBar.isPopupShowing()){
                ref.get().etSearchBar.dismissDropDown();
            }

            if( !validateData( ref.get() , appSisGoodsModel) ){
                return;
            }
            if( appSisGoodsModel.getResultData() ==null ) return;

            if( ref.get().isRefreshing){
                ref.get().goodsList.clear();
                ref.get().pageno = appSisGoodsModel.getResultData().getRpageno();
                if( appSisGoodsModel.getResultData().getList()!=null ) {
                    ref.get().goodsList.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().goodsAdapter.notifyDataSetChanged();
            }else{
                if (appSisGoodsModel.getResultData().getList() == null ||
                        appSisGoodsModel.getResultData().getList().size() == 0) {
                    ToastUtils.showShortToast(ref.get(), "已经没有数据了。");
                    return;
                }

                ref.get().pageno = appSisGoodsModel.getResultData().getRpageno();
                if( appSisGoodsModel.getResultData().getList()!=null ) {

                    removeRepeatData( appSisGoodsModel.getResultData().getList() );

                    ref.get().goodsList.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().goodsAdapter.notifyDataSetChanged();
            }
        }
    }

    static class MyGoodsErrorListener implements Response.ErrorListener{
        WeakReference<AddGoodsActivity> ref;
        public MyGoodsErrorListener(AddGoodsActivity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get() ==null ) return;

            ref.get().goodListView.onRefreshComplete();
            if(ref.get().etSearchBar.isPopupShowing()){
                ref.get().etSearchBar.dismissDropDown();
            }

            ToastUtils.showLongToast( ref.get() , "请求异常" );
        }
    }

    static class MyListener implements Response.Listener<AppSisSortModel>{
        WeakReference<AddGoodsActivity> ref;

        public MyListener(AddGoodsActivity act){
            ref =new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisSortModel appSisSortModel) {
            if( ref.get()==null)return;

            if( ref.get().progress !=null){
                ref.get().progress.dismissView();
            }
            if( ref.get().progressDialog !=null){
                ref.get().progressDialog.dismiss();
            }
            ref.get().goodListView.onRefreshComplete();

            if( !validateData(ref.get(), appSisSortModel)){
                return;
            }

            if( ref.get().categorys==null ){
                ref.get().categorys=new ArrayList<>();
            }

            SisSortModel allitem = new SisSortModel();
            allitem.setSisId(0L);
            allitem.setSisName("全部");
            List<SisSortModel> child =new ArrayList<>();
            for(SisSortModel item : appSisSortModel.getResultData().getList()){
                SisSortModel cc = new SisSortModel();
                cc.setSisId( item.getSisId() );
                cc.setSisName(item.getSisName());
                cc.setIsFirstClass(true);
                child.add(cc);
            }
            allitem.setList(child);
            ref.get().tvAll.setTag(allitem);

            ref.get().categorys.clear();
            ref.get().categorys = appSisSortModel.getResultData().getList();
            ref.get().categorys.add( allitem );
            SisConstant.CATEGORY = ref.get().categorys;
            ref.get().loadClass1(ref.get().categorys);

            ref.get().classid = 0L;
            ref.get().pageno=0;
            ref.get().loadClass2(allitem);

            ref.get().handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ref.get().goodListView.setRefreshing(true);
                }
            }, 300);
        }
    }

    static class MyErrorListener implements Response.ErrorListener{
        WeakReference<AddGoodsActivity> ref;

        public MyErrorListener(AddGoodsActivity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get() ==null ) return;

            if( ref.get().progress !=null){
                ref.get().progress.dismissView();
            }
            if( ref.get().progressDialog!=null){
                ref.get().progressDialog.dismiss();
            }
            ToastUtils.showLongToast( ref.get() , "请求异常" );
        }
    }

    class ClassAdapter extends BaseAdapter{
        List<SisSortModel> list;
        LayoutInflater inflater;
        SisSortModel selectedClass;

        public ClassAdapter( List<SisSortModel> list,Context context){
            this.list=list;
            this.inflater =LayoutInflater.from(context);
            selectedClass = null;
        }
        @Override
        public int getCount() {
            return list==null? 0: list.size();
        }

        @Override
        public Object getItem(int position) {
            return list==null? null: list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        protected void selectClass1( Long id ){
            for (int i = 0; i < llClass1.getChildCount(); i++) {
                View v = llClass1.getChildAt(i);
                View v1 = llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_footline);
                TextView v2 =  ((TextView) llClass1.getChildAt(i).findViewById(R.id.sis_goods_class_name));
                if( v.getTag() ==null ) continue;
                SisSortModel model = (SisSortModel)v.getTag();
                if( model.getSisId().equals( id ) ){
                    v1.setBackgroundColor( SystemTools.obtainColor( app.obtainMainColor() ) );
                    v2.setTextColor( SystemTools.obtainColor( app.obtainMainColor() ) );
                    v.performClick();
                    continue;
                }
                v1.setBackgroundColor(Color.WHITE);
                v2.setTextColor(Color.BLACK);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SisSortModel model = list.get(position);
            if( convertView ==null ){
                convertView = inflater.inflate(R.layout.sis_goods_class2, null);
            }

            TextView tvName = ViewHolderUtil.get(convertView , R.id.sis_goods_class2_name);
            tvName.setTag( model );
            tvName.setText( model.getSisName() );

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SisSortModel data = (SisSortModel)v.getTag();
                    if( data.getSisId()<0)return;

                    if( data.isFirstClass() ){
                        selectClass1( data.getSisId() );
                        return;
                    }

                    selectedClass = data;
                    ((TextView)v).setTextColor(SystemTools.obtainColor( app.obtainMainColor() ));

                    adapter.notifyDataSetChanged();

                    llClass2.setVisibility(View.GONE);
                    ivSelect.setTag("∨");
                    ivSelect.setImageResource(R.drawable.down);

                    classid = data.getSisId();
                    pageno=0;
                    isRefreshing=true;
                    goodsList.clear();
                    if( goodListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ){
                        goodListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        goodListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    goodListView.setRefreshing(true);
                }
            });

            if( selectedClass !=null && selectedClass.getSisId().equals( list.get(position).getSisId() ) ){
                tvName.setTextColor( SystemTools.obtainColor( app.obtainMainColor() ));
            }else{
                tvName.setTextColor(Color.GRAY);
            }

            //tvAll.setTextColor(Color.BLACK);
            //tvLine.setBackgroundColor(Color.WHITE);

            return convertView;
        }
    }

    class GoodsAdapter extends BaseAdapter{
        List<SisGoodsModel> list;
        LayoutInflater inflater;
        Context context;

        public GoodsAdapter(Context context , List<SisGoodsModel> list){
            this.list=list;
            this.context=context;
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView==null){
                convertView = inflater.inflate(R.layout.sis_addgoods_item ,null);
            }

            TextView tvName = ViewHolderUtil.get(convertView, R.id.sis_addgoods_item_name);
            NetworkImageViewCircle ivPic = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_pic);
            TextView tvDes= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_des);
            tvDes.setText("库存:"+ list.get(position).getStock()  +" 返利:"+ list.get(position).getRebate() );
            TextView tvCommsion = ViewHolderUtil.get(convertView, R.id.sis_addgoods_item_commission);
            DecimalFormat format =new DecimalFormat("0.00");
            String temp = format.format(list.get(position).getPrice());
            tvCommsion.setText(temp);
            LinearLayout llOperate =ViewHolderUtil.get( convertView , R.id.sis_addgoods_item_ll_operate );
            llOperate.setBackgroundColor( SystemTools.obtainColor( app.obtainMainColor() ) );
            TextView tvOperate= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_operate);
            tvOperate.setTextColor( SystemTools.obtainColor( app.obtainMainColor() ) );
            llOperate.setTag( list.get(position) );
            final ProgressBar pgbar = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_pgbar);
            RelativeLayout rl = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_ll);
            TextView tvValidate = ViewHolderUtil.get(convertView,R.id.sis_addgoods_items_invalidate);
            RelativeLayout main= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_submain);
            main.setTag( list.get(position) );

            rl.setVisibility( list.get(position).isValidate() ? View.VISIBLE:View.GONE );
            tvValidate.setVisibility(list.get(position).isValidate() ? View.GONE : View.VISIBLE);

            tvName.setText(list.get(position).getGoodsName());

            BitmapLoader.create().displayUrl(context, ivPic, list.get(position).getImgUrl(), R.drawable.sis_pic, R.drawable.sis_pic);

            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SisGoodsModel model = (SisGoodsModel)v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra("goods", model);
                    intent.putExtra("state", model.isGoodSelected() ? 0 : 1);
                    ActivityUtils.getInstance().showActivityForResult((Activity) context, SisConstant.REFRESHGOODS_CODE, intent);
                }
            });

            llOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SisGoodsModel model = (SisGoodsModel) v.getTag();
                    v.setVisibility(View.GONE);
                    pgbar.setVisibility(View.VISIBLE);
                    online(model);
                }
            });

            llOperate.setVisibility(list.get(position).isGoodSelected() || list.get(position).isProcessing() ? View.GONE : View.VISIBLE);
            pgbar.setVisibility( list.get(position).isProcessing() ? View.VISIBLE : View.GONE );

            return convertView;
        }


        protected void online( SisGoodsModel model){
            model.setIsProcessing(true);

            String url = SisConstant.INTERFACE_operGoods;
            AuthParamUtils authParamUtils =new AuthParamUtils( app ,
                    System.currentTimeMillis() , url , context );
            Map para = new HashMap();
            para.put("userid", app.readMemberId());
            para.put("goodsid", model.getGoodsId());
            para.put("opertype", 1);

            Map maps = authParamUtils.obtainParams( para );

            GsonRequest<BaseModel> request = new GsonRequest<BaseModel>(
                    Request.Method.POST, url , BaseModel.class , null , maps ,
                    new MyOnSaleListener(model , this , (AddGoodsActivity) context),
                    new MyOnSaleErrorListener( (AddGoodsActivity)context , model )
            );
            VolleyUtil.getRequestQueue().add(request);
        }
    }

    protected static boolean validateData( Context context , BaseModel data){

        if(null == data){
            ToastUtils.showLongToast( context ,"请求失败");
            return false;
        }else if(data.getSystemResultCode()!=1){
            ToastUtils.showLongToast( context , data.getSystemResultDescription());
            return false;
        }else if( data.getResultCode() != 1){
            ToastUtils.showLongToast( context , data.getResultDescription());
            return false;
        }
        return true;
    }

    static class MyOnSaleListener implements Response.Listener<BaseModel>{
        SisGoodsModel model;
        BaseAdapter adapter;
        WeakReference<AddGoodsActivity> ref;

        public MyOnSaleListener(SisGoodsModel model , BaseAdapter adapter , AddGoodsActivity act){
            this.model = model;
            this.adapter=adapter;
            ref=new WeakReference<>(act);
        }
        @Override
        public void onResponse(BaseModel baseModel) {
            if( ref.get()==null )return;

            if(!validateData(ref.get(), baseModel)){
                return;
            }

            //ToastUtils.showLongToast( ref.get() , "onsale success");
            this.model.setGoodSelected(true);
            this.model.setIsProcessing(false);
            this.adapter.notifyDataSetChanged();
            ref.get().setResult( RESULT_OK );
        }
    }

    static class MyOnSaleErrorListener implements Response.ErrorListener{
        WeakReference<AddGoodsActivity> ref;
        SisGoodsModel model;
        public MyOnSaleErrorListener(AddGoodsActivity act , SisGoodsModel model ){
            ref = new WeakReference<AddGoodsActivity>(act);
            this.model = model;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null)return;
            ToastUtils.showLongToast(ref.get(), "请求异常");
            this.model.setIsProcessing(false);
            ref.get().adapter.notifyDataSetChanged();
        }
    }
}
