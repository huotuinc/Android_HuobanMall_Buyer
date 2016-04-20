package com.huotu.partnermall.widgets;

import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.partnermall.inner.R;

public class WindowProgress {
	private static final int STATUS_SHOW = 1;
	private static final int STATUS_DISMISS = 0;
	private Activity mActivity;
	private WindowManager mWindowManager;
	private LinearLayout progressLay;
	private int state = STATUS_DISMISS;//1,show status;0,dismiss status
	private TextView txt;
	private LinearLayout layAll;

	public WindowProgress(Activity activity) {
		this.mActivity = activity;
	}


//	public int getState(){
//		return state;
//
//	}
	public boolean isShowing(){
		return state == STATUS_SHOW;
	}


	public void showProgress() {
		if(mActivity.isFinishing()) return;
		state = STATUS_SHOW;
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
		}
		if(layAll == null){
			layAll = new LinearLayout(mActivity);
			layAll.setBackgroundColor(Color.GRAY);
			layAll.getBackground().setAlpha(150);
			//layAll.setBackgroundResource(R.drawable.loading_bg);
			//layAll.setBackgroundColor(0x90545D66);
			//layAll.getBackground().setAlpha(150);
		}


		if (progressLay == null) {
			progressLay = new LinearLayout(mActivity);
//			LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
//			progressLay.setLayoutParams(lp1);
			progressLay.setBackgroundResource(R.drawable.window_progress_bg);

		}
//		layAll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//            	//layAll.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), blurImage(takeScreenShot(mActivity), 100)));
//            	blur(takeScreenShot(mActivity), layAll);
//            	//layAll.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), convertToBlur(takeScreenShot(mActivity))));
//                return true;
//            }
//        });
		//L.i(">>>>>>>progress:" + progressLay.getParent());

		if (layAll.getParent() == null) {
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.TYPE_APPLICATION,
					WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
					PixelFormat.TRANSLUCENT);

//			 mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//					 WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

			layAll.removeAllViews();
			progressLay.removeAllViews();
			//View pb = LayoutInflater.from(mContext).inflate(R.layout.progress_bar, null);
			ProgressBar bar = new ProgressBar(mActivity, null, android.R.attr.progressBarStyle);
			//bar.setIndeterminateDrawable(mContext.getResources().getDrawable(R.drawable.progressbar_custom));
			 /*
	         * 下面可以通过改变LayoutParams中的width参数来设置圆形进度条的直径
	         * height如果小于width，下圆环会被截掉，无法显示全圆
	         */
			LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(-2,-2);
		    bar.setLayoutParams(lp1);
		    bar.setIndeterminate(false);
		    //bar.setIndeterminateDrawable(mActivity.getResources().getDrawable(R.anim.loading));
			progressLay.setGravity(Gravity.CENTER);
			progressLay.setOrientation(LinearLayout.VERTICAL);
			progressLay.addView(bar);

			txt = new TextView(mActivity);
			txt.setTextColor(Color.BLACK);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
			txt.setLayoutParams(params);
			txt.setVisibility(View.GONE);
			progressLay.addView(txt);

//			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//			layAll.setLayoutParams(lp2);
			layAll.setGravity(Gravity.CENTER);
			layAll.addView(progressLay);
			layAll.setFocusable(true);
			layAll.setFocusableInTouchMode(true);
			layAll.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK){
						dismissProgress();
						if(keyBack != null)
							keyBack.onkeyBack();
						return true;
					}
					return false;
				}
			});

			mWindowManager.addView(layAll, lp);
		}
	}
	public interface progressOnKeyBack{
		void onkeyBack();
	}
	public progressOnKeyBack keyBack;
	public void setProgressOnkeyBack(progressOnKeyBack keyBack){
		this.keyBack = keyBack;
	}

	public void setText(String msg){
		if(txt.getVisibility() == View.GONE)
			txt.setVisibility(View.VISIBLE);
		txt.setText(msg);
	}

	public void dismissProgress() {
		state = STATUS_DISMISS;
		if (layAll != null && layAll.getParent() != null) {
			mWindowManager.removeView(layAll);
		}
	}

    /**
     * 高斯模糊
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth()/8;
        int height = bmp.getHeight()/8;
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

}