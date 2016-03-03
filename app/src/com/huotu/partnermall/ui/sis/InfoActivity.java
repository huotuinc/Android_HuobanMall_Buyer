package com.huotu.partnermall.ui.sis;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.widgets.CircleImageView;
import com.huotu.partnermall.widgets.CropperView;
import com.huotu.partnermall.widgets.PhotoSelectView;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InfoActivity extends BaseActivity implements View.OnClickListener ,
        PhotoSelectView.OnPhotoSelectBackListener , CropperView.OnCropperBackListener {
    private Button header_back;
    private TextView header_title;
    private LinearLayout imgLabel;
    private LinearLayout shopNameLabel;
    private PhotoSelectView pop;
    private CropperView cropperView;
    private RelativeLayout header;
    private TextView shopName;
    private LinearLayout shopDescriptionLabel;
    private LinearLayout shareTitleLabel;
    private LinearLayout shareDescriptionLabel;
    private TextView shopDescription;
    private TextView shareTitle;
    private TextView shareDesciption;
    private LinearLayout templateLabel;
    private CircleImageView logo;
    private Bitmap cropBitmap;
    private String imgPath;
    private BaseApplication app;
    private ProgressPopupWindow progressPopupWindow;
    private RelativeLayout rlcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_info);
        findViewById();
        initView();
    }

    protected void findViewById() {
        app = (BaseApplication) this.getApplication();
        shopName = (TextView) findViewById(R.id.shopName);
        header = (RelativeLayout) findViewById(R.id.sis_header);
        header_back = (Button) findViewById(R.id.header_back);
        header_title = (TextView) findViewById(R.id.header_title);
        imgLabel = (LinearLayout) findViewById(R.id.imgLabel);
        shopNameLabel = (LinearLayout) findViewById(R.id.shopNameLabel);
        shopDescription = (TextView) findViewById(R.id.shopDescription);
        shopDescriptionLabel = (LinearLayout) findViewById(R.id.shopDescriptionLabel);
        shareTitle = (TextView) findViewById(R.id.shareTitle);
        shareDesciption = (TextView) findViewById(R.id.shareDescription);
        shareTitleLabel = (LinearLayout) findViewById(R.id.shareTitleLabel);
        shareDescriptionLabel = (LinearLayout) findViewById(R.id.shareDescriptionLabel);
        templateLabel = (LinearLayout) findViewById(R.id.shopTemplateLabel);
        logo = (CircleImageView) findViewById(R.id.logo);
        rlcd = (RelativeLayout)findViewById(R.id.sis_info_cd);
        rlcd.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));
    }

    @Override
    protected void initView() {
        header.setBackgroundColor(SystemTools.obtainColor(app.obtainMainColor()));
        header_title.setText("小店设置");
        header_back.setOnClickListener(this);
        header_back.setText("返回");
        imgLabel.setOnClickListener(this);
        shopNameLabel.setOnClickListener(this);
        shopDescriptionLabel.setOnClickListener(this);
        shareTitleLabel.setOnClickListener(this);
        shareDescriptionLabel.setOnClickListener(this);
        templateLabel.setOnClickListener(this);
        setData();

        setImmerseLayout();
    }

    public void setImmerseLayout(){
        if ( ((BaseApplication)this.getApplication()).isKITKAT ()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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

    protected void setData() {
        if (SisConstant.SHOPINFO == null) return;
        VolleyUtil.getImageLoader(this).get(SisConstant.SHOPINFO.imgUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null && imageContainer.getBitmap() != null) {
                    logo.setImageBitmap(imageContainer.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showShortToast(InfoActivity.this, "加载Logo失败");
            }
        });
        shopName.setText(SisConstant.SHOPINFO.getTitle());
        shopDescription.setText(SisConstant.SHOPINFO.getSisDescription());
        shareTitle.setText(SisConstant.SHOPINFO.getShareTitle());
        shareDesciption.setText(SisConstant.SHOPINFO.getShareDescription());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back: {
                finish();
            }
            break;
            case R.id.imgLabel: {
                if (null == pop)
                    pop = new PhotoSelectView(this, this);
                pop.show();
            }
            break;
            case R.id.shopNameLabel: {
                Bundle bd = new Bundle();
                bd.putInt("type", EditSetTypeEnum.SHOPNAME.getIndex());
                bd.putString("text", shopName.getText().toString());
                ActivityUtils.getInstance().showActivityForResult(InfoActivity.this, SisConstant.REFRESHSHOPINFO_CODE, EditSetActivity.class, bd);
            }
            break;
            case R.id.shopTemplateLabel: {
                Intent intent = new Intent(this, SelectTempleteActivity.class);
                this.startActivity(intent);
            }
            break;
            case R.id.shopDescriptionLabel: {
                Bundle bd = new Bundle();
                bd.putInt("type", EditSetTypeEnum.SHOPDESCRIPTION.getIndex());
                bd.putString("text", shopDescription.getText().toString());
                ActivityUtils.getInstance().showActivityForResult(InfoActivity.this, SisConstant.REFRESHSHOPINFO_CODE, EditSetActivity.class, bd);

            }
            break;
            case R.id.shareTitleLabel: {
                Bundle bd = new Bundle();
                bd.putInt("type", EditSetTypeEnum.SHARETITLE.getIndex());
                bd.putString("text", shareTitle.getText().toString());
                ActivityUtils.getInstance().showActivityForResult(InfoActivity.this, SisConstant.REFRESHSHOPINFO_CODE, EditSetActivity.class, bd);

            }
            break;
            case R.id.shareDescriptionLabel: {
                Bundle bd = new Bundle();
                bd.putInt("type", EditSetTypeEnum.SHAREDESCRIPTION.getIndex());
                bd.putString("text", shareDesciption.getText().toString());
                ActivityUtils.getInstance().showActivityForResult(InfoActivity.this, SisConstant.REFRESHSHOPINFO_CODE, EditSetActivity.class, bd);

            }
            break;
        }
    }

    public void onPhotoSelectBack(PhotoSelectView.SelectType type) {
        // TODO Auto-generated method stub
        if (null == type)
            return;
        getPhotoByType(type);
    }

    private void getPhotoByType(PhotoSelectView.SelectType type) {
        switch (type) {
            case Camera:
                getPhotoByCamera();
                break;
            case File:
                getPhotoByFile();
                break;

            default:
                break;
        }
    }

    public void getPhotoByCamera() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss",
                Locale.CHINA);
        String imageName = "fm" + sdf.format(date) + ".jpg";
        imgPath = Environment.getExternalStorageDirectory() + "/" + imageName;
        File out = new File(imgPath);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("fileName", imageName);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0);
    }

    public void getPhotoByFile() {
        Intent intent2 = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent2, 1);
    }

    public void onDateBack(String date) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnCropperBack(Bitmap bitmap) {
        if( SisConstant.SHOPINFO ==null ) {
            ToastUtils.showShortToast(InfoActivity.this,"无法获得店铺信息，操作失败！");
            return;
        }

        if (null == bitmap) return;
        cropBitmap = bitmap;

        // 上传头像
        if (false == canConnect()) {
            return;
        }

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        cropBitmap.compress(Bitmap.CompressFormat.PNG, 90, bao);
        byte[] buffer = bao.toByteArray();
        String imgStr = Base64.encodeToString(buffer, 0, buffer.length,
                Base64.DEFAULT);
        String profileData = imgStr;

        String url = SisConstant.INTERFACE_updateSisProfile;
        Map<String, String> paras = new HashMap<>();
        paras.put("profiletype", String.valueOf(EditSetTypeEnum.LOGO.getIndex()));
        paras.put("profiledata", profileData);
        paras.put("sisid", String.valueOf(SisConstant.SHOPINFO.getSisId()));

        AuthParamUtils authParamUtils = new AuthParamUtils(application, System.currentTimeMillis(), url, InfoActivity.this);
        Map<String, String> maps = authParamUtils.obtainParams(paras);
        GsonRequest<AppSisBaseinfoModel> request = new GsonRequest<AppSisBaseinfoModel>(
                Request.Method.POST,
                url,
                AppSisBaseinfoModel.class,
                null,
                maps,
                new MyLogoListener(InfoActivity.this),
                new ErrorListener(InfoActivity.this, null, progressPopupWindow)
        );

        if (progressPopupWindow == null) {
            progressPopupWindow = new ProgressPopupWindow( InfoActivity.this);
        }
        progressPopupWindow.showProgress("正在上传，请稍等...");
        progressPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        VolleyUtil.getRequestQueue().add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == SisConstant.REFRESHSHOPINFO_CODE && resultCode == RESULT_OK) {
            setData();
            setResult(RESULT_OK);
            return;
        }

        if (requestCode == 0) {
            Bitmap bitmap = Util.readBitmapByPath(imgPath);
            if (bitmap == null) {
                ToastUtils.showLongToast(InfoActivity.this, "未获取到图片!");
                return;
            }
            if (null == cropperView)
                cropperView = new CropperView(this, (CropperView.OnCropperBackListener) this);
            cropperView.cropper(bitmap);
        } else if (requestCode == 1) {
            if (data != null) {
                Bitmap bitmap = null;
                Uri uri = data.getData();
                // url是content开头的格式
                if (uri.toString().startsWith("content://")) {
                    String path = null;
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, pojo, null,
                            null, null);
                    if (cursor != null) {
                        int colunm_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(colunm_index);

                        bitmap = Util.readBitmapByPath(path);
                    }

                    if (bitmap == null) {
                        ToastUtils.showLongToast(InfoActivity.this,
                                "未获取到图片!");
                        return;
                    }
                } else if (uri.toString().startsWith("file:///")) {
                    String path = uri.toString().substring(8,
                            uri.toString().length());
                    bitmap = Util.readBitmapByPath(path);
                    if (bitmap == null) {
                        ToastUtils.showLongToast(InfoActivity.this,
                                "未获取到图片!");
                        return;
                    }

                }
                if (null == cropperView)
                    cropperView = new CropperView(this, (CropperView.OnCropperBackListener) this);
                cropperView.cropper(bitmap);
            }

        }

    }

    static class MyLogoListener implements Response.Listener<AppSisBaseinfoModel> {
        WeakReference<InfoActivity> ref;

        public MyLogoListener(InfoActivity act) {
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisBaseinfoModel appSisBaseinfoModel) {
            if (ref.get() == null) return;

            if (ref.get().progressPopupWindow != null) {
                ref.get().progressPopupWindow.dismissView();
            }

            if (!validateData(ref.get(), appSisBaseinfoModel)) {
                return;
            }

            SisConstant.SHOPINFO = appSisBaseinfoModel.getResultData().getData();
            ref.get().setResult(RESULT_OK);
            ref.get().setData();
        }
    }

    static protected boolean validateData(Context context, BaseModel data) {
        if (null == data) {
            ToastUtils.showLongToast(context, "请求失败");
            return false;
        } else if (data.getSystemResultCode() != 1) {
            ToastUtils.showLongToast(context, data.getSystemResultDescription());
            return false;
        } else if (data.getResultCode() != 1) {
            ToastUtils.showLongToast(context, data.getResultDescription());
            return false;
        }
        return true;
    }

}
