package com.huotu.partnermall.ui.sis;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;
import com.huotu.partnermall.widgets.SharePopupWindow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


public class GoodManageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout header_container;
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
    private PopupWindow popWin;
    private SharePopupWindow sharePopWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_good_manage);
        List list = new ArrayList();
        list.add("aaaaa");
        list.add("bbbbb");
        list.add("ccccc");
        list.add("ddddd");
        list.add("eeeee");
        list.add("fffff");
        list.add("11111");
        list.add("22222");
        list.add("33333");
        list.add("44444");
        list.add("55555");
        list.add("66666");
        list.add("77777");
        list.add("88888");
        list.add("99999");

        goodmanageadapter goodmanageadapter = new goodmanageadapter(this, list);
        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.goodmanage_listview);
        listView.setAdapter(goodmanageadapter);
        findViewById();
        initView();

        setStyle();
    }

    /**
    * 方法描述：
    * 方法名称：
    * 参数：
    * 返回值：
    * 创建时间: 2015/11/9
    * 作者: 
    */
    private void setStyle(){
        StateListDrawable stateListDrawable =new StateListDrawable();
        stateListDrawable.addState( new int[]{ android.R.attr.state_pressed} , new ColorDrawable( getResources().getColor( R.color.lightgray )) );
        stateListDrawable.addState(new int[]{android.R.attr.state_hovered}, new ColorDrawable( SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor() )));
        //正常状态
        stateListDrawable.addState(new int[]{}, new ColorDrawable(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor())));

        //addgood_btn.setBackground( stateListDrawable );
        SystemTools.loadBackground( addgood_btn , stateListDrawable);
    }

    @Override
    protected void findViewById() {
        header_container = (RelativeLayout)findViewById(R.id.sis_header);
        header_container.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor()));

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
                //ToastUtils.showLongToast(this,"11111");
                GoodManageActivity.this.startActivity(new Intent(GoodManageActivity.this,AddGoodsActivity.class));
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
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_goodmanage_item, null);
            }
            NetworkImageViewCircle iv = (NetworkImageViewCircle)ViewHolderUtil.get(convertView,R.id.goods_item_picture);
            BitmapLoader.create().displayUrl( mContext , iv , "asdfasfsa" ,R.drawable.sis_pic , R.drawable.sis_pic);

            TextView txtName = ViewHolderUtil.get(convertView, R.id.goods_item_goodsName);
            txtName.setText( datas.get(position) );
            TextView txtamount = ViewHolderUtil.get(convertView, R.id.goods_item_amount);
            TextView txtprofit = ViewHolderUtil.get(convertView, R.id.goods_item_profit);
            final Button goods_item_btn = ViewHolderUtil.get(convertView, R.id.goods_item_btn);
            goods_item_btn.setTag( datas.get(position) );

            goods_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( popWin ==null ){
                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        View prView = layoutInflater.inflate(R.layout.layout_good_manage_popwindow, null);//自定义的布局文件
                        popWin  =new PopupWindow(prView,200,300);
                        ColorDrawable cd = new ColorDrawable(0x00000000);
                        popWin.setBackgroundDrawable(cd);
                        //popWin.setFocusable(true); //设置PopupWindow可获得焦点
                        popWin.setTouchable(true);
                        popWin.setOutsideTouchable(true);
                    }

                    popWin.getContentView()
                            .findViewById(R.id.sis_goods_menu_share)
                            .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWin.dismiss();
                            String goods =  (String)goods_item_btn.getTag();
                            ToastUtils.showLongToast(GoodManageActivity.this , goods);
                            share(goods, goods, "http://www.baidu.com", "http://www.baidu.com");
                        }
                    });

                    popWin.getContentView().findViewById(R.id.sis_goods_menu_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    popWin.getContentView().findViewById(R.id.sis_goods_menu_top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    popWin.getContentView().findViewById(R.id.sis_goods_menu_updown).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    popWin.showAsDropDown(v, -200, 10);
                }
            });
            return convertView;
        }

        protected void share(String title , String text , String imageUrl , String goodsUrl){
           if( sharePopWin ==null ){
               sharePopWin = new SharePopupWindow( GoodManageActivity.this , GoodManageActivity.this , GoodManageActivity.this.application );
           }
            ShareModel shareModel =new ShareModel();
            shareModel.setUrl(goodsUrl);
            shareModel.setImageUrl(imageUrl);
            shareModel.setText(text);
            shareModel.setTitle(title);

            sharePopWin.initShareParams(shareModel);
            sharePopWin.showShareWindow();
            sharePopWin.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            sharePopWin.setOnDismissListener(new PoponDismissListener(GoodManageActivity.this));
            sharePopWin.showAtLocation( GoodManageActivity.this.getWindow().getDecorView() , Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL ,0,0 );

        }

    }
}
