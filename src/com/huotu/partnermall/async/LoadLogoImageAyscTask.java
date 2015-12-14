package com.huotu.partnermall.async;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.huotu.partnermall.utils.KJLoger;
import com.huotu.partnermall.widgets.CircleImageDrawable;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 异步加载网络头像
 */
public
class LoadLogoImageAyscTask extends AsyncTask<Void, Void, Bitmap> {

    private
    ImageView view;
    private String url;
    private Resources resources;
    private int defaultImg;

    public LoadLogoImageAyscTask(Resources resources, ImageView view, String url, int defaultImg)
    {
        this.view = view;
        this.url = url;
        this.resources = resources;
        this.defaultImg = defaultImg;
    }

    @Override
    protected
    void onPostExecute ( Bitmap bitmap ) {
        super.onPostExecute ( bitmap );

        //加载图片
        if(null == bitmap)
        {
            bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
        }

        view.setImageDrawable ( new CircleImageDrawable ( bitmap ) );
    }

    @Override
    protected
    Bitmap doInBackground ( Void... params ) {

        Bitmap bitmap = null;
        URL imgUrl = null;
        InputStream is = null;
        if(TextUtils.isEmpty( url) || null == url)
        {
            bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
        }
        else if(!url.startsWith ( "http://" ) && !url.startsWith ( "https://" ))
        {
            bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
        }
        else
        {
            try {
                imgUrl = new URL ( url );
                //获取链接
                HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection ();
                conn.setConnectTimeout ( 10000 );
                conn.setDoInput ( true );
                conn.setUseCaches ( false );
                is = conn.getInputStream ();
                bitmap = BitmapFactory.decodeStream ( is );
            }
            catch ( MalformedURLException e ) {

                bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
                KJLoger.e ( e.getMessage () );
            }
            catch ( IOException e )
            {
                bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
                KJLoger.e ( e.getMessage () );
            }
            finally {
                if(null != is)
                {
                    try {
                        is.close ();
                    }
                    catch ( IOException e ) {
                        bitmap = BitmapFactory.decodeResource ( resources, defaultImg );
                        KJLoger.e ( e.getMessage ( ) );
                    }
                }
            }
        }
        return bitmap;
    }
}
