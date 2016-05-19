package com.huotu.partnermall.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.LinkEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/4/29.
 */
public class TipAlertDialog implements View.OnClickListener{
    AlertDialog dialog;
    Context context;
    TextView titleText;
    TextView messageText;
    Button btn_left;
    Button btn_right;

    public TipAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tipalertdialog , null);
        dialog.setView(view, 0, 0, 0, 0);
        titleText = (TextView) view.findViewById(R.id.titletext);
        messageText = (TextView) view.findViewById(R.id.messagetext);
        btn_left = (Button) view.findViewById(R.id.btn_left);
        btn_right = (Button) view.findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    public void show(String title , String message , String url ){
        titleText.setText(title);
        messageText.setText(message);
        btn_right.setTag( url);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if( v.getId()== R.id.btn_left){
            dialog.dismiss();
        }else if(v.getId()== R.id.btn_right){
            dialog.dismiss();

            if( null== btn_right.getTag() ) return;
            String linkUrl = btn_right.getTag().toString();
            if(TextUtils.isEmpty(linkUrl)) return;

            String linkName =titleText.getText().toString();
            LinkEvent event=new LinkEvent( linkName,linkUrl);
            EventBus.getDefault().post(event);
        }
    }
}
