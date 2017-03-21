package com.huotu.partnermall.widgets;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huotu.android.library.libpay.alipay.AliOrderInfo;
import com.huotu.android.library.libpay.alipay.AliPayInfo;
import com.huotu.android.library.libpay.alipay.AliPayUtil;
import com.huotu.android.library.libpay.alipayV2.AliPayInfoV2;
import com.huotu.android.library.libpay.alipayV2.AliPayUtilV2;
import com.huotu.android.library.libpay.weixin.WeiXinOrderInfo;
import com.huotu.android.library.libpay.weixin.WeiXinPayInfo;
import com.huotu.android.library.libpay.weixin.WeiXinPayUtil;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.listener.PoponDismissListener;
import com.huotu.partnermall.model.PayModel;
import com.huotu.partnermall.utils.EncryptUtil;
import com.huotu.partnermall.utils.WindowUtils;

import java.math.BigDecimal;

import static android.R.attr.key;

/**
 * 支付弹出框
 */
public class PayPopWindow extends PopupWindow implements View.OnClickListener{
    private Button wxPayBtn;
    private Button alipayBtn;
    private Button alipayMobileBtn;
    private Button cancelBtn;
    private View payView;
    private Activity aty;
    private Handler mHandler;
    private BaseApplication application;
    private PayModel payModel;
    //public ProgressPopupWindow progress;


    public PayPopWindow(final Activity aty, final Handler mHandler, final PayModel payModel) {
        super();
        this.aty = aty;
        this.mHandler = mHandler;
        this.application = BaseApplication.single;
        this.payModel = payModel;

        this.setOnDismissListener(new PoponDismissListener(aty));

        //progress = new ProgressPopupWindow(aty);
        LayoutInflater inflater = (LayoutInflater) aty.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        payView = inflater.inflate(R.layout.pop_pay_ui, null);
        wxPayBtn = (Button) payView.findViewById(R.id.wxPayBtn);
        alipayBtn = (Button) payView.findViewById(R.id.alipayBtn);
        alipayMobileBtn = (Button) payView.findViewById(R.id.alipayMobileBtn);
        cancelBtn = (Button) payView.findViewById(R.id.cancelBtn);

        showPayType();

        wxPayBtn.setOnClickListener(this);
        alipayBtn.setOnClickListener(this);
        alipayMobileBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(payView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        //this.setFocusable(true);
//        this.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                if( event.getAction() == MotionEvent.ACTION_OUTSIDE){
////                    return true;
////                }
//
//                return true;
//            }
//        });

        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(ContextCompat.getDrawable(aty , R.drawable.share_window_bg));

        WindowUtils.backgroundAlpha(aty, 0.4f);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.alipayBtn) {
            aliPay();
        } else if (v.getId() == R.id.wxPayBtn) {
            wxPay();
        } else if (v.getId() == R.id.alipayMobileBtn) {
            aliMobilePay();
        } else if (v.getId() == R.id.cancelBtn) {
            cancelPay();
        }
    }
    protected void cancelPay(){
        dismissView();
        Message msg = mHandler.obtainMessage(Constants.Message_GotoOrderList);
        mHandler.sendMessage(msg);
    }
    /***
     *  支付宝原生支付V2
     */
    protected void aliMobilePay(){
        dismissView();
        if (!application.scanAliPay()) {//缺少支付信息
            NoticePopWindow noticePop = new NoticePopWindow(aty, "缺少支付信息");
            noticePop.showNotice();
            noticePop.showAtLocation(aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0);
        }else{
            payModel.setAttach(payModel.getCustomId() + "_0");
            //payModel.setNotifyurl(application.obtainMerchantUrl() + application.readAlipayNotify());
            payModel.setNotifyurl( getNotifyUrl( application.readAlipayDomain() , application.readAlipayNotify() , application.obtainMerchantUrl() ) );
            AliPayInfoV2 aliPayInfoV2 = new AliPayInfoV2();
            aliPayInfoV2.setAppId( application.readAlipayAppId() );
            aliPayInfoV2.setpId(application.readAlipayParentId());
            aliPayInfoV2.setRsa_private(EncryptUtil.getInstance().decryptDES( application.readAlipayAppKey() , Constants.getDES_KEY() ));
            //aliPayInfoV2.setRsa2_private( EncryptUtil.getInstance().decryptDES(application.readAlipayAppKey(), Constants.getDES_KEY()) );
            aliPayInfoV2.setNotifyUrl( payModel.getNotifyurl() );

            AliOrderInfo aliOrderInfo = new AliOrderInfo();
            aliOrderInfo.setTotalfee( new BigDecimal( payModel.getAliAmount()));
            aliOrderInfo.setSubject(payModel.getTradeNo());
            aliOrderInfo.setOrderNo(payModel.getTradeNo());
            aliOrderInfo.setBody(payModel.getDetail());

            AliPayUtilV2 aliPayUtilV2  = new AliPayUtilV2(aty , mHandler , aliPayInfoV2);
            aliPayUtilV2.payV2(aliOrderInfo);
        }
    }

    public void aliPay() {
        Message msg = new Message();
        msg.what = Constants.PAY_NET;
        payModel.setPaymentType("1");
        msg.obj = payModel;
        mHandler.sendMessage(msg);
        dismissView();
    }

    public void wxPay(){
        dismissView();
        if (!application.scanWx()) {//缺少支付信息
            NoticePopWindow noticePop = new NoticePopWindow(aty, "缺少支付信息");
            noticePop.showNotice();
            noticePop.showAtLocation(aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0);
        } else {
            //progress.showProgress("正在加载支付信息");
            //progress.showAtLocation( aty.findViewById(R.id.titleText), Gravity.CENTER, 0, 0 );
            payModel.setAttach(payModel.getCustomId() + "_0");
            //添加微信回调路径
            //payModel.setNotifyurl(application.obtainMerchantUrl() + application.readWeixinNotify());
            payModel.setNotifyurl( getNotifyUrl( application.readWeixinDomain() , application.readWeixinNotify() , application.obtainMerchantUrl() ) );
//            PayFunc payFunc = new PayFunc(aty, payModel, application, mHandler, aty, progress);
//            payFunc.wxPay();

            WeiXinOrderInfo weiXinOrderInfo = new WeiXinOrderInfo();
            weiXinOrderInfo.setBody(payModel.getDetail());
            weiXinOrderInfo.setOrderNo(payModel.getTradeNo());
            weiXinOrderInfo.setTotal_fee(payModel.getAmount());
            weiXinOrderInfo.setAttach(payModel.getAttach());

            String wxAppId = application.readWxpayAppId();
            String wxAppSecret = EncryptUtil.getInstance().decryptDES( application.readWxpayAppKey() , Constants.getDES_KEY());
            String wxPartner=application.readWxpayParentId();
            String notifyUrl =application.obtainMerchantUrl() + application.readWeixinNotify();

            WeiXinPayInfo weiXinPayInfo = new WeiXinPayInfo( wxAppId , wxPartner , wxAppSecret , notifyUrl);
            WeiXinPayUtil weiXinPayUtil = new WeiXinPayUtil(aty , mHandler , weiXinPayInfo);
            weiXinPayUtil.pay(weiXinOrderInfo);
            //progress.dismissView();
        }
    }

    public void dismissView() {
        dismiss();
    }

    protected void showPayType() {
        String appid = application.readAlipayAppId();
        String key = application.readAlipayAppKey();
        String partnerid = application.readAlipayParentId();
        String notifyurl = application.readAlipayNotify();

        boolean isWebAliPay = application.readIsWebAliPay();

        if( isWebAliPay ){
            alipayBtn.setVisibility(View.VISIBLE);
            //alipayMobileBtn.setVisibility(View.GONE);
        }else {
            alipayBtn.setVisibility(View.GONE);
            if( TextUtils.isEmpty(appid) || TextUtils.isEmpty( key ) || TextUtils.isEmpty( partnerid ) ) {
                alipayMobileBtn.setVisibility(View.GONE);
            }else{
                alipayMobileBtn.setVisibility(View.VISIBLE);
            }
        }

        //key = application.rea

        key = application.readWxpayAppKey();
        partnerid = application.readWxpayParentId();
        if(TextUtils.isEmpty(key) || TextUtils.isEmpty( partnerid ) ){
            wxPayBtn.setVisibility(View.GONE);
        }
    }

    protected String getNotifyUrl( String domain , String notifyUrl , String mallUrl){
        if( domain == null || domain.trim().isEmpty() ){
            return mallUrl + notifyUrl;
        }else{
            return domain+notifyUrl;
        }
    }
}
