package com.huotu.partnermall.widgets.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.BuildConfig;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.model.MenuLinkEvent;
import com.huotu.partnermall.model.RefreshMessageEvent;
import com.huotu.partnermall.service.BussinessBiz;
import com.huotu.partnermall.ui.SplashActivity;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.PreferenceHelper;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 底部导航组件
 * Created by jinxiangdong on 2016/1/22.
 */
public class FooterOneWidget extends BaseLinearLayout
        implements Response.ErrorListener, Response.Listener<PageConfig>{
    private FooterOneConfig footerOneConfig;
    private MallInfoBean mallInfoBean;
    private List<SimpleDraweeView> ivList;
    /**
     * 底部导航栏图标的宽度
     */
    public final static int FOOTER_ICON_WIDTH = 25;
    /**
     * url地址中的参数 占位符
     */
    public final static String URL_PARAMETER_CUSTOMERID="{CustomerID}";
    /**
     * URL地址中的参数 占位符
     */
    public final static String URL_PARAMETER_QQ ="{QQ}";
    /**
     * 资源根地址
     */
    public String resourceUrl;
    /***
     * 红点控件
     */
    private View circleView;


    public FooterOneWidget(Context context ) {
        super(context);

        asyncGetMallInfo();

        checkConfig();
        //getFooterConfig();
    }

    protected void init( PageConfig pageConfig  ){
        if(pageConfig ==null) return;
        if( pageConfig.getWidgets()==null || pageConfig.getWidgets().size()<1 ) return;

        this.resourceUrl = pageConfig.getMallResourceURL();
        this.footerOneConfig  = new FooterOneConfig();
        this.footerOneConfig = convertMap( footerOneConfig , pageConfig.getWidgets().get(0).getProperties());

        this.setOrientation(VERTICAL);
        TextView tvLine = new TextView(getContext());
        int heightPx = DensityUtils.dip2px(getContext(),1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        tvLine.setBackgroundColor(Color.LTGRAY);
        tvLine.setLayoutParams(layoutParams);
        this.addView(tvLine);

        LinearLayout llContainer = new LinearLayout(getContext());
        llContainer.setOrientation(HORIZONTAL);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(layoutParams);
        llContainer.setBackgroundColor(SystemTools.parseColor(footerOneConfig.getBackgroundColor()));
        int topMargion = DensityUtils.dip2px(getContext(),  footerOneConfig.getTopMargion()==0? 1: footerOneConfig.getTopMargion());
        int bottomMargion = DensityUtils.dip2px(getContext(),footerOneConfig.getBottomMargion());
        llContainer.setPadding(0, topMargion, 0, bottomMargion);
        this.addView(llContainer);

        if( footerOneConfig.getRows()==null || footerOneConfig.getRows().size()<1 ) return;
        ivList=new ArrayList<>();
        for(FooterImageBean item : footerOneConfig.getRows()){
            LinearLayout ll = new LinearLayout(getContext());
            ll.setId(item.hashCode());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            ll.setOrientation(VERTICAL);
            ll.setLayoutParams(layoutParams);
            int padpx= DensityUtils.dip2px(getContext(),2);
            ll.setPadding(padpx,padpx,padpx,padpx);
            ll.setOnClickListener(this);

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            SimpleDraweeView iv = new SimpleDraweeView(getContext());
            int iconWidth = DensityUtils.dip2px(getContext(), FOOTER_ICON_WIDTH);
            //layoutParams = new LayoutParams( iconWidth , iconWidth );
            //layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(iconWidth,iconWidth);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            //iv.setLayoutParams(layoutParams);
            iv.setLayoutParams(layoutParams1);
            linearLayout.addView(iv);

            String urlLink = item.getLinkUrl();
            if( urlLink.contains( URL_PARAMETER_QQ ) ) {
                String kefuUrl = BaseApplication.single.readMerchantWebChannel();
                if (!TextUtils.isEmpty(kefuUrl)) {
                    View circle = new View(getContext());
                    int circleWidth = DensityUtils.dip2px(getContext(), 8);
                    int circleHeight = circleWidth;
                    circle.setVisibility(GONE);
                    circle.setBackgroundResource(R.drawable.circle_red);
                    layoutParams1 = new LinearLayout.LayoutParams(circleWidth, circleHeight);
                    layoutParams1.gravity = Gravity.TOP;
                    circle.setLayoutParams(layoutParams1);
                    linearLayout.addView(circle);
                    circleView = circle;
                }
            }

            ll.addView(linearLayout);

            String imageUrl = resourceUrl + item.getImageUrl();
            FrescoDraweeController.loadImage(iv, iconWidth , imageUrl);
            iv.setTag(item);

            TextView tv = new TextView(getContext());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tv.setLayoutParams(layoutParams);
            tv.setText(item.getName());
            tv.setTextColor(SystemTools.parseColor( footerOneConfig.getFontColor() ) );
            ll.addView(tv);

            ivList.add(iv);

            llContainer.addView(ll);


            String url = item.getLinkUrl();
            String customerId = BaseApplication.single.readMerchantId();
            url = url.replace( URL_PARAMETER_CUSTOMERID , customerId );

            if( !url.startsWith("http://")){
                url = BaseApplication.single.obtainMerchantUrl() + url;
            }
            if(UIUtils.isIndexPage( url )){
                changeButtonImage(ll);
            }
        }
    }

    private void changeButtonImage(View currentView ){
        try {
            if (ivList == null) return;

            int iconWidth = DensityUtils.dip2px(getContext(), FOOTER_ICON_WIDTH);
            for (SimpleDraweeView view : ivList) {
                if (null == view.getTag()) continue;
                FooterImageBean bean = (FooterImageBean) view.getTag();

                if ( currentView.getId() == bean.hashCode() ) {
                    if(bean.getHeightImageUrl()==null) continue;
                    if(bean.getHeightImageUrl().isEmpty()) continue;
                    String imageUrl = resourceUrl + bean.getHeightImageUrl();
                    FrescoDraweeController.loadImage(view, iconWidth, imageUrl);
                } else {
                    String imageUrl = resourceUrl + bean.getImageUrl();
                    FrescoDraweeController.loadImage(view, iconWidth, imageUrl);
                }
            }
        }catch (Exception ex){
            Log.e("error", "changeButtonImage()");
        }
    }


    @Override
    public void onClick(View v) {
        for( FooterImageBean item : footerOneConfig.getRows()) {
            boolean isOpenInNewWindow = false;
            if (v.getId() == item.hashCode()) {
                String url = item.getLinkUrl();
                //当检测到 {QQ} 特殊字符串时，则标记为在新窗口打开。
                if( url.contains( URL_PARAMETER_QQ ) ){
                    isOpenInNewWindow=true;
                }
                //当检测到 客服地址 存在时，则替换为新的客服地址，否则使用老的QQ联系方式
                String kefuUrl = BaseApplication.single.readMerchantWebChannel();
                if(!TextUtils.isEmpty( kefuUrl)){
                    url = url.replace( URL_PARAMETER_QQ , kefuUrl );
                    EventBus.getDefault().post(new RefreshMessageEvent(false));
                    showCircleView(false);
                }else {
                    String qq = mallInfoBean != null ? mallInfoBean.getClientQQ() : "";
                    url = url.replace(URL_PARAMETER_QQ, "http://wpa.qq.com/msgrd?v=3&uin=" + qq + "&site=qq&menu=yes");
                }

                String name = item.getName();
                link( name , url , isOpenInNewWindow );
                break;
            }
        }

        changeButtonImage(v);
    }

    protected void link(String linkName , String relativeUrl , boolean isOpenInNewWindow ){
        if( TextUtils.isEmpty( relativeUrl )) return;

        String url=relativeUrl;
        String customerId = BaseApplication.single.readMerchantId();
        url = url.replace( URL_PARAMETER_CUSTOMERID , customerId );

        if( !url.startsWith("http://")){
            url = BaseApplication.single.obtainMerchantUrl() + relativeUrl;
        }

        url = url.replace( URL_PARAMETER_CUSTOMERID, customerId );

        EventBus.getDefault().post(new MenuLinkEvent( linkName , url));
    }

    /**
     * 通过API获得 店铺信息
     */
    protected void asyncGetMallInfo(){
        String url = BuildConfig.SMART_Url;
        url +="buyerSeller/api/goods/getMallBaseInfo?customerId="+BaseApplication.single.readMerchantId();
        String key = Constants.getSMART_KEY();
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(key, Constants.getSMART_SECURITY() , random);
        Map<String,String> header = new HashMap<>();
        header.put("_user_key",key);
        header.put("_user_random",random);
        header.put("_user_secure",secure);

        GsonRequest<BizBean> request = new GsonRequest<>(Request.Method.GET,
                url, BizBean.class, header, new Response.Listener<BizBean>() {
            @Override
            public void onResponse(BizBean bizBean) {
                if (bizBean == null || bizBean.getResultCode() !=200 || bizBean.getData() == null ) {
                    return;
                }
                FooterOneWidget.this.mallInfoBean = bizBean.getData();
            }
            }, this
        );
        VolleyUtil.getRequestQueue().add(request);

    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("error", volleyError.getMessage()==null? "error": volleyError.getMessage());
    }
    @Override
    public void onResponse(PageConfig pageConfig) {
        if(pageConfig ==null) return;
        if( pageConfig.getWidgets()==null || pageConfig.getWidgets().size()<1 ) return;

        BaseApplication.writeBottomMenuConfig( JSONUtil.getGson().toJson( pageConfig ) );

        init( pageConfig  );
    }

    protected void checkConfig(){
        PageConfig pageConfig = BaseApplication.readBottomMenuConfig();
        if(pageConfig==null){
            getFooterConfig();
        }else{
            init( pageConfig );
        }
    }

    protected void getFooterConfig() {
//        String url = BuildConfig.SMART_Url;
//        url += "merchantWidgetSettings/search/findByMerchantIdAndScopeDependsScopeOrDefault/nativeCode/" + BaseApplication.single.readMerchantId() + "/global";
//
//        String key = Constants.getSMART_KEY();
//        String random = String.valueOf(System.currentTimeMillis());
//        String secure = SignUtil.getSecure(key, Constants.getSMART_SECURITY() , random);
//        Map<String,String> header = new HashMap<>();
//        header.put("_user_key",key);
//        header.put("_user_random",random);
//        header.put("_user_secure",secure);
//
//        GsonRequest<PageConfig> request = new GsonRequest<>(Request.Method.GET,
//                url, PageConfig.class, header, this, this
//        );
//        VolleyUtil.getRequestQueue().add(request);

        try {
            BussinessBiz bussinessBiz = new BussinessBiz();
            bussinessBiz.getFooterConfig( this , this );
        }catch (Exception ex){
            Log.e(SplashActivity.TAG , "FooterOneWidget.getFooterConfig Error!");
        }

    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     */
    public static <T> T convertMap(T thisObj, Map map) {
        try {
            if(map.containsKey("paddingLeft")){
                if( map.get("paddingLeft")!=null && map.get("paddingLeft")==""){
                    map.put("paddingLeft", 0);
                }
            }
            if(map.containsKey("paddingRight")){
                if( map.get("paddingRight")!=null && map.get("paddingRight")==""){
                    map.put("paddingRight",0);
                }
            }
            if(map.containsKey("paddingTop")){
                if( map.get("paddingTop")!=null && map.get("paddingTop")==""){
                    map.put("paddingTop",0);
                }
            }
            if(map.containsKey("paddingBottom")){
                if( map.get("paddingBottom")!=null && map.get("paddingBottom")==""){
                    map.put("paddingBottom",0);
                }
            }

            Gson gson = JSONUtil.getGson();
            String jsonString = gson.toJson(map);
            return (T) gson.fromJson(jsonString, thisObj.getClass());
        }catch ( Exception ex ){
            return null;
        }
    }

    public void showCircleView(boolean show){
        if(circleView==null)return;
        circleView.setVisibility(show?VISIBLE:GONE);
    }
}
