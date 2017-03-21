package com.huotu.partnermall.utils;

import android.content.Context;
import android.widget.Toast;

import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.widgets.AlarmDailog;

public class ToastUtils {
    private static AlarmDailog alarmDialog;
    private static Toast toast;

    public static void showShortToast(Context context, String showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.setDuration(Toast.LENGTH_SHORT);
        alarmDialog.show();

    }

    public static void showLongToast(Context context, String showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.show();
    }


    public static void showShortToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    private static void showToast(String msg, int duration) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.single, msg, duration);
        }
        toast.setDuration(duration);
        toast.setText(msg);
        toast.show();
    }

    public static void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

}
