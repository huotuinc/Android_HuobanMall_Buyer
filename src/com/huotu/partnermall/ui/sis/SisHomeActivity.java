package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.WindowUtils;
import com.huotu.partnermall.widgets.CircleImageView;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

public class SisHomeActivity extends Activity implements View.OnClickListener , Handler.Callback {

    Bitmap barCode;
    int screenW;
    int screenH;
    int barcodeW;
    int barcodeH;
    Handler handler = new Handler(this);
    ImageView ivBarcode;
    PopupWindow popWin ;
    RelativeLayout menu1;
    RelativeLayout menu2;
    RelativeLayout menu3;
    LinearLayout menu4;
    RelativeLayout rlStatData;
    CircleImageView logo;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.sis_activity_sis_home);

        screenW = getWindowManager().getDefaultDisplay().getWidth();
        screenH = getWindowManager().getDefaultDisplay().getHeight();
        barcodeW = screenW * 80/100;
        barcodeH = screenH * 80/100;
        if( barcodeW<barcodeH) {barcodeH = barcodeW;}

        header = (RelativeLayout)findViewById(R.id.sis_header);
        header.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) SisHomeActivity.this.getApplication()).obtainMainColor()));

        logo = (CircleImageView)findViewById(R.id.sis_logo);
        loadLogo();

        ivBarcode = (ImageView)findViewById(R.id.sis_barcode);
        ivBarcode.setOnClickListener(this);
        String text = "http://www.baidu.com";
        showBarCode(text , ivBarcode , barcodeW ,barcodeH );

        menu1 =(RelativeLayout)findViewById(R.id.sis_menu1);
        menu2 =(RelativeLayout)findViewById(R.id.sis_menu2);
        menu3 =(RelativeLayout)findViewById(R.id.sis_menu3);
        menu4 =(LinearLayout)findViewById(R.id.sis_menu4);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        rlStatData = (RelativeLayout)findViewById(R.id.sis_data);
        rlStatData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.sis_barcode){
            if( barCode ==null ) {
                ToastUtils.showLongToast(SisHomeActivity.this,"二维码没有生成");
            }else {
                showBarCode();
            }
        }else if(v.getId()==R.id.sis_menu1){
            SisHomeActivity.this.startActivity(new Intent(SisHomeActivity.this,GoodManageActivity.class));
        }else if(v.getId()==R.id.sis_menu2){
            SisHomeActivity.this.startActivity(new Intent(SisHomeActivity.this,GoodsDetailActivity.class).putExtra("url","http://www.sina.com.cn"));
        }else if(v.getId()==R.id.sis_menu3){
            SisHomeActivity.this.startActivity(new Intent(SisHomeActivity.this, InfoActivity.class));
        }else if(v.getId()==R.id.sis_menu4){
            Intent intent = new Intent();
            intent.setClass(this,SaleActivity.class);
            this.startActivity(intent);
        }else if( v.getId()==R.id.sis_data){

        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if( msg.what == 7000 ){
            ToastUtils.showLongToast(SisHomeActivity.this,"生成二维码失败");
            return true;
        }else if( msg.what==7001){
            ivBarcode.setImageBitmap( barCode );
            return true;
        }

        return false;
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
    private void loadLogo(){
        String logoUrl = "http://news.xinhuanet.com/photo/2015-10/29/128371793_14460865923871n.jpg";

        VolleyUtil.getImageLoader(SisHomeActivity.this)
                .get(logoUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        if (imageContainer != null && imageContainer.getBitmap() != null) {
                            logo.setImageBitmap(imageContainer.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtils.showShortToast(SisHomeActivity.this, "加载Logo失败");
                    }
                });
    }


    protected void showBarCode() {
        if (popWin == null) {
            popWin = new PopupWindow();

            LayoutInflater inflater = LayoutInflater.from(SisHomeActivity.this);
            View rootView = inflater.inflate(R.layout.sis_barcode,null);
            popWin.setContentView(rootView);
            popWin.setOnDismissListener(new PoponDismissListener( SisHomeActivity.this));

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

            int sw = screenW * 80 / 100;
            int sh = screenH * 80 / 100;
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
            WindowUtils.backgroundAlpha( SisHomeActivity.this, 0.7f);
            popWin.showAtLocation(ivBarcode, Gravity.CENTER, 0, 0);
        }
    }

    protected  void showBarCode(final String context , ImageView iv , final int width , final int height) {
        if( barCode!=null ) {
              iv.setImageBitmap(barCode);
              return;
        }

        final Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        final String filePath = getFileRoot(SisHomeActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
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

    protected void getData(){
//        GsonRequest<String> request =new GsonRequest<String>(Request.Method.GET,
//                String.class ,
//                 );
//        VolleyUtil.getRequestQueue().add(request);
    }
}
