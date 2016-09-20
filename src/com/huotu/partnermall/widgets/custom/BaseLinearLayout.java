package com.huotu.partnermall.widgets.custom;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

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

//    protected void golink( String linkName , String relativeUrl ){
//        CommonUtil.link( linkName , relativeUrl );
//    }
//

}
