package com.huotu.partnermall.widgets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 自定画登录按钮的
 */
public
class LoginDrawable extends Drawable {

    private Paint mPaint;
    private RectF rectF;
    private int color;


    public LoginDrawable(int color)
    {
        mPaint = new Paint (  );
        mPaint.setColor ( color );
    }
    @Override
    public
    void setBounds ( int left, int top, int right, int bottom ) {
        super.setBounds ( left, top, right, bottom );
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public
    void setBounds ( Rect bounds ) {
        super.setBounds ( bounds );
    }

    @Override
    public
    void draw ( Canvas canvas ) {

        canvas.drawRoundRect ( rectF, 2, 2, mPaint );

    }

    @Override
    public
    void setAlpha ( int alpha ) {

    }

    @Override
    public
    void setColorFilter ( ColorFilter cf ) {

    }

    @Override
    public
    int getOpacity ( ) {
        return 0;
    }
}
