package cy.com.partnermall.utils;

import android.content.Context;
import android.widget.Toast;

import cy.com.partnermall.widgets.AlarmDailog;

public class ToastUtils
{
    private static AlarmDailog alarmDialog;

    public static
    void showShortToast ( Context context, String showMsg ) {
        if ( null != alarmDialog ) {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.setDuration(Toast.LENGTH_SHORT);
        alarmDialog.show();

    }

    public static void showLongToast(Context context, String showMsg)
    {
        if (null != alarmDialog)
        {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.show();
    }
}
