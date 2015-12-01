package com.huotu.partnermall.ui.sis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.huotu.partnermall.BaseApplication;
import com.huotu.partnermall.config.Constants;
import com.huotu.partnermall.image.BitmapLoader;
import com.huotu.partnermall.image.VolleyUtil;
import com.huotu.partnermall.inner.R;
import com.huotu.partnermall.utils.AuthParamUtils;
import com.huotu.partnermall.utils.GsonRequest;
import com.huotu.partnermall.utils.SystemTools;
import com.huotu.partnermall.utils.ToastUtils;
import com.huotu.partnermall.utils.Util;
import com.huotu.partnermall.utils.ViewHolderUtil;
import com.huotu.partnermall.widgets.NetworkImageViewCircle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class SelectTempleteActivity extends Activity implements View.OnClickListener{
    FeatureCoverFlow flow;
    CoverFlowAdapter adapter;
    List<SisTemplateListModel> data;
    Button back;
    TextView operate;
    RelativeLayout header;
    Long templateId;
    BaseApplication app;
    ProgressDialog pgDlg;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sis_activity_select_templete);

        app=(BaseApplication)this.getApplication();
        handler =new Handler(getMainLooper());
        header  =(RelativeLayout)findViewById(R.id.sis_selecttemplate_header);
        header.setBackgroundColor(SystemTools.obtainColor(((BaseApplication) SelectTempleteActivity.this.getApplication()).obtainMainColor()));

        back=(Button)findViewById(R.id.sis_selecttemplate_back);
        back.setOnClickListener(this);
        operate=(TextView)findViewById(R.id.sis_selecttemplate_operate);
        operate.setOnClickListener(this);

        flow = (FeatureCoverFlow)findViewById(R.id.sis_selecttemplate_show);
        ViewGroup.LayoutParams layoutParams = flow.getLayoutParams();
        layoutParams.width = Constants.SCREEN_WIDTH * 90/100;
        layoutParams.height = Constants.SCREEN_HEIGHT * 90/100;
        int cw = layoutParams.width * 50/100;
        int ch = layoutParams.height * 60/100;
        flow.setLayoutParams( layoutParams );
        flow.setCoverWidth(cw);
        flow.setCoverHeight(ch);

        adapter =new CoverFlowAdapter(this);
        data=new ArrayList<>();
        SisTemplateListModel model = new SisTemplateListModel();
        model.setPictureUrl("");
        model.setTid(1L);
        data.add(model);

        adapter.setData(data);

        flow.setAdapter(adapter);

        getData();

        flow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //data.get(position).setSelected( !data.get(position).isSelected());
                //adapter.setSelectedId( data.get(position).getTid() );
                //flow.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //ToastUtils.showLongToast(SelectTempleteActivity.this,"nnnnnnnnnnn");
            }
        });

        flow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                flow.setSeletedItemPosition(position);
            }

            @Override
            public void onScrolling() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if( v.getId()==R.id.sis_selecttemplate_back){
            this.finish();
        }else if( v.getId()==R.id.sis_selecttemplate_operate){
            int idx = (int)flow.getSelectedItemId();
            long templateid = data.get(idx).getTid();
            update( templateid );
        }
    }

    protected void update( long id ){
        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            return;
        }

        if( pgDlg==null){
            pgDlg=new ProgressDialog(this);
            pgDlg.setCanceledOnTouchOutside(false);
        }
        pgDlg.setMessage("正在更新数据,请稍等...");
        pgDlg.show();

        String url = SisConstant.INTERFACE_updateTemplate;
        url +="?sisid="+ SisConstant.SHOPINFO.getSisId();
        url +="&templateid="+ String.valueOf(id);
        AuthParamUtils authParamUtils = new AuthParamUtils(app, System.currentTimeMillis() , url , this);
        url = authParamUtils.obtainUrlName();

        GsonRequest<BaseModel> request =new GsonRequest<BaseModel>(
                Request.Method.GET,
                url,
                BaseModel.class,
                null,
                new MyUpdateListener(this),
                new ErrorListener(this,null,null,pgDlg)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    protected  void getData(){
        if (false == Util.isConnect(this)) {
            ToastUtils.showLongToast(this,"无网络");
            return;
        }

        if( SisConstant.SHOPINFO==null )return;

        if( pgDlg==null){
            pgDlg=new ProgressDialog(this);
            pgDlg.setCanceledOnTouchOutside(false);
        }
        pgDlg.setMessage("正在获取数据,请稍等...");
        pgDlg.show();

        String url = SisConstant.INTERFACE_getTemplateList;
        url += "?sisid="+ String.valueOf( SisConstant.SHOPINFO.getSisId());
        AuthParamUtils authParamUtils=new AuthParamUtils(app , System.currentTimeMillis() , url , this);
        url = authParamUtils.obtainUrlName();

        GsonRequest<AppSisTemplateListModel> request =new GsonRequest<AppSisTemplateListModel>(
                Request.Method.GET ,
                url,
                AppSisTemplateListModel.class,
                null,
                new MyListener(this),
                new ErrorListener(this,null,null, pgDlg)
        );
        VolleyUtil.getRequestQueue().add(request);
    }

    static class MyUpdateListener implements Response.Listener<BaseModel> {
        WeakReference<SelectTempleteActivity> ref;
        public MyUpdateListener(SelectTempleteActivity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(BaseModel baseModel ) {
            if( ref.get()==null) return;
            if( ref.get().pgDlg!=null ) {
                ref.get().pgDlg.dismiss();
            }
            if (null == baseModel) {
                ToastUtils.showLongToast(ref.get(), "出错");
                return;
            }
            if (baseModel.getSystemResultCode() != 1) {
                ToastUtils.showLongToast( ref.get(), "出错");
                return;
            }

            if (baseModel.getResultCode() != 1) {
                ToastUtils.showLongToast( ref.get() , "出错");
                return;
            }

            ToastUtils.showShortToast( ref.get() ,"设置模板成功" );
            ref.get().finish();
        }
    }

    static class MyListener implements Response.Listener<AppSisTemplateListModel> {
        WeakReference<SelectTempleteActivity> ref;
        public MyListener(SelectTempleteActivity act){
            ref = new WeakReference<>(act);
        }

        @Override
        public void onResponse(AppSisTemplateListModel appSisTemplateListModel) {
            if( ref.get()==null) return;
            if( ref.get().pgDlg!=null ) {
                ref.get().pgDlg.dismiss();
            }

            if (null == appSisTemplateListModel) {
                ToastUtils.showLongToast(ref.get(), "出错");
                return;
            }
            if (appSisTemplateListModel.getSystemResultCode() != 1) {
                ToastUtils.showLongToast( ref.get(), "出错");
                return;
            }

            if (appSisTemplateListModel.getResultCode() != 1) {
                ToastUtils.showLongToast( ref.get() , "出错");
                return;
            }

            ref.get().data.clear();
            ref.get().data.addAll(appSisTemplateListModel.getResultData().getList());
            ref.get().adapter.setData(ref.get().data);
            ref.get().adapter.setSelectedId( appSisTemplateListModel.getResultData().getTemplateid() );
            ref.get().templateId = appSisTemplateListModel.getResultData().getTemplateid();


//            if( ref.get().templateId!=null) {
//                for (SisTemplateListModel item : ref.get().data) {
//                    if (item.getTid().equals(ref.get().templateId)) {
//                        item.setSelected(true);
//                        break;
//                    }
//                }
//            }

            ref.get().flow.setAdapter(ref.get().adapter);

            if( ref.get().templateId!=null) {
                for ( int position =0;position < ref.get().data.size();position++) {
                    if (ref.get().data.get(position).getTid().equals(ref.get().templateId)) {
                        final int pos = position;
                        ref.get().handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ref.get().flow.scrollToPosition(pos);
                            }
                        },400);

                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class CoverFlowAdapter extends BaseAdapter {

        private List<SisTemplateListModel> mData;
        private Context mContext;
        private Long selectId=-1L;

        public CoverFlowAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<SisTemplateListModel> data) {
            mData = data;
        }

        public void setSelectedId( Long id){
            selectId = id;
        }

        @Override
        public int getCount() {
            return mData==null? 0: mData.size();
        }

        @Override
        public Object getItem(int pos) {
            return mData==null ? null: mData.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.sis_selecttemplate_item, null);
            }

            NetworkImageViewCircle iv = ViewHolderUtil.get( rowView , R.id.sis_selecttemplate_item_pic );
            //TextView tv = ViewHolderUtil.get(rowView , R.id.sis_selecttemplate_item_title);
            TextView tvflag = ViewHolderUtil.get(rowView,R.id.sis_selecttemplate_item_flag);

            SisTemplateListModel model = mData.get(position);
            BitmapLoader.create().displayUrl(mContext, iv, model.getPictureUrl(), R.drawable.sis_pic, R.drawable.sis_pic);

            //tv.setText(model.ge());
            if( selectId !=null && selectId.equals( model.getTid() ) ) {
                tvflag.setVisibility(View.VISIBLE);
            }else{
                tvflag.setVisibility(View.GONE);
            }

            return rowView;
        }
    }
}
