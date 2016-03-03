package com.huotu.partnermall.utils;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by Administrator on 2015/9/19.
 */
public
class LoadingUtil {

    private Activity       aty;
    private WindowProgress progress;
    public Handler handler = new Handler ( );

    public
    LoadingUtil ( Activity aty ) {
        // TODO Auto-generated constructor stub
        this.aty = aty;
    }

    public
    void showProgressNotPost ( ) {
        //网络访问前先检测网络是否可用
        if (!Util.isConnect(aty)) {
            ToastUtils.showLongToast(aty, "无网络或当前网络不可用!");
            return;
        }
        if (progress == null) {
            progress = new WindowProgress(aty);
        }
        if (!aty.isFinishing()) {
            try {
                progress.showProgress();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public void showProgress() {
        //网络访问前先检测网络是否可用
        if(!Util.isConnect(aty)){
            ToastUtils.showLongToast(aty , "无网络或当前网络不可用!");
            return;
        }

        if(progress == null){
            progress = new WindowProgress(aty);
        }

        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             if(!aty.isFinishing())
                                 try {
                                     progress.showProgress();
                                 } catch (Exception e) {
                                     System.out.println(e.toString());
                                 }

                         }
                     });

    }
    public void dismissProgress(){
        if(progress == null)
            return;

        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             progress.dismissProgress();
                         }
                     });


    }

    public void dismissProgressNotPost(){
        if( progress ==null) return;
        progress.dismissProgress();
    }
}
