package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售明细 界面
 */
public class SalesDetailActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener,View.OnClickListener {
    RelativeLayout rlCd;

    RelativeLayout header_bar;

    RelativeLayout search_bar;

    TextView search_cancel;

    EditText search_text;

    TextView header_back;

    TextView header_operate;

    PullToRefreshListView _salesDetail_ListView;

    SalesDetailAdapter _salesDetailAdapter;

    List<SalesListModel> _saledetailList = null;

    OperateTypeEnum _operateType = OperateTypeEnum.REFRESH;

    Handler _handler=new Handler();
    View emptyView=null;
    boolean isSetEmptyView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_sales_detail);

        rlCd =(RelativeLayout)findViewById(R.id.sis_saledetail_cd);
        rlCd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) SalesDetailActivity.this.getApplication()).obtainMainColor()));

        header_bar=(RelativeLayout)findViewById(R.id.sis_saledetail_header);
        header_bar.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) SalesDetailActivity.this.getApplication()).obtainMainColor()));

        search_bar=(RelativeLayout)findViewById(R.id.sis_saledetail_searchbar);
        header_back=(TextView)findViewById(R.id.sis_saledetail_back);
        header_back.setOnClickListener(this);
        header_operate=(TextView)findViewById(R.id.sis_saledetail_operate);
        header_operate.setOnClickListener(this);
        search_cancel=(TextView)findViewById(R.id.sis_saledetail_searchcancel);
        search_cancel.setOnClickListener(this);
        search_text=(EditText)findViewById(R.id.sis_saledetail_searchtext);
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(search_text.getText())) {
                        search_text.requestFocus();
                        search_text.setError("不能为空");
                    } else {
                        firstSaleGoodData();
                    }
                    return true;
                }
                return false;
            }
        });

        _saledetailList = new ArrayList<>();
        _salesDetailAdapter= new SalesDetailAdapter(this, _saledetailList );
        _salesDetail_ListView = (PullToRefreshListView)findViewById(R.id.sis_saledetail_listview);
        _salesDetail_ListView.getRefreshableView().setAdapter(_salesDetailAdapter);
        _salesDetail_ListView.setPullToRefreshOverScrollEnabled(true);
        _salesDetail_ListView.setMode(PullToRefreshBase.Mode.BOTH);

        emptyView= new View(this);
        emptyView.setBackgroundResource(R.drawable.sis_tpzw);

        _salesDetail_ListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                    _operateType = OperateTypeEnum.REFRESH;
                    getData_MX(_operateType);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                _operateType = OperateTypeEnum.LOADMORE;
                getData_MX(_operateType);
            }
        });

        firstSaleGoodData();

        setImmerseLayout();
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
                rlCd.setPadding(0,statusBarHeight,0,0);
                rlCd.getLayoutParams().height+=statusBarHeight;
                rlCd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()) );
            }
        }
    }

    protected void getData_MX( OperateTypeEnum type  ){
//        String url = Constant.SALESLIST_INTERFACE;
//        Map<String,String> paras = new HashMap<>();
//        HttpParaUtils httpParaUtils = new HttpParaUtils();
//        if( type == OperateTypeEnum.REFRESH ) {
//        }else {
//
//            if( _saledetailList !=null && _saledetailList.size() >0 ) {
//                Date lastDate = _saledetailList.get(_saledetailList.size() - 1).getTime();
//                paras.put("lastDate", String.valueOf(lastDate.getTime()));
//            }
//        }
//
//        String key = search_text.getText().toString().trim();
//        if( key!=null && key.length()>0){
//            paras.put("key",key);
//        }
//
//        url= httpParaUtils.getHttpGetUrl(url , paras);
//        GsonRequest<MJSaleListModel> request = new GsonRequest<>(
//                Request.Method.GET,
//                url ,
//                MJSaleListModel.class,
//                null,
//                listener_MX,
//                this
//        );
//
//       VolleyUtil.getRequestQueue().add(request);
    }

    Response.Listener<MJSaleListModel> listener_MX =new Response.Listener<MJSaleListModel>() {
        @Override
        public void onResponse(MJSaleListModel mjSaleListModel) {
           if( SalesDetailActivity.this.isFinishing() ) return;

            _salesDetail_ListView.onRefreshComplete();

            if(!isSetEmptyView){
                _salesDetail_ListView.setEmptyView(emptyView);
                isSetEmptyView=true;
            }

            if(_operateType == OperateTypeEnum.REFRESH){
                _saledetailList.clear();
                if( mjSaleListModel.getResultData() !=null && mjSaleListModel.getResultData().getList() !=null && mjSaleListModel.getResultData().getList().size()>0) {
                    _saledetailList.addAll(mjSaleListModel.getResultData().getList());
                }
                _salesDetail_ListView.setAdapter( _salesDetailAdapter );
            }else{
                if( mjSaleListModel.getResultData() !=null && mjSaleListModel.getResultData().getList() !=null && mjSaleListModel.getResultData().getList().size()>0) {
                    _saledetailList.addAll(mjSaleListModel.getResultData().getList());
                }
                _salesDetailAdapter.notifyDataSetChanged();
            }
        }
    };


    protected void onDestroy() {
        super.onDestroy();
    }

    private void firstSaleGoodData() {
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SalesDetailActivity.this.isFinishing()) return;

                _operateType = OperateTypeEnum.REFRESH;
                _salesDetail_ListView.setRefreshing(true);
            }
        }, 800);
    }

    protected void openSearchBar(){
        search_bar.setVisibility(View.VISIBLE);
        header_bar.setVisibility(View.GONE);
    }

    protected void closeSearchBar(){
        search_text.setText("");
        search_bar.setVisibility(View.GONE);
        header_bar.setVisibility(View.VISIBLE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back: {
                finish();
                break;
            }
            case R.id.header_operate:{
                openSearchBar();
                break;
            }
            case R.id.search_cancel:{
                closeSearchBar();
                break;
            }
            default:
                break;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    public class MJSaleListModel extends BaseModel {
        public InnerClass getResultData() {
            return resultData;
        }

        public void setResultData(InnerClass resultData) {
            this.resultData = resultData;
        }

        private InnerClass resultData;


        public class InnerClass {
            public List<SalesListModel> getList() {
                return list;
            }

            public void setList(List<SalesListModel> list) {
                this.list = list;
            }

            List<SalesListModel> list;
        }
    }


    public class SalesListModel {
        private String orderNo;
        private String title;
        private String receiver;
        private String moblie;
        private Double money;
        private Date time;

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        private String pictureUrl;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getMoblie() {
            return moblie;
        }

        public void setMoblie(String moblie) {
            this.moblie = moblie;
        }

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    public class SalesDetailAdapter extends BaseAdapter {
        private List<SalesListModel> _list;
        private Context _context;

        public SalesDetailAdapter(Context context, List<SalesListModel> list) {
            _list = list;
            _context = context;
        }

        @Override
        public int getCount() {
            return _list.size();
        }

        @Override
        public Object getItem(int position) {
            return _list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(_context,
                        R.layout.sis_layout_salesdetail_item, null);
                holder.tvOrderNo = (TextView) convertView.findViewById(R.id.sis_saledetail_orderNo);
                holder.tvTime = (TextView) convertView.findViewById(R.id.sis_saledetail_salestime);
                holder.tvMoney = (TextView) convertView.findViewById(R.id.sis_saledetail_money);
                holder.ivPicture =(NetworkImageViewCircle) convertView.findViewById(R.id.sis_saledetail_imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvMoney.setText(String.valueOf(_list.get(position).getMoney()));
            holder.tvOrderNo.setText(String.valueOf(_list.get(position).getOrderNo()));
            String dateStr;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateStr =  format.format(_list.get(position).getTime());
            holder.tvTime.setText( dateStr );

            BitmapLoader.create().displayUrl(_context, holder.ivPicture , _list.get(position).getPictureUrl(),R.drawable.sis_ddgl_d ,R.drawable.sis_ddgl_d);

            return convertView;

        }

        class ViewHolder
        {
            TextView tvMoney;

            TextView tvOrderNo;

            TextView tvTime;

            NetworkImageViewCircle ivPicture;

        }
    }

    public enum OperateTypeEnum {
        REFRESH("刷新",1),
        LOADMORE("加载更多",2);

        private OperateTypeEnum(String name , int index){
            this.name=name;
            this.index=index;
        }

        // 普通方法
        public static String getName(int index) {
            for (OperateTypeEnum c : OperateTypeEnum.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private String name;
        private int index;
    }

}