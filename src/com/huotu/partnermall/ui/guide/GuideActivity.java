package com.huotu.partnermall.ui.guide;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.partnermall.adapter.ViewPagerAdapter;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.ui.HomeActivity;
import com.huotu.partnermall.ui.base.BaseActivity;
import com.huotu.partnermall.utils.ActivityUtils;
import com.huotu.partnermall.utils.DensityUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.huotu.partnermall.inner.R.id.ivGif;

/**
 * 引导界面
 */
public class GuideActivity extends BaseActivity
        implements View.OnClickListener, ViewPager.OnPageChangeListener, Handler.Callback , View.OnTouchListener{
    static String TAG = GuideActivity.class.getName();
    @Bind(R.id.vp_activity)
    ViewPager mVPActivity;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private Resources resources;
    //引导图片资源
    private String[] pics;
    //private List<Bitmap> bitmapList;
    private List<Integer> imageList;
    int lastX=0;


    @Override
    protected void onCreate ( Bundle arg0 ) {
        super.onCreate ( arg0 );
        resources = this.getResources ();
        setContentView(R.layout.guide_ui);
        ButterKnife.bind(this);
        views = new ArrayList<> ( );

        //initImage();

        //初始化Adapter
        vpAdapter = new ViewPagerAdapter ( views );
        mVPActivity.setAdapter ( vpAdapter );
        //绑定回调
        mVPActivity.addOnPageChangeListener ( this );

        //mVPActivity.setOnTouchListener(this);

        loadImages();
    }

    @Override
    protected
    void initView ( ) {
    }

    protected void loadImages(){
        final String packageName = getPackageName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenWidth = metrics.widthPixels;
                int screenHeight = metrics.heightPixels;

                pics = resources.getStringArray ( R.array.guide_icon );
                //bitmapList = new ArrayList<>();
                imageList = new ArrayList<>();
                //初始化引导图片列表
                for(int i=0; i<pics.length; i++) {
                    int iconId = resources.getIdentifier( pics[i] , "drawable" , packageName );
                    if( iconId >0) {
                        //Bitmap bmp = ImageUtils.decodeSampledBitmapFromResource( resources , iconId , screenWidth , screenHeight );
                        //bitmapList.add(bmp);
                        imageList.add(iconId);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showImages();
                    }
                });

            }
        }).start();
    }


    protected void showImages(){
        try {
            if(imageList.size()==0) {
                go();
                return;
            }

            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            pics = resources.getStringArray ( R.array.guide_icon );

            //初始化引导图片列表
            for(int i=0; i<imageList.size() ; i++) {
                RelativeLayout iv = (RelativeLayout) LayoutInflater.from(GuideActivity.this).inflate(R.layout.guid_item, null);
                TextView skipText = (TextView) iv.findViewById(R.id.skipText);
                TextView tvTry = (TextView)iv.findViewById(R.id.tryUse);
                tvTry.setOnClickListener(this);

                if( i == imageList.size()-1){
                    skipText.setVisibility( View.GONE );
                    tvTry.setVisibility(View.VISIBLE);
                }

                SimpleDraweeView ivGif= (SimpleDraweeView)iv.findViewById(R.id.guideGif);
                iv.setLayoutParams(mParams);
                //iv.setOnClickListener(this);
                ivGif.setOnClickListener(this);

                //SystemTools.loadBackground(iv, new BitmapDrawable(bitmaps.get(i)));
                setGif(ivGif , imageList.get(i));

                skipText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go();
                    }
                });
                views.add(iv);
            }

            vpAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e( TAG , e.getMessage());
        }
    }

    void setGif( SimpleDraweeView ivGif, int resId ){
        Uri uri = Uri.parse("res://" + this.getPackageName() + "/" + resId );
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        ivGif.setController(controller);
    }

//    private void initImage ( ) {
//        try {
//            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            pics = resources.getStringArray ( R.array.guide_icon );
//
//            //初始化引导图片列表
//            for(int i=0; i<pics.length; i++) {
//                RelativeLayout iv = ( RelativeLayout ) LayoutInflater.from(GuideActivity.this).inflate ( R.layout.guid_item, null );
//                TextView skipText = (TextView) iv.findViewById(R.id.skipText);
//                iv.setLayoutParams ( mParams );
//                iv.setOnClickListener(this);
//                int iconId = resources.getIdentifier( pics[i] , "drawable" , this.getPackageName() );
//
//                Drawable menuIconDraw = null;
//                if( iconId >0) {
//                    menuIconDraw = resources.getDrawable(iconId);
//                    SystemTools.loadBackground(iv, menuIconDraw);
//                }
//                skipText.setOnClickListener(this);
//                views.add(iv);
//            }
//        } catch (Exception e) {
//            Log.e( TAG , e.getMessage());
//        }
//    }

    protected void go() {
        ActivityUtils.getInstance().skipActivity(GuideActivity.this, HomeActivity.class);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
    }

    @Override
    public void onClick ( View v ) {
       if(v.getId()== R.id.skipText || v.getId() == R.id.rl1  || v.getId()==R.id.guideGif || v.getId() == R.id.tryUse ){
           if( mVPActivity.getCurrentItem() ==  (vpAdapter.getCount()-1) ){
               go();
           }
       }
    }

    @Override
    public void onPageScrolled ( int arg0, float v, int i1 ) {
        //lastValue = arg0;
    }

    @Override
    public void onPageSelected ( int arg0 ) {
        try {
            if (arg0 == vpAdapter.getCount() - 1) {
                ((TextView) views.get(arg0).findViewById(R.id.tryUse)).setVisibility(View.VISIBLE);
                ((TextView)views.get(arg0).findViewById(R.id.skipText)).setVisibility(View.GONE);

            } else {
                ((TextView) views.get(arg0).findViewById(R.id.skipText)).setText(getString(R.string.skipText));
                ((TextView)views.get(arg0).findViewById(R.id.skipText)).setVisibility(View.VISIBLE);
                ((TextView)views.get(arg0).findViewById(R.id.tryUse)).setVisibility(View.GONE);
            }
        }catch (Exception ex){
            Log.e(GuideActivity.TAG,  "onPageSelected Error");
        }
    }

    @Override
    public void onPageScrollStateChanged ( int arg0 ) {
    }

    @Override
    public boolean handleMessage ( Message msg ) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

//        if(bitmapList!=null){
//            for(Bitmap item : bitmapList){
//                item.recycle();
//            }
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ){
            lastX =(int) event.getX();
        }else if( event.getAction() == MotionEvent.ACTION_UP ){
            int tempX = (int)event.getX();
            int sw = DensityUtils.getScreenW(this);
            if( (lastX - tempX)>=( sw/4) && mVPActivity.getCurrentItem()== (vpAdapter.getCount()-1)){
                go();
            }
        }
        return false;
    }
}
