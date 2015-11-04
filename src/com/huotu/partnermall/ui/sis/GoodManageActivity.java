package com.huotu.partnermall.ui.sis;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.ViewHolderUtil;
import java.util.ArrayList;
import java.util.List;

public class GoodManageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout header_bar;
    private TextView goodmanage_header;
    private Button header_back;
    private TextView salestatus_sale;
    private TextView salestatus_remove;
    private TextView salestatus_saleline;
    private TextView salestatus_removeline;
    private TextView addgood_btn;
    private TextView header_operate;
    private RelativeLayout search_bar;
    private Button search_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_manage);
        List list = new ArrayList();
        list.add("asdassd");
        list.add("sdsdsdsd");
        list.add("gggggg");
        list.add("ddadada");
        list.add("dassadsadsad");
        list.add("sdasdwff");
        goodmanageadapter goodmanageadapter = new goodmanageadapter(this, list);
        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.goodmanage_listview);
        listView.setAdapter(goodmanageadapter);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        goodmanage_header= (TextView) findViewById(R.id.header_title);
        header_back= (Button) findViewById(R.id.header_back);
        salestatus_sale= (TextView) findViewById(R.id.salestatus_sale);
        salestatus_remove= (TextView) findViewById(R.id.salestatus_remove);
        salestatus_saleline= (TextView) findViewById(R.id.salestatus_sale_line);
        salestatus_removeline= (TextView) findViewById(R.id.salestatus_remove_line);
        addgood_btn= (TextView) findViewById(R.id.addgood_btn);
        header_operate= (TextView) findViewById(R.id.header_operate);
        search_bar= (RelativeLayout) findViewById(R.id.search_bar);
        header_bar= (RelativeLayout) findViewById(R.id.header_bar);
        search_cancel= (Button) findViewById(R.id.search_cancel);
    }

    @Override
    protected void initView() {
        goodmanage_header.setText("小店商品");
        header_back.setOnClickListener(this);
        addgood_btn.setOnClickListener(this);
        salestatus_sale.setOnClickListener(this);
        salestatus_remove.setOnClickListener(this);
        header_operate.setVisibility(View.VISIBLE);
        header_operate.setOnClickListener(this);
        search_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.salestatus_sale:{
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            case R.id.salestatus_remove:{
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.white));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
            }
            break;
            case R.id.addgood_btn:{
                ToastUtils.showLongToast(this,"11111");
            }
            break;
            case R.id.header_back:{
                finish();
            }
            break;
            case R.id.header_operate:{
                header_bar.setVisibility(View.GONE);
                search_bar.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.search_cancel:{
                search_bar.setVisibility(View.GONE);
                header_bar.setVisibility(View.VISIBLE);
            }
            break;
        }

    }

    class goodmanageadapter extends BaseAdapter {
        private Context mContext;
        private List<String> datas;
        private boolean[] tags;

        public goodmanageadapter(Context mContext, List<String> datas) {
            this.mContext = mContext;
            this.datas = datas;
            tags = new boolean[datas.size()];

        }

        @Override
        public int getCount() {
            return datas.size();
        }


        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_goodmanage_item, null);
            }
            TextView txtName = ViewHolderUtil.get(convertView, R.id.goods_item_goodsName);
            TextView txtamount = ViewHolderUtil.get(convertView, R.id.goods_item_amount);
            TextView txtprofit = ViewHolderUtil.get(convertView, R.id.goods_item_profit);
            Button goods_item_btn = ViewHolderUtil.get(convertView, R.id.goods_item_btn);

            goods_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View prView = layoutInflater.inflate(R.layout.layout_good_manage_popwindow, null);//自定义的布局文件
                    PopupWindow mPw = new PopupWindow(prView, 200, 330);
                    ColorDrawable cd = new ColorDrawable(0x00000000);
                    //mPw.setContentView(prView);
                    mPw.setBackgroundDrawable(cd);
                    mPw.setFocusable(true); //设置PopupWindow可获得焦点
                    mPw.setTouchable(true);
                    mPw.setOutsideTouchable(true);
                    mPw.showAsDropDown(v,-200, 10);
                }
            });
            return convertView;
        }
    }
}
