package com.huotu.partnermall.widgets;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.utils.SystemTools;

public class CountDownTimerButton extends CountDownTimer
{
    public interface CountDownFinishListener{
        void timeFinish();
    }
    
    TextView view;
    String txt;
    String formatTxt;
    CountDownFinishListener finishListener=null;
    
    
    public CountDownTimerButton( TextView view , String formatTxt , String txt ,
                                 long millisInFuture , CountDownFinishListener listener ) {
        super(millisInFuture, 1000 ); 
        this.view= view;
        this.formatTxt = formatTxt;
        this.txt = txt;
        this.view.setText(txt);
        this.view.setClickable(false);
        //this.view.setBackgroundResource(R.drawable.btn_mark_gray);
        this.view.setBackgroundColor(Color.parseColor("#D1D1D1"));
        finishListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished)
    {
        String content = String.format( formatTxt , millisUntilFinished / 1000 );
        view.setText( content );    
    }

    @Override
    public void onFinish()
    {
        view.setClickable(true);
        view.setText(txt);
        //view.setBackgroundResource(R.drawable.btn_red_sel);
        view.setBackgroundColor(SystemTools.obtainColor(((BaseApplication)view.getContext().getApplicationContext() ).obtainMainColor()));
        if( finishListener!=null){
            finishListener.timeFinish();
        }
    }
    
    public void Stop(){
        this.cancel();
    }

}
