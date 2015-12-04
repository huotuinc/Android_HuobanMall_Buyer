package com.huotu.partnermall.ui.sis;


import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.CircleImageView;
import com.huotu.partnermall.widgets.MsgPopWindow;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;
import com.huotu.partnermall.widgets.ProgressPopupWindow;
import com.huotu.partnermall.widgets.SharePopupWindow;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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
    private PopupWindow barcodePopWin;
    private SharePopupWindow sharePopWin;
    private RelativeLayout rlcd;
    private RelativeLayout rlshop;
    private TextView shopmanger;
    private TextView shopName;
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
    private ProgressPopupWindow progressPopupWindow;
    private MsgPopWindow msgPopWindow;
    private ProgressDialog progressdlg;
    private BaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_good_manage);

        findViewById();

        getinitdata();

        initView();
        setImmerseLayout();


    }

    protected void findViewById() {
        app = (BaseApplication)this.getApplication();
        rlcd = (RelativeLayout)findViewById(R.id.goodmange_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));

        header_container = (RelativeLayout)findViewById(R.id.sis_header);
        header_container.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));

        rlshop = (RelativeLayout)findViewById(R.id.sis_shop);
        rlshop.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));

        goodmanage_header= (TextView) findViewById(R.id.header_title);
        header_back= (Button) findViewById(R.id.header_back);
        salestatus_sale= (TextView) findViewById(R.id.salestatus_sale);
        salestatus_remove= (TextView) findViewById(R.id.salestatus_remove);
        salestatus_saleline= (TextView) findViewById(R.id.salestatus_sale_line);
        salestatus_removeline= (TextView) findViewById(R.id.salestatus_remove_line);

        header_operate= (TextView) findViewById(R.id.header_operate);
        header_operate.setText("添加商品");
        header_operate.setTextColor(Color.WHITE);
        search_bar= (RelativeLayout) findViewById(R.id.search_bar);
        header_bar= (RelativeLayout) findViewById(R.id.header_bar);
        search_cancel= (Button) findViewById(R.id.search_cancel);

        shopmanger = (TextView)findViewById(R.id.sis_shopmanager);
        shopmanger.setBackgroundColor(Color.WHITE);
        shopmanger.setTextColor(SystemTools.obtainColor(app.obtainMainColor()));

        logo = (CircleImageView)findViewById(R.id.sis_logo);
        ivBarcode = (ImageView)findViewById(R.id.sis_barcode);
        listview = (PullToRefreshListView)findViewById(R.id.goodmanage_listview);
        shopName = (TextView)findViewById(R.id.sis_shopname);
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
        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if( msg.what == 7001 ){
                    //ivBarcode.setImageBitmap( barCode );
                }else if( msg.what == 7000){
                    ToastUtils.showShortToast(app,"生成二维码失败");
                }
            }
        };
        goodmanage_header.setText("");
        header_back.setOnClickListener(this);
        salestatus_sale.setOnClickListener(this);
        salestatus_remove.setOnClickListener(this);
        header_operate.setVisibility(View.VISIBLE);
        header_operate.setOnClickListener(this);
        search_cancel.setOnClickListener(this);
        ivBarcode.setOnClickListener(this);
        shopmanger.setOnClickListener(this);
        logo.setOnClickListener(this);
        shopName.setOnClickListener(this);

        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageno1 = 0;
                pageno2 = 0;
                isRefresh = true;
                getGoods(salestate, 0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                getGoods(salestate, salestate ? pageno1 : pageno2);
            }
        });

        View emptView = new View(this);
        emptView.setBackgroundResource(R.drawable.sis_tpzw);
        listview.setEmptyView(emptView);
        listview.setScrollEmptyView(false);

        list1 = new ArrayList<>();
        adapter1 = new Goodmanageadapter(this, list1 , 0 );
        listview.setAdapter(adapter1);

        list2 = new ArrayList<>();
        adapter2 = new Goodmanageadapter(this,list2 , 1 );

        getShopInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == SisConstant.REFRESHGOODS_CODE ){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (GoodManageActivity.this.isFinishing()) return;
                    isRefresh = true;
                    salestate = true;
                    if(listview.getMode() == PullToRefreshBase.Mode.PULL_FROM_END){
                        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        listview.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    listview.setRefreshing(true);
                }
            }, 500);
        }

        if( resultCode ==RESULT_OK && requestCode == SisConstant.REFRESHSHOPINFO_CODE ){
            //刷新店铺信息
            if( SisConstant.SHOPINFO==null)return;
            if( barCode!=null) barCode.recycle();
            barCode=null;
            shopName.setText(SisConstant.SHOPINFO.getTitle());
            loadLogo(SisConstant.SHOPINFO.getImgUrl());
            //showBarCode( SisConstant.SHOPINFO.getIndexUrl() , ivBarcode );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.salestatus_sale:{
                salestate = true;
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.white));
                isRefresh=true;
                pageno1 = 0;
                list1.clear();
                //adapter1.notifyDataSetChanged();
                list2.clear();
                adapter2.notifyDataSetChanged();

                listview.setAdapter(adapter1);

                if( listview.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ) {
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    listview.setMode(PullToRefreshBase.Mode.BOTH);
                }
                listview.setRefreshing(true);

            }
            break;
            case R.id.salestatus_remove:{
                salestate =false;
                salestatus_saleline.setBackgroundColor(getResources().getColor(R.color.white));
                salestatus_removeline.setBackgroundColor(getResources().getColor(R.color.home_title_bg));
                isRefresh=true;
                pageno2 = 0;
                list1.clear();
                adapter1.notifyDataSetChanged();
                list2.clear();
                //adapter2.notifyDataSetChanged();
                listview.setAdapter(adapter2);

                if( listview.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END ) {
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    listview.setMode(PullToRefreshBase.Mode.BOTH);
                }

                listview.setRefreshing(true);
            }
            break;
            case R.id.header_operate:{
                listview.onRefreshComplete();
                GoodManageActivity.this.startActivityForResult(new Intent(GoodManageActivity.this, AddGoodsActivity.class), SisConstant.REFRESHGOODS_CODE );
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
            case R.id.sis_logo:
            case R.id.sis_shopname:
            case R.id.sis_shopmanager:{
                listview.onRefreshComplete();
                GoodManageActivity.this.startActivityForResult(new Intent(GoodManageActivity.this, InfoActivity.class), SisConstant.REFRESHSHOPINFO_CODE);
            }
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

    /**
     * 方法描述：加载店中店 logo
     * 方法名称：
     * 参数：
     * 返回值：
     * 创建时间: 2015/11/9
     * 作者:jxd
     */
    private void loadLogo( String logoUrl){
        if(TextUtils.isEmpty( logoUrl )) return;
        VolleyUtil.getImageLoader(this)
                .get(logoUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        if (imageContainer != null && imageContainer.getBitmap() != null) {
                            logo.setDrawingCacheEnabled(true);
                            logo.setImageBitmap(imageContainer.getBitmap());
                            showBarCode(SisConstant.SHOPINFO.getIndexUrl(), ivBarcode);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showShortToast(GoodManageActivity.this, "加载Logo失败");
                    }
                });
    }

    protected  void showBarCode(final String context , ImageView iv ) {
        if( context==null ){
            ToastUtils.showShortToast(GoodManageActivity.this, "内容空,无法生成二维码");
            return;
        }
        if( barCode!=null ) {
            iv.setImageBitmap(barCode);
            return;
        }

        int barcodeW = Constants.SCREEN_WIDTH * 80/100;
        int barcodeH = Constants.SCREEN_HEIGHT * 80/100;
        if( barcodeW<barcodeH) {barcodeH = barcodeW;}
        final int bw = barcodeW;
        final int bh=barcodeH;

        final Bitmap logobm = logo.getDrawingCache(); //BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //logo.setDrawingCacheEnabled(false);
        final String filePath = getFileRoot(this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(){
            @Override
            public void run() {
                try {
                    barCode = encodeAsBitmap(context, BarcodeFormat.QR_CODE, bw, bh , logobm , filePath );
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
        if (barcodePopWin == null) {
            barcodePopWin = new PopupWindow();
            LayoutInflater inflater = LayoutInflater.from(this);
            View rootView = inflater.inflate(R.layout.sis_barcode,null);
            barcodePopWin.setContentView(rootView);
            barcodePopWin.setOnDismissListener(new PoponDismissListener( this));

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barcodePopWin.dismiss();
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

            barcodePopWin.setWidth(sw );
            barcodePopWin.setHeight( sh );

            barcodePopWin.setOutsideTouchable(true);
            barcodePopWin.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            barcodePopWin.setBackgroundDrawable(dw);

            barcodePopWin.getContentView().findViewById(R.id.sis_barcode_share_wx).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareModel model =new ShareModel();
                    model.setTitle( SisConstant.SHOPINFO.getShareTitle() );
                    model.setText(SisConstant.SHOPINFO.getShareDescription());
                    model.setImageUrl(SisConstant.SHOPINFO.getImgUrl());
                    model.setUrl( SisConstant.SHOPINFO.getIndexUrl() );
                    wx(GoodManageActivity.this, model , ShareSDK.getPlatform( Wechat.NAME ));
                }
            });
            barcodePopWin.getContentView().findViewById(R.id.sis_barcode_share_wxpyq).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareModel model =new ShareModel();
                    model.setTitle( SisConstant.SHOPINFO.getShareTitle() );
                    model.setText(SisConstant.SHOPINFO.getShareDescription());
                    model.setImageUrl(SisConstant.SHOPINFO.getImgUrl());
                    model.setUrl( SisConstant.SHOPINFO.getIndexUrl() );
                    wx(GoodManageActivity.this , model , ShareSDK.getPlatform(WechatMoments.NAME) );
                }
            });
            barcodePopWin.getContentView().findViewById(R.id.sis_barcode_share_qqzone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareModel model =new ShareModel();
                    model.setTitle( SisConstant.SHOPINFO.getShareTitle() );
                    model.setText(SisConstant.SHOPINFO.getShareDescription());
                    model.setImageUrl(SisConstant.SHOPINFO.getImgUrl());
                    model.setUrl( SisConstant.SHOPINFO.getIndexUrl() );
                    qqzone(GoodManageActivity.this, model);
                }
            });
        }

        if( barcodePopWin.isShowing()==false ) {

            ImageView iv =(ImageView) barcodePopWin.getContentView().findViewById(R.id.sis_barcode_pic);
            iv.setImageBitmap(barCode);

            WindowUtils.backgroundAlpha(this, 0.7f);
            barcodePopWin.showAtLocation(ivBarcode, Gravity.CENTER, 0, 0);
        }
    }

    protected void qqzone( final Context context , ShareModel model ){
        Platform.ShareParams sp = new Platform.ShareParams ();
        sp.setTitle ( model.getTitle() );
        sp.setTitleUrl(model.getUrl()); // 标题的超链接
        sp.setText(model.getText());
        sp.setImageUrl ( model.getImageUrl ( ) );
        sp.setSiteUrl( model.getUrl() );
        Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.showShortToast(context , "QQ空间分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShortToast(context , "QQ空间分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShortToast(context , "取消QQ空间分享");
            }
        }); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    protected void wx(final  Context context , ShareModel model , Platform platform){
        Platform.ShareParams sp = new Platform.ShareParams ();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(model.getTitle());
        sp.setText(model.getText());
        sp.setUrl(model.getUrl());
        sp.setImageUrl(model.getImageUrl());
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                String msg = "";
                if( platform.getName().equals(Wechat.NAME) ) {
                    ToastUtils.showShortToast(context, "微信分享成功");
                }else if( platform.getName().equals(WechatMoments.NAME)){
                    ToastUtils.showShortToast(context, "微信朋友圈分享成功");
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if( platform.getName().equals(Wechat.NAME)) {
                    ToastUtils.showShortToast(context, "微信分享失败");
                }else if( platform.getName().equals(WechatMoments.NAME)){
                    ToastUtils.showShortToast(context, "微信朋友圈分享失败");
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if( platform.getName().equals(Wechat.NAME) ) {
                    ToastUtils.showShortToast(context, "取消微信分享");
                }else if( platform.getName().equals(WechatMoments.NAME)){
                    ToastUtils.showShortToast(context, "取消微信朋友圈分享");
                }
            }
        });
        platform.share(sp);

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
        private int tabtype =0;//0:代表 出售中，1：代表 已下架

        public Goodmanageadapter(Context mContext, List<SisGoodsModel> datas , int tabtype ) {
            this.mContext = mContext;
            this.datas = datas;
            tags = new boolean[datas.size()];
            this.tabtype = tabtype;
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

            RelativeLayout llmain = ViewHolderUtil.get(convertView,R.id.goodmange_item_ll);
            llmain.setTag(datas.get(position));

            llmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SisGoodsModel model = (SisGoodsModel) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(mContext, GoodsDetailActivity.class);
                    //intent.putExtra("goodsid", model.getGoodsId());
                    intent.putExtra("goods", model);
                    intent.putExtra("state", tabtype);
                    ActivityUtils.getInstance().showActivityForResult((Activity) mContext, SisConstant.REFRESHGOODS_CODE, intent);
                }
            });

            TextView txtName = ViewHolderUtil.get(convertView, R.id.goods_item_goodsName);
            txtName.setText(datas.get(position).getGoodsName());
            TextView txtamount = ViewHolderUtil.get(convertView, R.id.goods_item_amount);
            txtamount.setText("库存:" + String.valueOf(datas.get(position).getStock()));
            TextView txtprofit = ViewHolderUtil.get(convertView, R.id.goods_item_profit);
            txtprofit.setText("返利:" + String.valueOf(datas.get(position).getRebate()));
            TextView txtPrice = ViewHolderUtil.get(convertView , R.id.goods_item_commission);
            txtPrice.setText( "￥"+String.valueOf( datas.get(position).getPrice() ));

            final Button goods_item_btn = ViewHolderUtil.get(convertView, R.id.goods_item_btn);
            goods_item_btn.setTag(datas.get(position));

            goods_item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWin == null) {
                        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                        View prView = layoutInflater.inflate(R.layout.layout_good_manage_popwindow, null);//自定义的布局文件
                        popWin = new PopupWindow(prView, 200, 280);
                        ColorDrawable cd = new ColorDrawable(0x00000000);
                        popWin.setBackgroundDrawable(cd);
                        //popWin.setFocusable(true); //设置PopupWindow可获得焦点
                        popWin.setTouchable(true);
                        popWin.setOutsideTouchable(true);
                    }

                    TextView tvCvText = (TextView) popWin.getContentView().findViewById(R.id.sis_goods_menu_updown_text);
                    tvCvText.setText(tabtype == 0 ? "下架" : "上架");

                    popWin.getContentView()
                            .findViewById(R.id.sis_goods_menu_share)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popWin.dismiss();
                                    SisGoodsModel model = (SisGoodsModel) goods_item_btn.getTag();
                                    //ToastUtils.showLongToast(GoodManageActivity.this, model.getGoodsName());
                                    String goodurl = SisConstant.INTERFACE_getGoodDetails + "?goodsId="+ String.valueOf( model.getGoodsId() );
                                    share(model.getGoodsName(), model.getGoodsName(), model.getImgUrl(), goodurl );
                                }
                            });

                    popWin.getContentView().findViewById(R.id.sis_goods_menu_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SisGoodsModel model = (SisGoodsModel) goods_item_btn.getTag();
                            operateGoods(model, 3);
                        }
                    });
                    popWin.getContentView().findViewById(R.id.sis_goods_menu_top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SisGoodsModel model = (SisGoodsModel) goods_item_btn.getTag();
                            operateGoods(model, 2);
                        }
                    });
                    popWin.getContentView().findViewById(R.id.sis_goods_menu_updown).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SisGoodsModel model = (SisGoodsModel) goods_item_btn.getTag();
                            int state = tabtype == 0 ? 0 : 1; //0:代表 下架 ，1代表 上架
                            operateGoods(model, state);
                        }
                    });
                    popWin.showAsDropDown(v, -200, 8);
                }
            });
            return convertView;
        }

        protected void share(String title , String text , String imageUrl , String goodsUrl) {
            if (sharePopWin == null) {
                sharePopWin = new SharePopupWindow(GoodManageActivity.this, GoodManageActivity.this, GoodManageActivity.this.application);
            }
            ShareModel shareModel = new ShareModel();
            shareModel.setUrl(goodsUrl);
            shareModel.setImageUrl(imageUrl);
            shareModel.setText(text);
            shareModel.setTitle(title);

            sharePopWin.initShareParams(shareModel);
            sharePopWin.showShareWindow();
            sharePopWin.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    if (platform.getName().equals(Wechat.NAME)) {
                        ToastUtils.showShortToast(application, "微信分享成功");
                    } else if (platform.getName().equals(WechatMoments.NAME)) {
                        ToastUtils.showShortToast(application, "微信朋友圈分享成功");
                    } else if (platform.getName().equals(QZone.NAME)) {
                        ToastUtils.showShortToast(application, "QQ空间分享成功");
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    ToastUtils.showShortToast(application, "分享失败");
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    ToastUtils.showLongToast(application, "取消分享");
                }
            });
            sharePopWin.setOnDismissListener(new PoponDismissListener(GoodManageActivity.this));
            sharePopWin.showAtLocation(GoodManageActivity.this.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }

        protected void operateGoods(SisGoodsModel model , int operate){
            if( popWin !=null && popWin.isShowing()) {
                popWin.dismiss();
            }
            if( progressPopupWindow==null){
                progressPopupWindow= new ProgressPopupWindow(application , (GoodManageActivity) mContext , getWindowManager() );
            }
            progressPopupWindow.showProgress("请稍等...");
            progressPopupWindow.showAtLocation( getWindow().getDecorView() , Gravity.CENTER , 0 ,0 );

            String url = SisConstant.INTERFACE_operGoods;
            AuthParamUtils authParamUtils =new AuthParamUtils( (BaseApplication)mContext.getApplicationContext() ,
                    System.currentTimeMillis() , url , mContext );
            Map para = new HashMap();
            para.put("userid", ((BaseApplication) mContext.getApplicationContext()).readMemberId());
            para.put("goodsid", model.getGoodsId());
            para.put("opertype", operate);

            Map maps = authParamUtils.obtainParams( para );

            GsonRequest<BaseModel> request = new GsonRequest<BaseModel>(
                    Request.Method.POST, url , BaseModel.class , null , maps ,
                    new MyOperateListener ( (GoodManageActivity) mContext , model ,  operate ),
                    new MyGoodsErrorListener( (GoodManageActivity) mContext  )
            );
            VolleyUtil.getRequestQueue().add(request);
        }
    }

    static class MyOperateListener implements Response.Listener<BaseModel>{
        WeakReference<GoodManageActivity> ref;
        SisGoodsModel model;
        int operate;
        public MyOperateListener( GoodManageActivity act , SisGoodsModel model , int operate){
            this.ref = new WeakReference<GoodManageActivity>(act);
            this.model = model;
            this.operate = operate;
        }

        @Override
        public void onResponse(BaseModel baseModel) {
            if( ref.get()==null) return;
            if( ref.get().progressPopupWindow!=null ){
                ref.get().progressPopupWindow.dismissView();
            }

            if( !validateData( ref.get() , baseModel ) ){
                return;
            }

            ref.get().handler.post(new Runnable() {
                @Override
                public void run() {
                    ref.get().listview.setRefreshing(true);
                }
            });
        }
    }

    protected void getGoods(boolean selected , int pageno){

        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            listview.onRefreshComplete();
            return;
        }

        String url = SisConstant.INTERFACE_searchSisGoodsList;
        url += "?userid=" +  application.readMemberId();
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

            if( ref.get().progressPopupWindow!=null ){
                ref.get().progressPopupWindow.dismissView();
            }

            ref.get().listview.onRefreshComplete();
            ToastUtils.showLongToast(ref.get(),"请求异常");
        }
    }

    static class  MyGoodsDataListener implements Response.Listener<AppSisGoodsModel> {
        WeakReference<GoodManageActivity> ref;

        public MyGoodsDataListener(GoodManageActivity act) {
            ref = new WeakReference<>(act);
        }

        protected void removeRepeatData( List<SisGoodsModel> list ,  List<SisGoodsModel> data ){
            Iterator<SisGoodsModel> iterator = data.iterator();
            List<SisGoodsModel> delList=new ArrayList<>();
            while( iterator.hasNext() ){
                SisGoodsModel item = iterator.next();
                for(SisGoodsModel child : list ){
                    if( item.getGoodsId().equals( child.getGoodsId() ) ) {
                        //data.remove( item );
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
            if (ref.get() == null) return;
            ref.get().listview.onRefreshComplete();

            if (!validateData(ref.get(), appSisGoodsModel)) {
                return;
            }

            if (ref.get().salestate && ref.get().isRefresh) {
                ref.get().pageno1 = appSisGoodsModel.getResultData().getRpageno();
                ref.get().list1.clear();
                if (appSisGoodsModel.getResultData().getList() != null) {
                    ref.get().list1.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().listview.setAdapter(ref.get().adapter1);
            } else if (ref.get().salestate && !ref.get().isRefresh) {
                if (appSisGoodsModel.getResultData().getList() == null ||
                        appSisGoodsModel.getResultData().getList().size() == 0) {
                    ToastUtils.showShortToast(ref.get(), "已经没有数据了。");
                    return;
                }

                ref.get().pageno1 = appSisGoodsModel.getResultData().getRpageno();
                if (appSisGoodsModel.getResultData().getList() != null) {
                    removeRepeatData( ref.get().list1 , appSisGoodsModel.getResultData().getList() );
                    ref.get().list1.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().adapter1.notifyDataSetChanged();
            } else if (!ref.get().salestate && ref.get().isRefresh) {
                ref.get().pageno2 = appSisGoodsModel.getResultData().getRpageno();
                ref.get().list2.clear();
                if (appSisGoodsModel.getResultData().getList() != null) {
                    ref.get().list2.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().listview.setAdapter(ref.get().adapter2);
            } else if (!ref.get().salestate && !ref.get().isRefresh) {
                if (appSisGoodsModel.getResultData().getList() == null ||
                        appSisGoodsModel.getResultData().getList().size() == 0) {
                    ToastUtils.showShortToast(ref.get(), "已经没有数据了。");
                    return;
                }

                ref.get().pageno2 = appSisGoodsModel.getResultData().getRpageno();
                if (appSisGoodsModel.getResultData().getList() != null) {
                    removeRepeatData( ref.get().list2 , appSisGoodsModel.getResultData().getList() );
                    ref.get().list2.addAll(appSisGoodsModel.getResultData().getList());
                }
                ref.get().adapter2.notifyDataSetChanged();
            }

            String txt = "出售中(" + String.valueOf( appSisGoodsModel.getResultData().getSisuptotal() ) + ")";
            ref.get().salestatus_sale.setText(txt);
            txt = "已下架(" + String.valueOf( appSisGoodsModel.getResultData().getSisouttotal() ) + ")";
            ref.get().salestatus_remove.setText(txt);
        }
    }

    /**
     * 方法描述：
     * 方法名称：
     * 参数：
     * 返回值：
     * 创建时间: 2015/10/15
     * 作者: Administrator
     */
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

    protected void showOpenShopWindow(){
        if( msgPopWindow==null ) {
            msgPopWindow = new MsgPopWindow(GoodManageActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openShop();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodManageActivity.this.finish();
                }
            }, "提示", "你是否开通店中店功能?", true);
        }
        msgPopWindow.showAtLocation( getWindow().getDecorView() , Gravity.CENTER ,0,0 );
    }

    protected void loadGoods(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GoodManageActivity.this.isFinishing()) return;
                isRefresh = true;
                salestate = true;
                listview.setRefreshing(true);
            }
        }, 500);
    }

    protected void getShopInfo(){
        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            return;
        }

        if( SisConstant.SHOPINFO !=null ){
            shopName.setText(SisConstant.SHOPINFO.getTitle());
            loadLogo(SisConstant.SHOPINFO.getImgUrl());
            loadGoods();
            return;
        }

        String url = SisConstant.INTERFACE_getSisInfo;
        url += "?userid=" + application.readMemberId();
        AuthParamUtils authParamUtils = new AuthParamUtils(application , System.currentTimeMillis(), url , GoodManageActivity.this);
        url = authParamUtils.obtainUrlName();
        GsonRequest<AppSisBaseinfoModel> request=new GsonRequest<AppSisBaseinfoModel>(
                Request.Method.GET ,
                url ,
                AppSisBaseinfoModel.class,
                null,
                new MySisInfoListener(this),
                new MySisInfoErrorListener(this)
        );

        if( progressdlg ==null){
            progressdlg = new ProgressDialog(this);
            progressdlg.setCanceledOnTouchOutside(false);
        }
        progressdlg.setMessage("正在获取店铺信息,请稍等...");
        progressdlg.show();

        VolleyUtil.getRequestQueue().add(request);
    }

    static class MySisInfoErrorListener implements Response.ErrorListener{
        WeakReference<GoodManageActivity> ref;
        public MySisInfoErrorListener(GoodManageActivity act){
            ref = new WeakReference<>(act);
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get()==null)return;

            if(ref.get().progressPopupWindow!=null){
                ref.get().progressPopupWindow.dismissView();
            }

            if( ref.get().progressdlg!=null){
                ref.get().progressdlg.dismiss();
            }

            ToastUtils.showLongToast(ref.get(),"请求异常");
        }
    }

    static class  MySisInfoListener implements Response.Listener<AppSisBaseinfoModel>{
        WeakReference<GoodManageActivity> ref;

        public MySisInfoListener(GoodManageActivity act){
            ref=new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisBaseinfoModel appSisBaseinfoModel) {
            if( ref.get()==null) return;
            if(ref.get().progressPopupWindow!=null){
                ref.get().progressPopupWindow.dismissView();
            }

            if( ref.get().progressdlg!=null){
                ref.get().progressdlg.dismiss();
            }

            if(!validateData(appSisBaseinfoModel)){
                return;
            }

            if( appSisBaseinfoModel.getResultData().getData().isEnableSis() ==false ){
                ref.get().showOpenShopWindow();
                return;
            }

            SisConstant.SHOPINFO = appSisBaseinfoModel.getResultData().getData();
            ref.get().shopName.setText(SisConstant.SHOPINFO.getTitle());
            ref.get().loadLogo(SisConstant.SHOPINFO.getImgUrl());
            ref.get().loadGoods();
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

    /**
    * 方法描述：
    * 方法名称：
    * 参数：
    * 返回值：
    * 创建时间: 2015/11/18
    * 作者:
    */
    protected void openShop(){
        if( msgPopWindow!=null ){
            msgPopWindow.dismiss();
        }
        if( progressPopupWindow==null){
            progressPopupWindow=new ProgressPopupWindow(application, GoodManageActivity.this,getWindowManager());
        }
        progressPopupWindow.showProgress("正在开启,请稍等...");
        progressPopupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

        String url = SisConstant.INTERFACE_open;
        url += "?userid=" + application.readMemberId();
        AuthParamUtils authParamUtils = new AuthParamUtils(application , System.currentTimeMillis(), url , GoodManageActivity.this);
        url = authParamUtils.obtainUrlName();
        GsonRequest<AppSisBaseinfoModel> request=new GsonRequest<AppSisBaseinfoModel>(
                Request.Method.GET ,
                url ,
                AppSisBaseinfoModel.class,
                null,
                new MySisInfoListener(this),
                new MySisInfoErrorListener(this)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    protected void getinitdata(){
        String url = Constants.INTERFACE_PREFIX+"/mall/Init?customerId="+ app.readMerchantId();
        AuthParamUtils authParamUtils =new AuthParamUtils(app, System.currentTimeMillis(), url , this);
        url = authParamUtils.obtainUrlName();



    }

}