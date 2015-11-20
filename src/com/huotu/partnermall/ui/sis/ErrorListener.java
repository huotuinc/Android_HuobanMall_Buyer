package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.widgets.ProgressPopupWindow;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2015/11/17.
 */
class ErrorListener implements Response.ErrorListener{
    PullToRefreshBase _pullToRefreshBase =null;
        WeakReference<Activity> ref;
        ProgressPopupWindow progressPopupWindow;

        public ErrorListener(Activity act,PullToRefreshBase pullToRefreshBase , ProgressPopupWindow progressPopupWindow){
            _pullToRefreshBase= pullToRefreshBase;
            ref = new WeakReference<Activity>(act);
            this.progressPopupWindow = progressPopupWindow;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if( ref.get() ==null )return;

            if( ref.get().isFinishing() ) return;
            if( progressPopupWindow !=null){
                progressPopupWindow.dismissView();
            }

            if( _pullToRefreshBase!=null ){
                _pullToRefreshBase.onRefreshComplete();
            }

            String message="";
            if( volleyError instanceof TimeoutError){
                message = "网络连接超时";
            }else if( volleyError instanceof NetworkError || volleyError instanceof NoConnectionError) {
                message ="网络请求异常，请检查网络状态";
            }else if( volleyError instanceof ParseError){
                message = "数据解析失败，请检测数据的正确性";
            }else if( volleyError instanceof ServerError || volleyError instanceof AuthFailureError){
                if( null != volleyError.networkResponse){
                    message=new String( volleyError.networkResponse.data);
                }else{
                    message = volleyError.getMessage();
                }
            }

            if( message.length()<1){
                message = "网络请求失败，请检查网络状态";
            }

            ToastUtils.showLongToast(ref.get(), message);
            //DialogUtils.showDialog(BaseFragmentActivity.this, BaseFragmentActivity.this.getSupportFragmentManager(), "错误信息", message, "关闭");

        }
    }

