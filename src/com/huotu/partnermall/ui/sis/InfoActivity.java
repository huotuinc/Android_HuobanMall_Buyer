package com.huotu.partnermall.ui.sis;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.widgets.CropperView;
import com.huotu.partnermall.widgets.PhotoSelectView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoActivity extends BaseActivity implements View.OnClickListener , PhotoSelectView.OnPhotoSelectBackListener {
    private Button header_back;
    private TextView header_title;
    private LinearLayout imgLabel;
    private LinearLayout shopNameLabel;
    private LinearLayout shopdescriptionLabel;
    private PhotoSelectView pop;
    private CropperView cropperView;
    private RelativeLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_info);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        header = (RelativeLayout)findViewById(R.id.sis_header);
        header_back= (Button) findViewById(R.id.header_back);
        header_title= (TextView) findViewById(R.id.header_title);
        imgLabel= (LinearLayout) findViewById(R.id.imgLabel);
        shopNameLabel= (LinearLayout) findViewById(R.id.shopNameLabel);
        shopdescriptionLabel= (LinearLayout) findViewById(R.id.shopdescriptionLabel);

    }

    @Override
    protected void initView() {
        header.setBackgroundColor( SystemTools.obtainColor(((BaseApplication) InfoActivity.this.getApplication()).obtainMainColor()) );
        header_title.setText("小店设置");
        header_back.setOnClickListener(this);
        imgLabel.setOnClickListener(this);
        shopNameLabel.setOnClickListener(this);
        shopdescriptionLabel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:{
                finish();
            }
            break;
            case R.id.imgLabel:{
                if (null == pop)
                    pop = new PhotoSelectView(this, this);
                pop.show();

            }
            break;
            case R.id.shopNameLabel:{
                ToastUtils.showShortToast(this,"111");
            }
            break;
            case R.id.shopdescriptionLabel:{
                Intent intent =new Intent(this,SelectTempleteActivity.class);
                this.startActivity(intent);
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

    private String imgPath;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
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
                    String[] pojo = { MediaStore.Images.Media.DATA };
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
}
