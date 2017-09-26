package com.huotu.partnermall.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.GoIndexEvent;
import com.huotu.partnermall.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    static  String TAG = ScanActivity.class.getSimpleName();
    @Bind(R.id.zxingview)
    ZXingView zXingView;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        titleLeftImage.setBackgroundResource(R.drawable.main_title_left_back);

        zXingView.setDelegate(this);

    }

    @OnClick(R.id.titleLeftImage)
    protected void onBack() {
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        zXingView.startCamera();//打开后置摄像头开始预览，但是并未开始识别
        zXingView.showScanRect();//显示扫描框
    }

    @Override
    protected void onResume() {
        super.onResume();

        zXingView.startSpotDelay(100);//延迟0.1秒后开始识别
    }

    @Override
    protected void onPause() {
        super.onPause();

        zXingView.stopSpot();//停止识别
    }

    @Override
    protected void onStop() {
        super.onStop();

        zXingView.stopCamera();//关闭摄像头预览，并且隐藏扫描框
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        zXingView.onDestroy();
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "scan result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        Intent intent = this.getIntent();
        intent.putExtra("content" , result);
        this.setResult( RESULT_OK ,  intent);
        this.finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
        Toast.makeText(this, "扫描出错!", Toast.LENGTH_SHORT).show();
    }
}
