package com.huotu.partnermall.ui;

import com.huotu.partnermall.config.NativeConstants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.CircleProgressBar;
import com.huotu.partnermall.widgets.MsgPopWindow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
/**
 * 客户端升级
 * @author jinxiangdong
 *
 */
public class AppUpdateActivity extends BaseActivity{
	protected static final int REQUEST_INFO = 1567;
	protected static final int CODE_SUCCESS=1;
	protected static final int CODE_PROGRESS=2;
	protected static final int CODE_FAIL=0;
	private String softwarePath = null;
	private boolean isCancel;
	private boolean taskIsComplete;
	private String destMd5;
	private String tips;
	private boolean isForce;
	TextView txtTips;
	Handler handler;
	CircleProgressBar progressWithArrow;
	MsgPopWindow msgPopWindow;

	/**
	 * 整包更新，增量更新
	 *
	 */
	public enum UpdateType{
		FullUpate, DiffUpdate
	}
	private UpdateType updateType;
	private String clientURL;
	private String oldapk_filepath;
	private String newapk_savepath;

	protected String getAppTempPath(){
		String path;
		if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
			path = Environment.getExternalStorageDirectory().toString() + File.separator + "temp";
		}else{
			path = this.getFilesDir().getPath();
		}
		return path;


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_appupdate);

		oldapk_filepath = getAppTempPath()  + File.separator + getPackageName() + "_old.apk";
		newapk_savepath = getAppTempPath() + File.separator + getPackageName() + "_new.apk";
		softwarePath = getAppTempPath() + File.separator + getPackageName() + "_patch.apk";

		isCancel = false;
		taskIsComplete = false;
		initView();

		Bundle extra = getIntent().getExtras();
		if(extra != null){
			//start download
			File file = new File(softwarePath);
			if(!file.exists())
				file.mkdirs();
			clientURL = extra.getString("url");
			updateType = (UpdateType) extra.getSerializable("type");
			isForce = extra.getBoolean("isForce");
			destMd5 = extra.getString("md5");
			tips = extra.getString("tips");
			txtTips = (TextView) findViewById(R.id.txtTips);
			txtTips.setText(tips);

			new ApkDownloadThread(handler, clientURL).start();
		}else{
			ToastUtils.showLongToast( this ,"升级需要的参数不能为空");
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			msgPopWindow = new MsgPopWindow(this, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					isCancel = true;
					if (taskIsComplete) {
						downloadClientSuccess();
					}
					msgPopWindow.dismiss();
					AppUpdateActivity.this.finish();
				}
			}, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					isCancel = true;
//					//delete file
//					File file = new File(softwarePath);
//					if (file.exists())
//						file.delete();
//					finish();
					msgPopWindow.dismiss();
				}
			},"询问","确定要取消更新吗？",false);
			msgPopWindow.setWindowsStyle();
			msgPopWindow.showAtLocation( getWindow().getDecorView() , Gravity.CENTER ,0,0 );
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	protected void initView() {
		progressWithArrow = (CircleProgressBar) findViewById(R.id.progressWithArrow);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if( msg.what== CODE_SUCCESS){
					progressWithArrow.setProgress(100);
					//下载完成
					downloadClientSuccess();
				}
				else if( msg.what== CODE_PROGRESS){
					int percent = Integer.valueOf( msg.obj.toString() );
					progressWithArrow.setProgress( percent );
				}
				else if( msg.what ==CODE_FAIL){
					//下载失败
					downloadClientFailed();
				}
			}
		};

	}

	private void downloadClientFailed(){
		Toast.makeText(this, "新版本更新失败...", Toast.LENGTH_SHORT).show();
		Intent intent = getIntent();
		intent.putExtra("isForce", isForce);
		setResult(NativeConstants.RESULT_CODE_CLIENT_DOWNLOAD_FAILED, intent);
		finish();
	}

	//版本下载成功，开始启动安装
	private void downloadClientSuccess(){
			if(updateType == UpdateType.FullUpate){//整包更新,直接进行安装
				installApk(softwarePath);
			}
		}

	private void installApk(String path){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),	"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
		AppUpdateActivity.this.finish();
	}

	class ApkDownloadThread extends Thread {
		Handler mHandler;
		String mUrl;

		public ApkDownloadThread(Handler handler, String url) {
			this.mHandler = handler;
			this.mUrl = url;
		}

		@Override
		public void run() {
			HttpURLConnection hc = null;
			try {
				URL url = new URL(mUrl);
				hc = (HttpURLConnection) url.openConnection();
			} catch (IOException ex) {
			}
			if( hc ==null ){
				Message msg = mHandler.obtainMessage( CODE_FAIL , "下载APK文件失败。" );
				mHandler.sendMessage(msg);
				return;
			}

			InputStream update_is = null;
			BufferedInputStream update_bis = null;
			FileOutputStream update_os = null;
			BufferedOutputStream update_bos = null;
			byte[] buffer = null;
			try {
				if (hc.getResponseCode() != 200) {
					Message msg = mHandler.obtainMessage( CODE_FAIL ,  "下载APK文件失败。" );
					mHandler.sendMessage(msg);
					return;
				}
				int contentLen = hc.getContentLength();
				if (contentLen == 0) {
					Message msg = mHandler.obtainMessage( CODE_FAIL ,  "下载APK文件失败。" );
					mHandler.sendMessage(msg);
					return;
				}
				update_is = hc.getInputStream();
				update_bis = new BufferedInputStream(update_is, 1024);

				File cityMapFile = new File(softwarePath);
				if (cityMapFile.exists()) {
					cityMapFile.delete();
				}

				cityMapFile.createNewFile();

				update_os = new FileOutputStream(cityMapFile, false);
				update_bos = new BufferedOutputStream(update_os, 1024);

				buffer = new byte[1024];
				int readed = 0;
				int step = 0;
				while ((step = update_bis.read(buffer)) != -1 && !isCancel) {
					readed += step;
					update_bos.write(buffer, 0, step);
					update_bos.flush();

					int precent = (int) ((readed / (float) contentLen) * 100);
					//publishProgress((int) ((readed / (float)contentLen) * 100), readed ,contentLen);
					Message msg = mHandler.obtainMessage( CODE_PROGRESS , String.valueOf(precent));
					mHandler.sendMessage(msg);
				}
				update_os.flush();

				if( !isCancel ) {
					Message msg = mHandler.obtainMessage(CODE_SUCCESS, "100");
					mHandler.sendMessage(msg);
				}
				return;
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = mHandler.obtainMessage(CODE_FAIL,"下载失败。");
				mHandler.sendMessage(msg);
				return;
			} finally {
				try {
					if (update_bis != null)
						update_bis.close();
					if (update_is != null)
						update_is.close();
					if (update_bos != null)
						update_bos.close();
					if (update_os != null)
						update_os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
