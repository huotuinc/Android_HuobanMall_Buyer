package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.HttpUtil;
import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.widgets.KJEditText;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGoodsActivity extends Activity implements View.OnClickListener{
    LinearLayout llClass1;
    GridView llClass2;
    ClassAdapter adapter;
    GoodsAdapter goodsAdapter;
    PullToRefreshListView goodListView;
    List<GoodsModel> goodsList;
    KJEditText etSearchBar;
    TextView tvCancel;
    ImageView ivQuery;
    final String PRE_SEARCHKEY_FILE="searchkey_info";
    final String PRE_SEARCHKEY_NAME="searchkey";
    String searchKeys = "";
    List<String> searchKeysList=null;
    ArrayAdapter<String> keysAdapter=null;
    RelativeLayout rlSearchBar;
    RelativeLayout rlHeader;
    RelativeLayout rlcd;
    TextView tvSelect;
    HorizontalScrollView scrollView;
    BaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_add_goods);

        app = (BaseApplication)this.getApplication();
        rlcd = (RelativeLayout)findViewById(R.id.sis_addgoods_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()));
        rlSearchBar =(RelativeLayout)findViewById(R.id.sis_addgoods_searchBar);
        rlHeader = (RelativeLayout)findViewById(R.id.sis_addgoods_header);
        rlHeader.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()));
        scrollView =(HorizontalScrollView)findViewById(R.id.sis_addgoods_scrollView);
        tvSelect =(TextView)findViewById(R.id.sis_addgoods_select);
        tvSelect.setOnClickListener(this);
        tvCancel = (TextView)findViewById(R.id.sis_addgoods_cancel);
        tvCancel.setOnClickListener(this);
        ivQuery = (ImageView)findViewById(R.id.sis_addgoods_query);
        ivQuery.setOnClickListener(this);
        etSearchBar = (KJEditText)findViewById(R.id.sis_addgoods_key);
        etSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(etSearchBar.getText())) {
                        etSearchBar.requestFocus();
                        etSearchBar.setError("不能为空");
                    } else {
                        String key = etSearchBar.getText().toString();
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
                    }
                    return true;
                }

                return false;
            }
        });

        loadSearchKeys();

        goodsList=new ArrayList<GoodsModel>();
        llClass1=(LinearLayout)findViewById(R.id.sis_addgoods_class1);
        llClass2=(GridView)findViewById(R.id.sis_addgoods_class2);
        goodListView= (PullToRefreshListView)findViewById(R.id.sis_addgoods_listview);
        goodListView.setMode(PullToRefreshBase.Mode.BOTH);
        goodListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

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
            rlHeader.setVisibility(View.VISIBLE);
            rlSearchBar.setVisibility(View.GONE);
        }else if( v.getId() == R.id.sis_addgoods_query){
            rlHeader.setVisibility(View.GONE);
            rlSearchBar.setVisibility(View.VISIBLE);
            etSearchBar.requestFocus();
        }else if( v.getId()== R.id.sis_addgoods_select){
            if( ((TextView)v).getText().equals("∨")){
                ((TextView) v).setText("∧");
                llClass2.setVisibility(View.VISIBLE);
            }else{
                ((TextView) v).setText("∨");
                llClass2.setVisibility(View.GONE);
            }
        }
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
        String url = SisConstant.INTERFACE_getCategoryList;
        String userid = app.plat.getDb().getUserId();
        url += "?userId="+ userid;
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

    protected void loadClass1( List<SisSortModel> data ){
        llClass1.removeAllViews();
        if( data ==null )return;
        //List<String> classes = new ArrayList<String>();
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

                    v.findViewById(R.id.sis_goods_class_footline).setBackgroundColor(Color.BLUE);
                    ((TextView)v.findViewById(R.id.sis_goods_class_name)).setTextColor(Color.BLUE);
                    loadClass2( (SisSortModel)v.getTag());

                    int scrollX = scrollView.getScrollX();
                    System.out.println("scrollX----->"+scrollX);
                    int helfwid = (getWindowManager().getDefaultDisplay().getWidth()- tvSelect.getWidth()) /2;
                    int left = v.getLeft();
                    int leftScreen = left-scrollX;
                    scrollView.smoothScrollBy((leftScreen - helfwid ), 0);

                }
            });
            TextView tvClass = (TextView)v.findViewById(R.id.sis_goods_class_name);
            tvClass.setText( item.getName() );
            v.setTag( item );
            llClass1.addView(v);
        }
        if( data!=null && data.size()>0 ){
            llClass1.getChildAt(0).findViewById(R.id.sis_goods_class_footline).setBackgroundColor(Color.BLUE);
            ((TextView)llClass1.getChildAt(0).findViewById(R.id.sis_goods_class_name)).setTextColor(Color.BLUE);
            loadClass2( (SisSortModel) llClass1.getChildAt(0).getTag() );
        }
    }

    protected void loadClass2(SisSortModel model ){
        //List<String> classes=new ArrayList<String>();
        //for(int i=0;i<8;i++){
        //    classes.add(classId + "subclass" + i);
        //}
        adapter=new ClassAdapter( model.getList() ,this);
        llClass2.setAdapter(adapter);

        //loadGoods( classId , classes.get(0) , 0);
    }

    protected void loadGoods( long classid ,String key , int pageNo ){
//        goodsList.clear();
//        for(int i=0;i<40;i++){
//            GoodsModel item=new GoodsModel();
//            item.setName( classOne +" "+ classTwo + " 商品" + i);
//            item.setUrl("http://www.baidu.com");
//            item.setValidate(true);
//            if( i%4==0)item.setValidate(false);
//            goodsList.add(item);
//        }
//        goodsAdapter =new GoodsAdapter(AddGoodsActivity.this,goodsList);
//        goodListView.getRefreshableView().setAdapter(goodsAdapter);

        String url = SisConstant.INTERFACE_searchGoodsList;
        url ="?userId="+ app.readMemberId();
        url +="&categoryId="+ classid;
        url +="&key="+ key;
        url +="&pageNo=" + pageNo;
        AuthParamUtils authParamUtils = new AuthParamUtils(app , System.currentTimeMillis() , url , AddGoodsActivity.this );

        String urlstr = authParamUtils.obtainUrlName();

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
        WeakReference<Activity> ref;
        public MyGoodsListener(Activity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisGoodsModel appSisGoodsModel) {
            if( ref.get() ==null) return;


        }
    }

    static class MyGoodsErrorListener implements Response.ErrorListener{
        WeakReference<Activity> ref;
        public MyGoodsErrorListener(Activity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get() ==null ) return;
            ToastUtils.showLongToast( ref.get() , "error" );
        }
    }

    static class MyListener implements Response.Listener<AppSisSortModel>{
        WeakReference<AddGoodsActivity> ref;

        public MyListener(AddGoodsActivity act){
            ref =new WeakReference<AddGoodsActivity>(act);
        }


        @Override
        public void onResponse(AppSisSortModel appSisSortModel) {
            if( ref.get()==null)return;

        }
    }

    static class MyErrorListener implements Response.ErrorListener{
        WeakReference<Activity> ref;

        public MyErrorListener(Activity act){
            ref = new WeakReference<Activity>(act);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get() ==null ) return;
            ToastUtils.showLongToast( ref.get() , "error" );
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
            SisSortModel model = list.get(position);
            HolderView holder;
            if( convertView ==null ) {
                holder = new HolderView();
                convertView = inflater.inflate(R.layout.sis_goods_class2, null);

                TextView tvName = (TextView) convertView.findViewById(R.id.sis_goods_class2_name);


                holder.tvName=tvName;
                convertView.setTag(holder);
            }else{
                holder=(HolderView)convertView.getTag();
            }
            holder.tvName.setText( model.getName());
            holder.tvName.setTag(model );

            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SisSortModel data = (SisSortModel)v.getTag();
                    selectedClass = data;
                    ((TextView)v).setTextColor(Color.BLUE);
                    //loadGoods( data.getId() );
                }
            });

            if( selectedClass.equals( list.get(position) ) ){
                holder.tvName.setTextColor(Color.BLUE);
            }else{
                holder.tvName.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        class HolderView{

            TextView tvName;
        }
    }

    class GoodsAdapter extends BaseAdapter{
        List<GoodsModel> list;
        LayoutInflater inflater;
        Context context;

        public GoodsAdapter(Context context , List<GoodsModel> list){
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
                convertView = inflater.inflate(R.layout.sis_addgoods_item,null);
            }

            TextView tvName = ViewHolderUtil.get(convertView, R.id.sis_addgoods_item_name);
            NetworkImageViewCircle ivPic = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_pic);
            TextView tvDes= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_des);
            TextView tvCommsion = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_commission);
            TextView tvOperate= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_operate);
            tvOperate.setTag( list.get(position) );
            RelativeLayout rl = ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_ll);
            TextView tvValidate = ViewHolderUtil.get(convertView,R.id.sis_addgoods_items_invalidate);
            LinearLayout main= ViewHolderUtil.get(convertView,R.id.sis_addgoods_item_main);

            rl.setVisibility( list.get(position).isValidate() ? View.VISIBLE:View.GONE );
            tvValidate.setVisibility( list.get(position).isValidate()? View.GONE : View.VISIBLE );

            tvName.setText(list.get(position).getName());

            BitmapLoader.create().displayUrl(context, ivPic, list.get(position).getUrl(), R.drawable.ic_launcher, R.drawable.ic_launcher);

            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showLongToast(context, "goods");
                    Intent intent =new Intent();
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra("url","http://www.sina.com.cn");
                    context.startActivity(intent);
                }
            });

            tvOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showLongToast(context, "online");
                    GoodsModel model =(GoodsModel) v.getTag();

                }
            });

            return convertView;
        }

        protected void online( SisGoodsModel model){
            String url = SisConstant.INTERFACE_operGoods;
            AuthParamUtils authParamUtils =new AuthParamUtils( app ,
                    System.currentTimeMillis() , url , context );
            Map para = new HashMap();
            para.put("userId", app.readMemberId());
            para.put("goodsId", model.getId());
            para.put("operation", 1);

            Map maps = authParamUtils.obtainParams( para );

//            GsonRequest<BaseModel> request = new GsonRequest<BaseModel>(
//                    Request.Method.POST, url , null , maps , );
//            VolleyUtil.getRequestQueue().add(request);
        }


    }

    class GoodsModel{
        private String name;
        private boolean validate;
        private String url;
        private float commision;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isValidate() {
            return validate;
        }

        public void setValidate(boolean validate) {
            this.validate = validate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public float getCommision() {
            return commision;
        }

        public void setCommision(float commision) {
            this.commision = commision;
        }
    }

}
