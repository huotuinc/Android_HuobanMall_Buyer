package com.huotu.android.library.buyer.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Data.SmartUiEvent;
import com.huotu.android.library.buyer.bean.Variable;
import com.huotu.android.library.buyer.utils.CommonUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/3/21.
 */
public class BaseLinearLayout extends LinearLayout  implements View.OnClickListener{
    public BaseLinearLayout(Context context) {
        super(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
    }

    protected void golink( String linkName , String relativeUrl ){
        CommonUtil.link( linkName , relativeUrl );
    }
}
