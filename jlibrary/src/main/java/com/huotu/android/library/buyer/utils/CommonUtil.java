package com.huotu.android.library.buyer.utils;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.huotu.android.library.buyer.BizApiService;
import com.huotu.android.library.buyer.ConfigApiService;
import com.huotu.android.library.buyer.bean.BizBean.BizBaseBean;
import com.huotu.android.library.buyer.bean.BizBean.MallInfoBean;
import com.huotu.android.library.buyer.bean.Constant;
import com.huotu.android.library.buyer.bean.Data.ClassEvent;
import com.huotu.android.library.buyer.bean.Data.ClassificationConfig;
import com.huotu.android.library.buyer.bean.Data.LinkEvent;
import com.huotu.android.library.buyer.bean.Data.ScrollEvent;
import com.huotu.android.library.buyer.bean.Data.SearchEvent;
import com.huotu.android.library.buyer.bean.Data.SmartUiEvent;
import com.huotu.android.library.buyer.bean.Variable;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/1/11.
 */
public class CommonUtil {
    /*
    判断类是否实现指定接口
     */
    public static boolean isInterface( Class c , String interfaceName){
        Class[] infs = c.getInterfaces();
        if( infs ==null ) return false;
        for( int i=0;i< infs.length;i++){
            if( infs[i].getName().equals( interfaceName )){
                return true;
            }else{
                Class[]face1=infs[i].getInterfaces();
                for(int x=0;x<face1.length;x++)
                {
                    if(face1[x].getName().equals(interfaceName))
                    {
                        return true;
                    }
                    else if(isInterface(face1[x],interfaceName)) {
                        return true;
                    }
                }
            }
        }
        if(null!=c.getSuperclass()) {
            return isInterface(c.getSuperclass(), interfaceName );
        }
        return false;
    }

    public static String formatDouble(double value ){
        DecimalFormat format = new DecimalFormat("0.##");
        String str = format.format(value);
        return str;
    }

    /**
     * 格式化 会员价 数组 ,取第一个值
     * @param prices
     * @return
     */
    public static String formatPrice(List<Double> prices){
        if( prices==null|| prices.size()<1) return "";
        int count = prices.size();
        if(count>=1) return CommonUtil.formatDouble(prices.get(0));
        return "";
    }

    /**
     * 格式化 积分 数组
     * @param scores
     * @return
     */
    public static String formatJiFen(List<Integer> scores){
        if( scores==null|| scores.size()<1) return "0";
        int count = scores.size();
        if(count==1) return String.valueOf(scores.get(0));
        if(count>1) {
            if( scores.get(0).equals( scores.get(1) )){
                String s1 = String.valueOf(scores.get(0));
                return s1;
            }else {//
                return scores.get(1).toString(); //scores.toString();
            }
        }
        return "0";
    }

    /**
     * 解析 color 字符串
     * @param color
     * @return
     */
    public static int parseColor(String color){
        if( TextUtils.isEmpty( color)) return Color.WHITE;
        try {
            return Color.parseColor(color);
        }catch (Exception ex){
            Logger.e( ex.getMessage());
            return Color.WHITE;
        }
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
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

            Gson gson = new GsonBuilder().serializeNulls().create(); //new Gson();
            String jsonString = gson.toJson(map);
            return (T) gson.fromJson(jsonString, thisObj.getClass());
        }catch ( Exception ex ){
            return null;
        }
    }

    /**
     * 设置 SimpleDraweeView 控件的默认宽度和高度
     * @param width
     */
    public static void setSimpleDraweeViewWidthHeight( SimpleDraweeView iv , int width){
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        iv.setLayoutParams(layoutParams);
        iv.setAspectRatio(1.0f);
    }


    public static void link(String linkName , String relativeUrl){
        if( TextUtils.isEmpty( relativeUrl )) return;

        String url=relativeUrl;
        url = url.replace( Constant.URL_PARAMETER_CUSTOMERID , String.valueOf( Variable.CustomerId ));

        if( !url.startsWith("http://")){
            url = Variable.siteUrl + relativeUrl;
        }

        url = url.replace( Constant.URL_PARAMETER_CUSTOMERID, String.valueOf( Variable.CustomerId));

        Uri uri = Uri.parse(url);
        String path = uri.getPath().toLowerCase().trim();
        int idx = path.lastIndexOf("/");
        String fileName = path.substring(idx + 1);
        String indexPage = String.valueOf( Variable.CustomerId )+"/"+Constant.URL_SHOP_INDEX;//首页
        if (path.endsWith(Constant.URL_SMART_ASPX)) {
            String smartUrlPara = uri.getQueryParameter("url");
            EventBus.getDefault().post( new SmartUiEvent( smartUrlPara ,false));
        }else if( path.endsWith(indexPage)){
            String smartUrlPara = Variable.mainUiConfigUrl;
            EventBus.getDefault().post( new SmartUiEvent( smartUrlPara ,true));
        }else if( path.endsWith(Constant.URL_CLASS_ASPX)){
            String para_cateid = uri.getQueryParameter("cateid");
            String para_vircateid = uri.getQueryParameter("vircateid");
            int type = Constant.PAGE_TYPE_1;
            if( !TextUtils.isEmpty( para_cateid )){
                type= Constant.PAGE_TYPE_1;
                getClassPageConfig(Variable.CustomerId, para_cateid, String.valueOf(type), String.valueOf(-type), para_cateid);
            }else if( !TextUtils.isEmpty(para_vircateid)){
                type=Constant.PAGE_TYPE_2;
                getClassPageConfig(Variable.CustomerId, para_vircateid, String.valueOf(type), String.valueOf(Constant.TYPE_3) ,para_vircateid);
            }else{
                para_cateid="0";
                getClassPageConfig(Variable.CustomerId, para_cateid , String.valueOf(type), String.valueOf(-type), para_cateid);
            }
        }else if(path.endsWith(Constant.URL_SEARCH_ASPX)){
            deal_search( uri );
        }else{
            EventBus.getDefault().post(new LinkEvent( linkName , url));
        }
    }

    protected static void deal_search(Uri uri){
        int customerId = Variable.CustomerId;
        String keyword = uri.getQueryParameter("keyword");
        getSearchPageConfig(customerId, String.valueOf(Constant.TYPE_2) , String.valueOf( Constant.PAGE_TYPE_3 ) , keyword);
    }

    /**
     * 获得 商品分类 配置信息 url
     * @param catid
     * @param type
     */
    protected static void getClassPageConfig( final int customerid , final String catid , final String type , final String defaulttype , final String id ){
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);

        ConfigApiService configApiService = RetrofitUtil.getConfigInstance().create(ConfigApiService.class);
        Call<ClassificationConfig> call =
                configApiService.findByClassification(
                        key,
                        random,
                        secure,
                        customerid,
                        catid,
                        type);

        call.enqueue(new Callback<ClassificationConfig>() {
            @Override
            public void onResponse(Response<ClassificationConfig> response) {
                if (response == null || response.code() != Constant.REQUEST_SUCCESS ||
                        response.body() == null || response.body().get_links() == null) {
                    Logger.e(response.message());
                    //当无法获得商品分类页面配置地址时，则获取默认的配置地址。
                    if( customerid ==0 ) return;
                    getClassPageConfig(0, defaulttype, type, defaulttype, id);
                    return;
                }

                String smartUrlPara = response.body().get_links().getSelf().getHref();
                int classid = Integer.parseInt(id);
                EventBus.getDefault().post(new ClassEvent(smartUrlPara, classid));
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
            }
        });
    }

    /**
     * 获得 商品搜索 配置信息 url
     * @param customerid
     * @param pageclass
     * @param type
     * @param keyword
     */
    protected static void getSearchPageConfig(final int customerid , final String pageclass ,final String type , final String keyword ){
        String key = Variable.BizKey;
        String random = String.valueOf(System.currentTimeMillis());
        String secure = SignUtil.getSecure(Variable.BizKey, Variable.BizAppSecure, random);
        ConfigApiService configApiService = RetrofitUtil.getConfigInstance().create(ConfigApiService.class);
        Call<ClassificationConfig> call =
                configApiService.findByClassification(
                        key,
                        random,
                        secure,
                        customerid,
                        pageclass,
                        type);

        call.enqueue(new Callback<ClassificationConfig>() {
            @Override
            public void onResponse(Response<ClassificationConfig> response) {
                if (response == null || response.code() != Constant.REQUEST_SUCCESS ||
                        response.body() == null || response.body().get_links() == null) {
                    Logger.e(response.message());
                    //当无法获得商品分类页面配置地址时，则获取默认的配置地址。
                    if( customerid ==0 ) return;
                    getSearchPageConfig(0, pageclass, type, keyword);
                    return;
                }

                String smartUrlPara = response.body().get_links().getSelf().getHref();
                EventBus.getDefault().post(new SearchEvent(smartUrlPara , keyword));
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t.getMessage());
            }
        });
    }

}
