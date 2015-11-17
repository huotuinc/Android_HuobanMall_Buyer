package com.huotu.partnermall.ui.sis;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.ShareModel;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.ui.login.LoginActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.CircleImageView;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;
import com.huotu.partnermall.widgets.SharePopupWindow;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
    private TextView header_operate;
    private RelativeLayout search_bar;
    private Button search_cancel;
    private PopupWindow popWin;
    private SharePopupWindow sharePopWin;
    private RelativeLayout rlcd;
    private RelativeLayout rlshop;
    private TextView shopmanger;
    private CircleImageView logo;
    private ImageView ivBarcode;
    private Bitmap barCode;
    private Handler handler;
    private PullToRefreshListView listview;
    private int pageno1;
    private int pageno2;
    private boolean salestate = true;
    private Goodmanageadapter adapter1;
    private Goodmanageadapter adapter2;
    private List<SisGoodsModel> list1;
    private List<SisGoodsModel> list2;
    private boolean isRefresh=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_good_manage);

        findViewById();
        initView();
        setImmerseLayout();
    }

    /**
    * 方法描述：
    * 方法名称：
    * 参数：
    * 返回值：
    * 创建时间: 2015/11/9
    * 作者: 
    */
//    private void setStyle(){
//        StateListDrawable stateListDrawable =new StateListDrawable();
//        stateListDrawable.addState( new int[]{ android.R.attr.state_pressed} , new ColorDrawable( getResources().getColor( R.color.lightgray )) );
//        stateListDrawable.addState(new int[]{android.R.attr.state_hovered}, new ColorDrawable( SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor() )));
//        //正常状态
//        stateListDrawable.addState(new int[]{}, new ColorDrawable(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor())));
//
//        //addgood_btn.setBackground( stateListDrawable );
//        SystemTools.loadBackground( addgood_btn , stateListDrawable);
//    }

    @Override
    protected void findViewById() {
        rlcd = (RelativeLayout)findViewById(R.id.goodmange_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) this.getApplication()).obtainMainColor()));

        header_container = (RelativeLayout)findViewById(R.id.sis_header);
        header_container.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor()));
        rlshop = (RelativeLayout)findViewById(R.id.sis_shop);
        rlshop.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor()));

        goodmanage_header= (TextView) findViewById(R.id.header_title);
        header_back= (Button) findViewById(R.id.header_back);
        salestatus_sale= (TextView) findViewById(R.id.salestatus_sale);
        salestatus_remove= (TextView) findViewById(R.id.salestatus_remove);
        salestatus_saleline= (TextView) findViewById(R.id.salestatus_sale_line);
        salestatus_removeline= (TextView) findViewById(R.id.salestatus_remove_line);

        header_operate= (TextView) findViewById(R.id.header_operate);
        header_operate.setText("添加商品");
        header_operate.setTextColor(Color.WHITE);
        //header_operate.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        search_bar= (RelativeLayout) findViewById(R.id.search_bar);
        header_bar= (RelativeLayout) findViewById(R.id.header_bar);
        search_cancel= (Button) findViewById(R.id.search_cancel);

        shopmanger = (TextView)findViewById(R.id.sis_shopmanager);
        shopmanger.setBackgroundColor(Color.WHITE);
        shopmanger.setTextColor(SystemTools.obtainColor(((BaseApplication) GoodManageActivity.this.getApplication()).obtainMainColor()));

        logo = (CircleImageView)findViewById(R.id.sis_logo);
        ivBarcode = (ImageView)findViewById(R.id.sis_barcode);

        listview = (PullToRefreshListView)findViewById(R.id.goodmanage_listview);
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
    protected void initView() {
        handler = new Handler(getMainLooper());
        goodmanage_header.setText("小店商品");
        header_back.setOnClickListener(this);
        salestatus_sale.setOnClickListener(this);
        salestatus_remove.setOnClickListener(this);
        header_operate.setVisibility(View.VISIBLE);
        header_operate.setOnClickListener(this);
        search_cancel.setOnClickListener(this);
        ivBarcode.setOnClickListener(this);

        loadLogo();

        int barcodeW = Constants.SCREEN_WIDTH * 80/100;
        int barcodeH = Constants.SCREEN_HEIGHT * 80/100;
        if( barcodeW<barcodeH) {barcodeH = barcodeW;}
        showBarCode("http://m.cnblogs.com", ivBarcode, barcodeW, barcodeH);

        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageno1 = 0;
                pageno2 = 0;
                isRefresh=true;
                getGoods(salestate, 0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    isRefresh=false;
                    getGoods(salestate, salestate ? pageno1 : pageno2 );
            }
        });

        list1 = new ArrayList<>();
        adapter1 = new Goodmanageadapter(this, list1);
        listview.setAdapter(adapter1);

        list2 = new ArrayList<>();
        adapter2 = new Goodmanageadapter(this,list2);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( GoodManageActivity.this.isFinishing() )return;
                isRefresh=true;
                salestate = true;
                listview.setRefreshing(true);
            }
        },800);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.salestatus_sale:{
                salestate = true;
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            case R.id.salestatus_remove:{
                salestate =false;
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.white));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
            }
            break;
            case R.id.header_operate:{
                GoodManageActivity.this.startActivity(new Intent(GoodManageActivity.this,AddGoodsActivity.class));
            }
            break;
            case R.id.header_back:{
                finish();
            }
            break;
            case R.id.sis_barcode:{
                if( barCode ==null ) {
                    ToastUtils.showLongToast(this,"二维码没有生成");
                }else {
                    showBarCode();
                }
            }
            break;
//            case R.id.header_operate:{
//                header_bar.setVisibility(View.GONE);
//                search_bar.setVisibility(View.VISIBLE);
//            }
//            break;
//            case R.id.search_cancel:{
//                search_bar.setVisibility(View.GONE);
//                header_bar.setVisibility(View.VISIBLE);
//            }
//            break;
        }

    }

    /**
     * 方法描述：加载店中店 logo
     * 方法名称：
     * 参数：
     * 返回值：
     * 创建时间: 2015/11/9
     * 作者:jxd
     */
    private void loadLogo(){
        String logoUrl = "http://news.xinhuanet.com/photo/2015-10/29/128371793_14460865923871n.jpg";

        VolleyUtil.getImageLoader(this)
                .get(logoUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        if (imageContainer != null && imageContainer.getBitmap() != null) {
                            logo.setImageBitmap(imageContainer.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showShortToast( GoodManageActivity.this, "加载Logo失败");
                    }
                });
    }

    protected  void showBarCode(final String context , ImageView iv , final int width , final int height) {
        if( barCode!=null ) {
            iv.setImageBitmap(barCode);
            return;
        }

        final Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        final String filePath = getFileRoot(this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(){
            @Override
            public void run() {
                try {
                    barCode = encodeAsBitmap(context, BarcodeFormat.QR_CODE, width, height , logo , filePath );
                    if( barCode  == null){
                        Message msg= handler.obtainMessage(7000);
                        handler.sendMessage(msg);
                    }else {
                        Message msg = handler.obtainMessage(7001, barCode);
                        handler.sendMessage(msg);
                    }
                }catch ( WriterException e){
                    handler.sendEmptyMessage(7000);
                }
            }
        }.start();
    }

    protected void showBarCode() {
        if (popWin == null) {
            popWin = new PopupWindow();

            LayoutInflater inflater = LayoutInflater.from(this);
            View rootView = inflater.inflate(R.layout.sis_barcode,null);
            popWin.setContentView(rootView);
            popWin.setOnDismissListener(new PoponDismissListener( this));

            ImageView iv = (ImageView)rootView.findViewById(R.id.sis_barcode_pic);

            //iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWin.dismiss();
                }
            });

            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.alpha = 0.7f;
            this.getWindow().setAttributes(lp);

            int sw = Constants.SCREEN_WIDTH * 80 / 100;
            int sh = Constants.SCREEN_HEIGHT * 80 / 100;
            int h = sw;
            if( sw>sh) h =sh;
            //h = h +;

            popWin.setWidth(sw );
            popWin.setHeight( sh );

            popWin.setOutsideTouchable(true);
            popWin.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            popWin.setBackgroundDrawable(dw);

            //showBarCode("http://www.baidu.com", iv, barcodeW, barcodeH);
            iv.setImageBitmap( barCode );
        }
        if( popWin.isShowing()==false ) {
            WindowUtils.backgroundAlpha(this, 0.7f);
            popWin.showAtLocation(ivBarcode, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 文件存储根目录
     */
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 生成 二维码
     * @param contents
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @return
     * @throws WriterException
     */
    static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
                                 int desiredWidth, int desiredHeight, Bitmap logo , String filePath) throws WriterException {
        final int WHITE = 0xFFFFFFFF; //可以指定其他颜色，让二维码变成彩色效果
        final int BLACK = 0xFF000000;

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            //hints = new Hashtable<EncodeHintType, Object>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        //设置空白边距的宽度 
        //hints.put(EncodeHintType.MARGIN, 2); //default is 4 
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION,  ErrorCorrectionLevel.H);

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, desiredWidth,
                desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);


        if (logo != null) {
            bitmap = addLogo(bitmap, logo);
        }

        try {
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            if( bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath)))
            {
                bitmap = BitmapFactory.decodeFile( filePath );
                return  bitmap;
            }
        }catch (FileNotFoundException fex){
            return  null;
        }
        return null;
    }


    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return "UTF-8";
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }


    public class Goodmanageadapter extends BaseAdapter {
        private Context mContext;
        private List<SisGoodsModel> datas;
        private boolean[] tags;

        public Goodmanageadapter(Context mContext, List<SisGoodsModel> datas) {
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
            BitmapLoader.create().displayUrl(mContext, iv, datas.get(position).getImgUrl(), R.drawable.sis_pic, R.drawable.sis_pic);

            TextView txtName = ViewHolderUtil.get(convertView, R.id.goods_item_goodsName);
            txtName.setText( datas.get(position).getName() );
            TextView txtamount = ViewHolderUtil.get(convertView, R.id.goods_item_amount);
            txtamount.setText( datas.get(position).getStock() );
            TextView txtprofit = ViewHolderUtil.get(convertView, R.id.goods_item_profit);
            txtprofit.setText( String.valueOf(  datas.get(position).getProfit() ));
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

    protected void getGoods(boolean selected , int pageno){

        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            listview.onRefreshComplete();
            return;
        }

        String url = SisConstant.INTERFACE_searchSisGoodsList;
        url += "?userid=" + application.readMemberId();
        url +="&selected="+selected;
        url +="&key=";
        url +="&pageno="+ String.valueOf( pageno );
        AuthParamUtils authParamUtils = new AuthParamUtils(application , System.currentTimeMillis() , url ,this);
        String urlStr = authParamUtils.obtainUrlName();

        GsonRequest<AppSisGoodsModel> request = new GsonRequest<AppSisGoodsModel>(
                Request.Method.GET,
                urlStr,
                AppSisGoodsModel.class,
                null,
                new MyGoodsDataListener(this),
                new MyGoodsErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    static class MyGoodsErrorListener implements Response.ErrorListener{
        WeakReference<GoodManageActivity> ref;
        public MyGoodsErrorListener(GoodManageActivity act){
            ref = new WeakReference<GoodManageActivity>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null)return;
            ref.get().listview.onRefreshComplete();
            ToastUtils.showLongToast(ref.get(),"error");
        }
    }

    static class  MyGoodsDataListener implements Response.Listener<AppSisGoodsModel>{
        WeakReference<GoodManageActivity> ref;

        public MyGoodsDataListener(GoodManageActivity act){
            ref=new WeakReference<GoodManageActivity>(act);
        }

        @Override
        public void onResponse(AppSisGoodsModel appSisGoodsModel) {
            if( ref.get()==null) return;
            ref.get().listview.onRefreshComplete();

           if(!validateData(appSisGoodsModel)){
               return;
           }

            if(ref.get().salestate && ref.get().isRefresh ){
                ref.get().pageno1 = appSisGoodsModel.getResultData().getrPageNo();
                ref.get().list1.clear();
                if( appSisGoodsModel.getResultData().getList()!=null ) {
                    ref.get().list1.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().listview.setAdapter(ref.get().adapter1);
            }else if( ref.get().salestate && !ref.get().isRefresh ){
                ref.get().pageno1 = appSisGoodsModel.getResultData().getrPageNo();
                if( appSisGoodsModel.getResultData().getList() !=null ){
                    ref.get().list1.addAll( appSisGoodsModel.getResultData().getList());
                }
                ref.get().adapter1.notifyDataSetChanged();
            }else if( !ref.get().salestate && ref.get().isRefresh ){
                ref.get().pageno2 = appSisGoodsModel.getResultData().getrPageNo();
                ref.get().list2.clear();
                if( appSisGoodsModel.getResultData().getList()!=null ) {
                    ref.get().list2.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().listview.setAdapter(ref.get().adapter2);
            }else if( !ref.get().salestate && !ref.get().isRefresh ){
                ref.get().pageno2 = appSisGoodsModel.getResultData().getrPageNo();
                if( appSisGoodsModel.getResultData().getList() !=null ){
                    ref.get().list2.addAll( appSisGoodsModel.getResultData().getList());
                }
                ref.get().adapter2.notifyDataSetChanged();
            }

            String txt =  "出售中("+ String.valueOf( ref.get().list1.size() ) +")";
            ref.get().salestatus_sale.setText( txt );
            txt = "已下架("+ String.valueOf( ref.get().list2.size() ) + ")";
            ref.get().salestatus_remove.setText( txt );
        }

        /**
         * 方法描述：
         * 方法名称：
         * 参数：
         * 返回值：
         * 创建时间: 2015/10/15
         * 作者: Administrator
         */
        protected boolean validateData(BaseModel data){

            if(null == data){
                ToastUtils.showLongToast( ref.get() ,"请求失败");
                return false;
            }else if(data.getSystemResultCode()!=1){
                ToastUtils.showLongToast( ref.get() , data.getSystemResultDescription());
                return false;
            }else if( data.getResultCode() != 1){
                ToastUtils.showLongToast(ref.get() , data.getResultDescription());
                return false;
            }
            return true;
        }

    }
}
