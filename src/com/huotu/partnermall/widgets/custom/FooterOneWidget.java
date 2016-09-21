package com.huotu.partnermall.widgets.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.huotu.partnermall.model.MenuLinkEvent;
import com.huotu.partnermall.utils.DensityUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.JSONUtil;
import com.huotu.partnermall.utils.SignUtil;
import com.huotu.partnermall.utils.SystemTools;
import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;

/**
 * 底部导航组件
 * Created by jinxiangdong on 2016/1/22.
 */
public class FooterOneWidget extends BaseLinearLayout
        implements Response.ErrorListener, Response.Listener<PageConfig>{
    private FooterOneConfig footerOneConfig;
    private MallInfoBean mallInfoBean;
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

    public FooterOneWidget(Context context ) {
        super(context);

        asyncGetMallInfo();

        getFooterConfig();
    }

    protected void init( FooterOneConfig footerOneConfig ){
        this.footerOneConfig= footerOneConfig;
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setLayoutParams(layoutParams);

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
        int topMargion = DensityUtils.dip2px(getContext(), footerOneConfig.getTopMargion());
        int bottomMargion = DensityUtils.dip2px(getContext(),footerOneConfig.getBottomMargion());
        llContainer.setPadding(0, topMargion, 0, bottomMargion);
        this.addView(llContainer);

        if( footerOneConfig.getRows()==null || footerOneConfig.getRows().size()<1 ) return;
        for(FooterImageBean item : footerOneConfig.getRows()){
            LinearLayout ll = new LinearLayout(getContext());
            ll.setId(item.hashCode());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            ll.setOrientation(VERTICAL);
            ll.setLayoutParams(layoutParams);
            int padpx= DensityUtils.dip2px(getContext(),2);
            ll.setPadding(padpx,padpx,padpx,padpx);
            ll.setOnClickListener(this);

            SimpleDraweeView iv = new SimpleDraweeView(getContext());
            int iconWidth = DensityUtils.dip2px(getContext(), FOOTER_ICON_WIDTH);
            layoutParams = new LayoutParams( iconWidth , iconWidth );
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            iv.setLayoutParams(layoutParams);
            ll.addView(iv);
            String imageUrl = resourceUrl + item.getImageUrl();
            FrescoDraweeController.loadImage(iv, iconWidth , imageUrl);

            TextView tv = new TextView(getContext());
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            tv.setLayoutParams(layoutParams);
            tv.setText(item.getName());
            tv.setTextColor(SystemTools.parseColor( footerOneConfig.getFontColor() ) );
            ll.addView(tv);

            llContainer.addView(ll);
        }
    }

    private void changeButtonImage(){

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
                }else {
                    String qq = mallInfoBean != null ? mallInfoBean.getClientQQ() : "";
                    url = url.replace(URL_PARAMETER_QQ, "http://wpa.qq.com/msgrd?v=3&uin=" + qq + "&site=qq&menu=yes");
                }

                String name = item.getName();
                link( name , url , isOpenInNewWindow );
                break;
            }
        }
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

//    @Override
//    public void onResponse(Call<BizBaseBean<MallInfoBean>> call, Response<BizBaseBean<MallInfoBean>> response) {
//        if (response == null || response.code() != 200 || response.body() == null || response.body().getData() == null) {
//            Logger.e(response.message());
//            return;
//        }
//        mallInfoBean = response.body().getData();
//    }

//    @Override
//    public void onFailure(Call<BizBaseBean<MallInfoBean>> call, Throwable t) {
//        Log.e("error",t.getMessage());
//    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e("error", volleyError.getMessage()==null? "error": volleyError.getMessage());
    }
    @Override
    public void onResponse(PageConfig pageConfig) {
        if(pageConfig ==null) return;
        if( pageConfig.getWidgets()==null || pageConfig.getWidgets().size()<1 ) return;

        FooterOneConfig footerOneConfig = new FooterOneConfig();
        footerOneConfig = convertMap( footerOneConfig , pageConfig.getWidgets().get(0).getProperties());
        resourceUrl = pageConfig.getMallResourceURL();
        init( footerOneConfig );
    }

    protected void getFooterConfig() {
        String url = BuildConfig.SMART_Url;
        url += "merchantWidgetSettings/search/findByMerchantIdAndScopeDependsScopeOrDefault/nativeCode/" + BaseApplication.single.readMerchantId() + "/global";

        String key = Constants.getSMART_KEY();
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(key, Constants.getSMART_SECURITY() , random);
        Map<String,String> header = new HashMap<>();
        header.put("_user_key",key);
        header.put("_user_random",random);
        header.put("_user_secure",secure);

        GsonRequest<PageConfig> request = new GsonRequest<>(Request.Method.GET,
                url, PageConfig.class, header, this, this
        );
        VolleyUtil.getRequestQueue().add(request);
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
}
